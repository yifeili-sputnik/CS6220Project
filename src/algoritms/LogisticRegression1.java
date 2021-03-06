package algoritms;

//import java.util.ArrayList;
import java.util.List;

import dataobject.MatchObject;
import dataobject.Player;
import util.Constants;
//import util.Converter;

public class LogisticRegression1 {
	private static double learning_rate = 0.00001;
	// minus the label column
	double[] weights = new double[Constants.Cols1 - 1];
	int[][] matrix = null;

	public LogisticRegression1(List<MatchObject> mObjects) {
		// construct the match description matrix
		int matchNums = mObjects.size();

		int[][] matrix = new int[matchNums][Constants.Cols1];
		int matchCounter = 0;
		for (MatchObject m : mObjects) {
			// hero id
			for (Player p : m.getResult().getPlayers()) {
				int heroID = p.getHero_id();
				int playerSlot = p.getPlayer_slot();
				if (playerSlot <= 4) {
					matrix[matchCounter][heroID - 1] = 1;
				} else {
					matrix[matchCounter][heroID - 1 + 112] = 1;
				}
			}
			// win/loss
			if (m.getResult().isRadiant_win()) {
				matrix[matchCounter][Constants.Cols1 - 1] = 1;
			}
			// constants
			matrix[matchCounter][Constants.Cols1 - 2] = 1;
			// next match
			matchCounter++;
		}
		this.matrix = matrix;
	}

	public int[][] getTrainMatrix(int crossNum) {
		int trainMatrixLen = 0;
		for (int i = 0; i < matrix.length; i++) {
			if (i % Constants.CROSSNUMBER != crossNum) {
				trainMatrixLen++;
			}
		}
		int[][] trainMatrix = new int[trainMatrixLen][Constants.Cols1];
		int counter = 0;
		for (int i = 0; i < matrix.length; i++) {
			if (i % Constants.CROSSNUMBER != crossNum) {
				for (int j = 0; j < Constants.Cols1; j++) {
					trainMatrix[counter][j] = matrix[i][j];
				}
				counter++;
			}
		}
		return trainMatrix;
	}

	public int[][] getTestMatrix(int crossNum) {
		int testMatrixLen = 0;
		for (int i = 0; i < matrix.length; i++) {
			if (i % Constants.CROSSNUMBER == crossNum) {
				testMatrixLen++;
			}
		}
		int[][] testMatrix = new int[testMatrixLen][Constants.Cols1];
		int counter = 0;
		for (int i = 0; i < matrix.length; i++) {
			if (i % Constants.CROSSNUMBER == crossNum) {
				for (int j = 0; j < Constants.Cols1; j++) {
					testMatrix[counter][j] = matrix[i][j];
				}
				counter++;
			}
		}
		return testMatrix;
	}

	public void train(int[][] trainMatrix) {
		// first derivative
		double[] firstDerivative = new double[Constants.Cols1 - 1];
		double prelkh = 0.0;
		double currlkh = 0.0;
		// double L1R = 0.0;
		while (true) {
			for (int i = 0; i < trainMatrix.length; i++) {
				double xb = 0;
				for (int j = 0; j < Constants.Cols1 - 1; j++) {
					xb += trainMatrix[i][j] * weights[j];
				}
				double prob = Math.exp(xb) / (1 + Math.exp(xb));
				for (int j = 0; j < Constants.Cols1 - 1; j++) {
					firstDerivative[j] += trainMatrix[i][j] * (trainMatrix[i][Constants.Cols1 - 1] - prob);
				}
			}
			// update weights
			for (int j = 0; j < Constants.Cols1 - 1; j++) {
				weights[j] += learning_rate * firstDerivative[j];
			}

			// L1 regularization
			// for (int j = 0; j < Constants.Cols1 - 1; j++) {
			// L1R += weights[j];
			// }
			// likelihood
			currlkh = 0.0;
			for (int i = 0; i < trainMatrix.length; i++) {
				double xb = 0.0;
				for (int j = 0; j < Constants.Cols1 - 1; j++) {
					xb += trainMatrix[i][j] * weights[j];
				}
				currlkh += (trainMatrix[i][Constants.Cols1 - 1] * xb - Math.log(1 + Math.exp(xb)));
			}
			// currlkh += 1.0 / 5 * L1R;
			// System.out.println(currlkh);

			if (Math.abs(currlkh - prelkh) / currlkh < 1e-6)
				break;

			prelkh = currlkh;
		}
		System.out.println("MLE: " + currlkh);
	}

	public double test(int[][] testMatrix) {
		double accuracy = 0.0;
		int counter = 0;
		int output = -1;
		for (int i = 0; i < testMatrix.length; i++) {
			double xb = 0.0;
			for (int j = 0; j < Constants.Cols1 - 1; j++) {
				xb += testMatrix[i][j] * weights[j];
			}
			double prob = Math.exp(xb) / (1 + Math.exp(xb));
			if (prob > 0.5) {
				output = 1;
			} else {
				output = 0;
			}
			if (output == testMatrix[i][Constants.Cols1 - 1]) {
				counter++;
			}
		}
		accuracy = (double) counter / testMatrix.length;
		return accuracy;
	}

	// public static void main(String[] args) {
	// String matches = "data/rawdata";
	// // converter
	// Converter c = new Converter(matches);
	// List<MatchObject> mObjects = new ArrayList<MatchObject>();
	// mObjects = c.convert();
	// double accuracy = 0;
	// LogisticRegression1 l = new LogisticRegression1(mObjects);
	// for (int i = 0; i < Constants.CROSSNUMBER; i++) {
	// System.out.println("Cross " + (i + 1) + ": ");
	// int[][] trainMatrix = l.getTrainMatrix(i);
	// int[][] testMatrix = l.getTestMatrix(i);
	// l.train(trainMatrix);
	// double tmpAccuracy = l.test(testMatrix);
	// System.out.println("Accuracy: " + tmpAccuracy);
	// accuracy += tmpAccuracy;
	// }
	// System.out.println("Average accuracy: " + (double) accuracy /
	// Constants.CROSSNUMBER);
	// }
}
