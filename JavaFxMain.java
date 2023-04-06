package br.com.vener.projetoJavaFx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class JavaFxMain extends Application {

	public void start(Stage primaryStage) throws Exception {

		int rows = Enviroments.MAX_ROWS;
		int columns = Enviroments.MAX_COLUMNS;

		// Define o nó principal
		HBox root = new HBox();
		root.setPadding(new Insets(50.0));
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-background-color: rgba(255, 255, 255, 0)");

		// Adiciona a grade ao nó principal
		root.getChildren().addAll(new GridGame(rows, columns));

		// Cria a cena da aplicação
		Scene scene = new Scene(root, 700, 500, Color.SALMON);

		// Define e exibe a aplicação
		primaryStage.setTitle("Main");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
