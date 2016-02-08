package util;

public class Constants {
	public static final String API_KEY = "28C5E9EC3BD0AB9373AD5A709766A094";

	public static final String API_STRING = "&key=" + Constants.API_KEY;
	public static final String BASE_URL = "https://api.steampowered.com/IDOTA2Match_570/GetMatchDetails/V001/?match_id=";

	// CROSS NUMBER
	public static final int CROSSNUMBER = 10;

	// 126*2+1+1
	// 126 heroes + 126 heros + constant + label
	public static final int Cols1 = 254;

	// 126+1+1
	// 126 heroes + constant + frequent patterns + label
	public static final int Cols2 = 128;

	// constant hero number
	public static final int HERONUM = 126;
}
