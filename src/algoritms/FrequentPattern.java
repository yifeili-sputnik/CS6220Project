package algoritms;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.opencsv.CSVReader;

import dataobject.MatchObject;
import dataobject.Player;
import dataobject.Result;
import util.Constants;
//import util.Converter;

public class FrequentPattern {
	private int minSup;
	int[][] winners;
	private int playerNum = 5;

	public List<Integer> Len1Patterns;
	public List<List<Integer>> Len2Patterns;
	public List<List<Integer>> Len3Patterns;
	public List<List<Integer>> Len4Patterns;
	public List<List<Integer>> Len5Patterns;

	public FrequentPattern(List<String[]> outcomes, List<String[]> redchampions, List<String[]> bluechampions,
			int minsup) {
		this.minSup = minsup;
		this.winners = findWinners(outcomes, redchampions, bluechampions);
		Len1Patterns = new ArrayList<Integer>();
	}

	// Find out the winner of each match, and put them in a n*5 matrix
	private int[][] findWinners(List<String[]> outcomes, List<String[]> redchampions, List<String[]> bluechampions) {
		int[][] winners = new int[outcomes.size()][this.playerNum];
		for (int i = 0; i < outcomes.size(); i++) {
			int[] winner = new int[5];
			if (Integer.valueOf(outcomes.get(i)[0]) == 1) {
				String[] reds = redchampions.get(i);
				for (int j = 0; j < 5; j++) {
					winner[j] = Integer.valueOf(reds[j]);
				}
			} else {
				String[] blues = bluechampions.get(i);
				for (int j = 0; j < 5; j++) {
					winner[j] = Integer.valueOf(blues[j]);
				}
			}
			winners[i] = winner;
		}
		return winners;
	}

	/***
	 * // Helper function for findWinners(), find winners for one match private
	 * int[] findWinSide(boolean rWin, List<Player> players) { int[] winner =
	 * new int[this.playerNum]; for (Player p : players) { if (rWin &&
	 * p.getPlayer_slot() <= 4) { winner[p.getPlayer_slot()] = p.getHero_id(); }
	 * else if (!rWin && p.getPlayer_slot() > 4) { winner[p.getPlayer_slot() -
	 * 128] = p.getHero_id(); } } return winner; }
	 ***/

	public void Apriori() {
		List<List<Integer>> C = new ArrayList<List<Integer>>();
		List<List<Integer>> L = new ArrayList<List<Integer>>();
		int patternLen = 1;
		// initial data: find 1-itemset
		int[] initSet = new int[Constants.HERONUM];
		for (int i = 0; i < this.winners.length; i++) {
			for (int j = 0; j < this.playerNum; j++) {
				int heroId = this.winners[i][j];
				initSet[heroId]++;
			}
		}

		for (int i = 0; i < initSet.length; i++) {
			if (initSet[i] >= this.minSup) {
				// System.out.print(initSet[i] + ", ");
				List<Integer> item = new ArrayList<Integer>();
				item.add(i + 1);
				this.Len1Patterns.add(i + 1);
				L.add(item);
			}
		}
		// System.out.println();

		System.out.println("Len1 patterns found!");
		patternLen++;

		while (patternLen <= 5) {
			// find C(k) from L(k-1)
			// join
			C = findCandidate(L);
			// prunning
			C = pruning(C, L);
			L = TestCandidate(C);
			switch (patternLen) {
			case 2:
				this.Len2Patterns = L;
				System.out.println("Len2 patterns found!");
				break;
			case 3:
				this.Len3Patterns = L;
				System.out.println("Len3 patterns found!");
				break;
			case 4:
				this.Len4Patterns = L;
				System.out.println("Len4 patterns found!");
				break;
			case 5:
				this.Len5Patterns = L;
				System.out.println("Len5 patterns found!");
				break;
			}
			patternLen++;
		}
	}

	// Helper function for Apriori, join step for finding the C(k) candidates
	// from L(k-1)
	private List<List<Integer>> findCandidate(List<List<Integer>> L) {
		List<List<Integer>> C = new ArrayList<List<Integer>>();
		for (int i = 0; i < L.size(); i++) {
			List<Integer> itemset_1 = L.get(i);
			for (int j = i + 1; j < L.size(); j++) {
				List<Integer> itemset_2 = L.get(j);
				if (compareFirstkminus2items(itemset_1, itemset_2)
						&& itemset_1.get(itemset_1.size() - 1) < itemset_2.get(itemset_2.size() - 1)) {
					List<Integer> newItemSet = new ArrayList<Integer>(itemset_1);
					newItemSet.add(itemset_2.get(itemset_2.size() - 1));
					C.add(newItemSet);
				}
			}
		}
		return C;
	}

	// Helper function for findCandidate(), compare first k-2 items in itemset.
	private boolean compareFirstkminus2items(List<Integer> A, List<Integer> B) {
		boolean result = true;
		if (A.size() != B.size())
			return false;
		if (A == null || B == null)
			return false;
		for (int i = 0; i < A.size() - 1; i++) {
			result = result && (A.get(i) == B.get(i));
		}
		return result;
	}

	// Helper function for Apriori, pruning step of finding the C(k) candidates
	// from L(k-1)
	private List<List<Integer>> pruning(List<List<Integer>> c, List<List<Integer>> l) {
		Predicate<List<Integer>> prunSublist = (itemset) -> isNotMember(itemset, l);
		c.removeIf(prunSublist);
		return c;
	}

	private boolean isNotMember(List<Integer> set, List<List<Integer>> fpItemset) {
		boolean result = true;
		for (int i = 0; i < set.size(); i++) {
			List<Integer> tempSet = new ArrayList<Integer>(set);
			tempSet.remove(i);
			result = result && fpItemset.contains(tempSet);
		}
		return !result;
	}

	// Helper function for Apriori, testing step for test C(k) against data;
	private List<List<Integer>> TestCandidate(List<List<Integer>> candidate_k) {
		List<List<Integer>> fp_k = new ArrayList<List<Integer>>();
		int[] map = new int[candidate_k.size()];
		for (int i = 0; i < this.winners.length; i++) {
			for (int index = 0; index < candidate_k.size(); index++) {
				if (testContain(this.winners[i], candidate_k.get(index))) {
					map[index]++;
				}
			}
		}
		for (int index = 0; index < map.length; index++) {
			if (map[index] >= this.minSup)
				fp_k.add(candidate_k.get(index));
		}
		return fp_k;
	}

	// Helper function for TestCandidate(), test whether each itemset contains a
	// frequent parttern
	private boolean testContain(int[] is, List<Integer> list) {
		boolean result = true;
		List<Integer> isCopy = new ArrayList<Integer>();
		for (int i = 0; i < is.length; i++) {
			int id = is[i];
			isCopy.add(id);
		}
		for (int index = 0; index < list.size(); index++) {
			int id = list.get(index);
			result = result && isCopy.contains(id);
		}
		return result;
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		CSVReader reader;
		reader = new CSVReader(new FileReader("data/matchoutcome.csv"));
		List<String[]> outcomes = reader.readAll();
		reader = new CSVReader(new FileReader("data/BlueChampion.csv"));
		List<String[]> bluechampions = reader.readAll();
		reader = new CSVReader(new FileReader("data/RedChampion.csv"));
		List<String[]> redchampions = reader.readAll();

		FrequentPattern fp = new FrequentPattern(outcomes, bluechampions, redchampions, (int) outcomes.size() / 50);

		fp.Apriori();

		System.out.println();

	}

	// public static void main(String[] args) {
	// String matches = "data/rawdata";
	// // converter
	// Converter c = new Converter(matches);
	// List<MatchObject> mObjects = new ArrayList<MatchObject>();
	// mObjects = c.convert();
	// FrequentPattern fp = new FrequentPattern(mObjects, (int) (mObjects.size()
	// * 5 / 112 / 3));
	// fp.Apriori();
	//
	// System.out.println();
	//
	// }
}
