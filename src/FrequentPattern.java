import java.util.ArrayList;
import java.util.List;

import DataObject.MatchObject;
import DataObject.Player;
import DataObject.Result;
import Util.Constants;

public class FrequentPattern {
	private int minSup;
	int[][] winners;
	private int playerNum;
	public FrequentPattern(List<MatchObject> matches, int minsup){
		this.minSup = minsup;
		this.playerNum = matches.get(0).getResult().getHuman_players() / 2;
		this.winners = findWinners(matches); 
	}
	//Find out the winners of each match, and put them in a n*5 matrix
	private int[][] findWinners(List<MatchObject> matches){
		int[][] winners = new int[matches.size()][this.playerNum];
		for(int i =0; i< matches.size(); i++){
			MatchObject m = matches.get(i);
			Result r = m.getResult();
			int[] winner = findWinSide(r.isRadiant_win(), r.getPlayers());
			winners[i] = winner;
		}
		return winners;
	}
	//Helper function for findWinners(), find winners for one match
	private int[] findWinSide(boolean rWin, List<Player> players){
		int[] winner = new int[this.playerNum];
		if(rWin == true){
			for(Player p: players){
				if(p.getPlayer_slot()<=4){
					winner[p.getPlayer_slot()] = p.getHero_id();
				}
			}
		}
		return winner;
	}
	
	public void Apriori(){
		List<List<Integer>> C = new ArrayList<List<Integer>>();
		List<List<Integer>> L = new ArrayList<List<Integer>>();
		int cont = 0;
		//initial data: find 1-itemset
		int[] initSet = new int[Constants.HERONUM];
		for(int i = 0; i <this.winners.length; i++){
			for(int j =0; j < this.playerNum; j++){
				int heroId = this.winners[i][j];
				initSet[heroId-1]++;
			}
		}
		for(int i = 0; i< initSet.length; i++){
			if(initSet[i] >= this.minSup){
				List<Integer>  item = new ArrayList<Integer>();
				item.add(i);
				C.add(item);
			}
		}
		while(cont < 2){
			cont++;
		}
	}
}
