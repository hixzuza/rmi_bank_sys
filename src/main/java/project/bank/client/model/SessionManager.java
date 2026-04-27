package project.bank.client.model;

public class SessionManager {

    private static SessionManager instance;
    private Utilisateur currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null)
            instance = new SessionManager();
        return instance;
    }

    public Utilisateur getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Utilisateur user) {
        this.currentUser = user;
    }

    public String getUsername() {
        return currentUser != null ? currentUser.getUsername() : null;
    }

    public void clear() {
        this.currentUser = null;
    }
}