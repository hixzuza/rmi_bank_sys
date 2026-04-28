package project.bank.commun;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientRMI {

    private static IBanqueService service;
    private static boolean connected = false;

    public static void connect() throws Exception {
        if (!connected) {
            try {
                Registry registry = LocateRegistry.getRegistry("localhost", 1099);
                service = (IBanqueService) registry.lookup("BanqueService");
                connected = true;
                System.out.println(" connect  to RMI server");
            } catch (Exception e) {
                System.err.println(" error  to connect to RMI: " + e.getMessage());
                throw e;
            }
        }
    }

    public static IBanqueService getService() {
        if (service == null) {
            try {
                connect(); // auto connect if not connected
            } catch (Exception e) {
                System.err.println(" can t  connect to RMI server: " + e.getMessage());
                throw new IllegalStateException("not connected to RMI server check the server if running.", e);
            }
        }
        return service;
    }

}