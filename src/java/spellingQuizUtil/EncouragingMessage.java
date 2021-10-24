package spellingQuizUtil;

import fileManager.FileControl;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class handles all the encouraging messages
 */
public class EncouragingMessage {
    ArrayList<String> messageList;  // all the messages in the file


    /**
     * Setting up the encouraging message list
     *
     * @param result Encouraging messages for that specific result
     */
    public EncouragingMessage(String result) {
        // get all the messages/contents in the file and store them into the list
        String fileName = "messages/" + result;
        messageList = FileControl.readFile(fileName);
    }

    /**
     * Get a random message in the message list
     *
     * @return A random encouraging message
     */
    public String getEncourageMsg() {
        // get a random message
        Random rand = new Random();
        return messageList.get(rand.nextInt(messageList.size()));
    }

}
