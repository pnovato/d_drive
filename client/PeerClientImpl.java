package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.client;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common.PeerClientRI;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PeerClientImpl extends UnicastRemoteObject implements PeerClientRI
{
    private final String username;

    public PeerClientImpl(String username) throws RemoteException
    {
        super();
        this.username = username;
    }

    @Override
    public void notifyUpdate(String folderName, String message) throws RemoteException
    {
        System.out.println("[Notificação para " + username + "] Alteração em '" + folderName + "': " + message);
    }
}
