import java.io.IOException;

import Util.MatchDataCrawler;

public class FAQMinerMain {
	public static void main(String[] args) throws IOException {
		int match_id = 1852823265;
		for (int i = 0; i < 20; i++) {
			match_id--;
			MatchDataCrawler.sendGET(String.valueOf(match_id));
		}
	}
}
