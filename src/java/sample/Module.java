package sample;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


enum ModuleType {
    practise, game
}

enum QuizState {
    ready, running
}

enum Result {
    mastered, faulted, failed, skipped
}

public abstract class Module {
    private final static String FESTIVALCMDFILE = ".scm";


    protected int currentIndex, speechSpeed;
    protected String currentWord, mainLabelText, promptLabelText, userInput;
    protected QuizState currentQuizState;
    protected Result currentResult;
    protected static String selectedTopic;
    protected final Words words;
    protected encouragingMessage correctMessage, incorrectMessage, tryAgainMessage;


    // this method will only run once and will run at the start of the program
    // create a file that will be used to run the festival
    public static void initialise() throws IOException {
        File file = new File(FESTIVALCMDFILE);
        file.createNewFile();
    }

    // Constructor
    public Module(int numOfQuestions) {
        // setting up the words
        words = new Words(selectedTopic, numOfQuestions);

        correctMessage = new encouragingMessage("Correct");
        incorrectMessage = new encouragingMessage("Incorrect");
        tryAgainMessage = new encouragingMessage("TryAgain");
        currentIndex = 0;
        currentWord = "";
        mainLabelText = "";
        promptLabelText = "";
        setUserInput("");
        setQuizState(QuizState.ready);
        setResult(Result.mastered);
    }

    // this function generate a new word and then ask the user
    protected abstract boolean newQuestion();

    // this function check the spelling (input) and then set up a range of stuff
    protected abstract void checkSpelling();

    // this function will speak out the message using bash and festival scm
    protected void speak(String englishMessage, String maoriMessage) {
        try {
            // write the festival command into .scm file
            PrintWriter writeFile = new PrintWriter(new FileWriter(FESTIVALCMDFILE));

            // adjust the speed first
            writeFile.println("(Parameter.set 'Audio_Command \"aplay -q -c 1 -t raw -f s16 -r $(($SR*" + speechSpeed + "/100)) $FILE\")");

            // speak english / maori message if there is any
            if (!englishMessage.equals("")) {
                writeFile.println("(SayText \"" + englishMessage + "\")");
            }
            if (!maoriMessage.equals("")) {
                writeFile.println("(voice_akl_mi_pk06_cg)");  // change to maori voice
                writeFile.println("(SayText \"" + maoriMessage + "\")");
            }

            writeFile.close();

            // run festival schema file
            String command = "festival -b " + FESTIVALCMDFILE;
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
            pb.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // this method will speak the word again, only the word
    public void speakWordAgain() {
        speak("", currentWord);
    }

    // QuizState's getter, setter and equals to
    protected void setQuizState(QuizState newQuizState) {
        currentQuizState = newQuizState;
    }

    protected QuizState getQuizState() {
        return currentQuizState;
    }

    public boolean quizStateEqualsTo(QuizState quizState) {
        return getQuizState() == quizState;
    }

    // Result's getter, setter and equals to
    public void setResult(Result newResult) {
        currentResult = newResult;
    }

    protected Result getResult() {
        return currentResult;
    }

    public boolean resultEqualsTo(Result result) {
        return getResult() == result;
    }

    // userInput's getter and setter
    public void setUserInput(String newUserInput) {
        userInput = newUserInput;
    }

    protected String getUserInput() {
        return userInput;
    }

    // selectedTopic's setter
    public static void setTopic(String topic) {
        selectedTopic = topic;
    }

    // speechSpeed's setter
    public void setSpeechSpeed(int speed) {
        speechSpeed = speed;
    }

    // mainLabelText's getter
    public String getMainLabelText() {
        return mainLabelText;
    }

    // promptLabelText's getter
    public String getPromptLabelText() {
        return promptLabelText;
    }

}
