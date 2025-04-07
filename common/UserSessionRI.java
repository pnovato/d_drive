package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common;
import java.rmi.RemoteException;
import java.rmi.Remote;

public interface UserSessionRI extends Remote
{
    FileManagerRI getFileManager() throws RemoteException;
    PeerClientRI getPeerClientRI() throws RemoteException;
}
