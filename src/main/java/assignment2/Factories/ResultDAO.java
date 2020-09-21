package assignment2.Factories;

import assignment2.Models.Question.Question;
import assignment2.Models.Test.QuestionAttempt;
import assignment2.Models.Test.Result;
import assignment2.Models.Test.Test;
import assignment2.Models.User.Student;
import assignment2.Models.User.User;
import assignment2.Models.User.UserType;
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

public class ResultDAO
{
    Connection conn = null;

    public ResultDAO(Connection conn)
    {
        this.conn = conn;
    }

    public List<Result> GetAllResults()
    {
        List<Result> results = new ArrayList<>();

        try
        {
            String sql = "SELECT * FROM Result";
            Statement stmnt = conn.createStatement();
            ResultSet sqlResults = stmnt.executeQuery(sql);

            while (sqlResults.next())
            {
                Integer ID = sqlResults.getInt("ID");
                Integer UserID = sqlResults.getInt("UserID");
                String Name = sqlResults.getString("TestName");
                User user = DataFactory.getInstance().getUserDAO().GetUser(UserID);
                Result result = new Result(ID, (Student)user, new ArrayList<>(), Name);
                results.add(result);
            }

            stmnt.close();

            for(Result result : results)
            {
                sql = "SELECT * FROM QuestionAttempt WHERE ResultID = ?";
                PreparedStatement pstmnt = conn.prepareStatement(sql);
                pstmnt.setInt(1, result.ID);
                sqlResults = pstmnt.executeQuery();
                while (sqlResults.next())
                {
                    Question question = DataFactory.getInstance().getQuestionDAO().GetQuestion(sqlResults.getInt("QuestionID"));
                    result.QuestionAttempts.add(new QuestionAttempt(question, sqlResults.getString("AnswerGiven"), sqlResults.getBoolean("Correct")));
                }
                sqlResults.close();
                pstmnt.close();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return new ArrayList<>();
        }

        return results;
    }

    public Result GetResult(String testName, String studentName)
    {
        try
        {
            String sql = "SELECT Result.* FROM Result\n" +
                    "JOIN User on User.ID = Result.UserID\n" +
                    "WHERE User.Name = ? AND Result.TestName = ?";

            PreparedStatement pstmnt = conn.prepareStatement(sql);
            pstmnt.setString(1, studentName);
            pstmnt.setString(2, testName);
            ResultSet sqlResults = pstmnt.executeQuery();

            Result result = null;

            if(sqlResults.next())
            {
                Integer ID = sqlResults.getInt("ID");
                Integer UserID = sqlResults.getInt("UserID");
                String Name = sqlResults.getString("TestName");
                User user = DataFactory.getInstance().getUserDAO().GetUser(UserID);
                result = new Result(ID, (Student)user, new ArrayList<>(), Name);
            }

            sql = "SELECT * FROM QuestionAttempt WHERE ResultID = ?";
            pstmnt = conn.prepareStatement(sql);
            pstmnt.setInt(1, result.ID);
            sqlResults = pstmnt.executeQuery();

            while (sqlResults.next())
            {
                Question question = DataFactory.getInstance().getQuestionDAO().GetQuestion(sqlResults.getInt("QuestionID"));
                result.QuestionAttempts.add(new QuestionAttempt(question, sqlResults.getString("AnswerGiven"), sqlResults.getBoolean("Correct")));
            }
            sqlResults.close();
            pstmnt.close();
            return result;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }


    public void AddResult(Result result)
    {
        try
        {
            String sql = "INSERT INTO Result (UserID, TestName) VALUES (?,?)";
            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.setInt(1, result.Student.ID);
            stmnt.setString(2, result.TestName);
            stmnt.executeUpdate();

            int insertId = 0;
            ResultSet rs = stmnt.getGeneratedKeys();
            if (rs.next())
            {
                insertId = rs.getInt(1);
            }

            for (QuestionAttempt questionAttempt: result.QuestionAttempts)
            {
                sql = "INSERT INTO QuestionAttempt (ResultID, QuestionID, AnswerGiven, Correct) VALUES (?,?,?,?)";
                stmnt = conn.prepareStatement(sql);
                stmnt.setInt(1, insertId);
                stmnt.setInt(2, questionAttempt.Question.ID);
                stmnt.setString(3, questionAttempt.AnswerGiven);
                stmnt.setBoolean(4, questionAttempt.Correct);
                stmnt.executeUpdate();
            }

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
