package edu.ufp.inf.sd.rmi.projecto_SD.d_drive.server;
import edu.ufp.inf.sd.rmi.projecto_SD.d_drive.common.LoginServiceRI;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoginServer
{
    public static void main(String[] args)
    {
        try
        {
            String host = InetAddress.getLocalHost().getHostAddress();
            int port = 15679;
            String serviceName = "LoginService";

            if (args.length == 3)
            {
                host = args[0];
                port = Integer.parseInt(args[1]);
                serviceName = args[2];
            } else
            {
                System.err.println("Usage: java LoginServer <host> <port> <service_name>");
                System.exit(1);
            }

            System.setProperty("java.rmi.server.hostname", host);
            Registry registry = LocateRegistry.createRegistry(port);

            LoginServiceRI loginService = new LoginServiceImpl();
            registry.rebind(serviceName, loginService);

            System.out.println("LoginService bound at " + host + ":" + port + " with name '" + serviceName + "'");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

