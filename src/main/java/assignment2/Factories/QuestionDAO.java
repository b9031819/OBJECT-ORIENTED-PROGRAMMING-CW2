package assignment2.Factories;

import assignment2.Models.Question.*;
import assignment2.Models.User.Student;
import assignment2.Models.User.User;
import assignment2.Models.User.UserType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.PKIXRevocationChecker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO
{
    Connection conn = null;

    public QuestionDAO(Connection conn)
    {
        this.conn = conn;
    }

    public List<Question> GetAllQuestions()
    {
        try
        {
            //Get Questions
            List<Question> questions = new ArrayList<>();
            String sql = "SELECT * FROM Question";
            Statement stmnt = conn.createStatement();
            ResultSet results = stmnt.executeQuery(sql);
            while (results.next())
            {
                Integer ID = results.getInt("ID");
                Integer Type = results.getInt("Type");
                Integer Points = results.getInt("Points");
                String Question = results.getString("Question");

                Question question = new Question(ID,Question,new ArrayList<>(), QuestionType.values()[Type], Points);
                questions.add(question);
            }
            results.close();
            stmnt.close();

            for (Question question: questions)
            {
                question.Tags.addAll(getQuestionTags(question));
            }

            List<Question> questionsPrepared = new ArrayList<>();

            for(Question question : questions)
            {
                if(question.getType() == QuestionType.SIMPLE)
                {
                    question = getSimpleQuestion(question);
                }

                if(question.getType() == QuestionType.MULTIPLE)
                {
                    question = getMultipleQuestion(question);
                }

                questionsPrepared.add(question);
            }

            return questionsPrepared;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    private String getQuestionAnswer(Question question)
    {
        try
        {
            String sql = "SELECT * FROM QuestionAnswer WHERE QuestionID = ?";

            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.setInt(1, question.ID);

            ResultSet results = stmnt.executeQuery();
            if(results.next())
            {
                return results.getString("Answer");
            }
            results.close();
            stmnt.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return "";
    }

    private SimpleQuestion getSimpleQuestion(Question question)
    {
        if(question.Type != QuestionType.SIMPLE)
        {
            System.out.println("Question is not a simple question!");
            return null;
        }

        try
        {
           return new SimpleQuestion(question.ID, question.Question, question.Tags, question.getType(), question.Points, getQuestionAnswer(question));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }


    private MultipleQuestion getMultipleQuestion(Question question)
    {
        if(question.Type != QuestionType.MULTIPLE)
        {
            System.out.println("Question is not a simple question!");
            return null;
        }

        try
        {
            String sql = "SELECT * FROM QuestionOption WHERE QuestionID = ?";
            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.setInt(1, question.ID);
            ResultSet results = stmnt.executeQuery();

            String option1 = "";
            String option2 = "";
            String option3 = "";
            String option4 = "";

            if (results.next())
            {
                option1 = results.getString("Option1");
                option2 = results.getString("Option2");
                option3 = results.getString("Option3");
                option4 = results.getString("Option4");
            }

            results.close();
            stmnt.close();

            return new MultipleQuestion(question.ID, question.Question, question.Tags, question.getType(), question.Points, getQuestionAnswer(question), option1, option2, option3, option4);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }



    private List<String> getQuestionTags(Question question)
    {
        List<String> tags = new ArrayList<>();

        try
        {
            String sql = "SELECT * FROM QuestionTags WHERE QuestionID = ?";

            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.setInt(1, question.ID);

            ResultSet results = stmnt.executeQuery();
            while (results.next())
            {
                tags.add(results.getString("Tag"));
            }
            results.close();
            stmnt.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return tags;
    }

    public Question GetQuestion(int ID)
    {
        try
        {
            Question question = null;
            String sql = "SELECT * FROM Question WHERE ID = ?";
            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.setInt(1, ID);
            ResultSet results = stmnt.executeQuery();

            while (results.next())
            {
                Integer Type = results.getInt("Type");
                Integer Points = results.getInt("Points");
                String Question = results.getString("Question");

                question = new Question(ID,Question,new ArrayList<>(), QuestionType.values()[Type], Points);
            }
            results.close();
            stmnt.close();
            if(question == null) {
                System.out.println("Question is null");
            }
            question.Tags.addAll(getQuestionTags(question));

            if(question.getType() == QuestionType.SIMPLE)
            {
                return getSimpleQuestion(question);
            }

            if(question.getType() == QuestionType.MULTIPLE)
            {
                return getMultipleQuestion(question);
            }

            return question;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public Question GetQuestion(String QuestionText)
    {
        try
        {
            Question question = null;

            String sql = "SELECT * FROM Question WHERE Question = ?";
            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.setString(1, QuestionText);

            ResultSet results = stmnt.executeQuery();
            while (results.next())
            {
                Integer ID = results.getInt("ID");
                Integer Type = results.getInt("Type");
                Integer Points = results.getInt("Points");
                String Question = results.getString("Question");

                question = new Question(ID,Question,new ArrayList<>(), QuestionType.values()[Type], Points);
            }
            results.close();
            stmnt.close();

            question.Tags.addAll(getQuestionTags(question));

            if(question.getType() == QuestionType.SIMPLE)
            {
                return getSimpleQuestion(question);
            }

            if(question.getType() == QuestionType.MULTIPLE)
            {
                return getMultipleQuestion(question);
            }

            return question;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public void AddQuestion(Question question)
    {
        try
        {
            String sql = "INSERT INTO Question (Type, Points, Question) VALUES (?,?,?)";
            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.setInt(1, question.Type.ordinal());
            stmnt.setInt(2, question.Points);
            stmnt.setString(3, question.Question);
            stmnt.executeUpdate();

            int insertId = 0;
            ResultSet rs = stmnt.getGeneratedKeys();
            if (rs.next())
            {
                insertId = rs.getInt(1);
            }

            for (String tag :question.Tags)
            {
                sql = "INSERT INTO QuestionTags (QuestionID, Tag) VALUES (?,?)";
                stmnt = conn.prepareStatement(sql);
                stmnt.setInt(1, insertId);
                stmnt.setString(2, tag);
                stmnt.executeUpdate();
            }

            if(question.getType() == QuestionType.SIMPLE)
            {
                SimpleQuestion simpleQuestion = (SimpleQuestion)question;
                sql = "INSERT INTO QuestionAnswer (QuestionID, Answer) VALUES (?,?)";
                stmnt = conn.prepareStatement(sql);
                stmnt.setInt(1, insertId);
                stmnt.setString(2, simpleQuestion.Answer);
                stmnt.executeUpdate();
            }

            if(question.getType() == QuestionType.MULTIPLE)
            {
                MultipleQuestion multipleQuestion = (MultipleQuestion)question;

                sql = "INSERT INTO QuestionAnswer (QuestionID, Answer) VALUES (?,?)";
                stmnt = conn.prepareStatement(sql);
                stmnt.setInt(1, insertId);
                stmnt.setString(2, multipleQuestion.Answer);
                stmnt.executeUpdate();

                sql = "INSERT INTO QuestionOption (QuestionID, Option1, Option2, Option3, Option4) VALUES (?,?,?,?,?)";
                stmnt = conn.prepareStatement(sql);
                stmnt.setInt(1, insertId);
                stmnt.setString(2, multipleQuestion.Option1);
                stmnt.setString(3, multipleQuestion.Option2);
                stmnt.setString(4, multipleQuestion.Option3);
                stmnt.setString(5, multipleQuestion.Option4);
                stmnt.executeUpdate();
            }

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void DeleteQuestion(Question question)
    {

    }
}
