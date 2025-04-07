package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.server;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common.UserSessionRI;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common.FileManagerRI;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common.PeerClientRI;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class UserSessionImpl extends UnicastRemoteObject implements UserSessionRI
{
    private final String username;
    private final FileManagerRI fileManagerRI;
    private final PeerClientRI peerClientRI;

    public UserSessionImpl(String username, PeerClientRI peerClientRI, FileManagerRI fileManagerRI) throws RemoteException
    {
        super();
        this.username = username;
        this.peerClientRI = peerClientRI;
        this.fileManagerRI = new FileManagerImpl();
    }

    @Override
    public FileManagerRI getFileManager() throws RemoteException
    {
        return fileManagerRI;
    }

    @Override
    public PeerClientRI getPeerClientRI() throws RemoteException
    {
        return peerClientRI;
    }

    public String getUsername()
    {
        return username;
    }

}
