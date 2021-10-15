package spellingQuizUtil;

import java.util.ArrayList;

public class TestedWords {
	
	private static ArrayList<String> testedWords = new ArrayList<>();
	private static WordState currentWordState;
	
	public enum WordState {
	    CORRECT, INCORRECT
	}
	
	public static void addCorrect(String word) {
		testedWords.add(word);
		setWordState(WordState.CORRECT);
	}
	
	public static void addIncorrect(String word) {
		testedWords.add(word);
		setWordState(WordState.INCORRECT);
	}
	
	private static void setWordState(WordState newWordState) {
		currentWordState = newWordState;
	}
	public static void print() {
		for (int i = 0; i < testedWords.size(); i++) {
			System.out.println(testedWords.get(i));
		}
	}
	
	public static void clear() {
		testedWords.clear();
	}
	
}
