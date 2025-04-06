package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.server.FileManagerImpl;

import java.rmi.RemoteException;
import java.rmi.Remote;

public interface LoginServiceRI extends Remote
{
    boolean register (String username, String password) throws  RemoteException;
    boolean login (String username, String password) throws RemoteException;

    FileManagerRI getFileManager(String username) throws RemoteException;
}
