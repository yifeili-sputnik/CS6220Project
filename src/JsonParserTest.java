import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

import DataObject.MatchObject;
import DataObject.Player;
import DataObject.Result;

public class JsonParserTest {

	public static void main(String[] args) throws IOException, IOException {
		Gson gson = new Gson();
		try {
			BufferedReader br = new BufferedReader(new FileReader("data/1852823253.json"));
			MatchObject mo = gson.fromJson(br, MatchObject.class);
			Result res = mo.getResult();
			int matchDuration = res.getDuration() / 60;
			int radientTotalKill = 0;
			int radientTotalLH = 0;
			int i = 0;
			for (Player p : res.getPlayers()) {
				if (i >= 5)
					break;
				i++;
				radientTotalKill += p.getKills();
				radientTotalLH += p.getLast_hits();
			}

			System.out.println("Radient win: " + res.isRadiant_win());
			System.out.println("Radient average Kills/min: " + (double) radientTotalKill / matchDuration);
			System.out.println("Radient average Last_hits/min: " + (double) radientTotalLH / matchDuration);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
