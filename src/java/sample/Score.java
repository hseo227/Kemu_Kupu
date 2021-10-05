package sample;

public class Score {

	private static int score, totalScore;

	// reset score after each round
	public static void reset() {
		score = 0;
	}

	// for increasing the score when correct on first try
	public static void increase20() {
		score += 20;
	}

	// for increasing the score when correct on second try
	public static void increase10() {
		score += 10;
	}

	// score getter
	public static int getScore() {
		return score;
	}

	// totalScore getter
	public static int getTotalScore() {
		return score;
	}

	// change totalScore base on the number of questions
	public static void changeTotalScore(int numOfQuestions) {
		totalScore = 20 * numOfQuestions;
	}

}
