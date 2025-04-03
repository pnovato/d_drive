package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.server;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common.LoginServiceRI;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common.UserDatabase;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class LoginServiceImpl extends UnicastRemoteObject implements LoginServiceRI
{
    public LoginServiceImpl() throws RemoteException
    {
        super();
    }

    @Override
    public synchronized boolean register (String username, String password)
    {
        return UserDatabase.register (username, password);
    }

    @Override
    public synchronized boolean login (String username, String password)
    {
        return UserDatabase.login (username, password);
    }
}
