package assignment2.Models.Question;

import java.util.List;

public class ManualQuestion extends Question
{
    public ManualQuestion(int ID, String Question, List<String> Tags, QuestionType Type, int Points) {
        super(ID, Question, Tags, Type, Points);
    }
}
