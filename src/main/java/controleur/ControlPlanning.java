package controleur;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import dao.CoursDAO;
import dao.TicketDAO;
import dao.UtilisationDAO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import piscine.Cours;
import piscine.Main;
import piscine.Ticket;
import piscine.Utilisation;

public class ControlPlanning implements Initializable {

	private ObservableList<Cours> coursObs = FXCollections.observableArrayList();

	private Cours coursSelectionne;

	@FXML private Pane pPlanning, pReservation, pValidationCode, pReservationOk, pErreurReservation;
	@FXML private TextField tfCode;
	@FXML private Label lPiscine, lDebut, lFin, lDateCours, lErreur, lErreurReservation;
	@FXML private Button bAfficheCours, bInscrire;

	@FXML private TableView<Cours> planningTable;
	@FXML private TableColumn<Cours, String> nomPiscine, nomEmploye, date, horaireDebut, horaireFin;
	@FXML private TableColumn<Cours, Number> nbPlaces;

	// affiche les informations des cours dans le tableau lors de l'initialisation de la vue
	@Override
	public void initialize(java.net.URL arg0, ResourceBundle arg1) {
		pReservation.setVisible(false);
		pValidationCode.setVisible(false);
		pReservationOk.setVisible(false);
		afficherCours();
	}

	// ajoute à l'observable les objets de type cours en bd
	public ObservableList<Cours> getCours() {
		List<Cours> listeCours = CoursDAO.getInstance().readAll();
		for (Cours cours : listeCours) {
			if(cours.getPlacesRestantes()>0) {
				coursObs.add(cours);
			}
		}
		return coursObs;
	}

	// attribue les valeurs des objets aux colonnes de la tableview
	public void afficherCours() {
		nomPiscine.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPiscine().getNom()));
		nomEmploye.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmploye().getNom()));
		nbPlaces.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPlacesRestantes()));
		date.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toStringDate()));
		horaireDebut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toStringHoraireDebut()));
		horaireFin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toStringHoraireFin()));	
		planningTable.getItems().clear();
		planningTable.setItems(this.getCours());
	}

	// recupere les informations de l'objet selectionne
	public void selectionnerCours() {
		coursSelectionne = planningTable.getSelectionModel().getSelectedItem();
		if (coursSelectionne != null) {
			pReservation.setVisible(true);
			lPiscine.setText(coursSelectionne.getPiscine().getNom());
			lDateCours.setText(coursSelectionne.toStringDate());
			lDebut.setText(coursSelectionne.toStringHoraireDebut());
			lFin.setText(coursSelectionne.toStringHoraireFin());				
		}
	}

	// verifie si le ticket existe, que le solde n'est pas vide et qu'il est bien de type cours
	public void valider() {
		Ticket ticket = TicketDAO.getInstance().read(tfCode.getText());
		if(ticket!= null) {
			int utilisations = ticket.getSoldeCode();			
			if(utilisations>0) {
				Utilisation utilisation = new Utilisation(LocalDateTime.now(), ticket);
				if(ticket.getOffre().getNom().equals("cours")) {
					UtilisationDAO.getInstance().create(utilisation);
					TicketDAO.getInstance().ajouterParticipation(coursSelectionne, ticket);
					pReservationOk.setVisible(true);
				} else {
					pErreurReservation.setVisible(true);
					lErreurReservation.setText("Votre code ne permet pas de souscrire à un cours.");
				}
			} else {
				lErreur.setText("Il ne vous reste plus d'utilisations sur ce code.");
			}
		} else {
			lErreur.setText("Code non valide, veuillez réessayer.");
		}
	}

	public void annuler() {
		pReservation.setVisible(false);
		pValidationCode.setVisible(false);
	}

	public void reserver() {
		pValidationCode.setVisible(true);
	}

	public void retourPlanning() {
		pErreurReservation.setVisible(false);
	}

	public void toAccueil() {
		Parent loader;
		try {
			loader = FXMLLoader.load(getClass().getResource("/ihm/Accueil.fxml"));
			Scene scene = new Scene(loader);			
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
