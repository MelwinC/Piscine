package controleur;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import piscine.Main;

public class ControlAccueil {

	public void toAchat() {
		Parent loader;
		try {
			loader = FXMLLoader.load(getClass().getResource("/ihm/Achat.fxml"));
			Scene scene = new Scene(loader);			
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void toConsulterTicket() {
		Parent loader;
		try {
			loader = FXMLLoader.load(getClass().getResource("/ihm/ConsulterTicket.fxml"));
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

	public void toPlanning() {
		Parent loader;
		try {
			loader = FXMLLoader.load(getClass().getResource("/ihm/Planning.fxml"));
			Scene scene = new Scene(loader);			
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void toEntreeLibre() {
		Parent loader;
		try {
			loader = FXMLLoader.load(getClass().getResource("/ihm/EntreeLibre.fxml"));
			Scene scene = new Scene(loader);			
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

