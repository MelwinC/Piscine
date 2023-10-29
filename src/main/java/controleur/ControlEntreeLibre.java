package controleur;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import dao.TicketDAO;
import dao.UtilisationDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import piscine.Main;
import piscine.Ticket;
import piscine.Utilisation;

public class ControlEntreeLibre implements Initializable {
	@FXML
	TextField tfCode;
	@FXML
	Label lErreur;
	@FXML
	Pane pValide;

	@Override
	public void initialize(java.net.URL arg0, ResourceBundle arg1) {
		lErreur.setVisible(false);
	}

	// verifie si le ticket existe en bd, qu'il n'est pas expire, que son solde n'est pas vide et qu'il a souscrit au bon forfait
	public void checkTicket() {
		String code = tfCode.getText();
		Ticket ticket = TicketDAO.getInstance().read(code);
		if (ticket != null) {
			lErreur.setVisible(false);
			String nomOffre = ticket.getOffre().getNom();
			int solde = ticket.getSoldeCode();
			LocalDate exp = ticket.getDate_exp();
			if (exp.isAfter(LocalDate.now())) {
				if(nomOffre.equals("cours")){
					lErreur.setVisible(true);
					lErreur.setText("Votre ticket ne permet pas d'accéder librement à la piscine.");
				}
				else if (nomOffre.equals("solo") && solde>1) {
					Utilisation utilisation = new Utilisation(LocalDateTime.now(), ticket);
					UtilisationDAO.getInstance().create(utilisation);
					pValide.setVisible(true);
				} 
				else if (nomOffre.equals("duo") && solde>2) {
					Utilisation utilisation1 = new Utilisation(LocalDateTime.now(), ticket);
					UtilisationDAO.getInstance().create(utilisation1);
					// forfait duo donc cree 2 utilisations en bd
					Utilisation utilisation2 = new Utilisation(LocalDateTime.now().plusSeconds(5), ticket);
					UtilisationDAO.getInstance().create(utilisation2);
					pValide.setVisible(true);
				} else {
					lErreur.setVisible(true);
					lErreur.setText("Votre solde est vide.");
				}
			} else {
				lErreur.setVisible(true);
				lErreur.setText("Votre ticket est expiré depuis le "+exp);
			}

		} 
		else {
			lErreur.setVisible(true);
			lErreur.setText("Votre ticket n'existe pas, veuillez réessayer.");
		} 
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
