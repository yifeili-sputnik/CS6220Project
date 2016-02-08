package algoritms;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVReader;

import util.Constants;
//import util.Converter;

public class LogisticRegression2 {
	private static double learning_rate = 0.00001;
	// minus the label column
	double[] weights = new double[Constants.Cols2 - 1];

	public void train(List<String[]> outcomes, List<String[]> redchampions, List<String[]> bluechampions) {
		// construct train matrix
		int teamNumber = outcomes.size() * 2;
		// FrequentPattern fp = new FrequentPattern(mObjects, (int)
		// (mObjects.size() * 5 / 112 / 3));
		// fp.Apriori();

		int[][] trainMatrix = new int[teamNumber][Constants.Cols2];
		int counter = 0;

		while (counter < outcomes.size()) {
			String[] blues = bluechampions.get(counter);
			String[] reds = redchampions.get(counter);
			String[] outcome = outcomes.get(counter);
			for (String s : blues) {
				int index = Integer.valueOf(s);
				trainMatrix[counter * 2][index] = 1;
			}
			for (String s : reds) {
				int index = Integer.valueOf(s);
				trainMatrix[counter * 2 + 1][index] = 1;
			}
			// win/loss
			if (Integer.valueOf(outcome[0]) == 1) {
				trainMatrix[counter * 2 + 1][Constants.Cols2 - 1] = 1;
			} else {
				trainMatrix[counter * 2][Constants.Cols2 - 1] = 1;
			}
			// constants
			trainMatrix[counter * 2][Constants.Cols2 - 2] = 1;
			trainMatrix[counter * 2 + 1][Constants.Cols2 - 2] = 1;

			// int radiantFPs = 0;
			// int direFPs = 0;
			// Length 1 frequent pattern
			// System.out.println("Searching for L1 frequent patterns!" +
			// PatternSearchCounter);
			// radiantFPs = findLen1Patterns(radiantTeam, fp.Len1Patterns);
			// direFPs = findLen1Patterns(direTeam, fp.Len1Patterns);
			// System.out.println("Radiant L1 patterns: " + radiantFPs + "; " +
			// "Dire L1 patterns; " + direFPs);
			// trainMatrix[counter][Constants.Cols2 - 4] = radiantFPs;
			// trainMatrix[counter + 1][Constants.Cols2 - 4] = direFPs;

			// Length 2 frequent pattern
			// System.out.println("Searching for L2 frequent patterns!" +
			// PatternSearchCounter);
			// radiantFPs = findLen2Patterns(radiantTeam, fp.Len2Patterns);
			// direFPs = findLen2Patterns(direTeam, fp.Len2Patterns);
			// System.out.println("Radiant L2 patterns: " + radiantFPs + "; " +
			// "Dire L2 patterns; " + direFPs);
			// trainMatrix[counter][Constants.Cols2 - 3] = radiantFPs;
			// trainMatrix[counter + 1][Constants.Cols2 - 3] = direFPs;

			// next match
			counter++;
		}

		// first derivative
		double[] firstDerivative = new double[Constants.Cols2 - 1];
		double prelkh = 0.0;
		double currlkh = 0.0;
		// double L1R = 0.0;
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

			// L1 regularization
			// for (int j = 0; j < Constants.Cols2 - 1; j++) {
			// L1R += weights[j];
			// }
			// likelihood
			currlkh = 0.0;
			for (int i = 0; i < trainMatrix.length; i++) {
				double xb = 0.0;
				for (int j = 0; j < Constants.Cols2 - 1; j++) {
					xb += trainMatrix[i][j] * weights[j];
				}
				currlkh += (trainMatrix[i][Constants.Cols2 - 1] * xb - Math.log(1 + Math.exp(xb)));
			}
			// currlkh += 1.0 / 5 * L1R;
			// System.out.println(currlkh);

			if (Math.abs(currlkh - prelkh) / currlkh < 1e-6)
				break;

			prelkh = currlkh;
		}
		System.out.println("MLE: " + currlkh);
	}

	/***
	 * public int findLen1Patterns(List<Integer> team, List<Integer> patterns) {
	 * int counter = 0; for (int i = 0; i < team.size(); i++) { if
	 * (patterns.contains(team.get(i))) counter++; } return counter; }
	 * 
	 * public int findLen2Patterns(List<Integer> team, List<List<Integer>>
	 * patterns) { int counter = 0; for (int i = 0; i < team.size(); i++) { for
	 * (int j = i; j < team.size(); j++) { List<Integer> tmpSet = new ArrayList
	 * <Integer>(); tmpSet.add(team.get(i)); tmpSet.add(team.get(j)); if
	 * (patterns.contains(tmpSet)) counter++; } } return counter; }
	 ***/

	public double test(List<String[]> outcomes, List<String[]> redchampions, List<String[]> bluechampions) {
		double accuracy = 0.0;
		int counter = 0;
		int match = 0;
		while (match < outcomes.size()) {
			int[] red = new int[Constants.Cols2 - 1];
			int[] blue = new int[Constants.Cols2 - 1];
			red[Constants.Cols2 - 2] = 1;
			blue[Constants.Cols2 - 2] = 1;
			int output;

			String[] blues = bluechampions.get(match);
			String[] reds = redchampions.get(match);
			String[] outcome = outcomes.get(match);
			for (String s : blues) {
				int index = Integer.valueOf(s);
				blue[index] = 1;
			}
			for (String s : reds) {
				int index = Integer.valueOf(s);
				red[index] = 1;
			}

			double redXB = 0.0;
			double blueXB = 0.0;
			for (int j = 0; j < Constants.Cols2 - 1; j++) {
				redXB += red[j] * weights[j];
				blueXB += blue[j] * weights[j];
			}
			double redWin = Math.exp(redXB - blueXB) / (1 + Math.exp(redXB - blueXB));
			if (redWin > 0.5) {
				output = 1;
			} else {
				output = 0;
			}
			if (output == Integer.valueOf(outcome[0]))
				counter++;
			match++;
		}
		accuracy = (double) counter / outcomes.size();
		return accuracy;
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
		reader = new CSVReader(new FileReader("data/testMatchOutcome.csv"));
		List<String[]> testoutcomes = reader.readAll();
		reader = new CSVReader(new FileReader("data/testBlueChampion.csv"));
		List<String[]> testbluechampions = reader.readAll();
		reader = new CSVReader(new FileReader("data/testRedChampion.csv"));
		List<String[]> testredchampions = reader.readAll();
		LogisticRegression2 l = new LogisticRegression2();
		l.train(outcomes, redchampions, bluechampions);
		System.out.println(l.test(outcomes, redchampions, bluechampions));
	}
	// public static void main(String[] args) {
	// String matches = "data/rawdata";
	// // converter
	// Converter c = new Converter(matches);
	// List<MatchObject> mObjects = new ArrayList<MatchObject>();
	// mObjects = c.convert();
	// double accuracy = 0.0;
	// LogisticRegression2 l = new LogisticRegression2(mObjects);
	// for (int i = 0; i < Constants.CROSSNUMBER; i++) {
	// System.out.println("Cross " + (i + 1) + ": ");
	// List<MatchObject> trainMatches = l.getTrainMatches(i);
	// List<MatchObject> testMatches = l.getTestMatches(i);
	// l.train(trainMatches);
	// double tmpAccuracy = l.test(testMatches);
	// System.out.println("Accuracy: " + tmpAccuracy);
	// accuracy += tmpAccuracy;
	// }
	// System.out.println("Average accuracy: " + (double) accuracy /
	// Constants.CROSSNUMBER);
	// }
}
