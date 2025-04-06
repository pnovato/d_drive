package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.server;

import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common.FileManagerRI;
import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileManagerImpl extends UnicastRemoteObject implements FileManagerRI
{
    private final String baseDir = "users";

    public FileManagerImpl() throws RemoteException
    {
        super();
    }

    //esta parte vai criar uma referencia de caminho para /local, e não cria a pasta em si
    private File resolvePath(String username, String path)
    {
        return new File (baseDir + File.separator + username + "/local" + path);
    }

    @Override
    public List<String> listDirectory(String username, String path) throws RemoteException
    {
        File dir = resolvePath(username, path);
        if (dir.exists() && dir.isDirectory())
        {
            return Arrays.asList(dir.list());
        }
        return new ArrayList<>();
    }

    @Override
    public boolean createFolder(String username, String path, String folderName) throws RemoteException
    {
        File dir = resolvePath(username, path + File.separator + folderName);
        return dir.mkdirs();
    }

    @Override
    public boolean deleteFolder(String username, String path) throws RemoteException
    {
        File file = resolvePath(username, path);
        return deleteRecursively(file);
    }

    public boolean deleteRecursively(File file)
    {
        if (!file.exists()) return false;
        if (file.isDirectory())
        {
            for (File f : file.listFiles())
                deleteRecursively(file);
        }
        return file.delete();
    }

    @Override
    public boolean renameFolder(String username, String oldName, String newName) throws RemoteException
    {
        File oldFile = resolvePath(username, oldName);
        File newFile = new File(oldFile.getParent(), newName);
        return oldFile.renameTo(newFile);
    }

}
