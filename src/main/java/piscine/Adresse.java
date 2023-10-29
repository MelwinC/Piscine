package piscine;

public class Adresse {
	private int id = -1;
	private String ville;
	private int code_postal;
	private int num_rue;
	private String rue;
	
	public Adresse(int id, String ville, int cp, int num_rue, String rue) {
		super();
		this.id=id;
		this.ville=ville;
		this.code_postal=cp;
		this.num_rue=num_rue;
		this.rue=rue;
	}
	
	public Adresse(String ville, int cp, int num_rue, String rue) {
		super();
		this.ville=ville;
		this.code_postal=cp;
		this.num_rue=num_rue;
		this.rue=rue;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getVille() {
		return ville;
	}
	
	public void setVille(String ville) {
		this.ville = ville;
	}
	
	public int getCode_postal() {
		return code_postal;
	}
	
	public void setCode_postal(int code_postal) {
		this.code_postal = code_postal;
	}
	
	public int getNum_rue() {
		return num_rue;
	}
	
	public void setNum_rue(int num_rue) {
		this.num_rue = num_rue;
	}
	
	public String getRue() {
		return rue;
	}
	
	public void setRue(String rue) {
		this.rue = rue;
	}
	
	@Override
	public String toString() {
		return "Adresse [id=" + id + ", ville=" + ville + ", code_postal=" + code_postal + ", num_rue=" + num_rue
				+ ", rue=" + rue + "]";
	}

}
