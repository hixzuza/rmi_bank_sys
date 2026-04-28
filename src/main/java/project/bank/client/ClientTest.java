package project.bank.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import project.bank.commun.IBanqueService;

// this class just for test the connectin between the client and server --RMI--

public class ClientTest {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            IBanqueService service =
                    (IBanqueService) registry.lookup("BanqueService");

            // =========================
            // 1. AUTHENTICATION
            // =========================
            boolean ok = service.authentifier("client1", "client123");
            System.out.println("authentification : " + ok);

            if (!ok) {
                System.out.println(" Auth failed, stopping tests.");
                return;
            }

            // =========================
            // 2. CHECK INITIAL STATE
            // =========================
            System.out.println("\n--- INITIAL STATE ---");
            System.out.println("Solde C001 : " + service.consulterSolde("C001"));
            System.out.println("Solde C002 : " + service.consulterSolde("C002"));

            // =========================
            // 3. DEPOT
            // =========================
            System.out.println("\n--- DEPOT ---");
            boolean depot = service.deposer("C001", 100);
            System.out.println("Depot 100 C001 : " + depot);
            System.out.println("Nouveau solde C001 : " + service.consulterSolde("C001"));

            // =========================
            // 4. RETRAIT
            // =========================
            System.out.println("\n--- RETRAIT ---");
            boolean retrait = service.retirer("C001", 50);
            System.out.println("Retrait 50 C001 : " + retrait);
            System.out.println("Nouveau solde C001 : " + service.consulterSolde("C001"));

            // =========================
            // 5. VIREMENT
            // =========================
            System.out.println("\n--- VIREMENT ---");
            boolean virement = service.virement("C001", "C002", 200);
            System.out.println("Virement 200 C001 -> C002 : " + virement);

            System.out.println("Solde C001 : " + service.consulterSolde("C001"));
            System.out.println("Solde C002 : " + service.consulterSolde("C002"));

            // =========================
            // 6. HISTORIQUE
            // =========================
            System.out.println("\n--- HISTORIQUE C001 ---");
            List<?> histo1 = service.getHistorique("C001");
            for (Object h : histo1) {
                System.out.println(h);
            }

            System.out.println("\n--- HISTORIQUE C002 ---");
            List<?> histo2 = service.getHistorique("C002");
            for (Object h : histo2) {
                System.out.println(h);
            }

            // =========================
            // 7. LIST ACCOUNTS
            // =========================
//            System.out.println("\n--- LISTE COMPTES ---");
//            List<?> comptes = service.listerComptes();
//            for (Object c : comptes) {
//                System.out.println(c);
//            }

            System.out.println("\n✔ ALL TESTS COMPLETED SUCCESSFULLY");

        } catch (Exception e) {
            System.out.println(" ERROR DURING TESTS");
            e.printStackTrace();
        }
    }
}