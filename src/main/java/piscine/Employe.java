package piscine;

import java.util.ArrayList;
import java.util.List;

public class Employe {

	private int id = -1;
	private String nom;
	private String prenom;
	private Adresse adr;
	private List<Piscine> lesPiscines = new ArrayList<Piscine>(); 

	public Employe(int id, String nom, String prenom, Adresse adr, List<Piscine> lesPiscines) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.adr = adr;
		this.setLesPiscines(lesPiscines);
	}

	public Employe(String nom, String prenom, Adresse adr, List<Piscine> lesPiscines) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.adr = adr;
		this.setLesPiscines(lesPiscines);
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String getPrenom() {
		return prenom;
	}
	
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	public Adresse getAdr() {
		return adr;
	}
	
	public void setAdr(Adresse adr) {
		this.adr = adr;
	}

	public List<Piscine> getLesPiscines() {
		return lesPiscines;
	}

	public void setLesPiscines(List<Piscine> lesPiscines) {
		this.lesPiscines = lesPiscines;
	}

	//	Affiche proprement la liste des piscines d'un employé (AdminPersonnel)
	public String toStringPiscines() {
		int lenPiscine = lesPiscines.size();
		String[] piscines = new String[lenPiscine];
		for (int i = 0; i < lenPiscine; i++) {
			piscines[i] = lesPiscines.get(i).getNom();
		}
		return String.join(", ", piscines);
	}

	@Override
	public String toString() {
		return "Employe [id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", " + adr + ", lesPiscines=" + lesPiscines + "]";
	}

}