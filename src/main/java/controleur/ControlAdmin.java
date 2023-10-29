package controleur;

import java.io.IOException;
import java.util.ResourceBundle;

import dao.AdminDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import piscine.Admin;
import piscine.Main;

public class ControlAdmin implements Initializable {

	public static Admin adminCourant;

	@FXML private TextField tfLogin, pfPassword;
	@FXML private Label lErreur;
	@FXML private Pane pAdminMenu, pAdminForm;

	// test si un admin est deja connecte lors de l'initialisation de la vue
	@Override
	public void initialize(java.net.URL arg0, ResourceBundle arg1) {
		isLoggedIn();
	}

	// si un admin est connecte, affiche le menu, sinon le formulaire de connexion
	public void isLoggedIn() {
		pAdminMenu.setVisible(false);
		pAdminForm.setVisible(false);
		if(adminCourant!=null) {
			pAdminMenu.setVisible(true);
		}
		else {
			pAdminForm.setVisible(true);
		}
	}

	// test l'authentification puis affiche la vue
	public void authentification() {
		lErreur.setVisible(false);
		String loginTF = tfLogin.getText();
		String passwordTF = pfPassword.getText();
		Admin admin = AdminDAO.getInstance().readFromLogin(loginTF);
		if (admin !=null && admin.getMdp().equals(passwordTF)) {
			adminCourant = admin;
			isLoggedIn();
		}
		else {
			lErreur.setVisible(true);
		}
	}

	public void deconnexion() {
		adminCourant = null;
		isLoggedIn();
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

	public void toAdminOffres() {
		Parent loader;
		try {
			loader = FXMLLoader.load(getClass().getResource("/ihm/AdminOffres.fxml"));
			Scene scene = new Scene(loader);			
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void toAdminPersonnel() {
		Parent loader;
		try {
			loader = FXMLLoader.load(getClass().getResource("/ihm/AdminPersonnel.fxml"));
			Scene scene = new Scene(loader);			
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void toAdmin() {
		Parent loader;
		try {
			loader = FXMLLoader.load(getClass().getResource("/ihm/Admin.fxml"));
			Scene scene = new Scene(loader);
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
