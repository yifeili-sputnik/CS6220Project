package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import dataobject.MatchObject;
import dataobject.Player;

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
		File[] matches = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.charAt(0) == '.')
					return false;
				return true;
			}
		});

		// change data size
		// matches = Arrays.copyOfRange(matches, 0, 29999);

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
		if (mo.getResult().getMatch_id() == 0 || mo.getResult().getHuman_players() != 10
				|| (mo.getResult().getGame_mode() != 1 && mo.getResult().getGame_mode() != 22)) {
			filesToBeDeleted.add(file.getAbsolutePath());
		}
		return mo;
	}
}
