package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import piscine.Adresse;
import piscine.Employe;
import piscine.Piscine;

public class EmployeDAO extends DAO<Employe> {

	// ----------------------------------------Attributs------------------------------

	private static final String CLE_PRIMAIRE = "id";
	private static final String TABLE = "employe";
	private static final String NOM = "nom";
	private static final String PRENOM = "prenom";
	private static final String ADR = "adresse_id";
	private static final String TABLE_TRAVAIL = "travail";
	private static final String ID_EMP_TRAVAIL = "employe_id";
	private static final String ID_PISC_TRAVAIL = "piscine_id";

	// ----------------------------------------CRUD--------------------------------

	private static EmployeDAO instance = null;

	public static EmployeDAO getInstance() {
		if (instance == null) {
			instance = new EmployeDAO();
		}
		return instance;
	}

	@Override
	public boolean create(Employe emp) {
		boolean succes = true;
		try {
			// vérifier que la clé étrangère Adresse existe dans la table Adresse, sinon on la créer :
			Adresse adresse = emp.getAdr();
			String requete = "INSERT INTO " + TABLE + " (" + NOM + ", " + PRENOM + "," + ADR + ") VALUES (?, ?, ?)";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, emp.getNom());
			pst.setString(2, emp.getPrenom());
			pst.setInt(3, adresse.getId());
			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				emp.setId(rs.getInt(1));
			}

			// vérifier pour chaque piscine de la liste de piscines dans l'objet employé
			// qu'il y a une ligne dans la table
			// piscine de la bd. Si la piscine n'existe pas la créer avec
			// PiscineDAO.getInstance().create()

			for (Piscine piscine : emp.getLesPiscines()) {
				if (piscine.getId() == -1) {
					PiscineDAO.getInstance().create(piscine);
				}
			}

			// Boucle pour sur la liste de piscines dans l'objet employé et insertion dans
			// TRAVAILPOUR des id uniquement

			int idEmp = emp.getId();
			String requete2 = "INSERT INTO " + TABLE_TRAVAIL + " (" + ID_EMP_TRAVAIL + ", " + ID_PISC_TRAVAIL
					+ ") VALUES (?, ?)";
			for (Piscine piscine : emp.getLesPiscines()) {
				PreparedStatement pst2 = Connexion.getInstance().prepareStatement(requete2);
				pst2.setInt(1, idEmp);
				pst2.setInt(2, piscine.getId());
				pst2.executeUpdate();
			}
		} catch (SQLException e) {
			succes = false;
			e.printStackTrace();
			// gerer les erreurs si clé etrangeres inexistantes
			if (emp.getAdr().getId() == -1) {
				System.out.println("Adresse inexistante");
			}
		}
		return succes;

	}

	public Employe read(int id) {
		Employe emp = null;
		try {
			String requete = "SELECT * FROM " + TABLE + " WHERE " + CLE_PRIMAIRE + "=" + id + ";";
			ResultSet rs = Connexion.executeQuery(requete);
			rs.next();
			String nom = rs.getString(NOM);
			String prenom = rs.getString(PRENOM);
			Adresse adr = AdresseDAO.getInstance().read(rs.getInt(ADR));
			List<Piscine> lesPiscines = new ArrayList<Piscine>();
			requete = "SELECT * FROM " + TABLE_TRAVAIL + " WHERE " + ID_EMP_TRAVAIL + "=" + id + ";";
			rs = Connexion.executeQuery(requete);
			while (rs.next()) {
				int idPis = rs.getInt(ID_PISC_TRAVAIL);
				Piscine piscine = PiscineDAO.getInstance().read(idPis);
				lesPiscines.add(piscine);
			}
			emp = new Employe(id, nom, prenom, adr, lesPiscines);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return emp;
	}

	public List<Employe> readAll() {
		Employe emp = null;
		List<Employe> arr = new ArrayList<>();
		try {
			String requete = "SELECT * FROM " + TABLE + ";";
			ResultSet rs = Connexion.executeQuery(requete);
			while (rs.next()) {
				int id = rs.getInt("id");
				String nom = rs.getString(NOM);
				String prenom = rs.getString(PRENOM);
				Adresse adr = AdresseDAO.getInstance().read(rs.getInt(ADR));
				List<Piscine> lesPiscines = new ArrayList<Piscine>();
				requete = "SELECT * FROM " + TABLE_TRAVAIL + " WHERE " + ID_EMP_TRAVAIL + "=" + id + ";";
				ResultSet rs2 = Connexion.executeQuery(requete);
				while (rs2.next()) {
					int idPis = rs2.getInt(ID_PISC_TRAVAIL);
					Piscine piscine = PiscineDAO.getInstance().read(idPis);
					lesPiscines.add(piscine);
				}
				emp = new Employe(id, nom, prenom, adr, lesPiscines);
				arr.add(emp);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}

	@Override
	public boolean update(Employe emp) {
		boolean success = true;
		Adresse adr = emp.getAdr();
		String nom = emp.getNom();
		String prenom = emp.getPrenom();
		int id = emp.getId();
		try {
			if (AdresseDAO.getInstance().read(emp.getAdr().getId()) == null) {
				AdresseDAO.getInstance().create(emp.getAdr());
			}
			String requete = "UPDATE " + TABLE + " SET " + NOM + " = ?, " + PRENOM + " = ?, " + ADR + " = ? WHERE "
					+ CLE_PRIMAIRE + " = ?";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete);
			pst.setString(1, nom);
			pst.setString(2, prenom);
			pst.setInt(3, adr.getId());
			pst.setInt(4, id);
			pst.executeUpdate();

			for (Piscine piscine : emp.getLesPiscines()) {
				if (piscine.getId() == -1) {
					PiscineDAO.getInstance().create(piscine);
				}
			}
			
			// delete all de travail et reinserer les bonnes lignes
			requete = "DELETE FROM " + TABLE_TRAVAIL + " WHERE employe_id = ?;";
			PreparedStatement pst2 = Connexion.getInstance().prepareStatement(requete);
			pst2.setInt(1, id);
			pst2.executeUpdate();

			String requete2 = "INSERT INTO " + TABLE_TRAVAIL + " (" + ID_EMP_TRAVAIL + ", " + ID_PISC_TRAVAIL
					+ ") VALUES (?, ?)";
			for (Piscine piscine : emp.getLesPiscines()) {
				PreparedStatement pst3 = Connexion.getInstance().prepareStatement(requete2);
				pst3.setInt(1, id);
				pst3.setInt(2, piscine.getId());
				pst3.executeUpdate();
			}

		} catch (SQLException e) {
			success = false;
			e.printStackTrace();
		}
		return success;
	}

	@Override
	public boolean delete(Employe emp) {
		boolean succes = true;
		int id = emp.getId();
		try {
			// Delete dans la table travail
			String requete = "DELETE FROM " + TABLE_TRAVAIL + " WHERE " + ID_EMP_TRAVAIL + " = ?";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete);
			pst.setInt(1, id);
			pst.executeUpdate();

			//Delete dans la table admin
			requete = "DELETE FROM admin WHERE id = ?";
			pst = Connexion.getInstance().prepareStatement(requete);
			pst.setInt(1, id);
			pst.executeUpdate();
			
			// Delete dans la table employe
			requete = "DELETE FROM " + TABLE + " WHERE " + CLE_PRIMAIRE + " = ?";
			pst = Connexion.getInstance().prepareStatement(requete);
			pst.setInt(1, id);
			pst.executeUpdate();
			
			// Delete dans la table cours
            requete = "DELETE FROM cours WHERE id = ?";
            pst = Connexion.getInstance().prepareStatement(requete) ;
            pst.setInt(1, id) ;
            pst.executeUpdate() ;
            
		} catch (SQLException e) {
			succes = false;
			e.printStackTrace();
		}
		return succes;
	}

}