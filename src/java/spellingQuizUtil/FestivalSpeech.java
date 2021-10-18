package spellingQuizUtil;

import fileManager.FileControl;

import java.io.IOException;
import java.util.ArrayList;

import static fileManager.FileManager.FESTIVAL_CMD_FILE;

/**
 * This class contains all the methods that are related to festival tts
 */
public class FestivalSpeech {
    private static double speechSpeed;


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
            ArrayList<String> commands = new ArrayList<>();

            // speak english / maori message if there is any
            if (!englishMessage.equals("")) {
                // adjust the speed before say text
                commands.add("(Parameter.set 'Duration_Stretch' " + speechSpeed + ")");
                commands.add("(SayText \"" + englishMessage + "\")");
            }
            if (!maoriMessage.equals("")) {
                commands.add("(voice_akl_mi_pk06_cg)");  // change to maori voice
                // adjust the speed before say text
                commands.add("(Parameter.set 'Duration_Stretch' " + speechSpeed + ")");
                commands.add("(SayText \"" + maoriMessage + "\")");
            }

            // write the commands into the scheme file
            FileControl.writeFile(FESTIVAL_CMD_FILE, commands);

            // run festival scheme file
            String command = "festival -b " + FESTIVAL_CMD_FILE;
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
            pb.start();

        } catch (IOException e) {
            System.err.println("Failed to run linux command that speaks out the scheme file");
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
        } catch (IOException e) {
            System.err.println("Failed to run linux command that stops the festival");
        }
    }
}
