package project.bank.commun;

import project.bank.client.model.Compte;
import project.bank.client.model.Transaction;
import project.bank.client.model.Utilisateur;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IBanqueService extends Remote {

    // Authentication
    boolean authentifier(String login, String motDePasse) throws RemoteException;
    Utilisateur getUserByUsername(String username) throws RemoteException;

    // Client operations
    double consulterSolde(String numeroCompte) throws RemoteException;
    boolean deposer(String numeroCompte, double montant) throws RemoteException;
    boolean retirer(String numeroCompte, double montant) throws RemoteException;
    boolean virement(String compteSource, String compteDest, double montant) throws RemoteException;
    List<Transaction> getHistorique(String numeroCompte) throws RemoteException;

    // Admin operations
    boolean creerCompte(String titulaire, double soldeInitial) throws RemoteException;
    boolean supprimerCompte(String numeroCompte) throws RemoteException;
    List<Compte> listerComptes(String username) throws RemoteException;
}