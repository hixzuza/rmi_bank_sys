package project.bank.client.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction implements Serializable {


    private int idTransaction;
    private String numeroCompte;
    private String typeOp;
    private double montant;
    private LocalDateTime dateOp;
    private String compteDest;

    public Transaction() {}

    public Transaction(int idTransaction, String numeroCompte, String typeOp,
                       double montant, LocalDateTime dateOp, String compteDest) {
        this.idTransaction = idTransaction;
        this.numeroCompte = numeroCompte;
        this.typeOp = typeOp;
        this.montant = montant;
        this.dateOp = dateOp;
        this.compteDest = compteDest;
    }

    // Getters and Setters
    public int getIdTransaction() { return idTransaction; }
    public void setIdTransaction(int idTransaction) { this.idTransaction = idTransaction; }

    public String getNumeroCompte() { return numeroCompte; }
    public void setNumeroCompte(String numeroCompte) { this.numeroCompte = numeroCompte; }

    public String getTypeOp() { return typeOp; }
    public void setTypeOp(String typeOp) { this.typeOp = typeOp; }

    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }

    public LocalDateTime getDateOp() { return dateOp; }
    public void setDateOp(LocalDateTime dateOp) { this.dateOp = dateOp; }

    public String getCompteDest() { return compteDest; }
    public void setCompteDest(String compteDest) { this.compteDest = compteDest; }

    // Formatted date for table display
    public String getFormattedDateOp() {
        if (dateOp == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateOp.format(formatter);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + idTransaction +
                ", compte=" + numeroCompte +
                ", type='" + typeOp + '\'' +
                ", montant=" + montant +
                ", date=" + dateOp +
                ", dest='" + compteDest + '\'' +
                '}';
    }
}