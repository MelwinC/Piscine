package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import piscine.Offre;

public class OffreDAO extends DAO<Offre>{

	private static final String CLE_PRIMAIRE = "id";
	private static final String TABLE = "offre";
	private static final String NOM_OFFRE = "nom";
	private static final String VALIDITE = "validite";
	private static final String TARIF = "tarif";
	private static final String NBR_UTILISATION = "nbr_utilisation";

	private static OffreDAO instance=null;

	public static OffreDAO getInstance(){
		if (instance==null) {
			instance = new OffreDAO();
		}
		return instance;
	}

	public OffreDAO() {
		super();
	}

	@Override
	public boolean create(Offre offre) {

		boolean succes=true;
		try {
			String requete = "INSERT INTO "+TABLE+" ("+NOM_OFFRE+", "+VALIDITE+", "+TARIF+", "+NBR_UTILISATION+") VALUES (?, ?, ?, ?)";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, offre.getNom());
			pst.setInt(2, offre.getValidite());
			pst.setFloat(3, offre.getTarif());
			pst.setInt(4, offre.getNbUtilisations());
			pst.executeUpdate() ;
			//Récupère la clé auto-incrémentée
			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				offre.setId(rs.getInt(1));
			}
		} catch (SQLException e) {
			succes=false;
			e.printStackTrace();
		}
		return succes;
	}

	@Override
	public Offre read(int id) {

		Offre offre = null;
		try {
			String requete = "SELECT * FROM "+TABLE+" WHERE "+CLE_PRIMAIRE+"="+id+";";
			ResultSet rs = Connexion.executeQuery(requete);
			rs.next();				
			String nom_offre =rs.getString("nom");
			int validite = rs.getInt("validite");
			float tarif = rs.getFloat("tarif");
			int nbr_utilisation = rs.getInt("nbr_utilisation");
			offre=new Offre(id, nom_offre, validite, tarif, nbr_utilisation);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return offre;
	}

	public List<Offre> readAll(){
		Offre offre = null;
		List<Offre> arr = new ArrayList<>();
		try {
			String requete = "SELECT * FROM "+TABLE+";";
			ResultSet rs = Connexion.executeQuery(requete);
			while(rs.next()) {				
				int id = rs.getInt("id");
				String nom_offre =rs.getString("nom");
				int validite = rs.getInt("validite");
				float tarif = rs.getFloat("tarif");
				int nbr_utilisation = rs.getInt("nbr_utilisation");
				offre=new Offre(id, nom_offre, validite, tarif, nbr_utilisation);
				arr.add(offre);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}  
	
	@Override
	public boolean update(Offre offre) {
		
		boolean succes=true;
		int id= offre.getId();
		String nomOffre= offre.getNom();
		int validite = offre.getValidite();
		float tarif = offre.getTarif();
		int nbrUtil = offre.getNbUtilisations();
		try {
			String requete = "UPDATE "+TABLE+" SET "+NOM_OFFRE+" = ?, "+VALIDITE+" = ?, "+TARIF+" = ?, "+NBR_UTILISATION+" = ?  WHERE "+CLE_PRIMAIRE+" = ?";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete) ;
			pst.setString(1,nomOffre); 
			pst.setInt(2, validite);
			pst.setFloat(3, tarif);
			pst.setInt(4, nbrUtil);
			pst.setInt(5,id); 
			pst.executeUpdate() ;
		} catch (SQLException e) {
			succes = false;
			e.printStackTrace();
		} 
		return succes;	
	}

	@Override
	public boolean delete(Offre obj) {
		boolean succes=true;
		int id = obj.getId();
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
