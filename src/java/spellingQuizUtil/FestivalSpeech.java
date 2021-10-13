package spellingQuizUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FestivalSpeech {
    private final static String FESTIVAL_CMD_FILE = ".scm";
    private static double speechSpeed;


    // this method will only run once and will run at the start of the program
    // create a file that will be used to run the festival
    public static void settingUp() throws IOException {
        File file = new File(FESTIVAL_CMD_FILE);
        file.createNewFile();
    }

    // calculate the speech speed that festival will understand and set it
    public static void setSpeechSpeed(int speed) {
        speechSpeed = (200 - speed) / 100.0;
    }

    // this function will speak out the message using bash and festival scm
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

            // run festival schema file
            String command = "festival -b " + FESTIVAL_CMD_FILE;
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
            pb.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
