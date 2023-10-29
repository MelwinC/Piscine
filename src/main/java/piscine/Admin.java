package piscine;

public class Admin {

	private Employe employe;
	private String identifiant;
	private String mdp;
	
	public Admin(Employe emp, String identifiant, String mdp) {
		super();
		this.employe = emp;
		this.identifiant = identifiant;
		this.mdp = mdp;
	}

	public Employe getEmploye() {
		return employe;
	}
	
	public void setEmploye(Employe emp) {
		this.employe = emp;
	}
	
	public String getIdentifiant() {
		return identifiant;
	}
	
	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}
	
	public String getMdp() {
		return mdp;
	}
	
	public void setMdp(String mdp) {
		this.mdp = mdp;
	}

	@Override
	public String toString() {
		return "Admin ["+ employe + ", identifiant=" + identifiant + ", mdp=" + mdp + "]";
	}
	
}