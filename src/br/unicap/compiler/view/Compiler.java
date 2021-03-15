package br.unicap.compiler.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Compiler extends Application {
    public static Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("Pavo Compiler");
        Parent root = FXMLLoader.load(getClass().getResource("/br/unicap/compiler/view/fxml/FXMLMainScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.getScene().getStylesheets().add(getClass()
                .getResource("/br/unicap/compiler/view/css/classic.css")
                .toExternalForm());
        Image icon = new Image(getClass().getResourceAsStream("/br/unicap/compiler/view/images/peacock.png"), 32, 32,false,false);
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
