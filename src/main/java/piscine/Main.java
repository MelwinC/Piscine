package piscine;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	public AnchorPane lAutreFenetre;
	
	public static Stage stage;
	
	@Override
	public void start(Stage racine) throws Exception {
		// Noeud racine.
		Main.stage = racine;
        Parent root = FXMLLoader.load(getClass().getResource("../ihm/Accueil.fxml"));
		Scene scene = new Scene(root);
		// Configuration de la fenêtre.
		racine.setScene(scene);
		racine.setTitle("Piscine");
		racine.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
