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
				// TO-DO
				// delete useless matches
				// if (f.delete()) {
				// System.out.println("deleted!");
				// } else {
				// System.out.println("delete failed.");
				// }
			}
		}
		System.out.printf("Successfully converted %d matches", matchCounter);
		System.out.println();
		return res;
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
		return mo;
	}
}
