package assignment2.Factories;

import assignment2.Models.Question.MultipleQuestion;
import assignment2.Models.Question.Question;
import assignment2.Models.Question.QuestionType;
import assignment2.Models.Question.SimpleQuestion;
import assignment2.Models.Test.Test;
import assignment2.Models.User.Student;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TestDAO
{
    Connection conn = null;

    public TestDAO(Connection conn)
    {
        this.conn = conn;
    }

    public List<Test> GetAllTest()
    {
        List<Test> tests = new ArrayList<>();

        try
        {
            String sql = "SELECT * FROM Test";
            Statement stmnt = conn.createStatement();
            ResultSet results = stmnt.executeQuery(sql);

            while (results.next())
            {
                Integer ID = results.getInt("ID");
                String Name = results.getString("Name");

                Test test = new Test(ID, Name, new ArrayList<>());
                tests.add(test);
            }

            for(Test test : tests)
            {
                sql = "SELECT * FROM TestQuestion WHERE TestID = ?";
                PreparedStatement pstmnt = conn.prepareStatement(sql);
                pstmnt.setInt(1, test.ID);
                results = pstmnt.executeQuery();
                while (results.next())
                {
                    test.Questions.add(DataFactory.getInstance().getQuestionDAO().GetQuestion(results.getInt("QuestionID")));
                }
                results.close();
                stmnt.close();
            }

            results.close();
            stmnt.close();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return new ArrayList<>();
        }

        return tests;
    }


    public Test GetTest(String testName)
    {
        Test test = null;

        try
        {
            String sql = "SELECT * FROM Test WHERE Name = ?";
            PreparedStatement pstmnt = conn.prepareStatement(sql);
            pstmnt.setString(1, testName);
            ResultSet results = pstmnt.executeQuery();

            if (results.next())
            {
                Integer ID = results.getInt("ID");
                String Name = results.getString("Name");

                test = new Test(ID, Name, new ArrayList<>());
            }

            sql = "SELECT * FROM TestQuestion WHERE TestID = ?";
            pstmnt = conn.prepareStatement(sql);
            pstmnt.setInt(1, test.ID);
            results = pstmnt.executeQuery();
            while (results.next())
            {
                test.Questions.add(DataFactory.getInstance().getQuestionDAO().GetQuestion(results.getInt("QuestionID")));
            }

            results.close();
            pstmnt.close();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return test;
    }


    public void AddTest(Test test)
    {
        try
        {
            String sql = "INSERT INTO Test (Name) VALUES (?)";
            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.setString(1, test.TestName);
            stmnt.executeUpdate();

            int insertId = 0;
            ResultSet rs = stmnt.getGeneratedKeys();
            if (rs.next())
            {
                insertId = rs.getInt(1);
            }

            for (Question question : test.Questions)
            {
                sql = "INSERT INTO TestQuestion (TestID, QuestionID) VALUES (?,?)";
                stmnt = conn.prepareStatement(sql);
                stmnt.setInt(1, insertId);
                stmnt.setInt(2, question.ID);
                stmnt.executeUpdate();
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void DeleteTest(String testName)
    {
        try
        {
            String sql = "SELECT * FROM Test WHERE Name = ?";
            PreparedStatement pstmnt = conn.prepareStatement(sql);
            pstmnt.setString(1, testName);
            ResultSet results = pstmnt.executeQuery();
            Integer TestID = 0;

            if (results.next())
            {
                TestID = results.getInt("ID");
            }

            sql = "DELETE FROM TestQuestion WHERE TestID = ?";
            pstmnt = conn.prepareStatement(sql);
            pstmnt.setInt(1, TestID);
            pstmnt.executeUpdate();

            sql = "DELETE FROM Test WHERE ID = ?";
            pstmnt = conn.prepareStatement(sql);
            pstmnt.setInt(1, TestID);
            pstmnt.executeUpdate();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

    public void EditTest(String testName, List<Question> questions)
    {

    }
}
