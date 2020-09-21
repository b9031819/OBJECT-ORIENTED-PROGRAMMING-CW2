package assignment2.Models.User;

public class Student extends User
{
    public String DOB;
    public String Class;
    public int Year;

    public Student(int ID, String name, assignment2.Models.User.UserType userType, String DOB, String classID, int year) {
        super(ID, name, userType);
        this.DOB = DOB;
        Class = classID;
        Year = year;
    }
}
