package spellingQuiz;

import spellingQuizUtil.*;

import static spellingQuizUtil.FestivalSpeech.speak;


/**
 * This class stores only the functionalities of Practise Module
 */
public class ModulePractise extends Module {

    /**
     * Constructor
     * Start the quiz with the number of questions that user chose
     * @param numOfQuestions Number of questions for the quiz
     */
    public ModulePractise(int numOfQuestions) {
        super(numOfQuestions);
    }

    /**
     * This function updates the text of the labels according to the results
     */
    protected void updateLabelsContents() {
        if (resultEqualsTo(Result.SKIPPED)) {
            mainLabelText = "Correct answer: " + currentWord;
            promptLabelText = "Press 'Enter' or click 'Check' button again to continue";

        // correct spelling (Mastered and Failed), 1st attempt and 2nd attempt respectively
        } else if ( words.checkUserSpelling(userInput) ) {
            mainLabelText = correctMessage.getEncourageMsg();
            promptLabelText = "";

        // still 1st attempt, but incorrect
        } else if (resultEqualsTo(Result.MASTERED)) {
            mainLabelText = tryAgainMessage.getEncourageMsg();
            promptLabelText = words.getHint(Hint.PRACTISE_M_HINT);

        // 2nd attempt, and it is the second times got it incorrect --> failed
        } else {
            mainLabelText = "Correct answer: " + currentWord;
            promptLabelText = "Press 'Enter' or click 'Check' button again to continue";
        }
    }

}
