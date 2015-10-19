import java.io.IOException;

import Util.MatchDataCrawler;

public class FAQMinerMain {
	public static void main(String[] args) throws IOException {
		int match_id = 1878831789;
		for (int i = 0; i < 500; i++) {
			match_id--;
			MatchDataCrawler.sendGET(String.valueOf(match_id));
		}
	}
}
