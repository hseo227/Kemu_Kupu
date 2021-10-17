package spellingQuiz;

import spellingQuizUtil.Hint;
import spellingQuizUtil.QuizState;
import spellingQuizUtil.Result;
import spellingQuizUtil.Statistics;

import static spellingQuizUtil.FestivalSpeech.speak;


public class ModulePractise extends Module {

    /**
     * Constructor
     * Start the quiz with the number of questions that user chose
     * @param numOfQuestions Number of questions for the quiz
     */
    public ModulePractise(int numOfQuestions) {
        super(numOfQuestions);
    	Statistics.practiceMode();
    }

    /**
     * This function check the spelling (input)
     * And then set up the labels, speak, increase score with respective Result
     */
    public void checkSpelling() {
        // first check if the word is skipped
        if (resultEqualsTo(Result.SKIPPED)) {
            setQuizState(QuizState.READY);  // set the state to ready for the next question

            // setting up the labels' text and speak out the message
            mainLabelText = "Correct answer: " + currentWord;
            promptLabelText = "Press 'Enter' or click 'Check' button again to continue";
            speak("Word skipped", "");

        // if statement for each result after checking the spelling (input)
        } else if ( words.checkUserSpelling(getUserInput()) ) {  // mastered and failed, 1st attempt and 2nd attempt respectively
            setQuizState(QuizState.READY);  // set the state to ready for the next question

            // setting up the labels' text and speak out the message
            mainLabelText = correctMessage.getEncourageMsg();
            promptLabelText = "";
            speak("Correct", "");
            increaseScore();

        } else if (resultEqualsTo(Result.MASTERED)) {  // still 1st attempt, but incorrect
            setResult(Result.FAULTED);

            // setting up the labels' text and speak out the message
            mainLabelText = tryAgainMessage.getEncourageMsg();
            promptLabelText = words.getHint(Hint.PRACTISE_M_HINT);
            speak("Incorrect, try once more.", currentWord);

        } else {  // 2nd attempt, and it is the second times got it incorrect --> failed
            setResult(Result.FAILED);
            setQuizState(QuizState.READY);  // set the state to ready for the next question

            // setting up the labels' text and speak out the message
            mainLabelText = "Correct answer: " + currentWord;
            promptLabelText = "Press 'Enter' or click 'Check' button again to continue";
            speak("Incorrect", "");
        }
    }

}
