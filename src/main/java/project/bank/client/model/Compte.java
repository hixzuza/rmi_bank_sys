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

    // Add this property for the table view
    private String formattedDateCreation;

    public Compte() {
    }

    public Compte(String numeroCompte, double solde, LocalDate dateCreation, boolean actif, int idUser) {
        this.numeroCompte = numeroCompte;
        this.solde = solde;
        this.dateCreation = dateCreation;
        this.actif = actif;
        this.idUser = idUser;
        updateFormattedDate(); // Set formatted date
    }

    // Getters and setters
    public String getNumeroCompte() {
        return numeroCompte;
    }

    public double getSolde() {
        return solde;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    // Add this getter for the table
    public String getFormattedDateCreation() {
        return formattedDateCreation;
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

    // Setters
    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        updateFormattedDate();
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

    // Helper method to update formatted date
    private void updateFormattedDate() {
        if (dateCreation != null) {
            formattedDateCreation = dateCreation.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } else {
            formattedDateCreation = "";
        }
    }

    @Override
    public String toString() {
        return numeroCompte + " | solde: " + solde + " | actif: " + actif;
    }
}