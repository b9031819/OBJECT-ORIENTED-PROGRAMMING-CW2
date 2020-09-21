package assignment2.Models.Question;

import java.util.List;

public class MultipleQuestion extends AutoQuestion
{
    public String Option1;
    public String Option2;
    public String Option3;
    public String Option4;

    public MultipleQuestion(int ID, String Question, List<String> Tags, QuestionType Type, int Points, String answer, String option1, String option2, String option3, String option4) {
        super(ID, Question, Tags, Type, Points, answer);
        Option1 = option1;
        Option2 = option2;
        Option3 = option3;
        Option4 = option4;
    }
}