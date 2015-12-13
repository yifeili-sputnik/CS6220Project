package dataobject;

import java.util.ArrayList;
import java.util.List;

/*"radiant_win": false,
		"duration": 1914,
		"start_time": 1342739723,
		"match_id": 27110133,
		"match_seq_num": 27106670,
		"tower_status_radiant": 4,
		"tower_status_dire": 1974,
		"barracks_status_radiant": 3,
		"barracks_status_dire": 63,
		"cluster": 131,
		"first_blood_time": 133,
		"lobby_type": 0,
		"human_players": 10,
		"leagueid": 0,
		"positive_votes": 0,
		"negative_votes": 0,
		"game_mode": 0,
		"engine": 0
*/
public class Result {
	List<Player> players = new ArrayList<Player>();
	boolean radiant_win;
	int start_time;
	int match_id;
	int match_seq_num;
	int tower_status_radiant;
	int tower_status_dire;
	int barracks_status_radiant;
	int barracks_status_dire;
	int cluster;
	int first_blood_time;
	int lobby_type;
	int human_players;
	int leaguei;
	int positive_votes;
	int negative_votes;
	int game_mode;
	int engine;

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public boolean isRadiant_win() {
		return radiant_win;
	}

	public void setRadiant_win(boolean radiant_win) {
		this.radiant_win = radiant_win;
	}

	int duration;

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getStart_time() {
		return start_time;
	}

	public void setStart_time(int start_time) {
		this.start_time = start_time;
	}

	public int getMatch_id() {
		return match_id;
	}

	public void setMatch_id(int match_id) {
		this.match_id = match_id;
	}

	public int getMatch_seq_num() {
		return match_seq_num;
	}

	public void setMatch_seq_num(int match_seq_num) {
		this.match_seq_num = match_seq_num;
	}

	public int getTower_status_radiant() {
		return tower_status_radiant;
	}

	public void setTower_status_radiant(int tower_status_radiant) {
		this.tower_status_radiant = tower_status_radiant;
	}

	public int getTower_status_dire() {
		return tower_status_dire;
	}

	public void setTower_status_dire(int tower_status_dire) {
		this.tower_status_dire = tower_status_dire;
	}

	public int getBarracks_status_radiant() {
		return barracks_status_radiant;
	}

	public void setBarracks_status_radiant(int barracks_status_radiant) {
		this.barracks_status_radiant = barracks_status_radiant;
	}

	public int getBarracks_status_dire() {
		return barracks_status_dire;
	}

	public void setBarracks_status_dire(int barracks_status_dire) {
		this.barracks_status_dire = barracks_status_dire;
	}

	public int getCluster() {
		return cluster;
	}

	public void setCluster(int cluster) {
		this.cluster = cluster;
	}

	public int getFirst_blood_time() {
		return first_blood_time;
	}

	public void setFirst_blood_time(int first_blood_time) {
		this.first_blood_time = first_blood_time;
	}

	public int getLobby_type() {
		return lobby_type;
	}

	public void setLobby_type(int lobby_type) {
		this.lobby_type = lobby_type;
	}

	public int getHuman_players() {
		return human_players;
	}

	public void setHuman_players(int human_players) {
		this.human_players = human_players;
	}

	public int getLeaguei() {
		return leaguei;
	}

	public void setLeaguei(int leaguei) {
		this.leaguei = leaguei;
	}

	public int getPositive_votes() {
		return positive_votes;
	}

	public void setPositive_votes(int positive_votes) {
		this.positive_votes = positive_votes;
	}

	public int getNegative_votes() {
		return negative_votes;
	}

	public void setNegative_votes(int negative_votes) {
		this.negative_votes = negative_votes;
	}

	public int getGame_mode() {
		return game_mode;
	}

	public void setGame_mode(int game_mode) {
		this.game_mode = game_mode;
	}

	public int getEngine() {
		return engine;
	}

	public void setEngine(int engine) {
		this.engine = engine;
	}

}
