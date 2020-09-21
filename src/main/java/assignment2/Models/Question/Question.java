package assignment2.Models.Question;

import java.util.List;

public class Question implements IQuestion
{
    public int ID;
    public String Question;
    public List<String> Tags;
    public QuestionType Type;
    public int Points;

    public Question(int ID, String Question, List<String> Tags, QuestionType Type, int Points)
    {
        this.ID = ID;
        this.Question = Question;
        this.Tags = Tags;
        this.Type = Type;
        this.Points = Points;
    }

    @Override
    public QuestionType getType() {
        return this.Type;
    }
}
