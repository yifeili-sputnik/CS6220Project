import java.util.ArrayList;
import java.util.List;

import DataObject.MatchObject;
import DataObject.Player;
import Util.Constants;
import Util.Converter;

public class LogisticRegression1 {
	private static double learning_rate = 0.001;
	// minus the label column
	double[] weights = new double[Constants.Cols - 1];
	int[][] trainMatrix = null;
	int[][] testMatrix = null;

	public void getMatrix(int crossNumber) {
		// Convert json to POJO
		String matches = "data/rawdata";
		Converter c = new Converter(matches);
		List<MatchObject> mObjects = new ArrayList<MatchObject>();
		mObjects = c.convert();

		// split to train and test sets
		List<MatchObject> trainObjects = new ArrayList<MatchObject>();
		List<MatchObject> testObjects = new ArrayList<MatchObject>();

		for (int j = 0; j < mObjects.size(); j++) {
			MatchObject match = mObjects.get(j);
			if (j % crossNumber == 1) {
				testObjects.add(match);
			} else {
				trainObjects.add(match);
			}
		}
		// construct the match description matrix
		int trainMatchNums = trainObjects.size();
		int testMatchNums = testObjects.size();

		// train matrix
		int[][] trainMatrix = new int[trainMatchNums][Constants.Cols];
		int matchCounter = 0;
		for (MatchObject m : trainObjects) {
			// hero id
			for (Player p : m.getResult().getPlayers()) {
				int heroID = p.getHero_id();
				int playerSlot = p.getPlayer_slot();
				if (playerSlot <= 4) {
					trainMatrix[matchCounter][heroID - 1] = 1;
				} else {
					trainMatrix[matchCounter][heroID - 1 + 112] = 1;
				}
			}
			// win/loss
			if (m.getResult().isRadiant_win()) {
				trainMatrix[matchCounter][Constants.Cols - 1] = 1;
			}
			trainMatrix[matchCounter][Constants.Cols - 2] = 1;
			// next match
			matchCounter++;
		}
		this.trainMatrix = trainMatrix;
		matchCounter = 0;

		// test matrix
		int[][] testMatrix = new int[testMatchNums][Constants.Cols];
		for (MatchObject m : testObjects) {
			// hero id
			for (Player p : m.getResult().getPlayers()) {
				int heroID = p.getHero_id();
				int playerSlot = p.getPlayer_slot();
				if (playerSlot <= 4) {
					testMatrix[matchCounter][heroID - 1] = 1;
				} else {
					testMatrix[matchCounter][heroID - 1 + 112] = 1;
				}
			}
			// win/loss
			if (m.getResult().isRadiant_win()) {
				testMatrix[matchCounter][Constants.Cols - 1] = 1;
			}
			testMatrix[matchCounter][Constants.Cols - 2] = 1;
			// next match
			matchCounter++;
		}
		this.testMatrix = testMatrix;

		// for (int i = 0; i < trainMatrix.length; i++) {
		// for (int j = 0; j < trainMatrix[0].length; j++) {
		// System.out.print(trainMatrix[i][j] + " ");
		// }
		// System.out.println();
		// }
		// System.out.println("-------------");
		// for (int i = 0; i < testMatrix.length; i++) {
		// for (int j = 0; j < testMatrix[0].length; j++) {
		// System.out.print(testMatrix[i][j] + " ");
		// }
		// System.out.println();
		// }
	}

	public void train() {
		// first derivative
		double[] firstDerivative = new double[Constants.Cols - 1];
		double prelkh = 0.0;
		double currlkh = 0.0;
		while (true) {
			for (int i = 0; i < trainMatrix.length; i++) {
				double xb = 0;
				for (int j = 0; j < Constants.Cols - 1; j++) {
					xb += trainMatrix[i][j] * weights[j];
				}
				double prob = Math.exp(xb) / (1 + Math.exp(xb));
				for (int j = 0; j < Constants.Cols - 1; j++) {
					firstDerivative[j] += trainMatrix[i][j] * (trainMatrix[i][Constants.Cols - 1] - prob);
				}
			}
			// update weights
			for (int j = 0; j < Constants.Cols - 1; j++) {
				weights[j] += learning_rate * firstDerivative[j];
			}
			// likelihood
			currlkh = 0.0;
			for (int i = 0; i < trainMatrix.length; i++) {
				double xb = 0.0;
				for (int j = 0; j < Constants.Cols - 1; j++) {
					xb += trainMatrix[i][j] * weights[j];
				}
				currlkh += (trainMatrix[i][Constants.Cols - 1] * xb - Math.log(1 + Math.exp(xb)));
			}
			 System.out.println(currlkh);

			if (Math.abs(currlkh - prelkh) < 0.01)
				break;
		}
		System.out.println(currlkh);
	}

	public double test() {
		double accuracy = 0.0;
		int counter = 0;
		int output = -1;
		for (int i = 0; i < testMatrix.length; i++) {
			double xb = 0.0;
			for (int j = 0; j < Constants.Cols - 1; j++) {
				xb += testMatrix[i][j] * weights[j];
			}
			double prob = Math.exp(xb) / (1 + Math.exp(xb));
			if (prob > 0.5) {
				output = 1;
			} else {
				output = 0;
			}
			if (output == testMatrix[i][Constants.Cols - 1]) {
				counter++;
			}
		}
		accuracy = (double) counter / testMatrix.length;
		return accuracy;
	}

	public static void main(String[] args) {
		LogisticRegression1 l = new LogisticRegression1();
		l.getMatrix(5);
		l.train();
		System.out.println(l.test());
	}
}
