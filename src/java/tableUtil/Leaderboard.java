package tableUtil;

/**
 * Leaderboard class for the table of leaderboard in reward screen
 *
 * Do not change this class
 */

public class Leaderboard {
    private final int rank;
    private final String name;
    private final String totalScore;
    private final String totalTime;


    public Leaderboard(int rank, String name, String totalScore, String totalTime) {
        this.rank = rank;
        this.name = name;
        this.totalScore = totalScore;
        this.totalTime = totalTime;
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public String getTotalTime() {
        return totalTime;
    }

    /**
     * Get all the stats in an array
     *
     * @return All stats in an array
     */
    public String[] getAllStats() {
        return new String[]{name, totalScore, totalTime};
    }
}
