package controleur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import dao.AdresseDAO;
import dao.EmployeDAO;
import dao.PiscineDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import piscine.Adresse;
import piscine.Employe;
import piscine.Piscine;

public class ControlAdminPersonnel extends ControlAdmin {

	private ObservableList<Employe> empObs = FXCollections.observableArrayList();

	private Employe empSelectionne;

	private HashMap<String, CheckBox> piscineMap = new HashMap<>();

	@FXML private Label lErreur, lErreurCrea;
	@FXML private Pane pModifEmp, pConfirmUpdate, pCreationEmp, pConfirmCrea;
	@FXML private TextField tfNom, tfPrenom, tfNum, tfRue, tfVille, tfCp, tfNomCrea, tfPrenomCrea, tfNumCrea, tfRueCrea, tfVilleCrea, tfCpCrea;
	@FXML private CheckBox vanocea, kercado, vanoceaCrea, kercadoCrea;

	@FXML private TableView<Employe> empTable;
	@FXML private TableColumn<Employe, String> nomEmp, prenomEmp, piscinesEmp;

	// affiche les informations des employes dans le tableau lors de l'initialisation de la vue
	@Override
	public void initialize(java.net.URL arg0, ResourceBundle arg1) {
		lErreur.setVisible(false);
		lErreurCrea.setVisible(false);
		piscineMap.put("Vanocéa", vanocea);
		piscineMap.put("Kercado", kercado);
		afficherEmployes();
	}

	// ajoute à l'observable les objets de type employe en bd
	public ObservableList<Employe> getEmployes() {
		List<Employe> lesEmployes = EmployeDAO.getInstance().readAll();
		for (Employe emp : lesEmployes) {
			empObs.add(emp);
		}
		return empObs;
	}

	// attribue les valeurs des objets aux colonnes de la tableview
	public void afficherEmployes() {
		nomEmp.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
		prenomEmp.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenom()));
		piscinesEmp.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toStringPiscines()));
		empTable.getItems().clear();
		empTable.setItems(this.getEmployes());
	}

	// recupere les informations de l'objet selectionne
	public void selectionnerEmp() {
		for(CheckBox piscineCheck : piscineMap.values()) {
			piscineCheck.setSelected(false);
		}
		empSelectionne = empTable.getSelectionModel().getSelectedItem();
		if (empSelectionne != null) {
			Adresse adr = empSelectionne.getAdr();
			tfNom.setText(empSelectionne.getNom());
			tfPrenom.setText(empSelectionne.getPrenom());
			tfNum.setText(Integer.toString(adr.getNum_rue()));
			tfRue.setText(adr.getRue());
			tfVille.setText(adr.getVille());
			tfCp.setText(Integer.toString(adr.getCode_postal()));
			for (Piscine piscine : empSelectionne.getLesPiscines()) {
				piscineMap.get(piscine.getNom()).setSelected(true);
			}
			pModifEmp.setVisible(true);
		}
	}

	// retourne la liste des piscines selectionnes (checkbox)
	public List<Piscine> selectionPiscine(){
		List<Piscine> piscines = new ArrayList<>();
		if (vanocea.isSelected()) {
			Piscine pisc = PiscineDAO.getInstance().read(1);
			piscines.add(pisc);
		} 
		if (kercado.isSelected()) {
			Piscine pisc = PiscineDAO.getInstance().read(2);
			piscines.add(pisc);
		}
		return piscines;
	}

	// update l'employe selectionne avec les nouvelles informations
	public void updateEmp() {
		lErreur.setVisible(false);
		if (tfNom!=null && tfPrenom!=null && tfNum!=null && tfRue!=null && tfVille!=null && tfCp!=null && ((kercado.isSelected() || vanocea.isSelected()))) {
			Adresse adr = empSelectionne.getAdr();
			adr.setNum_rue(Integer.parseInt(tfNum.getText()));
			adr.setRue(tfRue.getText());
			adr.setVille(tfVille.getText());
			adr.setCode_postal(Integer.parseInt(tfCp.getText()));
			empSelectionne.setPrenom(tfPrenom.getText());
			empSelectionne.setNom(tfNom.getText());
			empSelectionne.setAdr(adr);
			List<Piscine> piscineChecked = selectionPiscine();
			empSelectionne.setLesPiscines(piscineChecked);
			EmployeDAO.getInstance().update(empSelectionne);
			pModifEmp.setVisible(false);
			afficherEmployes();
		} else {
			lErreur.setVisible(true);
		}
	}

// supprime l'employe selectionne
	public void confirmDel() {
		EmployeDAO.getInstance().delete(empSelectionne);
		pConfirmUpdate.setVisible(false);
		pModifEmp.setVisible(false);
		afficherEmployes();
	}

	// cree en bd l'employe avec les informations indiquees
	public void confirmCrea() {
		lErreur.setVisible(false);
		if (tfNomCrea!=null && tfPrenomCrea!=null && tfNumCrea!=null && tfRueCrea!=null && tfVilleCrea!=null && tfCpCrea!=null && ((vanoceaCrea.isSelected() || kercadoCrea.isSelected()))) {
			Adresse adrCrea = new Adresse(tfVilleCrea.getText(), Integer.parseInt(tfCpCrea.getText()), Integer.parseInt(tfNumCrea.getText()), tfRueCrea.getText());
			AdresseDAO.getInstance().create(adrCrea);
			List<Piscine> piscinesChecked = selectionPiscine();
			Employe empCrea = new Employe(tfNomCrea.getText(), tfPrenomCrea.getText(), adrCrea, piscinesChecked);
			EmployeDAO.getInstance().create(empCrea);
			pConfirmCrea.setVisible(true);
			lErreurCrea.setVisible(false);
		} else {
			lErreurCrea.setVisible(true);
		}
	}

	public void creerEmp() {
		pCreationEmp.setVisible(true);
	}

	public void annuler() {
		pModifEmp.setVisible(false);
		pConfirmUpdate.setVisible(false);
		pCreationEmp.setVisible(false);
	}

	public void deleteEmp() {
		pConfirmUpdate.setVisible(true);
	}

	public void annulerDel() {
		pConfirmUpdate.setVisible(false);
	}

}
