package assignment2.Models.Test;

import assignment2.Models.Question.Question;

public class QuestionAttempt
{
    public Question Question;
    public String AnswerGiven ;
    public Boolean Correct;

    public QuestionAttempt(assignment2.Models.Question.Question question, String answerGiven, Boolean correct)
    {
        Question = question;
        AnswerGiven = answerGiven;
        Correct = correct;
    }


}
