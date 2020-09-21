package assignment2.Models.Question;

import java.util.List;

public class SimpleQuestion extends AutoQuestion
{
    public SimpleQuestion(int ID, String Question, List<String> Tags, QuestionType Type, int Points, String answer)
    {
        super(ID, Question, Tags, Type, Points, answer);
    }
}
