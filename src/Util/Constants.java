package Util;

public class Constants {
	public static final String API_KEY = "28C5E9EC3BD0AB9373AD5A709766A094";

	public static final String API_STRING = "&key=" + Constants.API_KEY;
	public static final String BASE_URL = "https://api.steampowered.com/IDOTA2Match_570/GetMatchDetails/V001/?match_id=";

	// 112*2+1+1
	// 112 heros + 112 heros + constant + label
	public static final int Cols1 = 226;

	// 112+1+1
	public static final int Cols2 = 114;
}
