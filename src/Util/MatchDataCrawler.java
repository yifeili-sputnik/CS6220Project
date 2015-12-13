package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class MatchDataCrawler {

	public static void main(String[] args) throws IOException, InterruptedException {
		// get match data from match_id-i to match_id
		int match_id = 1942120639;

		for (int i = 0; i < 3000; i++) {
			match_id--;
			sendGET(String.valueOf(match_id));
			Thread.sleep(new Random().nextInt(500));
		}
		System.out.println("API call finished.");
	}

	public static void sendGET(String match_id) throws IOException {
		URL obj = new URL(Constants.BASE_URL + match_id + Constants.API_STRING);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();

		System.out.println("Match ID :: " + match_id + "; Response Code :: " + responseCode);
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
