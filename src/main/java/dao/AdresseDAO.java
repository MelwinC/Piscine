package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import piscine.Adresse;

public class AdresseDAO extends DAO<Adresse>{

	private static final String CLE_PRIMAIRE = "id";
	private static final String TABLE = "ADRESSE";
	private static final String VILLE = "ville";
	private static final String CODE_POSTAL = "code_postal";
	private static final String NUM_RUE = "num_rue";
	private static final String RUE = "rue";

	
	private static AdresseDAO instance=null;
	
	public static AdresseDAO getInstance(){
		if (instance==null){
			instance = new AdresseDAO();
		}
		return instance;
	}

	private AdresseDAO() {
		super();
	}
	
	@Override
	public boolean create(Adresse adresse) {
		Boolean succes= true;
		try {
			String requete = "INSERT INTO "+TABLE+"("+VILLE+", "+ CODE_POSTAL +", "+ NUM_RUE +", "+ RUE +") VALUES (?, ?, ?, ?)";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, adresse.getVille());
			pst.setInt(2, adresse.getCode_postal());
			pst.setInt(3, adresse.getNum_rue());
			pst.setString(4, adresse.getRue());
			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				adresse.setId(rs.getInt(1));
			}
		} catch (SQLException e) {
			succes=false;
			e.printStackTrace();
		}
		return succes;
	}
	
	@Override
	public Adresse read(int id) {
		Adresse adresse = null;
		try {
			String requete = "SELECT * FROM "+TABLE+" WHERE "+CLE_PRIMAIRE+"="+id+";";
			ResultSet rs = Connexion.executeQuery(requete);
			rs.next();
			String ville=rs.getString(VILLE);
			int cp=rs.getInt(CODE_POSTAL);
			int num_rue=rs.getInt(NUM_RUE);
			String rue=rs.getString(RUE);
			adresse=new Adresse(id, ville, cp, num_rue, rue);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return adresse;
	}

	@Override
	public boolean update(Adresse adresse) {
		boolean succes=true;
		String ville= adresse.getVille();
		int cp = adresse.getCode_postal();
		int num_rue = adresse.getNum_rue();
		String rue = adresse.getRue();
		int id = adresse.getId();
		try {
			String requete = "UPDATE "+TABLE+" SET "+VILLE+" = ?, "+CODE_POSTAL+" = ?, "+NUM_RUE+" = ?, "+RUE+" = ? WHERE "+CLE_PRIMAIRE+" = ?";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete) ;
			pst.setString(1,ville) ; 
			pst.setInt(2, cp);
			pst.setInt(3, num_rue);
			pst.setString(4, rue);
			pst.setInt(5, id);
			pst.executeUpdate() ;
		} catch (SQLException e) {
			succes = false;
			e.printStackTrace();
		} 
		return succes;	
	}

	@Override
	public boolean delete(Adresse adresse) {
		boolean succes=true;
		int id = adresse.getId();
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
