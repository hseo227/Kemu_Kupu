package spellingQuizUtil;

public class Score {
	private static final int MARK_INCREASE = 10;

	private static int score, totalScore;

	// reset score after each round
	public static void reset() {
		score = 0;
	}

	// increase the score with respect score multiplier
	public static void increaseBy(int scoreMultiplier) {
		score += MARK_INCREASE * scoreMultiplier;
	}

	// score getter
	public static int getScore() {
		return score;
	}

	// totalScore getter
	public static int getTotalScore() {
		return totalScore;
	}

	// change totalScore base on the number of questions
	public static void changeTotalScore(int numOfQuestions) {
		totalScore = 20 * numOfQuestions;
	}

}
