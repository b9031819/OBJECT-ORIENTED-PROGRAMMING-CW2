package assignment2.Factories;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataFactory
{
    private static String dbPath = "assignment2.db";
    private static DataFactory INSTANCE;

    private Connection conn = null;

    private QuestionDAO questionDAO;
    private ResultDAO resultDAO;
    private TestDAO testDAO;
    private UserDAO userDAO;

    private DataFactory(String dbPath)
    {
        try
        {
            String url = "jdbc:sqlite:" + dbPath;
            conn = DriverManager.getConnection(url);
            questionDAO = new QuestionDAO(conn);
            resultDAO = new ResultDAO(conn);
            testDAO = new TestDAO(conn);
            userDAO = new UserDAO(conn);
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }

        if(conn == null)
        {
            return ;
        }
    }

    public static DataFactory getInstance()
    {
        if(INSTANCE == null) {
            INSTANCE = new DataFactory(dbPath);
        }

        return INSTANCE;
    }

    public QuestionDAO getQuestionDAO() {
        return questionDAO;
    }

    public ResultDAO getResultDAO()
    {
        return resultDAO;
    }

    public TestDAO getTestDAO()
    {
        return testDAO;
    }

    public UserDAO getUserDAO()
    {
        return userDAO;
    }
}
