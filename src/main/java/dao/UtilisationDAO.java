package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import piscine.Ticket;
import piscine.Utilisation;

public class UtilisationDAO  extends DAO<Utilisation> {
	private static final String CLE_PRIMAIRE = "date_utilisation";
	private static final String TABLE = "utilisation";
	private static final String CODE = "code";

	private static UtilisationDAO instance=null;

	public static UtilisationDAO getInstance(){
		if (instance==null){
			instance = new UtilisationDAO();
		}
		return instance;
	}

	private UtilisationDAO() {
		super();
	}
	
	@Override
	public boolean create(Utilisation utilisation) {
		boolean succes = true;
		try {
			String requete = "INSERT INTO " + TABLE + " (" + CLE_PRIMAIRE + ", " + CODE +  ") VALUES (?, ?)";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete);
			pst.setObject(1, utilisation.getDate_utilisation());
			pst.setString(2, utilisation.getTicket().getCode());
			pst.execute();

		} catch (SQLException e) {
			succes = false;
			e.printStackTrace();
		}
		return succes;
	}
	
	public Utilisation read(LocalDateTime date_utilisation, String code) {
		Utilisation utilisation = null;
		try {
			String requete = "SELECT * FROM " + TABLE + " WHERE " + CLE_PRIMAIRE + " = ? AND " + CODE + " = ? ;";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete);
			pst.setObject(1, date_utilisation);
			pst.setObject(2, code);
			pst.execute();
			ResultSet rs =pst.getResultSet();
			rs.next();
			date_utilisation = rs.getTimestamp(CLE_PRIMAIRE).toLocalDateTime();
			Ticket ticket = TicketDAO.getInstance().read(rs.getString(CODE));
			utilisation = new Utilisation(date_utilisation, ticket);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return utilisation;
	}

	@Override
	public Utilisation read(int id) {
		throw new IllegalArgumentException();
	}
	
	@Override
	public boolean update(Utilisation obj) {
		throw new IllegalArgumentException();
	}

	public boolean deleteCode(Ticket ticket) {
		boolean succes = true;
		try {
			String code = ticket.getCode();
			String requete = "DELETE FROM " + TABLE + " WHERE " + CODE + " = ?";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete);
			pst.setString(1, code);
			pst.executeUpdate();
		} catch (SQLException e) {
			succes = false;
			e.printStackTrace();
		}
		return succes;
	}
	
	@Override
	public boolean delete(Utilisation obj) {
		boolean succes = true;
		try {
			LocalDateTime date_utilisation = obj.getDate_utilisation();
			String code = obj.getTicket().getCode();
			String requete = "DELETE FROM " + TABLE + " WHERE " + CLE_PRIMAIRE + " = ? AND " + CODE + " = ?";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete);
			pst.setObject(1, date_utilisation);
			pst.setString(2, code);
			pst.executeUpdate();
		} catch (SQLException e) {
			succes = false;
			e.printStackTrace();
		}
		return succes;
	}

	public int getNombreUtilisation(String code) {
		int nombreUtilisation = 0;
		try {
			String requete = "SELECT COUNT (*) FROM " + TABLE + " WHERE " + CODE + " = ? ;";
			PreparedStatement pst = Connexion.getInstance().prepareStatement(requete);
			pst.setString(1, code);
			pst.execute();
			ResultSet rs =pst.getResultSet();
			if (rs.next()) {
	            nombreUtilisation = rs.getInt(1);
	        }
		} catch (SQLException e) {
			System.out.println("code inexistant");
			e.printStackTrace();
		}
		return nombreUtilisation;
	}

	
}
