package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import piscine.Admin;
import piscine.Employe;

public class AdminDAO extends DAO<Admin>{

	private static final String CLE_PRIMAIRE = "id";
	private static final String TABLE = "admin";
	private static final String IDENTIFIANT = "identifiant";
	private static final String MDP = "mot_de_passe";

	private static AdminDAO instance=null;

	public static AdminDAO getInstance(){
		if (instance==null){
			instance = new AdminDAO();
		}
		return instance;
	}

	private AdminDAO() {
		super();
	}

	@Override
	public boolean create (Admin admin) {

		boolean succes=true;
		try {
			if (EmployeDAO.getInstance().read(admin.getEmploye().getId())==null) {
				EmployeDAO.getInstance().create(admin.getEmploye());
			}
			Employe employe = admin.getEmploye();
			String requete = "INSERT INTO "+TABLE+" ("+CLE_PRIMAIRE+","+IDENTIFIANT+", "+MDP+") VALUES (?, ?, ?)";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, employe.getId());
			pst.setString(2,admin.getIdentifiant());
			pst.setString(3, admin.getMdp());
			pst.executeUpdate() ;

		} catch (SQLException e) {
			succes=false;
			e.printStackTrace();
		}
		return succes;
	}

	@Override
	public Admin read(int id) {
		Admin admin = null;
		try {
			String requete = "SELECT * FROM "+TABLE+" WHERE "+CLE_PRIMAIRE+"="+id+";";
			ResultSet rs = Connexion.executeQuery(requete);

			rs.next();
			Employe emp=EmployeDAO.getInstance().read(rs.getInt(CLE_PRIMAIRE));
			String identifiant=rs.getString(IDENTIFIANT);
			String mdp = rs.getString(MDP);
			admin = new Admin(emp, identifiant, mdp);
			donnees.put(id, admin);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return admin;
	}

	public Admin readFromLogin(String login) {
		Admin admin = null;
		try {
			String requete = "SELECT * FROM "+TABLE+" WHERE "+IDENTIFIANT+"=?;";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete);
			pst.setString(1,login);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				int id = rs.getInt(CLE_PRIMAIRE);
				Employe emp=EmployeDAO.getInstance().read(id);
				String identifiant=rs.getString(IDENTIFIANT);
				String mdp = rs.getString(MDP);
				admin = new Admin(emp, identifiant, mdp);
				donnees.put(id, admin);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return admin;
	}

	@Override
	public boolean update(Admin admin) {
		boolean succes=true;
		Employe employe = admin.getEmploye();
		String identifiant = admin.getIdentifiant();
		String mdp = admin.getMdp();
		try {
			String requete = "UPDATE "+TABLE+" SET "+IDENTIFIANT+" = ?, "+MDP+" = ? WHERE "+CLE_PRIMAIRE+" = ?";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete) ;
			pst.setString(1, identifiant);
			pst.setString(2, mdp );
			pst.setInt(3, employe.getId());
			pst.executeUpdate() ;
		} catch (SQLException e) {
			succes = false;
			e.printStackTrace();
		} 
		return succes;

	}

	@Override
	public boolean delete(Admin admin) {
		boolean succes=true;
		int id = admin.getEmploye().getId();
		try {
			String requete = "DELETE FROM "+TABLE+" WHERE "+CLE_PRIMAIRE+" = ?";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete) ;
			pst.setInt(1, id) ;
			pst.executeUpdate() ;
			donnees.remove(id);
		} catch (SQLException e) {
			succes = false;
			e.printStackTrace();
		} 
		return succes;	
	}

}