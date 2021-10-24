package spellingQuiz;

import spellingQuizUtil.Hint;
import spellingQuizUtil.Result;


/**
 * This class stores only the functionalities of Games Module
 */
public class ModuleGames extends Module {
    private static final int NUMBER_OF_QUESTIONS = 5;

    /**
     * Constructor
     * Start the quiz with number of question 5
     */
    public ModuleGames() {
        super(NUMBER_OF_QUESTIONS);
    }

    /**
     * This function updates the text of the labels according to the results
     */
    protected void updateLabelsContents() {
        if (resultEqualsTo(Result.SKIPPED)) {
            mainLabelText = incorrectMessage.getEncourageMsg();
            promptLabelText = "";

        // correct spelling (Mastered and Failed), 1st attempt and 2nd attempt respectively
        } else if ( words.checkUserSpelling(userInput) ) {
            mainLabelText = correctMessage.getEncourageMsg();
            promptLabelText = "";

        // still 1st attempt, but incorrect
        } else if (resultEqualsTo(Result.MASTERED)) {
            mainLabelText = tryAgainMessage.getEncourageMsg();
            promptLabelText = words.getHint(Hint.GAMES_M_HINT);

        // 2nd attempt, and it is the second times got it incorrect --> failed
        } else {
            mainLabelText = incorrectMessage.getEncourageMsg();
            promptLabelText = "";
        }
    }

}
