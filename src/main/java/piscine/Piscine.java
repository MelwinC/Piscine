package piscine;

public class Piscine {
	private int id = -1;
	private String nom;
	private Adresse adresse;
	
	public Piscine (int id, String nom, Adresse adresse) {
		super();
		this.id=id;
		this.nom=nom;
		this.adresse=adresse;
	}
	
	public Piscine (String nom, Adresse adresse) {
		super();
		this.nom=nom;
		this.adresse=adresse;
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

	public Adresse getAdresse() {
		return adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	@Override
	public String toString() {
		return "Piscine [id=" + id + ", nom=" + nom + ", " + adresse + "]";
	}
	
}
