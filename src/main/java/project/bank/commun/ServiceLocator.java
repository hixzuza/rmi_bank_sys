package project.bank.commun;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServiceLocator {

    private static IBanqueService service;
    private static boolean connected = false;

    public static void connect() throws Exception {
        if (!connected) {
            try {
                Registry registry = LocateRegistry.getRegistry("localhost", 1099);
                service = (IBanqueService) registry.lookup("BanqueService");
                connected = true;
                System.out.println("✅ Connected to RMI server");
            } catch (Exception e) {
                System.err.println("❌ Failed to connect to RMI: " + e.getMessage());
                throw e;
            }
        }
    }

    public static IBanqueService getService() {
        if (service == null) {
            try {
                connect(); // Auto-connect if not connected
            } catch (Exception e) {
                System.err.println("❌ Cannot connect to RMI server: " + e.getMessage());
                throw new IllegalStateException("Not connected to RMI server. Make sure the server is running.", e);
            }
        }
        return service;
    }

    public static boolean isConnected() {
        return connected && service != null;
    }
}