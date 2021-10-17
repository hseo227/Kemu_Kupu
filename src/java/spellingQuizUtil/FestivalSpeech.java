package spellingQuizUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class contains all the methods that are related to festival tts
 */
public class FestivalSpeech {
    private final static String FESTIVAL_CMD_FILE = ".hide/.scm";
    private static double speechSpeed;


    /**
     * This method will only run once and will run at the start of the program
     * Create the necessary folder that scheme file will be in
     * and then create a scheme file that will be used to run the festival tts
     */
    public static void settingUp() {
        try {
            // create the folder
            String dirName = FESTIVAL_CMD_FILE.substring(0, FESTIVAL_CMD_FILE.lastIndexOf("/"));
            File file = new File(dirName);
            file.mkdirs();

            // create the scheme file
            file = new File(FESTIVAL_CMD_FILE);
            file.createNewFile();
            
        } catch(IOException e) {
            System.err.println("Unable to create scheme file \"" + FESTIVAL_CMD_FILE + "\"");
        }
    }

    /**
     * Calculate the speech speed that festival will understand and set it
     * speechSpeed = 2.0 - SayText slower
     *               1.0 - SayText normal speed
     *               0.5 - SayText faster
     * @param speed This value is inverted, this is why (200 - speed)
     */
    public static void setSpeechSpeed(int speed) {
        speechSpeed = (200 - speed) / 100.0;
    }

    /**
     * Speak out / SayText the message using bash and festival scm
     * Speak the English and Maori words separately
     * Speak the English words first then followed by Maori words
     * Also speak both words/messages in selected speech speed
     * To SayText, first write the commands into .scm file and then run the scheme file
     *
     * @param englishMessage Message in English only
     * @param maoriMessage Message in Maori only
     */
    public static void speak(String englishMessage, String maoriMessage) {
        try {
            // write the festival command into .scm file
            PrintWriter writeFile = new PrintWriter(new FileWriter(FESTIVAL_CMD_FILE));

            // speak english / maori message if there is any
            if (!englishMessage.equals("")) {
                // adjust the speed before say text
                writeFile.println("(Parameter.set 'Duration_Stretch' " + speechSpeed + ")");
                writeFile.println("(SayText \"" + englishMessage + "\")");
            }
            if (!maoriMessage.equals("")) {
                writeFile.println("(voice_akl_mi_pk06_cg)");  // change to maori voice
                // adjust the speed before say text
                writeFile.println("(Parameter.set 'Duration_Stretch' " + speechSpeed + ")");
                writeFile.println("(SayText \"" + maoriMessage + "\")");
            }

            writeFile.close();

            // run festival scheme file
            String command = "festival -b " + FESTIVAL_CMD_FILE;
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
            pb.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Kill all festival tts instantly
     */
    public static void shutDownAllFestival() {
        try {
            String command = "killall festival; killall aplay";
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
            pb.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
