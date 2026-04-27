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
    public boolean authentifier(String login, String motDePasse) throws RemoteException {
        System.out.println(" authentifier: " + login);
        return db.auth(login, motDePasse);
    }

    // Get user by username
    public Utilisateur getUserByUsername(String username) throws RemoteException {
        System.out.println("→ getUserByUsername: " + username);

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

                System.out.println("✅ User found: " + user.getNom() + " " + user.getPrenom());
                return user;
            } else {
                System.out.println("⚠️ No user found with username: " + username);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching user: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException("Error fetching user data: " + e.getMessage(), e);
        }
        return null;
    }

    // client operations
    @Override
    public double consulterSolde(String numeroCompte) throws RemoteException {
        System.out.println("→ consulterSolde: " + numeroCompte);
        try {
            return db.consulterSolde(numeroCompte);
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public boolean deposer(String numeroCompte, double montant) throws RemoteException {
        System.out.println("→ deposer: " + numeroCompte + " montant: " + montant);
        try {
            boolean result = db.deposer(numeroCompte, montant);
            if (result)
                db.enregistrerTransaction(numeroCompte, "DEPOT", montant, null);
            return result;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public boolean retirer(String numeroCompte, double montant) throws RemoteException {
        System.out.println("→ retirer: " + numeroCompte + " montant: " + montant);
        try {
            boolean result = db.retirer(numeroCompte, montant);
            if (result)
                db.enregistrerTransaction(numeroCompte, "RETRAIT", montant, null);
            return result;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public boolean virement(String compteSource, String compteDest, double montant) throws RemoteException {
        System.out.println("→ virement: " + compteSource + " → " + compteDest + " montant: " + montant);
        try {
            boolean result = db.virement(compteSource, compteDest, montant);
            if (result)
                db.enregistrerTransaction(compteSource, "VIREMENT", montant, compteDest);
            return result;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
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
    public boolean creerCompte(String titulaire, double soldeInitial) throws RemoteException {
        System.out.println("→ creerCompte: " + titulaire + " solde: " + soldeInitial);
        try {
            return db.creerCompte(titulaire, soldeInitial);
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
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
        System.out.println("→ listerComptes called for username: " + username);
        try {
            List<Compte> comptes = db.listerComptes(username);
            System.out.println("Found " + (comptes != null ? comptes.size() : 0) + " accounts");

            // Debug: Print each account
            if (comptes != null) {
                for (Compte c : comptes) {
                    System.out.println("  Account: " + c.getNumeroCompte() + " Balance: " + c.getSolde());
                }
            }

            return comptes;
        } catch (Exception e) {
            System.err.println("❌ Error in listerComptes: " + e.getMessage());
            e.printStackTrace();
            throw new RemoteException(e.getMessage());
        }
    }
}