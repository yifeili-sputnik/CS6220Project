import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import DataObject.MatchObject;
import DataObject.Player;

public class Converter {
	public static void convert() throws IOException {
		FileWriter fl = new FileWriter("data/Matrix.txt");
		File dir = new File("data/rawdata/");
		File[] matches = dir.listFiles();
		for (File f : matches) {
			MatchObject mo = jsonToObject(f);
			String line = analyzeMatch(mo);
			fl.write(line);
			fl.write(System.getProperty("line.separator"));
		}
		fl.close();
		System.out.println("Data created.");
	}

	/*
	 * parse json match data to POJO
	 */
	public static MatchObject jsonToObject(File file) {
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
	public static String analyzeMatch(MatchObject mo) {
		if (mo.getResult().getPlayers().size() == 10) {
			StringBuilder sb = new StringBuilder();
			int[] instance = new int[221];
			for (int i = 0; i < 221; i++) {
				instance[i] = 0;
			}

			/*
			 * 0-4 radiant players 5-9 dire players
			 */
			for (Player p : mo.getResult().getPlayers()) {
				int heroID = p.getHero_id();
				int playerSlot = p.getPlayer_slot();
				if (playerSlot <= 4) {
					instance[heroID - 1] = 1;
				} else {
					instance[heroID + 109] = 1;
				}
			}

			// win/loss
			if (mo.getResult().isRadiant_win()) {
				instance[220] = 1;
			}
			for (int i = 0; i < 221; i++) {
				sb.append(instance[i] + " ");
			}
			return sb.toString();
		}
		return null;
	}

	public static void main(String[] args) {
		try {
			Converter.convert();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
