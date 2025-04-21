package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.server;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common.*;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.util.PartilhaManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
        File newFolder = new File(baseDir + File.separator + username + "/local" + path + "/" + folderName);

        if (!newFolder.exists()) {
            boolean created = newFolder.mkdirs();
            if (created) {
                System.out.println("Pasta criada: " + newFolder.getAbsolutePath());

                // R5: Se a pasta já foi partilhada, propagar nova versão para os destinatários
                if (PartilhaManager.isPartilhada(username, folderName)) {
                    System.out.println("[R5] Propagando atualização para partilhas persistentes...");
                    propagatePersintentUpdate(username, folderName);
                }

                return true;
            }
        } else {
            System.out.println("Pasta já existe: " + newFolder.getAbsolutePath());
        }

        return false;
    }




    @Override
    public boolean deleteFolder(String username, String path) throws RemoteException
    {
        File folderToDelete = new File(baseDir + File.separator + username + "/local" + path);

        if (folderToDelete.exists())
        {
            try
            {
                deleteDirectory(folderToDelete);
                System.out.println("Pasta eliminada: " + folderToDelete.getAbsolutePath());

                // R5: Propaga remoção para partilhas persistentes
                String folderName = folderToDelete.getName();
                if (PartilhaManager.isPartilhada(username, folderName))
                {
                    System.out.println("[R5] Propagando remoção para partilhas persistentes...");
                    propagatePersintentUpdate(username, folderName);
                }

                return true;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        System.out.println("Pasta não encontrada para exclusão.");
        return false;

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
        File oldFolder = new File(baseDir + File.separator + username + "/local" + oldName);
        File newFolder = new File(oldFolder.getParent(), newName);

        if (oldFolder.exists() && !newFolder.exists())
        {
            boolean renamed = oldFolder.renameTo(newFolder);
            if (renamed)
            {
                System.out.println("Pasta renomeada: " + oldFolder.getName() + " → " + newFolder.getName());

                // R5: Propaga alteração de nome para os utilizadores partilhados
                if (PartilhaManager.isPartilhada(username, oldFolder.getName()))
                {
                    System.out.println("[R5] Propagando renomeação para partilhas persistentes...");
                    propagatePersintentUpdate(username, newFolder.getName());
                }

                return true;
            }
        }

        System.out.println("Erro ao renomear pasta.");
        return false;
    }





        @Override
        public boolean shareFolder(String fromUser, String toUser, String folderName, boolean isPersistent) throws RemoteException
        {
            try
            {
                // Caminho da pasta que vai ser partilhada
                File sourceFolder = new File(baseDir + File.separator + fromUser + "/local/" + folderName);

                if (fromUser.equals(toUser)) {
                    System.out.println("[AVISO] Utilizador tentou partilhar consigo mesmo.");
                    return false;
                }


                if (!sourceFolder.exists() || !sourceFolder.isDirectory())
                {
                    System.out.println("Pasta a partilhar não existe: " + sourceFolder.getPath());
                    return false;
                }

                File targetFolder;
                if (isPersistent)
                {
                    // R5 - cópia real
                    String sharedName = fromUser + "_real_copy_" + folderName;
                    targetFolder = new File(baseDir + File.separator + toUser + "/partilhas/" + sharedName);
                    if (targetFolder.exists())
                    {
                        System.out.println("A cópia persistente já existe.");
                        return false;
                    }

                    copyDirectory(sourceFolder, targetFolder);
                    System.out.println("Pasta copiada para " + targetFolder.getPath());
                    PartilhaManager.addPartilha(fromUser, folderName, toUser); // mantém registo
                }
                else
                {
                    // R3 - link simbólico
                    String sharedName = fromUser + "_symbolic_link_" + folderName;
                    targetFolder = new File(baseDir + File.separator + toUser + "/partilhas/symbolic_links/" + sharedName);
                    if (targetFolder.exists())
                    {
                        System.out.println("Pasta já foi partilhada com esse utilizador.");
                        return false;
                    }

                    boolean created = targetFolder.mkdirs();
                    if (!created)
                    {
                        System.out.println("Falha ao criar diretório simbólico.");
                        return false;
                    }
                    System.out.println("Pasta simbólica criada em: " + targetFolder.getPath());
                }


                UserSessionRI toUserSession = SessionManager.activeSessions.get(toUser);
                if (toUserSession != null)
                {
                    PeerClientRI peer = toUserSession.getPeerClientRI();
                    peer.notifyUpdate(folderName, "Você recebeu uma nova partilha de " + fromUser + "!");
                }
                else
                {
                    System.out.println("Utilizador " + toUser + " não está online. Notificação não enviada.");
                }

                String rabbitMessage = "Pasta '" + folderName + "' partilhada contigo por " + fromUser;
                RabbitManager.publishUpdate(toUser, rabbitMessage);

                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }





    @Override
    public boolean syncSharedFolder(String username, String folderName, String ownerUsername) throws RemoteException
    {
        try
        {
            File sourceDir = new File(baseDir + File.separator + ownerUsername + "/local/" + folderName);
            File targetDir = new File(baseDir + File.separator + username + "/partilhas/real_copies/" + ownerUsername + "_real_copy_" + folderName);

            if (!sourceDir.exists() || !sourceDir.isDirectory())
            {
                System.out.println("[SYNC] Pasta original não encontrada para sincronizar.");
                return false;
            }

            if (!targetDir.exists())
            {
                targetDir.mkdirs();
            }

            // Copiar ficheiros da pasta partilhada
            File[] files = sourceDir.listFiles();
            if (files != null)
            {
                for (File f : files)
                {
                    File dest = new File(targetDir, f.getName());
                    Files.copy(f.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }

            System.out.println("[SYNC] Pasta sincronizada com sucesso: " + targetDir.getAbsolutePath());
            return true;

        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }





    private void copyDirectory(File source, File target) throws IOException
    {
        if (source.isDirectory())
        {
            if (!target.exists())
            {
                target.mkdirs();
            }

            String[] children = source.list();
            if (children != null)
            {
                for (String file : children)
                {
                    copyDirectory(new File(source, file), new File(target, file));
                }
            }
        }
        else
        {
            try (InputStream in = new FileInputStream(source);
                 OutputStream out = new FileOutputStream(target))
            {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0)
                {
                    out.write(buffer, 0, length);
                }
            }
        }
    }






    private void deleteDirectory(File dir) throws IOException
    {
        if (dir.isDirectory())
        {
            File[] children = dir.listFiles();
            if (children != null)
            {
                for (File child : children)
                {
                    deleteDirectory(child);
                }
            }
        }
        if (!dir.delete()) {
            throw new IOException("Falha ao deletar: " + dir.getAbsolutePath());
        }
    }





    private void propagatePersintentUpdate(String username, String folderName)
    {
        List<String> destinatarios = PartilhaManager.getDestinatarios(username, folderName);
        for (String user : destinatarios)
        {
            File origem = new File(baseDir + File.separator + username + "/local/" + folderName);
            File destino = new File(baseDir + File.separator + user + "/partilhas/" + username + "_real_copy_" + folderName);

            try
            {
                if (destino.exists())
                {
                    deleteDirectory(destino);
                }
                copyDirectory(origem, destino);
                System.out.println("[SYNC] Pasta atualizada para " + user + ": " + destino.getAbsolutePath());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


}
