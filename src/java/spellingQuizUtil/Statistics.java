package spellingQuizUtil;

import java.util.ArrayList;

public class Statistics {
	
	private static ArrayList<String> testedWords = new ArrayList<>();
	private static ArrayList<Result> wordResult = new ArrayList<>();
	private static ArrayList<Integer> score = new ArrayList<>();
	private static ArrayList<Integer> time = new ArrayList<>();
	
	public static void addCorrect(String word) {
		testedWords.add(word);
		testedWordsState.add(WordState.CORRECT);
	}
	
	public static void addIncorrect(String word) {
		testedWords.add(word);
		testedWordsState.add(WordState.INCORRECT);
	}

	public static String get(int i) {
		return testedWords.get(i);
	}
	
	public static void reset() {
		testedWords.clear();
		testedWordsState.clear();
	}
	
}
