import java.util.ArrayList;
import java.util.List;

import algoritms.*;
import dataobject.MatchObject;
import util.Constants;
import util.Converter;

public class FAQMinerMain {
	public static void main(String[] args) {
		String matches = "data/rawdata";
		// converter
		Converter c = new Converter(matches);
		List<MatchObject> mObjects = new ArrayList<MatchObject>();
		mObjects = c.convert();
		System.out.println("Data format convertion completed!");
		System.out.println();

		double accuracy = 0;
		// logistic 1
		System.out.println("Logistic regression 1:");
		accuracy = 0.0;
		LogisticRegression1 l1 = new LogisticRegression1(mObjects);
		for (int i = 0; i < Constants.CROSSNUMBER; i++) {
			System.out.println("Cross " + (i + 1) + ": ");
			int[][] trainMatrix = l1.getTrainMatrix(i);
			int[][] testMatrix = l1.getTestMatrix(i);
			l1.train(trainMatrix);
			double tmpAccuracy = l1.test(testMatrix);
			System.out.println("Accuracy: " + tmpAccuracy);
			accuracy += tmpAccuracy;
		}
		System.out.println("Average accuracy: " + (double) accuracy / Constants.CROSSNUMBER);
		System.out.println("Logistic regression 1 completed!");
		System.out.println("---------------------------------------");
		System.out.println();

		// logistic 2
		System.out.println("Logistic regression 2:");
		accuracy = 0.0;
		LogisticRegression2 l2 = new LogisticRegression2(mObjects);
		for (int i = 0; i < Constants.CROSSNUMBER; i++) {
			System.out.println("Cross " + (i + 1) + ": ");
			List<MatchObject> trainMatches = l2.getTrainMatches(i);
			List<MatchObject> testMatches = l2.getTestMatches(i);
			l2.train(trainMatches);
			double tmpAccuracy = l2.test(testMatches);
			System.out.println("Accuracy: " + tmpAccuracy);
			accuracy += tmpAccuracy;
		}
		System.out.println("Average accuracy: " + (double) accuracy / Constants.CROSSNUMBER);
		System.out.println("Logistic regression 2 completed!");
		System.out.println("---------------------------------------");
		System.out.println();

		// KNN
		System.out.println("KNN:");
		accuracy = 0.0;
		KNN k = new KNN(mObjects);
		for (int i = 0; i < Constants.CROSSNUMBER; i++) {
			System.out.println("Cross " + (i + 1) + ": ");
			List<MatchObject> trainMatches = k.getTrainMatches(i);
			List<MatchObject> testMatches = k.getTestMatches(i);
			double tmpAccuracy = k.classify(trainMatches, testMatches);
			System.out.println("Accuracy: " + tmpAccuracy);
			accuracy += tmpAccuracy;
		}
		System.out.println("Average accuracy: " + (double) accuracy / Constants.CROSSNUMBER);
		System.out.println("KNN completed!");
		System.out.println("---------------------------------------");

	}
}
