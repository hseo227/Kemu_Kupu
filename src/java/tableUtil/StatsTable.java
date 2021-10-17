package tableUtil;

/**
 * StatsTable class for the table of statistics in reward screen
 *
 * Do not change this class
 */

public class StatsTable {
    private final int round;
    private final String word;
    private final String result;
    private final int score;
    private final int time;


    public StatsTable(int round, String word, String result, int score, int time) {
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

    public String getResult() {
        return result;
    }

    public int getScore() {
        return score;
    }

    public int getTime() {
        return time;
    }
}
