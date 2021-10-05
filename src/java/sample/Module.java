package sample;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


enum ModuleType {
    practise, game
}

enum QuizState {
    ready, running, finished
}

enum Result {
    mastered, faulted, failed, skipped
}

public abstract class Module extends Service<Void> {
    private final int NUMOFQUESTIONS = 5;
    private final static String FESTIVALCMDFILE = ".scm";


    private int currentIndex, speechSpeed;
    private String currentWord, mainLabelText, promptLabelText, userInput;
    private QuizState currentQuizState;
    private Result currentResult;
    private static String selectedTopic;
    private final Words words;
    private encouragingMessage correctMessage, incorrectMessage, tryAgainMessage;


    // this method will only run once and will run at the start of the program
    // create a file that will be used to run the festival
    public static void initialise() throws IOException {
        File file = new File(FESTIVALCMDFILE);
        file.createNewFile();
    }

    // Constructor
    public Module() {
        // setting up the words
        words = new Words(selectedTopic, NUMOFQUESTIONS);

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

    // this method is invoked by the Server thread
    // this method will then call either newQuestion() or checkSpelling() depends on the QuizState
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // if the quiz state is ready for the next question, then generate next question
                if (quizStateEqualsTo(QuizState.ready)) {
                    newQuestion();

                // otherwise, check the spelling
                } else {
                    checkSpelling();
                }

                // Title = main label;  Message = prompt label
                // update the text in mainLabel and promptLabel
                updateTitle(mainLabelText);
                updateMessage(promptLabelText);
                return null;
            }
        };
    }

    // this function generate a new word and then ask the user
    protected abstract void newQuestion();

    // this function check the spelling (input) and then set up a range of stuff
    protected abstract void checkSpelling();

    // this function will speak out the message using bash and festival scm
    private void speak(String englishMessage, String maoriMessage) {
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
    private void setQuizState(QuizState newQuizState) {
        currentQuizState = newQuizState;
    }

    private QuizState getQuizState() {
        return currentQuizState;
    }

    public boolean quizStateEqualsTo(QuizState quizState) {
        return getQuizState() == quizState;
    }

    // Result's getter, setter and equals to
    public void setResult(Result newResult) {
        currentResult = newResult;
    }

    private Result getResult() {
        return currentResult;
    }

    public boolean resultEqualsTo(Result result) {
        return getResult() == result;
    }

    // userInput's getter and setter
    public void setUserInput(String newUserInput) {
        userInput = newUserInput;
    }

    private String getUserInput() {
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

}
