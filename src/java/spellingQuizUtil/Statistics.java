package spellingQuizUtil;

import java.util.ArrayList;

public class Statistics {
	
	private static ArrayList<String> testedWords = new ArrayList<>();
	private static ArrayList<WordState> testedWordsState = new ArrayList<>();
	
	private enum WordState {
	    CORRECT, INCORRECT, PRACTICE
	}
	
	public static void addCorrect(String word) {
		testedWords.add(word);
		testedWordsState.add(WordState.CORRECT);
	}
	
	public static void addIncorrect(String word) {
		testedWords.add(word);
		testedWordsState.add(WordState.INCORRECT);
	}
	
	public static boolean isCorrect(int i) {
		if (testedWordsState.get(i).equals(WordState.CORRECT)) {
			return true;
		} else {
			return false;
		}
	}

	public static String get(int i) {
		return testedWords.get(i);
	}
	
	public static void clear() {
		testedWords.clear();
		testedWordsState.clear();
	}
	
	public static void practiceMode() {

		testedWords.add("");
		testedWords.add("Good Practice!");
		testedWords.add("Attempt a quiz to test your knowledge or");
		testedWords.add("play another practice round to gain confidence!");
		testedWords.add("");
		
		for (int i = 0; i < 5; i++) {
			testedWordsState.add(WordState.PRACTICE);
		}
	}
	
	public static boolean isPracticeMode(int i) {
		if (testedWordsState.get(i).equals(WordState.PRACTICE)) {
			return true;
		} else {
			return false;
		}
	}
	
}
