package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.client;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common.LoginServiceRI;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common.FileManagerRI;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.server.LoginServiceImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoginClient
{
    public static void main(String[] args)
    {
        try
        {
            // Conectar ao rmiregistry na porta personalizada
            Registry registry = LocateRegistry.getRegistry("localhost", 15679);

            // Fazer lookup do serviço de login
            LoginServiceRI loginService = (LoginServiceRI) registry.lookup("LoginService");

            String username = "patrick";
            String password = "1234";

            System.out.println("Tentando registar utilizador...");
            boolean registered = loginService.register(username, password);
            System.out.println("Registo: " + registered);

            System.out.println("Tentando login...");
            boolean loggedIn = loginService.login(username, password);
            System.out.println("Login: " + loggedIn);

            if (loggedIn)
            {
                FileManagerRI fm = loginService.getFileManager(username);

                System.out.println("Criando pasta 'teste'...");
                boolean created = fm.createFolder(username, "/", "teste");
                System.out.println("Pasta criada: " + created);

                System.out.println("Conteúdo de /local:");
                for (String item : fm.listDirectory(username, "/"))
                {
                    System.out.println(" - " + item);
                }

                System.out.println("Renomeando 'teste' para 'meuTeste'...");
                boolean renamed = fm.renameFolder(username, "/teste", "meuTeste");
                System.out.println("Renomeado: " + renamed);

                System.out.println("Eliminando pasta 'meuTeste'...");
                boolean deleted = fm.deleteFolder(username, "/meuTeste");
                System.out.println("Eliminado: " + deleted);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}