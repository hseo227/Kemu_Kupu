package fileManager;

import java.io.File;
import java.io.IOException;

/**
 * This class contains the file names that we are going to use throughout the game.
 * Before using them, create those files first if they do not exist yet.
 */
public class FileManager {
    public final static String HIDDEN_DIR_NAME = ".hide";
    public final static String FESTIVAL_CMD_FILE = HIDDEN_DIR_NAME + "/.scm";
    public final static String LEADERBOARD_FILE = HIDDEN_DIR_NAME + "/leaderboard";

    /**
     * This method will only run once and will run at the start of the program.
     * Create the necessary folder, and then the files
     */
    public static void settingUp() {
        try {
            // create the folder
            File file = new File(HIDDEN_DIR_NAME);
            file.mkdirs();

            // create the scheme file
            file = new File(FESTIVAL_CMD_FILE);
            file.createNewFile();

            // the file that stores the leaderboard
            file = new File(LEADERBOARD_FILE);
            file.createNewFile();

        } catch (IOException e) {
            System.err.println("Unable to create scheme file \"" + FESTIVAL_CMD_FILE + "\" or the file that stores the leaderboard \"" + LEADERBOARD_FILE + "\"");
        }
    }
}
