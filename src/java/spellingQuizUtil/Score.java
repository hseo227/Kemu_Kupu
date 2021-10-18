package spellingQuizUtil;

/**
 * This class handles the scoring system
 */
public class Score {
	private static final int SCORE_INCREASE = 10;
	private static int score, totalScore;

	/**
	 * Constructor
	 * Reset score to 0
	 * Also, reset totalScore and change it based on the number of questions
	 * e.g. If there are 5 questions in quiz, then the totalScore is 100 = 2 * 10 * 5
	 *
	 * @param numOfQuestions Number of questions
	 */
	public Score(int numOfQuestions) {
		score = 0;
		totalScore = 2 * SCORE_INCREASE * numOfQuestions;
	}

	/**
	 * Increase the score with respective result.
	 * Increase the score if faulted and double the score if mastered
	 * @param currentResult The result of the current word
	 * @return The amount of score is increased
	 */
	public int increaseScore(Result currentResult) {
		int scoreIncreased = SCORE_INCREASE;

		if (currentResult == Result.MASTERED) {
			scoreIncreased *= 2;
		}
		score += scoreIncreased;
		return scoreIncreased;
	}

	/**
	 * score's getter
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

}
