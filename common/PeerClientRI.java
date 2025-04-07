package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common;

import java.rmi.RemoteException;
import java.rmi.Remote;

public interface PeerClientRI extends Remote
{
    void notifyUpdate(String folderName, String message) throws RemoteException;
}
