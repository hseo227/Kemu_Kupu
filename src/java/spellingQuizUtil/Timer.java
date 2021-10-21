package spellingQuizUtil;

/**
 * This class is a timer
 */
public class Timer {
    private static long startTime;

    /**
     * When this method is called, it stores the current time
     */
    public static void start() {
        startTime = System.currentTimeMillis();
    }

    /**
     * When this method is called, calculate the time taken and then outputs it
     *      System.currentTimeMillis() is current time in milli second
     *      To get the time taken in second -> (endTime - startTime) / 1000
     * @return The time has elapsed in second
     */
    public static int getTime() {
        return (int) ((System.currentTimeMillis() - startTime) / 1000);
    }
}
