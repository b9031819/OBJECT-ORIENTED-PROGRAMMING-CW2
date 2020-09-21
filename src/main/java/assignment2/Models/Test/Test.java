package assignment2.Models.Test;

import assignment2.Models.Question.Question;

import java.util.List;

public class Test
{
    public int ID;
    public String TestName;
    public List<Question> Questions;

    public Test(int ID,String TestName, List<Question> Questions)
    {
        this.ID = ID;
        this.TestName = TestName;
        this.Questions = Questions;
    }
}
