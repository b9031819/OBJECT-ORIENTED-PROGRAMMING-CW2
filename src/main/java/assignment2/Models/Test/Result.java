package assignment2.Models.Test;

import assignment2.Models.User.Student;

import java.util.List;

public class Result
{
    public int ID;
    public Student Student;
    public List<QuestionAttempt> QuestionAttempts;
    public String TestName;

    public Result(int ID, Student student, List<assignment2.Models.Test.QuestionAttempt> questionAttempts, String testName)
    {
        this.ID = ID;
        Student = student;
        QuestionAttempts = questionAttempts;
        TestName = testName;
    }

    public double getTotalScore()
    {
        int score = 0;
        for (var questionAttempt : QuestionAttempts)
        {
            if(questionAttempt.Correct)
            {
                score = score + questionAttempt.Question.Points;
            }
        }

        return score;
    }

    public double getMaxScore()
    {
        int score = 0;
        for (var questionAttempt : QuestionAttempts)
        {
                score = score + questionAttempt.Question.Points;
        }

        return score;
    }
}
