package piscine;

public class Offre {

	private int id;
	private String nom;
	private int validite;
	private float tarif;
	private int nbUtilisations;
	
	public Offre(int id, String nom, int validite, float tarif, int nbUtilisations) {
		super();
		this.id = id;
		this.nom = nom;
		this.validite = validite;
		this.tarif = tarif ;
		this.nbUtilisations = nbUtilisations;
	
	}

	public Offre(String nom, int validite, float tarif, int nbUtilisations) {
		super();
		this.nom = nom;
		this.validite = validite;
		this.tarif = tarif ;
		this.nbUtilisations = nbUtilisations;
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

	public int getValidite() {
		return validite;
	}

	public void setValidite(int validite) {
		this.validite = validite;
	}

	public float getTarif() {
		return tarif;
	}

	public void setTarif(float tarif) {
		this.tarif = tarif;
	}

	public int getNbUtilisations() {
		return nbUtilisations;
	}

	public void setNbUtilisations(int nbUtilisations) {
		this.nbUtilisations = nbUtilisations;
	}

	@Override
	public String toString() {
		return "Offre [id=" + id + ", nom=" + nom + ", validite=" + validite + ", tarif=" + tarif + ", nombre d'utilisations="
				+ nbUtilisations + "]";
	}
	
}
