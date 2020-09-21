package assignment2.Models.Question;

import java.util.List;

public class AutoQuestion extends Question
{
    public String Answer;

    public AutoQuestion(int ID, String Question, List<String> Tags, QuestionType Type, int Points, String answer) {
        super(ID, Question, Tags, Type, Points);
        Answer = answer;
    }
}
