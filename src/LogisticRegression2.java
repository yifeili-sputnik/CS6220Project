import java.util.ArrayList;
import java.util.List;

import DataObject.MatchObject;
import DataObject.Player;
import Util.Constants;
import Util.Converter;

public class LogisticRegression2 {
	private static double learning_rate = 0.00001;
	private static final int CROSSNUMBER = 10;
	// minus the label column
	double[] weights = new double[Constants.Cols2 - 1];
	List<MatchObject> matches = new ArrayList<MatchObject>();

	public LogisticRegression2(List<MatchObject> mObjects) {
		this.matches = mObjects;
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

	public void train(List<MatchObject> mObjects) {
		// construct train matrix
		int teamNumber = mObjects.size() * 2;
		FrequentPattern fp = new FrequentPattern(mObjects, (int) (mObjects.size() * 5 / 112 / 2 / 1.5));
		fp.Apriori();

		int[][] trainMatrix = new int[teamNumber][Constants.Cols2];
		int counter = 0;

		for (MatchObject m : mObjects) {
			List<Integer> radiantTeam = new ArrayList<Integer>();
			List<Integer> direTeam = new ArrayList<Integer>();
			// hero id
			for (Player p : m.getResult().getPlayers()) {
				int heroID = p.getHero_id();
				int playerSlot = p.getPlayer_slot();
				if (playerSlot <= 4) {
					trainMatrix[counter][heroID - 1] = 1;
					radiantTeam.add(heroID);
				} else {
					trainMatrix[counter + 1][heroID - 1] = 1;
					direTeam.add(heroID);
				}
			}
			// win/loss
			if (m.getResult().isRadiant_win()) {
				trainMatrix[counter][Constants.Cols2 - 1] = 1;
			} else {
				trainMatrix[counter + 1][Constants.Cols2 - 1] = 1;
			}
			// constants
			trainMatrix[counter][Constants.Cols2 - 2] = 1;
			trainMatrix[counter + 1][Constants.Cols2 - 2] = 1;

			int radiantFPs = 0;
			int direFPs = 0;
			// Length 1 frequent pattern
			// System.out.println("Searching for L1 frequent patterns!" +
			// PatternSearchCounter);
			radiantFPs = findLen1Patterns(radiantTeam, fp.Len1Patterns);
			direFPs = findLen1Patterns(direTeam, fp.Len1Patterns);
			// System.out.println("Radiant L1 patterns: " + radiantFPs + "; " +
			// "Dire L1 patterns; " + direFPs);
			trainMatrix[counter][Constants.Cols2 - 4] = radiantFPs;
			trainMatrix[counter + 1][Constants.Cols2 - 4] = direFPs;

			// Length 2 frequent pattern
			// System.out.println("Searching for L2 frequent patterns!" +
			// PatternSearchCounter);
			radiantFPs = findLen2Patterns(radiantTeam, fp.Len2Patterns);
			direFPs = findLen2Patterns(direTeam, fp.Len2Patterns);
			// System.out.println("Radiant L2 patterns: " + radiantFPs + "; " +
			// "Dire L2 patterns; " + direFPs);
			trainMatrix[counter][Constants.Cols2 - 3] = radiantFPs;
			trainMatrix[counter + 1][Constants.Cols2 - 3] = direFPs;

			// next match
			counter += 2;
		}

		// first derivative
		double[] firstDerivative = new double[Constants.Cols2 - 1];
		double prelkh = 0.0;
		double currlkh = 0.0;
		while (true) {
			for (int i = 0; i < trainMatrix.length; i++) {
				double xb = 0;
				for (int j = 0; j < Constants.Cols2 - 1; j++) {
					xb += trainMatrix[i][j] * weights[j];
				}
				double prob = Math.exp(xb) / (1 + Math.exp(xb));
				for (int j = 0; j < Constants.Cols2 - 1; j++) {
					firstDerivative[j] += trainMatrix[i][j] * (trainMatrix[i][Constants.Cols2 - 1] - prob);
				}
			}
			// update weights
			for (int j = 0; j < Constants.Cols2 - 1; j++) {
				weights[j] += learning_rate * firstDerivative[j];
			}
			// likelihood
			currlkh = 0.0;
			for (int i = 0; i < trainMatrix.length; i++) {
				double xb = 0.0;
				for (int j = 0; j < Constants.Cols2 - 1; j++) {
					xb += trainMatrix[i][j] * weights[j];
				}
				currlkh += (trainMatrix[i][Constants.Cols2 - 1] * xb - Math.log(1 + Math.exp(xb)));
			}
			// System.out.println(currlkh);

			if (Math.abs(currlkh - prelkh) / currlkh < 1e-6)
				break;

			prelkh = currlkh;
		}
		System.out.println("MLE: " + currlkh);
	}

	public int findLen1Patterns(List<Integer> team, List<Integer> patterns) {
		int counter = 0;
		for (int i = 0; i < team.size(); i++) {
			if (patterns.contains(team.get(i)))
				counter++;
		}
		return counter;
	}

	public int findLen2Patterns(List<Integer> team, List<List<Integer>> patterns) {
		int counter = 0;
		for (int i = 0; i < team.size(); i++) {
			for (int j = i; j < team.size(); j++) {
				List<Integer> tmpSet = new ArrayList<Integer>();
				tmpSet.add(team.get(i));
				tmpSet.add(team.get(j));
				if (patterns.contains(tmpSet))
					counter++;
			}
		}
		return counter;
	}

	public double test(List<MatchObject> testMatches) {
		double accuracy = 0.0;
		int counter = 0;
		for (MatchObject m : testMatches) {
			int[] radientTeam = new int[Constants.Cols2 - 1];
			int[] direTeam = new int[Constants.Cols2 - 1];
			radientTeam[Constants.Cols2 - 2] = 1;
			direTeam[Constants.Cols2 - 2] = 1;
			boolean output;
			for (Player p : m.getResult().getPlayers()) {
				int heroID = p.getHero_id();
				int playerSlot = p.getPlayer_slot();
				if (playerSlot <= 4) {
					radientTeam[heroID - 1] = 1;
				} else {
					direTeam[heroID - 1] = 1;
				}
			}
			double radiantXB = 0.0;
			double direXB = 0.0;
			for (int j = 0; j < Constants.Cols2 - 1; j++) {
				radiantXB += radientTeam[j] * weights[j];
				direXB += direTeam[j] * weights[j];
			}
			double radiantWin = Math.exp(radiantXB - direXB) / (1 + Math.exp(radiantXB - direXB));
			if (radiantWin > 0.5) {
				output = true;
			} else {
				output = false;
			}
			if (output == m.getResult().isRadiant_win())
				counter++;
		}
		accuracy = (double) counter / testMatches.size();
		return accuracy;
	}

	public static void main(String[] args) {
		String matches = "data/rawdata";
		// converter
		Converter c = new Converter(matches);
		List<MatchObject> mObjects = new ArrayList<MatchObject>();
		mObjects = c.convert();
		double accuracy = 0.0;
		LogisticRegression2 l = new LogisticRegression2(mObjects);
		for (int i = 0; i < CROSSNUMBER; i++) {
			System.out.println("Cross " + (i + 1) + ": ");
			List<MatchObject> trainMatches = l.getTrainMatches(i);
			List<MatchObject> testMatches = l.getTestMatches(i);
			l.train(trainMatches);
			double tmpAccuracy = l.test(testMatches);
			System.out.println("Accuracy: " + tmpAccuracy);
			accuracy += tmpAccuracy;
		}
		System.out.println("Average accuracy: " + (double) accuracy / CROSSNUMBER);
	}
}
