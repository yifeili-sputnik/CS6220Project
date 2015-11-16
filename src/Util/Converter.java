package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import DataObject.MatchObject;
import DataObject.Player;

public class Converter {
	private String file = null;
	private List<String> filesToBeDeleted = new ArrayList<String>();

	public Converter(String f) {
		this.file = f;
	}

	public boolean checkValidation(MatchObject m) {
		List<Player> players = m.getResult().getPlayers();
		if (players.size() != 10)
			return false;
		for (Player p : players) {
			if (p.getHero_id() == 0) {
				return false;
			}
		}
		return true;
	}

	public List<MatchObject> convert() {
		int matchCounter = 0;
		File dir = new File(file);
		ArrayList<MatchObject> res = new ArrayList<MatchObject>();
		File[] matches = dir.listFiles();
		for (File f : matches) {
			MatchObject mo = jsonToObject(f);
			// check validation of match data
			if (checkValidation(mo)) {
				matchCounter++;
				res.add(mo);
			} else {
				filesToBeDeleted.add(f.getAbsolutePath());
			}
		}
		System.out.println("Total matches: " + matches.length);
		System.out.printf("Successfully converted %d matches", matchCounter);
		System.out.println();
		deleteInvalidMatches();
		return res;
	}

	public void deleteInvalidMatches() {
		int counter = 0;
		for (String str : filesToBeDeleted) {
			File f = new File(str);
			if (f.exists() && f.delete()) {
				counter++;
			}
		}
		System.out.printf("Successfully deleted %d matches", counter);
		System.out.println();
	}

	/*
	 * parse json match data to POJO
	 */
	public MatchObject jsonToObject(File file) {
		Gson gson = new Gson();
		MatchObject mo = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			mo = gson.fromJson(br, MatchObject.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (mo.getResult() == null) {
			filesToBeDeleted.add(file.getAbsolutePath());
		}
		return mo;
	}

	// public static void main(String[] args) {
	// File f = new File("data/rawdata/1939645009.json");
	// MatchObject m = Converter.jsonToObject(f);
	// System.out.println();
	// }
}
