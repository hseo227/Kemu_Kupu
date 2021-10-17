package spellingQuizUtil;

public class Timer {
    private static long startTime;

    public static void start() {
        startTime = System.currentTimeMillis();
    }

    public static int end() {
        return (int) ((System.currentTimeMillis() - startTime) / 1000);
    }
}
