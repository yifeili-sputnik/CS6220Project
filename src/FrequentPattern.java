import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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
	
	public List<List<Integer>> Apriori(){
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
				L.add(item);
			}
		}
		while(cont < 2){
			//find C(k) from L(k-1)
			//join
			C = findCandidate(L);
			//prunning
			C = pruning(C, L);
			L = TestCandidate(C);
			cont++;
		}
		return C;
	}
	//Helper function for Apriori, join step for finding the C(k) candidates from L(k-1)
	private List<List<Integer>> findCandidate(List<List<Integer>> L){
		List<List<Integer>> C = new ArrayList<List<Integer>>();
		for(int i = 0; i<L.size(); i++){
			List<Integer> itemset_1 = L.get(i);
			for(int j = i+1; j<L.size(); j++){
				List<Integer> itemset_2 = L.get(j);
				if(compare(itemset_1, itemset_2)
						&& itemset_1.get(itemset_1.size() - 1) < itemset_2.get(itemset_2.size()-1)){
					List<Integer> newItemSet = new ArrayList<Integer>(itemset_1);
					newItemSet.add(itemset_2.get(itemset_2.size()-1));
					C.add(newItemSet);
				}
			}
		}
		return C;
	}
	//Helper function for findCandidate(), compare first k-2 items in itemset.
	private boolean compare(List<Integer> A, List<Integer> B){
		boolean result = true;
		if(A.size() != B.size()) return false;
		if(A == null || B == null) return false;
		for(int i = 0; i < A.size()-1; i++){
			result = result && (A.get(i) == B.get(i));
		}
		return result;
	}
	//Helper function for Apriori, pruning step of finding the C(k) candidates from L(k-1)
	private List<List<Integer>> pruning(List<List<Integer>> c, List<List<Integer>> l){
		Predicate<List<Integer>> prunSublist = (itemset) -> isNotMember(itemset, l);
		c.removeIf(prunSublist);
		return c;
	}
	private boolean isNotMember(List<Integer> set, List<List<Integer>> fpItemset){
		boolean result = true;
		for(int i = 0; i<set.size(); i++){
			List<Integer> tempSet = new ArrayList<Integer>(set);
			result = result && fpItemset.contains(tempSet.remove(i));
		}
		return !result;
	}
	//Helper function for Apriori, testing step for test C(k) against data;
	private List<List<Integer>> TestCandidate(List<List<Integer>> candidate_k){
		List<List<Integer>> fp_k = new ArrayList<List<Integer>>();
		
		return fp_k;
	}
}
