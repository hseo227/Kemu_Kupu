package spellingQuizUtil;

/**
 * This class handles the scoring system
 */
public class Score {
	private static final int SCORE_INCREASE = 10;
	private static int score, totalScore;

	/**
	 * Reset score to 0 after each round
	 */
	public static void reset() {
		score = 0;
	}

	/**
	 * Increase the score with respective score multiplier
	 * @param scoreMultiplier Tells how much the score is going to increase
	 */
	public static void increaseBy(int scoreMultiplier) {
		score += SCORE_INCREASE * scoreMultiplier;
	}

	/**
	 * score's
	 * 		   getter
	 * @return score
	 */
	public static int getScore() {
		return score;
	}

	/**
	 * totalScore's getter
	 * @return totalScore
	 */
	public static int getTotalScore() {
		return totalScore;
	}

	/**
	 * Change totalScore base on the number of questions
	 * e.g. If there are 5 questions in quiz, then the totalScore is 100 = 2 * 10 * 5
	 * @param numOfQuestions Number of questions
	 */
	public static void changeTotalScore(int numOfQuestions) {
		totalScore = 2 * SCORE_INCREASE * numOfQuestions;
	}

}
