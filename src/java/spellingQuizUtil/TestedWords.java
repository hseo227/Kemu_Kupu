package spellingQuizUtil;

import java.util.ArrayList;

public class TestedWords {
	
	private static ArrayList<String> testedWords = new ArrayList<>();
	private static ArrayList<WordState> testedWordsState = new ArrayList<>();
	private static WordState currentWordState;
	
	public enum WordState {
	    CORRECT, INCORRECT
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
	}
	
}
