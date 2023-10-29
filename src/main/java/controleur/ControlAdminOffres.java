package controleur;

import java.util.List;
import java.util.ResourceBundle;

import dao.OffreDAO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import piscine.Offre;

public class ControlAdminOffres extends ControlAdmin {

	private ObservableList<Offre> offresObs = FXCollections.observableArrayList();

	private Offre offreSelectionne;

	@FXML private Label lErreur, lErreurCrea;
	@FXML private Pane pModifOffre, pConfirmUpdate, pCreationOffre, pConfirmCrea;
	@FXML private TextField tfNom, tfValidite, tfTarif, tfNbUtil, tfNomCrea, tfValiditeCrea, tfTarifCrea, tfNbUtilCrea;

	@FXML private TableView<Offre> offreTable;
	@FXML private TableColumn<Offre, String> nomOffre, tarifOffre;
	@FXML private TableColumn<Offre, Number> validiteOffre, nbUtilOffre;

	// affiche les informations des offres dans le tableau lors de l'initialisation de la vue
	@Override
	public void initialize(java.net.URL arg0, ResourceBundle arg1) {
		lErreur.setVisible(false);
		lErreurCrea.setVisible(false);
		afficherOffres();
	}

	// ajoute à l'observable les objets de type offre en bd
	public ObservableList<Offre> getOffres() {
		List<Offre> lesOffres = OffreDAO.getInstance().readAll();
		for (Offre offre : lesOffres) {
			offresObs.add(offre);
		}
		return offresObs;
	}

	// attribue les valeurs des objets aux colonnes de la tableview
	public void afficherOffres() {
		nomOffre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
		tarifOffre.setCellValueFactory(cellData -> new SimpleStringProperty(Float.toString(cellData.getValue().getTarif())));
		validiteOffre.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValidite()));
		nbUtilOffre.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNbUtilisations()));
		offreTable.getItems().clear();
		offreTable.setItems(this.getOffres());
	} 

	// recupere les informations de l'objet selectionne
	public void selectionnerOffre() {
		offreSelectionne = offreTable.getSelectionModel().getSelectedItem();
		if (offreSelectionne != null) {
			tfNom.setText(offreSelectionne.getNom());
			tfValidite.setText(Integer.toString(offreSelectionne.getValidite()));
			tfTarif.setText(Float.toString(offreSelectionne.getTarif()));
			tfNbUtil.setText(Integer.toString(offreSelectionne.getNbUtilisations()));
			pModifOffre.setVisible(true);
		}
	}

	// update l'offre selectionne avec les nouvelles informations
	public void updateOffre() {
		lErreur.setVisible(false);
		if (tfNom!=null && tfValidite!=null && tfTarif!=null && tfNbUtil!=null) {
			offreSelectionne.setNom(tfNom.getText());
			offreSelectionne.setValidite(Integer.parseInt(tfValidite.getText()));
			offreSelectionne.setTarif(Float.parseFloat(tfTarif.getText()));
			offreSelectionne.setNbUtilisations(Integer.parseInt(tfNbUtil.getText()));
			OffreDAO.getInstance().update(offreSelectionne);
			pModifOffre.setVisible(false);
			afficherOffres();
		} else {
			lErreur.setVisible(true);
		}
	}

	// supprime l'offre selectionne
	public void confirmDel() {
		OffreDAO.getInstance().delete(offreSelectionne);
		pConfirmUpdate.setVisible(false);
		pModifOffre.setVisible(false);
		afficherOffres();
	}

	// cree en bd l'offre avec les informations indiquees
	public void confirmCrea() {
		lErreur.setVisible(false);
		if (tfNomCrea!=null && tfValiditeCrea!=null && tfTarifCrea!=null && tfNbUtilCrea!=null) {
			Offre offre = new Offre(
					tfNomCrea.getText(), 
					Integer.parseInt(tfValiditeCrea.getText()), 
					Float.parseFloat(tfTarifCrea.getText()), 
					Integer.parseInt(tfNbUtilCrea.getText())
					);
			OffreDAO.getInstance().create(offre);
			pConfirmCrea.setVisible(true);
			lErreurCrea.setVisible(false);
		} else {
			lErreurCrea.setVisible(true);
		}
	}

	public void creerOffre() {
		pCreationOffre.setVisible(true);
	}

	public void annuler() {
		pModifOffre.setVisible(false);
		pConfirmUpdate.setVisible(false);
		pCreationOffre.setVisible(false);
	}

	public void deleteOffre() {
		pConfirmUpdate.setVisible(true);
	}

	public void annulerDel() {
		pConfirmUpdate.setVisible(false);
	}

}

