package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.server;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common.*;
import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginServiceImpl extends UnicastRemoteObject implements LoginServiceRI
{

    private final Map<String, UserSessionRI> activeSessions;

    public LoginServiceImpl() throws RemoteException
    {
        super();
        this.activeSessions = new ConcurrentHashMap<>();
        SessionManager.activeSessions = this.activeSessions;
    }

    @Override
    public boolean register (String username, String password) throws RemoteException
    {
        return UserDatabase.register (username, password);
    }

    @Override
    public UserSessionRI login (String username, String password, PeerClientRI peerClientRI) throws RemoteException
    {
        if (UserDatabase.login (username, password))
        {
            createUserDirectories(username);
            FileManagerRI fileManagerRI = new FileManagerImpl();
            UserSessionRI session = new UserSessionImpl(username, peerClientRI, fileManagerRI);
            activeSessions.put(username, session);
            return session;
        }
        return null;
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
