package project.bank.server.db;

import java.sql.*;
import java.time.LocalDate;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import project.bank.client.model.Compte;
import project.bank.client.model.Transaction;
import project.bank.server.db.DatabaseConnection;

public class DbManager {

	private Connection conn;

	public DbManager() throws SQLException {
		// CRITICAL FIX: Ensure DatabaseConnection is initialized first
		DatabaseConnection.getInstance(); // Initialize the singleton
		this.conn = DatabaseConnection.getConnection();

		if (this.conn == null || this.conn.isClosed()) {
			throw new SQLException("Failed to establish database connection");
		}
		System.out.println("✅ DbManager initialized with valid connection");
	}

	private String hash(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] result = md.digest(input.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (byte b : result)
				sb.append(String.format("%02x", b));
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException("Hashing failed", e);
		}
	}


	public void test_the_db() {
		try {
			System.out.println("Connect to DB");
			System.out.println("=====================================");

			// ========== 1. INSERT CLIENT 1 ==========
			String insertClient = "INSERT INTO UTILISATEUR (username, mot_pass, nom, prenom, role) "
					+ "VALUES (?,?,?,?,?)";

			PreparedStatement psClient1 = conn.prepareStatement(insertClient, Statement.RETURN_GENERATED_KEYS);
			psClient1.setString(1, "client1");
			psClient1.setString(2, hash("client1"));  // FIX: hash the actual password
			psClient1.setString(3, "A");              // one uppercase letter
			psClient1.setString(4, "BB");             // two uppercase letters
			psClient1.setString(5, "CLIENT");
			psClient1.executeUpdate();

			ResultSet rsClient1 = psClient1.getGeneratedKeys();
			int clientId1 = 1;
			if (rsClient1.next()) {
				clientId1 = rsClient1.getInt(1);
			}
			System.out.println("Inserted CLIENT 1 with id = " + clientId1 + " (username: client1, password: client1)");

			// ========== 2. INSERT CLIENT 2 ==========
			PreparedStatement psClient2 = conn.prepareStatement(insertClient, Statement.RETURN_GENERATED_KEYS);
			psClient2.setString(1, "client2");
			psClient2.setString(2, hash("client2"));  // FIX: hash the actual password
			psClient2.setString(3, "M");              // one uppercase letter
			psClient2.setString(4, "SS");             // two uppercase letters
			psClient2.setString(5, "CLIENT");
			psClient2.executeUpdate();

			ResultSet rsClient2 = psClient2.getGeneratedKeys();
			int clientId2 = 1;
			if (rsClient2.next()) {
				clientId2 = rsClient2.getInt(1);
			}
			System.out.println("Inserted CLIENT 2 with id = " + clientId2 + " (username: client2, password: client2)");

			// ========== 3. INSERT ADMIN ==========
			PreparedStatement psAdmin = conn.prepareStatement(insertClient, Statement.RETURN_GENERATED_KEYS);
			psAdmin.setString(1, "admin");
			psAdmin.setString(2, hash("admin"));      // FIX: hash the actual password
			psAdmin.setString(3, "K");                // one uppercase letter
			psAdmin.setString(4, "DD");               // two uppercase letters
			psAdmin.setString(5, "ADMIN");
			psAdmin.executeUpdate();

			ResultSet rsAdmin = psAdmin.getGeneratedKeys();
			int adminId = 1;
			if (rsAdmin.next()) {
				adminId = rsAdmin.getInt(1);
			}
			System.out.println("Inserted ADMIN with id = " + adminId + " (username: admin, password: admin)");

			// ========== 4. INSERT COMPTES ==========
			String insertCompte = "INSERT INTO COMPTE (numero_compte, solde, id_user, date_creation, actif) "
					+ "VALUES (?,?,?,?,?)";

			// Client 1 accounts: 100 and 200
			PreparedStatement psC1 = conn.prepareStatement(insertCompte);
			psC1.setString(1, "ACC100");
			psC1.setDouble(2, 100.00);
			psC1.setInt(3, clientId1);
			psC1.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
			psC1.setBoolean(5, true);
			psC1.executeUpdate();
			System.out.println("Inserted ACCOUNT ACC100 (balance: 100) for client1");

			PreparedStatement psC2 = conn.prepareStatement(insertCompte);
			psC2.setString(1, "ACC200");
			psC2.setDouble(2, 200.00);
			psC2.setInt(3, clientId1);
			psC2.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
			psC2.setBoolean(5, true);
			psC2.executeUpdate();
			System.out.println("Inserted ACCOUNT ACC200 (balance: 200) for client1");

			// Client 2 accounts: 400 and 500
			PreparedStatement psC3 = conn.prepareStatement(insertCompte);
			psC3.setString(1, "ACC400");
			psC3.setDouble(2, 400.00);
			psC3.setInt(3, clientId2);
			psC3.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
			psC3.setBoolean(5, true);
			psC3.executeUpdate();
			System.out.println("Inserted ACCOUNT ACC400 (balance: 400) for client2");

			PreparedStatement psC4 = conn.prepareStatement(insertCompte);
			psC4.setString(1, "ACC500");
			psC4.setDouble(2, 500.00);
			psC4.setInt(3, clientId2);
			psC4.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
			psC4.setBoolean(5, true);
			psC4.executeUpdate();
			System.out.println("Inserted ACCOUNT ACC500 (balance: 500) for client2");

			// ========== 5. CLOSE RESOURCES ==========
			psClient1.close();
			psClient2.close();
			psAdmin.close();
			psC1.close();
			psC2.close();
			psC3.close();
			psC4.close();
			rsClient1.close();
			rsClient2.close();
			rsAdmin.close();

			System.out.println("=====================================");
			System.out.println("✅ All test data inserted successfully!");
			System.out.println("=====================================");
			System.out.println("Summary:");
			System.out.println("  - Client 1: " + clientId1 + " | User: client1 / Pass: client1 | Accounts: ACC100 (100), ACC200 (200)");
			System.out.println("  - Client 2: " + clientId2 + " | User: client2 / Pass: client2 | Accounts: ACC400 (400), ACC500 (500)");
			System.out.println("  - Admin: " + adminId + " | User: admin / Pass: admin");
			System.out.println("=====================================");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}






	public boolean auth(String username, String motPasse) {
		try {
			String query = "SELECT * FROM UTILISATEUR " + "WHERE username = ? AND mot_pass = ?";

			PreparedStatement ps = conn.prepareStatement(query);

			ps.setString(1, username);
			ps.setString(2, hash(motPasse));

			ResultSet rs = ps.executeQuery();

			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}



	// operation
	public double consulterSolde(String numeroCompte) {
	    try {
	        String query = "SELECT solde FROM COMPTE WHERE numero_compte = ? AND actif = true";
	        PreparedStatement ps = conn.prepareStatement(query);
	        ps.setString(1, numeroCompte);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next())
	            return rs.getDouble("solde");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return -1; // account not found or error
	}


	public boolean deposer(String numeroCompte, double montant) {
		try {
			String query = "UPDATE COMPTE SET solde = solde + ? " + "WHERE numero_compte = ? AND actif = true";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setDouble(1, montant);
			ps.setString(2, numeroCompte);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


	public boolean retirer(String numeroCompte, double montant) {
		try {
			String query = "UPDATE COMPTE SET solde = solde - ? "
					+ "WHERE numero_compte = ? AND actif = true AND solde >= ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setDouble(1, montant);
			ps.setString(2, numeroCompte);
			ps.setDouble(3, montant);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


	public boolean virement(String source, String dest, double montant) {
	    try {
	        conn.setAutoCommit(false);
	        boolean acc1 = retirer(source, montant);
	        boolean acc2 = deposer(dest, montant);
	        if (acc1 && acc2) {
	            conn.commit();
	            return true;
	        } else {
	            conn.rollback();
	            return false;
	        }
	    } catch (SQLException e) {
	        try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
	        e.printStackTrace();
	        return false;
	    } finally {
	        try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
	    }
	}

	public List<Compte> listerComptesParUser(int idUser) {
	    try {
	        String query = "SELECT * FROM COMPTE WHERE id_user = ? ORDER BY numero_compte";
	        PreparedStatement ps = conn.prepareStatement(query);
	        ps.setInt(1, idUser);
	        ResultSet rs = ps.executeQuery();
	        List<Compte> comptes = new ArrayList<>();
	        while (rs.next()) {
	            Compte c = new Compte(
	                rs.getString("numero_compte"),
	                rs.getDouble("solde"),
	                rs.getDate("date_creation").toLocalDate(),
	                rs.getBoolean("actif"),
	                rs.getInt("id_user")
	            );
	            comptes.add(c);
	        }
	        return comptes;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return new ArrayList<>(); // return empty list on error
	    }
	}

	public void enregistrerTransaction(String numeroCompte, String typeOp, double montant, String compteDest) {
	    try {
	        String query = "INSERT INTO TRANSACTION_B(numero_compte, type_op, montant, compte_dest) VALUES (?, ?, ?, ?)";
	        PreparedStatement ps = conn.prepareStatement(query);
	        ps.setString(1, numeroCompte);
	        ps.setString(2, typeOp);
	        ps.setDouble(3, montant);
	        if (compteDest == null)
	            ps.setNull(4, java.sql.Types.VARCHAR);
	        else
	            ps.setString(4, compteDest);
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	// his
	public List<Transaction> getHistorique(String numeroCompte) {
	    try {
	        String query = "SELECT * FROM TRANSACTION_B WHERE numero_compte = ? ORDER BY date_op DESC";
	        PreparedStatement ps = conn.prepareStatement(query);
	        ps.setString(1, numeroCompte);
	        ResultSet rs = ps.executeQuery();
	        List<Transaction> list = new ArrayList<>();
	        while (rs.next()) {
	            Transaction t = new Transaction(
	                rs.getInt("id_transaction"),
	                rs.getString("numero_compte"),
	                rs.getString("type_op"),
	                rs.getDouble("montant"),
	                rs.getTimestamp("date_op").toLocalDateTime(),
	                rs.getString("compte_dest")
	            );
	            list.add(t);
	        }
	        return list;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return new ArrayList<>(); // return empty list on error
	    }
	}


	// admin
	public boolean creerCompte(String titulaire, double soldeInitial) {
	    try {
	        String getUser = "SELECT id_user FROM UTILISATEUR WHERE username = ?";
	        PreparedStatement ps1 = conn.prepareStatement(getUser);
	        ps1.setString(1, titulaire);
	        ResultSet rs = ps1.executeQuery();
	        if (!rs.next())
	            return false;
	        int idUser = rs.getInt("id_user");
	        String numeroCompte = "CPT-" + System.currentTimeMillis();
	        String query = "INSERT INTO COMPTE(numero_compte, solde, id_user) VALUES (?, ?, ?)";
	        PreparedStatement ps2 = conn.prepareStatement(query);
	        ps2.setString(1, numeroCompte);
	        ps2.setDouble(2, soldeInitial);
	        ps2.setInt(3, idUser);
	        return ps2.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	public boolean supprimerCompte(String numeroCompte) {
	    try {
	        String query = "UPDATE COMPTE SET actif = false WHERE numero_compte = ?";
	        PreparedStatement ps = conn.prepareStatement(query);
	        ps.setString(1, numeroCompte);
	        return ps.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	public List<Compte> listerComptes(String username) {
		System.out.println("=== DbManager.listerComptes START ===");
		System.out.println("Username: " + username);
		System.out.println("Connection status: " + (conn != null ? "valid" : "NULL"));

		if (conn == null) {
			System.err.println("❌ Connection is null!");
			return new ArrayList<>();
		}

		try {
			// Check if connection is closed
			if (conn.isClosed()) {
				System.err.println("❌ Connection is closed! Reconnecting...");
				this.conn = DatabaseConnection.getConnection();
			}

			String query = "SELECT c.numero_compte, c.solde, c.date_creation, c.actif, c.id_user " +
					"FROM COMPTE c " +
					"JOIN UTILISATEUR u ON c.id_user = u.id_user " +
					"WHERE u.username = ? AND c.actif = 1 " +
					"ORDER BY c.numero_compte";

			System.out.println("SQL Query: " + query);
			System.out.println("Parameter: " + username);

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			List<Compte> comptes = new ArrayList<>();

			while (rs.next()) {
				String numCompte = rs.getString("numero_compte");
				double solde = rs.getDouble("solde");
				Date dateCreation = rs.getDate("date_creation");
				boolean actif = rs.getBoolean("actif");
				int idUser = rs.getInt("id_user");

				System.out.println("Found account: " + numCompte + " Balance: " + solde);

				Compte c = new Compte();
				c.setNumeroCompte(numCompte);
				c.setSolde(solde);
				if (dateCreation != null) {
					c.setDateCreation(dateCreation.toLocalDate());
				}
				c.setActif(actif);
				c.setIdUser(idUser);

				comptes.add(c);
			}

			rs.close();
			ps.close();

			System.out.println("=== DbManager.listerComptes END - Found " + comptes.size() + " accounts ===");
			return comptes;

		} catch (SQLException e) {
			System.err.println("❌ SQL Error in listerComptes: " + e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	// Run this first to insert test data
	public static void main(String[] args) {
		try {
			DbManager db = new DbManager();
			db.test_the_db();

			// Test listing accounts immediately
			System.out.println("\n=== Testing listerComptes ===");
			List<Compte> comptes = db.listerComptes("client1");
			System.out.println("Accounts for client1: " + comptes.size());
			for (Compte c : comptes) {
				System.out.println("  " + c.getNumeroCompte() + ": " + c.getSolde());
			}
		} catch (SQLException e) {
			System.err.println("Failed to initialize database: " + e.getMessage());
			e.printStackTrace();
		}
	}

}