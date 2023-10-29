package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import piscine.Adresse;
import piscine.Piscine;

public class PiscineDAO extends DAO<Piscine>{

	private static final String CLE_PRIMAIRE = "id";
	private static final String TABLE = "PISCINE";
	private static final String NOM_PISCINE = "nom";
	private static final String ADRESSE_ID= "adresse_id";

	
	private static PiscineDAO instance=null;
	
	public static PiscineDAO getInstance(){
		if (instance==null){
			instance = new PiscineDAO();
		}
		return instance;
	}

	private PiscineDAO() {
		super();
	}
	
	@Override
	public boolean create(Piscine piscine) {
		Boolean succes= true;
		try {
			Adresse adresse = piscine.getAdresse();
			String requete = "INSERT INTO "+TABLE+"("+NOM_PISCINE+", "+ ADRESSE_ID +") VALUES (?, ?)";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, piscine.getNom());
			pst.setInt(2, adresse.getId());
			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				piscine.setId(rs.getInt(1));
			}
		} catch (SQLException e) {
			succes=false;
			e.printStackTrace();
		}
		return succes;
	}

	@Override
	public Piscine read(int id) {
		Piscine piscine = null;
		try {
			String requete = "SELECT * FROM "+TABLE+" WHERE "+CLE_PRIMAIRE+"="+id+";";
			ResultSet rs = Connexion.executeQuery(requete);
			rs.next();
			String nom=rs.getString(NOM_PISCINE);
			Adresse adresse = AdresseDAO.getInstance().read(rs.getInt(ADRESSE_ID));
			piscine=new Piscine(id, nom, adresse);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return piscine;
	}

	@Override
	public boolean update(Piscine piscine) {
		boolean succes=true;
		String nom= piscine.getNom();
		int idAdresse = piscine.getAdresse().getId();
		int id = piscine.getId();
		try {
			String requete = "UPDATE "+TABLE+" SET "+NOM_PISCINE+" = ?, "+ADRESSE_ID+" = ? WHERE "+CLE_PRIMAIRE+" = ?";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete) ;
			pst.setString(1,nom) ; 
			pst.setInt(2, idAdresse);
			pst.setInt(3, id);
			pst.executeUpdate() ;
		} catch (SQLException e) {
			succes = false;
			e.printStackTrace();
		} 
		return succes;	
	}
	
	@Override
	public boolean delete(Piscine piscine) {
		boolean succes=true;
		int id = piscine.getId();
		try {
			String requete = "DELETE FROM "+TABLE+" WHERE "+CLE_PRIMAIRE+" = ?";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete) ;
			pst.setInt(1, id) ;
			pst.executeUpdate() ;
		} catch (SQLException e) {
			succes = false;
			e.printStackTrace();
		} 
		return succes;	
	}

}
