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
	public static void main(String[] args) {
		File dir = new File("data/");
		File[] matches = dir.listFiles();
		List<MatchObject> mObjects = new ArrayList<MatchObject>();
		for (File f : matches) {
			MatchObject mo = jsonToObject(f);
			mObjects.add(mo);
		}
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
	 * return the match result for both radiant and dire the first array is the
	 * result of radiant, the second array is the result of the dire, the last
	 * line is the total average result, we need the data to build our decision
	 * tree
	 */
	public static int[][] analyzeMatch(MatchObject mo) {
		int[][] res = new int[2][7];
		int radiantKills = 0, radiantDeaths = 0, radiantAssists = 0, radiantLastHits = 0, radiantDenies = 0,
				radiantHeroDamage = 0, radiantTowerDamage = 0;
		int direKills = 0, direDeaths = 0, direAssists = 0, direLastHits = 0, direDenies = 0, direHeroDamage = 0,
				direTowerDamage = 0;
		boolean radiantWin = mo.getResult().isRadiant_win();
		int duration = mo.getResult().getDuration() / 60;
		for (int i = 0; i < 10; i++) {
			for (Player p : mo.getResult().getPlayers()) {
				if (i < 5) {
					radiantKills += p.getKills();
					radiantDeaths += p.getDeaths();
					radiantAssists += p.getAssists();
					radiantLastHits += p.getLast_hits();
					radiantDenies += p.getDenies();
					radiantHeroDamage += p.getHero_damage();
					radiantTowerDamage += p.getTower_damage();
				} else {
					direKills += p.getKills();
					direDeaths += p.getDeaths();
					direAssists += p.getAssists();
					direLastHits += p.getLast_hits();
					direDenies += p.getDenies();
					direHeroDamage += p.getHero_damage();
					direTowerDamage += p.getTower_damage();
				}
			}
		}

		// average radiant kills
		res[0][0] = radiantKills / duration;
		// average radiant deaths
		res[0][1] = radiantDeaths / duration;
		// average radiant assists
		res[0][2] = radiantAssists / duration;
		// average radiant last hits
		res[0][3] = radiantLastHits / duration;
		// average radiant denies
		res[0][4] = radiantDenies / duration;
		// average radiant hero damage
		res[0][5] = radiantHeroDamage / duration;
		// average radiant tower damage
		res[0][6] = radiantTowerDamage / duration;
		// radiant win
		if (radiantWin)
			res[0][7] = 1;

		// average dire kills
		res[1][0] = direKills / duration;
		// average dire deaths
		res[1][1] = direDeaths / duration;
		// average dire assists
		res[1][2] = direAssists / duration;
		// average dire last hits
		res[1][3] = direLastHits / duration;
		// average dire denies
		res[1][4] = direDenies / duration;
		// average dire hero damage
		res[1][5] = direHeroDamage / duration;
		// average dire tower damage
		res[1][6] = direTowerDamage / duration;
		// dire win
		if (!radiantWin)
			res[0][7] = 1;

		// average kills
		res[2][0] = radiantKills + direKills / duration;
		// average deaths
		res[2][1] = radiantDeaths + direDeaths / duration;
		// average assists
		res[2][2] = radiantAssists + direAssists / duration;
		// average last hits
		res[2][3] = radiantLastHits + direLastHits / duration;
		// average denies
		res[2][4] = radiantDenies + direDenies / duration;
		// average hero damage
		res[2][5] = radiantHeroDamage + direHeroDamage / duration;
		// average tower damage
		res[2][6] = radiantTowerDamage + direTowerDamage / duration;

		return res;
	}
}
