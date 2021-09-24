package sample;

public class Score {

	public static int score;

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

}
