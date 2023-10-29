package controleur;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import dao.TicketDAO;
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

public class ControlConsulterTicket implements Initializable{
	@FXML
	TextField tfCode;
	@FXML
	Label lCode, lDateAchat, lDateExp, lOffre, lUtilisations, lErreur;
	@FXML
	Pane pInfosTicket;

	@Override
	public void initialize(java.net.URL arg0, ResourceBundle arg1) {
		lErreur.setVisible(false);
	}

	// verifie si le ticket existe en bd et s'il n'est pas expire
	public void checkTicket() {
		String code = tfCode.getText();
		Ticket ticket = TicketDAO.getInstance().read(code);
		if (ticket != null) {
			lErreur.setVisible(false);
			String nomOffre = ticket.getOffre().getNom();
			int solde = ticket.getSoldeCode();
			String strSolde = Integer.toString(solde);
			LocalDate achat = ticket.getDate_achat();
			LocalDate exp = ticket.getDate_exp();
			if (exp.isAfter(LocalDate.now())) {
				lCode.setText(code);
				lDateAchat.setText(achat.toString());
				lDateExp.setText(exp.toString());
				lOffre.setText(nomOffre);
				lUtilisations.setText(strSolde);
				pInfosTicket.setVisible(true);
			} else {
				lErreur.setVisible(true);
				lErreur.setText("Votre ticket est expiré depuis le "+exp);
			}
		} else {
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
