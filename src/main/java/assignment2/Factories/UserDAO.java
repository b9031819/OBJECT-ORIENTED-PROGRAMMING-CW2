package assignment2.Factories;

import assignment2.Models.User.Student;
import assignment2.Models.User.Teacher;
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

public class UserDAO
{
    Connection conn = null;

    public UserDAO(Connection conn)
    {
        this.conn = conn;
    }

    public List<User> GetAllUsers()
    {

        List<User> users = new ArrayList<>();
        try
        {
            String sql = "SELECT * FROM User";
            Statement stmnt = conn.createStatement();
            ResultSet results = stmnt.executeQuery(sql);

            while (results.next())
            {
                Integer ID = results.getInt("ID");
                String Name = results.getString("Name");
                Integer Type = results.getInt("Type");

                User user = new User(ID,Name,UserType.values()[Type]);
                users.add(user);
            }
            results.close();
            stmnt.close();

            List<User> preparedUsers = new ArrayList<>();

            for(User user : users)
            {
                if(user.UserType == UserType.STUDENT)
                {
                    preparedUsers.add(getStudentUser(user));
                }
                else
                {
                    preparedUsers.add(user);
                }
            }

            return preparedUsers;

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return new ArrayList<>();
        }

    }

    public User GetUser(int ID)
    {
        User user = null;

        try
        {
            String sql = "SELECT * FROM User WHERE ID = ?";
            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.setInt(1, ID);
            ResultSet results = stmnt.executeQuery();

            if (results.next())
            {
                String Name = results.getString("Name");
                Integer Type = results.getInt("Type");

                user = new User(ID,Name,UserType.values()[Type]);
            }
            results.close();
            stmnt.close();

            if(user.UserType == UserType.STUDENT)
            {
                return getStudentUser(user);
            }
            else
            {
                return user;
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return user;
        }
    }

    private Student getStudentUser(User user)
    {
        if(user.UserType != UserType.STUDENT)
        {
            System.out.println("User is not a student");
            return null;
        }

        try
        {
            String sql = "SELECT * FROM Student WHERE UserID = " + user.ID;
            Statement stmnt = conn.createStatement();
            ResultSet results = stmnt.executeQuery(sql);
            Student student = null;
            if(results.next())
            {
                String Class = results.getString("Class");
                Integer Year = results.getInt("Year");
                String DOB = results.getString("DOB");
                student = new Student(user.ID, user.Name, user.UserType, DOB, Class, Year);
            }
            results.close();
            stmnt.close();
            return student;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public void AddUser(User user)
    {
        try
        {
            String sql = "INSERT INTO User (Name, Type) VALUES (?,?)";
            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.setString(1, user.Name);
            stmnt.setInt(2, user.UserType.ordinal());
            stmnt.executeUpdate();

            int insertId = 0;
            ResultSet rs = stmnt.getGeneratedKeys();
            if (rs.next())
            {
                insertId = rs.getInt(1);
            }

            if(user.UserType == UserType.STUDENT)
            {
                Student student = (Student)user;

                sql = "INSERT INTO Student (UserID, Class, Year, DOB) VALUES (?,?,?,?)";
                stmnt = conn.prepareStatement(sql);
                stmnt.setInt(1, insertId);
                stmnt.setString(2, student.Class);
                stmnt.setInt(3, student.Year);
                stmnt.setString(4, student.DOB);
                stmnt.executeUpdate();
            }

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
