package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common;
import java.rmi.RemoteException;
import java.rmi.Remote;

public interface LoginServiceRI extends Remote
{
    boolean register (String username, String password) throws  RemoteException;
    boolean login (String username, String password) throws RemoteException;
}
