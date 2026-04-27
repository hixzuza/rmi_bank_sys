package project.bank.client.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Compte implements Serializable {

    private static final long serialVersionUID = 5676201359327767535L;

    private String numeroCompte;
    private double solde;
    private LocalDate dateCreation;
    private boolean actif;
    private int idUser;
    private String clientName;
    private String lastTransaction;

    public Compte() {
    }

    public Compte(String numeroCompte, double solde, LocalDate dateCreation, boolean actif, int idUser) {
        this.numeroCompte = numeroCompte;
        this.solde = solde;
        this.dateCreation = dateCreation;
        this.actif = actif;
        this.idUser = idUser;
    }

    // Getters
    public String getNumeroCompte() {
        return numeroCompte;
    }

    public double getSolde() {
        return solde;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public boolean isActif() {
        return actif;
    }

    public int getIdUser() {
        return idUser;
    }

    public String getClientName() {
        return clientName;
    }

    public String getLastTransaction() {
        return lastTransaction;
    }

    // Setters
    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setLastTransaction(String lastTransaction) {
        this.lastTransaction = lastTransaction;
    }

    // Formatted date for table display
    public String getFormattedDateCreation() {
        if (dateCreation == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dateCreation.format(formatter);
    }

    @Override
    public String toString() {
        return numeroCompte + " | solde: " + solde + " | actif: " + actif;
    }
}