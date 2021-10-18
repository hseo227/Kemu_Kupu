package spellingQuizUtil;

import fileManager.FileManager;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class handles all the encouraging messages
 */
public class EncouragingMessage {
    ArrayList<String> messageList;  // all the messages in the file


    /**
     * Setting up the encouraging message list
     * @param result Encouraging messages for that specific result
     */
    public EncouragingMessage(String result) {
        // get all the messages in the file
        String fileName = "messages/" + result;

        // First, get all the words in the file
        messageList = FileManager.readFile(fileName);
    }

    /**
     * Get a random message in the message list
     * @return A random encouraging message
     */
    public String getEncourageMsg() {
        // get random message
        Random rand = new Random();
        return messageList.get(rand.nextInt(messageList.size()));
    }

}
