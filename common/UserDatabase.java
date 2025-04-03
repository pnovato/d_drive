package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common;
import java.util.HashMap;
import java.util.Map;

public class UserDatabase
{
    private static final Map<String, User>users = new HashMap<>();

    public static synchronized boolean register (String username, String password)
    {
        if (users.containsKey(username))
        {
            return false;
        }
        users.put (username, new User(username, password));
        return true;
    }

    public static synchronized boolean login (String username, String password)
    {
        User user = users.get (username);
        return user != null && user.getPassword().equals(password);
    }
}
