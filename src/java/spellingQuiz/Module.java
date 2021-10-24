package spellingQuiz;

import spellingQuizUtil.*;

import static spellingQuizUtil.FestivalSpeech.speak;


/**
 * This class contains all the common functionalities of both Games and Practise Module
 */
public abstract class Module {
    private static ModuleType moduleType;
    private QuizState currentQuizState;
    private Result currentResult;
    protected final Words words;
    protected final Score score;
    protected final Statistics statistics;
    protected EncouragingMessage correctMessage, incorrectMessage, tryAgainMessage;
    protected String currentWord, mainLabelText, promptLabelText, userInput;
    private final int NUMBER_OF_QUESTIONS;
    private int currentIndex;


    /**
     * Constructor
     * Initialising the variables
     *
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
     *
     * @return Return true if successfully generate next question, i.e. the quiz is not finished
     *         Return false if unable to generate next question, i.e. the quiz is finished
     */
    public boolean newQuestion() {
        if (isQuizFinished()) {  // the quiz is finished
            return false;
        }

        setQuizState(QuizState.JUST_STARTED);  // now set the state to just started
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
     * This function check the spelling (input),
     * set up the labels, speak, increase score and add statistics of the current word
     */
    public void checkSpelling() {
        // update the labels first
        updateLabelsContents();

        int scoreIncreased = 0;

        // first check if the word is skipped
        if (resultEqualsTo(Result.SKIPPED)) {
            speak("Word skipped", "");

        // correct spelling (Mastered and Failed), 1st attempt and 2nd attempt respectively
        } else if ( words.checkUserSpelling(userInput) ) {
            // speak out the message and then increase the score
            speak("Correct", "");
            scoreIncreased = score.increaseScore(getResult());

        // still 1st attempt, but incorrect
        } else if (resultEqualsTo(Result.MASTERED)) {
            setResult(Result.FAULTED);
            setQuizState(QuizState.RUNNING);  // set the state to Running, because still on the same question
            speak("Incorrect, try once more.", currentWord);
            return;  // return now, so do not add any statistics later on

        // 2nd attempt, and it is the second times got it incorrect --> failed
        } else {
            setResult(Result.FAILED);
            speak("Incorrect", "");
        }

        // except getting incorrect the 1st attempt,
        // set the state to ready for the next question and then add current word statistics
        setQuizState(QuizState.READY);
        statistics.addStatistics(currentWord, getResult(), scoreIncreased, Timer.stop());
    }

    /**
     * Update the labels contents
     * Different module set up the labels differently
     */
    abstract protected void updateLabelsContents();

    /**
     * This method will speak the word again, only the maori word
     */
    public void speakWordAgain() {
        if (quizStateEqualsTo(QuizState.JUST_STARTED)) {
            setQuizState(QuizState.RUNNING);
        }
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
     *
     * @return Return true if it is finished, otherwise return false
     */
    protected boolean isQuizFinished() {
        return currentIndex == NUMBER_OF_QUESTIONS;
    }

}
