package spellingQuizUtil;

import java.util.ArrayList;

public class Statistics {
	
	private static ArrayList<String> testedWords = new ArrayList<>();
	private static ArrayList<Result> wordResult = new ArrayList<>();
	private static ArrayList<Integer> wordScore = new ArrayList<>();
	private static ArrayList<Integer> wordTime = new ArrayList<>();
	
	public static void addStatistics(String word, Result result, int score, int time) {
		testedWords.add(word);
		wordResult.add(result);
		wordScore.add(score);
		wordTime.add(time);
	}
	
	public static void reset() {
		testedWords.clear();
		wordResult.clear();
		wordScore.clear();
		wordTime.clear();
	}

	public static ArrayList<String> getTestedWords() {
		return testedWords;
	}

	public static ArrayList<Result> getWordResult() {
		return wordResult;
	}

	public static ArrayList<Integer> getWordScore() {
		return wordScore;
	}

	public static ArrayList<Integer> getWordTime() {
		return wordTime;
	}

}
