package spellingQuiz;

import spellingQuizUtil.*;
import static spellingQuizUtil.FestivalSpeech.speak;


public abstract class Module {

    protected int currentIndex;
    protected String currentWord, mainLabelText, promptLabelText, userInput;
    protected QuizState currentQuizState;
    protected Result currentResult;
    protected static ModuleType moduleType;
    protected final Words words;
    protected EncouragingMessage correctMessage, incorrectMessage, tryAgainMessage;


    // Constructor
    public Module(int numOfQuestions) {
        // setting up the words
        words = new Words(numOfQuestions);

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

        // update the the total score
        Score.changeTotalScore(numOfQuestions);
    }

    // this function generate a new word and then ask the user
    public abstract boolean newQuestion();

    // this function check the spelling (input) and then set up a range of stuff
    public abstract void checkSpelling();

    // this method will speak the word again, only the word
    public void speakWordAgain() {
        speak("", currentWord);
    }

    // QuizState's getter, setter and equals to
    protected void setQuizState(QuizState newQuizState) {
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

    // moduleType's getter, setter and equals to
    public static void setModuleType(ModuleType newModuleType) {
        moduleType = newModuleType;
    }

    private static ModuleType getModuleType() {
        return moduleType;
    }

    public static boolean moduleTypeEqualsTo(ModuleType newModuleType) {
        return getModuleType() == newModuleType;
    }

    // userInput's getter and setter
    public void setUserInput(String newUserInput) {
        userInput = newUserInput;
    }

    protected String getUserInput() {
        return userInput;
    }

    // mainLabelText's getter
    public String getMainLabelText() {
        return mainLabelText;
    }

    // promptLabelText's getter
    public String getPromptLabelText() {
        return promptLabelText;
    }

    // get number of letters of the current word
    public int getNumOfLettersOfWord() {
        return words.getNumOfLettersOfWord();
    }

    // score increases, the score multiplier depends on the result
    public void increaseScore() {
        if (resultEqualsTo(Result.MASTERED)) {
            Score.increaseBy(2);
        } else {
            Score.increaseBy(1);
        }
    }

}
