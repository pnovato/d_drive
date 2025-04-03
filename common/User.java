package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common;
import java.io.Serializable;

public class User implements Serializable
{
    private String username;
    private String password;

    public User (String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }
}
