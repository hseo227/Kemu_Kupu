package spellingQuizUtil;

/**
 * This class is a timer
 */
public class Timer {
    private static long startTime, endTime;
    private static boolean timerIsRunning;

    /**
     * When this method is called (timer starts),
     * it stores the current time as start time and then set timer IS running
     */
    public static void start() {
        startTime = System.currentTimeMillis();
        timerIsRunning = true;
    }

    /**
     * When this method is called (timer ends),
     * it stores the current time as end time
     * and then calculate the total time taken for this question and then outputs it
     *      System.currentTimeMillis() is current time in milli second
     *      To get the time taken in second -> (endTime - startTime) / 1000
     * @return The time has elapsed in second
     */
    public static int stop() {
        timerIsRunning = false;
        endTime = System.currentTimeMillis();
        return (int) ((endTime - startTime) / 1000);
    }

    /**
     * When this method is called (get the time on the timer),
     * if the timer is currently running, get the current time taken,
     * if the timer has stopped, then get the time when it was stopped.
     * @return The time has elapsed in second
     */
    public static int getTime() {
        if (timerIsRunning) {
            return (int) ((System.currentTimeMillis() - startTime) / 1000);
        } else {
            return (int) ((endTime - startTime) / 1000);
        }
    }
}
