package piscine;

import java.time.LocalDate;

import dao.UtilisationDAO;

public class Ticket {
	private String code;
	private Offre offre;
	private LocalDate date_achat;
	private LocalDate date_exp;

	public Ticket(String code, Offre offre, LocalDate date_achat, LocalDate date_exp){
		this.code = code;
		this.offre = offre;
		this.date_achat = date_achat;
		this.date_exp = date_exp;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Offre getOffre() {
		return offre;
	}

	public void setOffre(Offre offre) {
		this.offre = offre;
	}

	public LocalDate getDate_achat() {
		return date_achat;
	}

	public void setDate_achat(LocalDate date_achat) {
		this.date_achat = date_achat;
	}

	public LocalDate getDate_exp() {
		return date_exp;
	}

	public void setDate_exp(LocalDate date_exp) {
		this.date_exp = date_exp;
	}

	public int getSoldeCode() {
		String code = this.getCode();
		int nombreUtilisation = UtilisationDAO.getInstance().getNombreUtilisation(code);
		Offre offre = this.getOffre();
		int nbUtilInitial = offre.getNbUtilisations();
		int solde = nbUtilInitial - nombreUtilisation;
		return solde;
	}

	@Override
	public String toString() {
		return "Ticket [code=" + code + ", " + offre + ", date_achat=" + date_achat + ", date_exp=" + date_exp + "nombre places restantes=" + getSoldeCode() + "]";
	}

}
