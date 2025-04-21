package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.client;
import edu.ufp.inf.sd.rmi.p01_helloworld.server.HelloWorldServer;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import com.rabbitmq.client.DeliverCallback;
import java.nio.charset.StandardCharsets;

public class LoginClient
{

    public static void main(String[] args)
    {
        if (args == null || args.length < 5)
        {
            System.out.println("Usage: java LoginClient <host> <port> <service_name> <username> <password>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String serviceName = args[2];
        String username = args[3];
        String password = args[4];

        try
        {
            Registry registry = LocateRegistry.getRegistry(host, port);
            LoginServiceRI loginService = (LoginServiceRI) registry.lookup(serviceName);

            boolean registered = loginService.register(username, password);
            System.out.println("Tentando login remoto com callback...");

            // R1: Login com callback (que retorna uma sessão)
            PeerClientRI peerClient = new PeerClientImpl(username);
            UserSessionRI session = loginService.login(username, password, peerClient);

            if (session != null)
            {
                System.out.println("Login realizado com sucesso!");
                FileManagerRI fileManager = session.getFileManager();

                DeliverCallback callback = (consumerTag, delivery) ->
                {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    System.out.println("[RabbitMQ] Nova mensagem: " + message);
                };
                RabbitManager.subscribeToUserQueue(username, callback);


                // ────────────────────────────────────────────────
                // R1: Registro
                // boolean success = loginService.register("ana", "123");
                // System.out.println("Registrou ana: " + success);

                // ────────────────────────────────────────────────
                // R2: Operações com ficheiros
                // fileManager.createFolder(username, "/", "testeSync");
                // fileManager.renameFolder(username, "/testeSync", "testeRenomeado");
                // fileManager.deleteFolder(username, "/testeRenomeado");

                // ────────────────────────────────────────────────
                // R2: Conteúdos de uma pasta (Listando)
                // String[] contents = fileManager.listDirectory(username, "/");
                // System.out.println("Conteúdo de /local/:");
                // for (String item : contents) System.out.println(" - " + item);

                // ────────────────────────────────────────────────
                // R3: Pasta partilhada (via RMI)
                //fileManager.createFolder(username, "/", "testeSync");
                //fileManager.shareFolder(username, "ana", "testeSync", false);

                // ────────────────────────────────────────────────
                // R3: Pasta partilhada (via RabbitMQ apenas - teste manual)
                // RabbitManager.publishUpdate("ana", "Pasta 'testeSync' partilhada contigo por " + username);

                // ────────────────────────────────────────────────
                // R3: Pasta partilhada com notificações RMI + RabbitMQ

                //String target = "ana";
                //String folderName = "testeSync";
                //fileManager.createFolder(username, "/", folderName);
                //if (username.equals(target))
                //{
                //    System.out.println("Não é possível partilhar uma pasta consigo mesmo.");
                //}
                //else
                //{
                //    System.out.println("Partilhando pasta testeSync com '" + target + "'...");
                //    fileManager.shareFolder(username, target, folderName, false);
                //}

                // ────────────────────────────────────────────────
                // R4: Sincronizar pastas partilhadas com o utilizador
                //String folderName = "testeSync";
                //String owner = "ptrck"; // ← substitui pelo nome real de quem partilhou

                //System.out.println("Sincronizando pasta partilhada '" + folderName + "' de " + owner + "...");
                //boolean syncOk = fileManager.syncSharedFolder(username, folderName, owner);
                //System.out.println("Status da sincronização: " + syncOk);


                // ────────────────────────────────────────────────
                // R5: TESTE DE PROPAGAÇÃO PERSISTENTE (JSON)
                // Partilha persistente (real copy), sincronização automática

                // Criação inicial da pasta
                fileManager.createFolder(username, "/", "pastaR5");

                // Partilha real (persistente) com outro utilizador
                fileManager.shareFolder(username, "ptrck", "pastaR5", true);

                // Renomear a pasta para verificar propagação
                fileManager.renameFolder(username, "/pastaR5", "pastaR5_renomeada");

                // Apagar a pasta para verificar se cópias nas partilhas são removidas
                fileManager.deleteFolder(username, "/pastaR5_renomeada");


            }
            else
            {
                System.out.println("Login falhou. Verifique credenciais.");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}