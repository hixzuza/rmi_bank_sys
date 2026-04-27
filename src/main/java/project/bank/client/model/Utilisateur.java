package project.bank.client.model;

import java.io.Serializable;

public class Utilisateur implements Serializable {

   

	
	private static final long serialVersionUID = -3158237873476172117L;
	private int    idUser;
    private String username;
    private String nom;
    private String prenom;
    private String role;

    public Utilisateur() {}

    public Utilisateur(int idUser, String username, String nom, String prenom, String role) {
        this.idUser   = idUser;
        this.username = username;
        this.nom      = nom;
        this.prenom   = prenom;
        this.role     = role;
    }

    public int    getIdUser()   { return idUser; }
    public String getUsername() { return username; }
    public String getNom()      { return nom; }
    public String getPrenom()   { return prenom; }
    public String getRole()     { return role; }

    public void setIdUser(int idUser)       { this.idUser   = idUser; }
    public void setUsername(String username){ this.username = username; }
    public void setNom(String nom)          { this.nom      = nom; }
    public void setPrenom(String prenom)    { this.prenom   = prenom; }
    public void setRole(String role)        { this.role     = role; }

    @Override
    public String toString() {
        return idUser + " | " + username + " | " + nom + " " + prenom + " | " + role;
    }
}