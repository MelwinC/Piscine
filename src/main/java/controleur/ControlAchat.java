package controleur;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import dao.OffreDAO;
import dao.TicketDAO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import piscine.Main;
import piscine.Offre;
import piscine.Ticket;

public class ControlAchat implements Initializable{

	private ObservableList<Offre> offresObs = FXCollections.observableArrayList();

	private Offre offreSelectionne;

	@FXML private Pane pOffre, pPaiement, pTicket;

	@FXML private Label lNom, lTarif, lValidite, lNbUtil, lErreur, lErreurCode, lCode, lDateExp, lNomTicket, lNbUtilTicket;

	@FXML private PasswordField pfCode;

	@FXML private TableView<Offre> offreTable;
	@FXML private TableColumn<Offre, String> nomOffre;
	@FXML private TableColumn<Offre, String> tarifOffre;
	@FXML private TableColumn<Offre, Number> validiteOffre;
	@FXML private TableColumn<Offre, Number> utilOffre;

	// affiche les informations des offres dans le tableau lors de l'initialisation de la vue
	@Override
	public void initialize(java.net.URL arg0, ResourceBundle arg1) {
		lErreurCode.setVisible(false);
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
		utilOffre.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNbUtilisations()));
		offreTable.getItems().clear();
		offreTable.setItems(this.getOffres());
	}

	// recupere les informations de l'objet selectionne
	public void selectionnerOffre() {
		offreSelectionne = offreTable.getSelectionModel().getSelectedItem();
		Offre offre = offreSelectionne;
		if(offre!=null) {
			lNom.setText(offre.getNom());
			lTarif.setText(Float.toString(offre.getTarif()));
			lValidite.setText(Integer.toString(offre.getValidite()));
			lNbUtil.setText(Integer.toString(offre.getNbUtilisations()));
			pOffre.setVisible(true);
		}
	}

	// recupere les valeurs des boutons cliques, et verifie que le code possede 4 chiffres
	public void codeCB(ActionEvent event) {
		if(pfCode.getText().length()<4) {
			lErreurCode.setVisible(false);
			String ancienCode = pfCode.getText();
			String input = ((Button) event.getSource()).getText();
			pfCode.setText(ancienCode+input);
		} else {
			lErreurCode.setText("Maximum 4 caractères autorisé.");
			lErreurCode.setVisible(true);
		}
	}

	// test le format du codeCB puis cree le ticket dans la bd avec les informations choisies
	public void valider() {
//		on peut imaginer ajouter une condition sur le code entre : && pfCode.getText().equals("1234");
		if(pfCode.getText().length()==4) {
			String code = genererCode();
			// vérifie si le code existe déjà dans la bd
			while(TicketDAO.getInstance().read(code)!=null) {
				code = genererCode();
			}
			LocalDate now = LocalDate.now();
			LocalDate dateExp = now.plusDays(offreSelectionne.getValidite());
			Ticket ticket = new Ticket(genererCode(), offreSelectionne, now, dateExp);
			TicketDAO.getInstance().create(ticket);
			lCode.setText(ticket.getCode());
			lNomTicket.setText(ticket.getOffre().getNom());
			lDateExp.setText(toStringDate(dateExp));
			lNbUtilTicket.setText(Integer.toString(ticket.getSoldeCode()));
			pTicket.setVisible(true);
		} else {
			lErreurCode.setText("Code erroné, veuillez réessayer.");
			lErreurCode.setVisible(true);
			pfCode.setText("");
		}
	}

	// genere un code aleatoirement
	public static String genererCode() {
		return UUID.randomUUID().toString();
	}

	// formate la date d'une maniere plus lisible 
	public String toStringDate(LocalDate date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy");
		return date.format(formatter);
	}

	public void paiement() {
		pPaiement.setVisible(true);
	}

	public void corriger() {
		pfCode.setText("");
		lErreurCode.setVisible(false);
	}

	public void annuler() {
		pOffre.setVisible(false);
		pPaiement.setVisible(false);
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

