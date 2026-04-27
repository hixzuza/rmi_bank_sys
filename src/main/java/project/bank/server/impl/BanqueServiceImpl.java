package project.bank.server.impl;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import project.bank.client.model.Compte;
import project.bank.client.model.Transaction;
import project.bank.client.model.Utilisateur;
import project.bank.commun.IBanqueService;
import project.bank.server.db.DatabaseConnection;
import project.bank.server.db.DbManager;

public class BanqueServiceImpl extends UnicastRemoteObject implements IBanqueService {

    @Serial
    private static final long serialVersionUID = -4362846381655663124L;

    private DbManager db;

    public BanqueServiceImpl() throws RemoteException, SQLException {
        super();
        this.db = new DbManager();
        System.out.println("BanqueServiceImpl created");
    }

    // auth
    @Override
    public boolean authentifier(String login, String password) throws RemoteException {
        System.out.println(" AUTH =  " + login);
        return db.auth(login, password);
    }


    // Get user by username
    public Utilisateur getUserByUsername(String username) throws RemoteException {
        System.out.println(" getUserByUsername: " + username);

        String sql = "SELECT id_user, username, nom, prenom, role FROM UTILISATEUR WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Utilisateur user = new Utilisateur();

                user.setIdUser(rs.getInt("id_user"));
                user.setUsername(rs.getString("username"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setRole(rs.getString("role"));

                System.out.println(" user found: " + user.getNom() + "  |  " + user.getPrenom());
                return user;
            } else {
                System.out.println(" no user found with username: " + username);
            }
        } catch (SQLException e) {
            System.err.println(" error getting  user: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // client operations
    @Override
    public double consulterSolde(String numCompte) throws RemoteException {
        System.out.println(" SOLD = " + numCompte);
        try {
            return db.consulterSolde(numCompte);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean deposer(String numCompte, double montant) throws RemoteException {
        System.out.println(" deposer : " + numCompte + " montant : " + montant);
        try {
            boolean result = db.deposer(numCompte, montant);
            if (result)
                db.enregistrerTransaction(numCompte, "DEPOT", montant, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }


    @Override
    public boolean retirer(String numeroCompte, double montant) throws RemoteException {
        System.out.println(" retirer : " + numeroCompte + " montant: " + montant);
        try {
            boolean result = db.retirer(numeroCompte, montant);
            if (result)
                db.enregistrerTransaction(numeroCompte, "RETRAIT", montant, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }

    @Override
    public boolean virement(String compteSource, String compteDest, double montant) throws RemoteException {
        System.out.println(" virement : " + compteSource + " ==>  " + compteDest + " montant : " + montant);
        try {
            boolean result = db.virement(compteSource, compteDest, montant);
            if (result)
                db.enregistrerTransaction(compteSource, "VIREMENT", montant, compteDest);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Transaction> getHistorique(String numeroCompte) throws RemoteException {
        System.out.println("→ getHistorique: " + numeroCompte);
        try {
            return db.getHistorique(numeroCompte);
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    // admin operations
    @Override
    public boolean creerCompte(String numCompte, double soldeInitial) throws RemoteException {
        System.out.println(" creerCompte : " + numCompte + " sold : " + soldeInitial);
        try {
            return db.creerCompte(numCompte, soldeInitial);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean supprimerCompte(String numeroCompte) throws RemoteException {
        System.out.println("→ supprimerCompte: " + numeroCompte);
        try {
            return db.supprimerCompte(numeroCompte);
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public List<Compte> listerComptes(String username) throws RemoteException {
        System.out.println("lister Comptes for : " + username);
        try {
            return db.listerComptes(username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage());
        }
    }
}