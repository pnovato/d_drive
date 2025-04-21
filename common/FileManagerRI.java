package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common;

import java.rmi.RemoteException;
import java.rmi.Remote;
import java.util.List;

public interface FileManagerRI extends Remote
{
    List<String> listDirectory (String username, String path) throws RemoteException;
    boolean createFolder (String username, String path, String folderName) throws RemoteException;
    boolean deleteFolder (String username, String path) throws RemoteException;
    boolean renameFolder (String username, String oldPath, String newName) throws RemoteException;
    boolean shareFolder(String fromUser, String toUser, String folderName, boolean isPersistent) throws RemoteException;
    boolean syncSharedFolder(String username, String folderName, String ownerUsername) throws RemoteException;
}
