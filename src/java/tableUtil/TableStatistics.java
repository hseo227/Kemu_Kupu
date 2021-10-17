package tableUtil;

import spellingQuizUtil.Result;

/**
 * TableStatistics class for the table of statistics in reward screen
 *
 * Do not change this class
 */

public class TableStatistics {
    private final int round;
    private final String word;
    private final Result result;
    private final int score;
    private final int time;


    public TableStatistics(int round, String word, Result result, int score, int time) {
        this.round = round;
        this.word = word;
        this.result = result;
        this.score = score;
        this.time = time;
    }

    public int getRound() {
        return round;
    }

    public String getWord() {
        return word;
    }

    public Result getResult() {
        return result;
    }

    public int getScore() {
        return score;
    }

    public int getTime() {
        return time;
    }
}
