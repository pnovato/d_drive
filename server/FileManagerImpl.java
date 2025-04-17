package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.server;

import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common.*;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileManagerImpl extends UnicastRemoteObject implements FileManagerRI
{
    private final String baseDir = System.getProperty("user.dir") + "/../users";

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

    @Override
    public boolean shareFolder(String fromUser, String toUser, String folderName) throws RemoteException
    {
        try
        {
            // Caminho da pasta que vai ser partilhada
            File sourceFolder = new File(baseDir + "/" + fromUser + "/local/" + folderName);

            if (fromUser.equals(toUser)) {
                System.out.println("[AVISO] Utilizador tentou partilhar consigo mesmo.");
                return false;
            }


            if (!sourceFolder.exists() || !sourceFolder.isDirectory())
            {
                System.out.println("Pasta a partilhar não existe: " + sourceFolder.getPath());
                return false;
            }

            // Caminho de destino da partilha
            String sharedName = fromUser + "_" + folderName;
            File targetFolder = new File(baseDir + File.separator + toUser + "/partilhas/" + sharedName);

            if (targetFolder.exists())
            {
                System.out.println("Pasta já foi partilhada com esse utilizador.");
                return false;
            }

            // Criar pasta partilhas vazia
            boolean created = targetFolder.mkdirs();

            //ONDE ENVIO NOTIFICAÇÃO SOBRE A NOVA PASTA PARTILHADA VIA RMI OU RABBITMQ
            if (created)
            {
                System.out.println("Pasta partilhada criada em: " + targetFolder.getPath());

                //R3: RMI - notificação síncrona (se online)
                UserSessionRI toUserSession = SessionManager.activeSessions.get(toUser);
                if (toUserSession != null)
                {
                    PeerClientRI peer = toUserSession.getPeerClientRI();
                    peer.notifyUpdate(folderName, "Você recebeu uma nova partilha de " + fromUser + "!");
                }
                else
                    System.out.println("Utilizador " + toUser + " não está online. Notificação não enviada.");

                //R3: RabbitMQ - notificação assíncrona
                String rabbitMessage = "Pasta '" + folderName + "' partilhada contigo por " + fromUser;
                RabbitManager.publishUpdate(toUser, rabbitMessage);
            }
            return created;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
