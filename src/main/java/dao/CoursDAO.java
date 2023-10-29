package dao;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import piscine.Cours;
import piscine.Employe;
import piscine.Piscine;


public class CoursDAO {

	private static final String CLE_PRIMAIRE = "id";
	private static final String TABLE = "cours";
	private static final String HORAIRE_DEBUT = "horaire_debut";
	private static final String HORAIRE_FIN = "horaire_fin";
	private static final String EMP_ID = "employe_id";
	private static final String PISC_ID = "piscine_id";
	private static final String NBR_PLACES = "nbr_places";
	private static final String PARTICIPE = "participe";
	private static final String ID_COURS_PARTICIPE = "cours_id";

	private static CoursDAO instance=null;

	public static CoursDAO getInstance(){
		if (instance==null){
			instance = new CoursDAO();
		}
		return instance;
	}

	private CoursDAO() {
		super();
	}

	public boolean create(Cours cours) {
		boolean succes=true;
		try {
			String requete = "INSERT INTO "+TABLE+" ("+HORAIRE_DEBUT+", "+HORAIRE_FIN+", "+EMP_ID+", "+PISC_ID+", " + NBR_PLACES+ ") VALUES (?, ?, ?, ?, ?)";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
			pst.setObject(1, cours.getHoraireDebut());
			pst.setObject(2, cours.getHoraireFin());
			pst.setInt(3, cours.getEmploye().getId());
			pst.setInt(4, cours.getPiscine().getId());
			pst.setInt(5, cours.getNombrePlaces());
			pst.executeUpdate() ;
			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				cours.setIdCours(rs.getInt(1));
			}
		} catch (SQLException e) {
			succes=false;
			e.printStackTrace();
		}
		return succes;
	}

	public Cours read(int id) {
		String requete = "SELECT * FROM "+TABLE+" WHERE "+CLE_PRIMAIRE+"="+id+";";
		ResultSet rs = Connexion.executeQuery(requete);
		Cours cours = null;
		try {
			rs.next();
			int num = rs.getInt(CLE_PRIMAIRE);
			LocalDateTime horairedebut = rs.getTimestamp(HORAIRE_DEBUT).toLocalDateTime();
			LocalDateTime horairefin = rs.getTimestamp(HORAIRE_FIN).toLocalDateTime();
			Employe employe = EmployeDAO.getInstance().read(rs.getInt(EMP_ID));
			Piscine pisc = PiscineDAO.getInstance().read(rs.getInt(PISC_ID));
			int nbrPlaces = rs.getInt(NBR_PLACES);
			cours = new Cours(num, horairedebut, horairefin, employe, pisc, nbrPlaces);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cours;
	}

	public List<Cours> readAll() {
		List<Cours> listeCours = new ArrayList<Cours>();
		String requete = "SELECT * FROM "+TABLE+" WHERE horaire_debut > GETDATE();";
		ResultSet rs = Connexion.executeQuery(requete);
		Cours cours = null;
		try {
			while(rs.next()) {	
				int num = rs.getInt(CLE_PRIMAIRE);
				LocalDateTime horairedebut = rs.getTimestamp(HORAIRE_DEBUT).toLocalDateTime();
				LocalDateTime horairefin = rs.getTimestamp(HORAIRE_FIN).toLocalDateTime();
				Employe employe = EmployeDAO.getInstance().read(rs.getInt(EMP_ID));
				Piscine pisc = PiscineDAO.getInstance().read(rs.getInt(PISC_ID));
				int nbrPlaces = rs.getInt(NBR_PLACES);
				cours = new Cours(num, horairedebut, horairefin, employe, pisc, nbrPlaces);
				listeCours.add(cours);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listeCours;
	}

	public boolean update(Cours obj) {
		boolean succes=true;
		int id = obj.getIdCours();
		LocalDateTime horaireDebut = obj.getHoraireDebut();
		LocalDateTime horaireFin = obj.getHoraireFin();
		int idEmploye= obj.getEmploye().getId();
		int idPiscine = obj.getPiscine().getId();
		int nbrPlaces = obj.getNombrePlaces();

		try {
			if (EmployeDAO.getInstance().read(obj.getEmploye().getId())==null) {
				EmployeDAO.getInstance().create(obj.getEmploye());
			}
			String requete = "UPDATE "+TABLE+" SET "+HORAIRE_DEBUT+" = ?, "+HORAIRE_FIN+" = ?, "+EMP_ID+" = ?, "+PISC_ID+" = ?, "+NBR_PLACES+" = ? WHERE "+CLE_PRIMAIRE+" = ?";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete) ; 
			pst.setObject(1,horaireDebut) ; 
			pst.setObject(2, horaireFin) ;
			pst.setObject(3, idEmploye);
			pst.setObject(4, idPiscine);
			pst.setInt(5, nbrPlaces);
			pst.setInt(6, id);
			pst.executeUpdate();
		} catch (SQLException e) {
			succes = false;
			e.printStackTrace();
		} 
		return succes;	
	}

	public boolean delete(Cours cours) {
		boolean succes=true;
		try {
			int id = cours.getIdCours();

			// supprime les cours de la table participe
			String requete = "DELETE FROM " + PARTICIPE +  " WHERE " + ID_COURS_PARTICIPE +" = ?;";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete);
			pst.setInt(1, id);
			pst.executeUpdate();

			requete = "DELETE FROM "+TABLE+" WHERE "+CLE_PRIMAIRE+" = ?";
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