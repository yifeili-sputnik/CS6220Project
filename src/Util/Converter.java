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

	/*
	 * generate data in the format we need
	 */
	// public static String analyzeMatch(MatchObject mo) {
	// if (mo.getResult().getPlayers().size() == 10) {
	// StringBuilder sb = new StringBuilder();
	// int[] instance = new int[221];
	// for (int i = 0; i < 221; i++) {
	// instance[i] = 0;
	// }
	//
	// /*
	// * 0-4 radiant players 5-9 dire players
	// */
	// for (Player p : mo.getResult().getPlayers()) {
	// int heroID = p.getHero_id();
	// int playerSlot = p.getPlayer_slot();
	// if (playerSlot <= 4) {
	// instance[heroID - 1] = 1;
	// } else {
	// instance[heroID + 109] = 1;
	// }
	// }
	//
	// // win/loss
	// if (mo.getResult().isRadiant_win()) {
	// instance[220] = 1;
	// }
	// for (int i = 0; i < 221; i++) {
	// sb.append(instance[i] + " ");
	// }
	// return sb.toString();
	// }
	// return null;
	// }
}
