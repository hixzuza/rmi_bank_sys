package project.bank.server;

import project.bank.commun.IBanqueService;
import project.bank.server.impl.BanqueServiceImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServeurPrincipal {

    public static void main(String[] args) {
        try {

            Registry registry;

            try {
            	
                registry = LocateRegistry.createRegistry(1099);
                System.out.println("New RMI registry created.");
                
            } catch (Exception e) {
                registry = LocateRegistry.getRegistry(1099);
                System.out.println("Existing RMI registry used.");
            }

            IBanqueService service = new BanqueServiceImpl();
            registry.rebind("BanqueService", service);

            System.out.println("Server RMI running on port 1099.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}