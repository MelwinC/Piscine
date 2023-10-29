package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import piscine.Cours;
import piscine.Offre;
import piscine.Ticket;

public class TicketDAO extends DAO<Ticket>{

	private static final String CLE_PRIMAIRE = "code";
	private static final String TABLE = "TICKET";
	private static final String OFFRE_ID = "offre_id";
	private static final String DATE_ACHAT= "date_achat";
	private static final String DATE_EXP= "date_exp";
	private static final String PARTICIPE = "participe";
	private static final String ID_CODE_PARTICIPE = "code";
	private static final String ID_COURS_PARTICIPE = "cours_id";

	private static TicketDAO instance=null;

	public static TicketDAO getInstance(){
		if (instance==null){
			instance = new TicketDAO();
		}
		return instance;
	}

	public TicketDAO() {
		super();
	}

	@Override
	public boolean create(Ticket ticket) {
		Boolean succes= true;
		try {
			String requete = "INSERT INTO "+TABLE+"("+CLE_PRIMAIRE+", "+ OFFRE_ID +", "+ DATE_ACHAT +", "+ DATE_EXP +") VALUES (?, ?, ?, ?)";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete);
			pst.setString(1, ticket.getCode());
			pst.setInt(2, ticket.getOffre().getId());
			pst.setObject(3, ticket.getDate_achat());
			pst.setObject(4, ticket.getDate_exp());
			pst.executeUpdate();
		} catch (SQLException e) {
			succes=false;
			e.printStackTrace();
		}
		return succes;
	}
	
	public Ticket read(String code) {
		Ticket ticket= null;
		try {
			String requete = "SELECT * FROM "+TABLE+" WHERE "+CLE_PRIMAIRE+" = ? ;";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete) ;
			pst.setString(1, code);
			pst.execute();
			ResultSet rs = pst.getResultSet();
			rs.next();
			Offre offre = OffreDAO.getInstance().read(rs.getInt(OFFRE_ID));
			Date date_achat = rs.getDate(DATE_ACHAT);
			Date date_exp = rs.getDate(DATE_EXP);
			ticket=new Ticket(code, offre, date_achat.toLocalDate(), date_exp.toLocalDate());
		} catch (SQLException e) {
			System.out.println("Ce code n'existe pas en bd");
		}
		return ticket;
	}

	@Override
	public Ticket read(int id) {
		throw new IllegalArgumentException();
	}

	@Override
	public boolean update(Ticket ticket) {
		boolean succes=true;
		String code = ticket.getCode();
		int offreId= ticket.getOffre().getId();
		LocalDate date_achat = ticket.getDate_achat();
		LocalDate date_exp = ticket.getDate_exp();
		try {
			String requete = "UPDATE "+TABLE+" SET "+OFFRE_ID+" = ?, "+DATE_ACHAT+" = ?, "+DATE_EXP+" = ? WHERE "+CLE_PRIMAIRE+" = ?";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete) ;
			pst.setObject(1,offreId) ; 
			pst.setObject(2, date_achat);
			pst.setObject(3, date_exp);
			pst.setString(4, code);
			pst.executeUpdate() ;
		} catch (SQLException e) {
			succes = false;
			e.printStackTrace();
		} 
		return succes;	
	}

	@Override
	public boolean delete(Ticket ticket) {
		boolean succes=true;
		String code = ticket.getCode();
		try {
			String requete = "DELETE FROM "+TABLE+" WHERE "+CLE_PRIMAIRE+" = ?";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete) ;
			pst.setString(1, code) ;
			pst.executeUpdate() ;
		} catch (SQLException e) {
			succes = false;
			e.printStackTrace();
		} 
		return succes;	
	}
	
	public boolean ajouterParticipation(Cours cours, Ticket ticket) {
		boolean succes=true;
		try {			
			String requete = "INSERT INTO "+PARTICIPE+" ("+ID_CODE_PARTICIPE+", "+ ID_COURS_PARTICIPE + ") VALUES (?, ?)";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, ticket.getCode());
			pst.setInt(2, cours.getIdCours());
			pst.executeUpdate() ;
		} catch (SQLException e) {
			succes=false;
			e.printStackTrace();
		}
		return succes;
	}
	
	public int getNombreParticipant(int cours) {
		int nombreParticipant = 0;
		try {
			String requete = "SELECT COUNT (*) FROM " + PARTICIPE + " WHERE " + ID_COURS_PARTICIPE + " = ? ;";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete);
			pst.setInt(1, cours);
			pst.execute();
			ResultSet rs =pst.getResultSet();
			if (rs.next()) {
	            nombreParticipant = rs.getInt(1);
	        }
		} catch (SQLException e) {
			System.out.println("code inexistant");
			e.printStackTrace();
		}
		return nombreParticipant;
	}

}
