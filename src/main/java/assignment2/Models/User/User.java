package assignment2.Models.User;

public class User implements IUser
{
    public int ID;
    public String Name;
    public UserType UserType;

    public User(int id, String name, assignment2.Models.User.UserType userType)
    {
        ID = id;
        Name = name;
        UserType = userType;
    }

    @Override
    public assignment2.Models.User.UserType getType()
    {
        return this.UserType;
    }
}
