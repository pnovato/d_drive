package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.client;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common.*;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.server.LoginServiceImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoginClient
{

    public static void main(String[] args)
    {
        try
        {
            // Conectar ao rmiregistry na porta definida
            Registry registry = LocateRegistry.getRegistry("localhost", 15679);

            // Obter referência remota ao serviço de login
            LoginServiceRI loginService = (LoginServiceRI) registry.lookup("LoginService");

            String username = "patrick";
            String password = "123";

            // Criar instância remota do cliente (callback)
            PeerClientRI peerClient = new PeerClientImpl(username);

            boolean registered = loginService.register(username, password);

            System.out.println("Tentando login remoto com callback...");

            // Login com callback
            UserSessionRI session = loginService.login(username, password, peerClient);

            if (session != null)
            {
                System.out.println("Login realizado com sucesso!");

                FileManagerRI fileManager = session.getFileManager();

                boolean shared = fileManager.shareFolder("patrick", "ana", "testeSync");
                System.out.println("Pasta partilhada com ana: " + shared);

                // Teste: criar pasta "testeSync"
                System.out.println("Criando pasta partilhada 'testeSync'...");
                boolean created = fileManager.createFolder(username, "/", "testeSync");
                System.out.println("Criada: " + created);

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