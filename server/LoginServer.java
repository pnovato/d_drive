package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.server;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common.LoginServiceRI;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoginServer
{
    public static void main (String[] args)
    {
        try
        {
            LoginServiceRI loginService = new LoginServiceImpl();
            Registry registry = LocateRegistry.getRegistry("localhost",15679);
            registry.rebind("LoginService", loginService);
            System.out.println("LoginService pronto e registrado no RMI registry");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
