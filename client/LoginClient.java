package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.client;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common.LoginServiceRI;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.server.LoginServiceImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoginClient
{
    public static void main (String[] args)
    {
        try
        {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            LoginServiceRI loginService = (LoginServiceRI) registry.lookup("LoginService");
            boolean registered = loginService.register("patrick", "1234");
            System.out.println("Registo: " + (registered ? "Sucesso" : "Já existe"));

            boolean loggedIn = loginService.login("patrick", "1234");
            System.out.println("Login: " + (loggedIn ? "Sucesso" : "Falhou"));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
