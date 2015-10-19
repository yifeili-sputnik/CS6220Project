package Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MatchDataCrawler {
	private static final String API_STRING = "&key=" + Constants.API_KEY;
	private static final String BASE_URL = "https://api.steampowered.com/IDOTA2Match_570/GetMatchDetails/V001/?match_id=";

	public static void main(String[] args) throws IOException {
		// get match data from match_id-i to match_id
		int match_id = 1852823265;
		for (int i = 0; i < 20; i++) {
			match_id--;
			sendGET(String.valueOf(match_id));
		}
	}

	public static void sendGET(String match_id) throws IOException {
		URL obj = new URL(BASE_URL + match_id + API_STRING);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();
		System.out.println("Match ID :: "+ match_id+ "; Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			BufferedWriter bwr = new BufferedWriter(new FileWriter(new File("data/rawdata/" + match_id + ".json")));

			bwr.write(response.toString());
			bwr.flush();
			bwr.close();
		} else {
			System.out.println("GET request not worked");
		}

	}
}
