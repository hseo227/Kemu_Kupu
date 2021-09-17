package sample;

public class Statistics {
    private final String word;
    private final int mastered;
    private final int faulted;
    private final int failed;

    public Statistics(String word, int mastered, int faulted, int failed) {
        this.word = word;
        this.mastered = mastered;
        this.faulted = faulted;
        this.failed = failed;
    }

    public String getWord() {
        return word;
    }

    public int getMastered() {
        return mastered;
    }

    public int getFaulted() {
        return faulted;
    }

    public int getFailed() {
        return failed;
    }
}
