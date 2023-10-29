package piscine;

import java.time.LocalDateTime;
//----------------------------------------Attributs------------------------------
import java.time.format.DateTimeFormatter;

import dao.TicketDAO;

public class Cours {
	private int idCours;
	private LocalDateTime horaireDebut;
	private LocalDateTime horaireFin;
	private Piscine piscine;
	private Employe employe;
	private int nombrePlaces;

	public Cours(int idCours, LocalDateTime horaireDebut, LocalDateTime horaireFin, Employe employe, Piscine piscine, int nbrPlaces) {
		super();
		this.idCours=idCours;
		this.horaireDebut = horaireDebut;
		this.horaireFin = horaireFin;
		this.employe = employe;
		this.piscine = piscine;
		this.nombrePlaces = nbrPlaces;
	
	}

	public Cours(LocalDateTime horaireDebut, LocalDateTime horaireFin, Employe employe, Piscine piscine, int nbrPlaces) {
		super();

		this.horaireFin = horaireFin;
		this.horaireDebut = horaireDebut;
		this.employe = employe;
		this.piscine = piscine;
		this.nombrePlaces = nbrPlaces;
	}

	public int getIdCours() {
		return idCours;
	}

	public void setIdCours(int idCours) {
		this.idCours = idCours;
	}

	public Piscine getPiscine() {
		return piscine;
	}

	public void setPiscine(Piscine piscineID) {
		this.piscine = piscineID;
	}

	public Employe getEmploye() {
		return employe;
	}

	public void setEmploye(Employe employe) {
		this.employe = employe;
	}

	public int getNombrePlaces() {
		return nombrePlaces;
	}

	public void setNombrePlaces(int nombrePlaces) {
		this.nombrePlaces = nombrePlaces;
	}

	public LocalDateTime getHoraireDebut() {
		return horaireDebut;
	}


	public void setHoraireDebut(LocalDateTime horaireDebut) {
		this.horaireDebut = horaireDebut;
	}


	public LocalDateTime getHoraireFin() {
		return horaireFin;
	}


	public void setHoraireFin(LocalDateTime horaireFin) {
		this.horaireFin = horaireFin;
	}
	
	public int getPlacesRestantes() {
		int idCours = this.getIdCours();
		int nombreParticipant = TicketDAO.getInstance().getNombreParticipant(idCours);
		int nbPlacesInitiales = this.getNombrePlaces();
		int nbPlacesRestantes = nbPlacesInitiales - nombreParticipant;
		return nbPlacesRestantes;
	}

	public String toStringDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy");
		return horaireDebut.format(formatter);
	}
	
	public String toStringHoraireDebut() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return horaireDebut.format(formatter);
	}
	
	public String toStringHoraireFin() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return horaireFin.format(formatter);
	}

	@Override
	public String toString() {
		return "Cours [idCours=" + idCours + ", date : " + toStringDate() +", horaireDebut=" + toStringHoraireDebut()
				+ ", horaireFin=" + toStringHoraireFin() + ", nombrePlacesInitiales=" + nombrePlaces
				+ ", placesRestantes=" + this.getPlacesRestantes() + ", employe=" + employe + "]";
	}
	
}