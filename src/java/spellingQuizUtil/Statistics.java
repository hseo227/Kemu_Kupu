package spellingQuizUtil;

import java.util.ArrayList;

/**
 * This class handles the statistics system
 */
public class Statistics {

	private static final ArrayList<String> testedWords = new ArrayList<>();
	private static final ArrayList<String> wordResult = new ArrayList<>();
	private static final ArrayList<Integer> wordScore = new ArrayList<>();
	private static final ArrayList<Integer> wordTime = new ArrayList<>();

	/**
	 * Add and store the current word's statistics
	 *
	 * @param word   Current word
	 * @param result Result of the current word
	 * @param score  Score of the current word
	 * @param time   Time taken of the current word
	 */
	public static void addStatistics(String word, Result result, int score, int time) {
		testedWords.add(word);
		wordResult.add(result.name().toLowerCase());
		wordScore.add(score);
		wordTime.add(time);
	}

	/**
	 * Reset the statistics
	 */
	public static void reset() {
		testedWords.clear();
		wordResult.clear();
		wordScore.clear();
		wordTime.clear();
	}

	/**
	 * The following 4 methods are getters of
	 * 		testedWords
	 * 		wordResult
	 * 		wordScore
	 * 		wordTime
	 */
	public static ArrayList<String> getTestedWords() {
		return testedWords;
	}

	public static ArrayList<String> getWordResult() {
		return wordResult;
	}

	public static ArrayList<Integer> getWordScore() {
		return wordScore;
	}

	public static ArrayList<Integer> getWordTime() {
		return wordTime;
	}

	/**
	 * Get total time taken by summing up all the individual word's time taken
	 *
	 * @return Total time taken to finished the whole game
	 */
	public static int getTotalTime() {
		int sum = 0;
		for (int i : wordTime) {
			sum += i;
		}
		return sum;
	}

}
