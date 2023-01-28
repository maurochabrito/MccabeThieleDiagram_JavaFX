package application;

import java.io.IOException;
import java.util.List;

import brain.MccabeThiele;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	private static Scene mainScene;
	
	@Override
	public void start(Stage stage) {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("/gui/View.fxml"));
			//Scene scene = new Scene(parent);
			mainScene = new Scene(parent);
			//stage.setScene(scene);
			stage.setScene(mainScene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static Scene getMainScene() {
		return mainScene;
	}
	public static void main(String[] args) {
		launch(args);
	}
}
