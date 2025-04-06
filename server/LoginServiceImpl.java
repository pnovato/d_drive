package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.server;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common.LoginServiceRI;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common.UserDatabase;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common.FileManagerRI;
import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class LoginServiceImpl extends UnicastRemoteObject implements LoginServiceRI
{
    public LoginServiceImpl() throws RemoteException
    {
        super();
    }

    @Override
    public boolean register (String username, String password) throws RemoteException
    {
        return UserDatabase.register (username, password);
    }

    @Override
    public boolean login (String username, String password)
    {
        if (UserDatabase.login (username, password))
        {
            createUserDirectories(username);
            return true;
        }
        return false;
    }

    @Override
    public FileManagerRI getFileManager(String username) throws RemoteException
    {
        return new FileManagerImpl();
    }

    private void createUserDirectories(String username)
    {
        File localDir = new File("users/" + username + "/local");
        File partilhasDir = new File("users/" + username + "/partilhas");

        if (!localDir.exists())
            localDir.mkdir();
        if (!partilhasDir.exists())
            partilhasDir.mkdir();
    }
}
