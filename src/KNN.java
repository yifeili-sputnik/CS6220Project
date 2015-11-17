import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import DataObject.MatchObject;
import DataObject.Player;
import Util.Converter;

public class KNN {
	private static int NEIGHBORNUMBER = 88;
	List<MatchObject> matches = new ArrayList<MatchObject>();
	private static final int CROSSNUMBER = 10;

	public KNN(List<MatchObject> mObjects) {
		this.matches = mObjects;
	}

	public double getSimilarity(MatchObject m1, MatchObject m2) {
		double similarity = 0.0;
		int[] m1Array = objectToArray(m1);
		int[] m2Array = objectToArray(m2);
		// counter same heroes in two matches
		int counter = 0;
		for (int i = 0; i < m1Array.length; i++) {
			if (m1Array[i] == 1 && m2Array[i] == 1) {
				counter++;
			}
		}
		// TO-DO design a better distance formula
		similarity = counter * counter * counter;
		return similarity;
	}

	// convert match object to heroes array
	public int[] objectToArray(MatchObject m) {
		int totalHeroes = 112 * 2;
		int[] array = new int[totalHeroes];
		for (Player p : m.getResult().getPlayers()) {
			int heroID = p.getHero_id();
			int playerSlot = p.getPlayer_slot();
			if (playerSlot <= 4) {
				array[heroID - 1] = 1;
			} else {
				array[heroID - 1 + 112] = 1;
			}
		}
		return array;
	}

	public double classify(List<MatchObject> trainMatches, List<MatchObject> testMatches) {
		// count the number of correct output test matches
		int count = 0;
		for (MatchObject testM : testMatches) {
			/* Store the k-nearest neighbors */
			HashMap<MatchObject, Double> neighbors = new HashMap<MatchObject, Double>();
			for (MatchObject trainM : trainMatches) {
				if (neighbors.size() < NEIGHBORNUMBER) {
					neighbors.put(trainM, getSimilarity(trainM, testM));
				} else {
					double distance = getSimilarity(trainM, testM);
					boolean toAdd = false;
					Iterator<Entry<MatchObject, Double>> iter = neighbors.entrySet().iterator();
					while (iter.hasNext()) {
						Entry<MatchObject, Double> e = iter.next();
						if (e.getValue() < distance) {
							iter.remove();
							toAdd = true;
							break;
						}
					}
					if (toAdd)
						neighbors.put(trainM, distance);
				}
			}
			// count the number of radiant win
			int counter = 0;
			for (Entry<MatchObject, Double> e : neighbors.entrySet()) {
				if (e.getKey().getResult().isRadiant_win())
					counter++;
			}
			// majority voting
			boolean output;
			if (counter >= neighbors.size() / 2) {
				output = true;
			} else {
				output = false;
			}
			if (output == testM.getResult().isRadiant_win())
				count++;
		}
		return (double) count / testMatches.size();
	}

	public List<MatchObject> getTrainMatches(int crossNum) {
		List<MatchObject> res = new ArrayList<MatchObject>();
		for (int i = 0; i < matches.size(); i++) {
			if (i % CROSSNUMBER != crossNum) {
				res.add(matches.get(i));
			}
		}
		return res;
	}

	public List<MatchObject> getTestMatches(int crossNum) {
		List<MatchObject> res = new ArrayList<MatchObject>();
		for (int i = 0; i < matches.size(); i++) {
			if (i % CROSSNUMBER == crossNum) {
				res.add(matches.get(i));
			}
		}
		return res;
	}

	public static void main(String[] args) {
		String matches = "data/rawdata";
		// converter
		Converter c = new Converter(matches);
		List<MatchObject> mObjects = new ArrayList<MatchObject>();
		mObjects = c.convert();
		double accuracy = 0.0;
		KNN k = new KNN(mObjects);
		for (int i = 0; i < CROSSNUMBER; i++) {
			System.out.println("Cross " + (i + 1) + ": ");
			List<MatchObject> trainMatches = k.getTrainMatches(i);
			List<MatchObject> testMatches = k.getTestMatches(i);
			double tmpAccuracy = k.classify(trainMatches, testMatches);
			System.out.println("Accuracy: " + tmpAccuracy);
			accuracy += tmpAccuracy;
		}
		System.out.println("Average accuracy: " + (double) accuracy / CROSSNUMBER);
	}
}
