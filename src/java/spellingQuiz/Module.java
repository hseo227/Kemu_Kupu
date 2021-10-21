package spellingQuiz;

import spellingQuizUtil.*;

import static spellingQuizUtil.FestivalSpeech.speak;


/**
 * This class contains all the common functionalities of both Games and Practise Module
 */
public abstract class Module {
    private final int NUMBER_OF_QUESTIONS;

    private int currentIndex;
    private QuizState currentQuizState;
    private Result currentResult;
    private static ModuleType moduleType;
    protected String currentWord, mainLabelText, promptLabelText, userInput;
    protected EncouragingMessage correctMessage, incorrectMessage, tryAgainMessage;
    protected final Words words;
    protected final Score score;
    protected final Statistics statistics;


    /**
     * Constructor
     * Initialising the variables
     * @param numOfQuestions Number of questions for the quiz
     */
    public Module(int numOfQuestions) {
        NUMBER_OF_QUESTIONS = numOfQuestions;

        // setting up the words
        words = new Words(NUMBER_OF_QUESTIONS);

        score = new Score(numOfQuestions);
        statistics = new Statistics();
        correctMessage = new EncouragingMessage("Correct");
        incorrectMessage = new EncouragingMessage("Incorrect");
        tryAgainMessage = new EncouragingMessage("TryAgain");
        currentIndex = 0;
        currentWord = "";
        mainLabelText = "";
        promptLabelText = "";
        setUserInput("");
        setQuizState(QuizState.READY);
        setResult(Result.MASTERED);
    }

    /**
     * This function generate a new word / next question and then ask the user
     * @return Return true if successfully generate next question, i.e. the quiz is not finished
     *         Return false if unable to generate next question, i.e. the quiz is finished
     */
    public boolean newQuestion() {
        if (isQuizFinished()) {  // the quiz is finished
            return false;
        }

        setQuizState(QuizState.RUNNING);  // now set the state to running
        setResult(Result.MASTERED);  // set original result to Mastered
        currentIndex++;

        currentWord = words.nextWord();  // set up the word and get the word that is testing on

        // set the labels' messages and also speak out the message
        mainLabelText = "Spell Word " + currentIndex + " of " + NUMBER_OF_QUESTIONS + ":";
        promptLabelText = words.getHint(Hint.NO_HINT);
        speak("Please spell", currentWord);

        return true;
    }

    /**
     * This function check the spelling (input) and then set up the labels, speak, increase score
     * Different module set up the labels differently
     */
    abstract public void checkSpelling();

    /**
     * This method will speak the word again, only the maori word
     */
    public void speakWordAgain() {
        speak("", currentWord);
    }

    /**
     * currentQuizState's
     *                    setter
     *                    equals to
     */
    protected void setQuizState(QuizState newQuizState) {
        currentQuizState = newQuizState;
    }

    public boolean quizStateEqualsTo(QuizState quizState) {
        return currentQuizState == quizState;
    }

    /**
     * currentResult's
     *                 setter
     *                 getter
     *                 equals to
     */
    public void setResult(Result newResult) {
        currentResult = newResult;
    }

    protected Result getResult() {
        return currentResult;
    }

    public boolean resultEqualsTo(Result result) {
        return currentResult == result;
    }

    /**
     * moduleType's
     *              setter
     *              equals to
     */
    public static void setModuleType(ModuleType newModuleType) {
        moduleType = newModuleType;
    }

    public static boolean moduleTypeEqualsTo(ModuleType newModuleType) {
        return moduleType == newModuleType;
    }

    /**
     * userInput's
     *             setter
     */
    public void setUserInput(String newUserInput) {
        userInput = newUserInput;
    }

    /**
     * mainLabelText's
     *                 getter
     * @return mainLabelText
     */
    public String getMainLabelText() {
        return mainLabelText;
    }

    /**
     * promptLabelText's
     *                   getter
     * @return promptLabelText
     */
    public String getPromptLabelText() {
        return promptLabelText;
    }

    /**
     * Check is the quiz finished, that is current index is equal to number of questions
     * @return Return true if it is finished, otherwise return false
     */
    protected boolean isQuizFinished() {
        return currentIndex == NUMBER_OF_QUESTIONS;
    }

}
