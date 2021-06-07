package br.unicap.compiler.view;

import br.unicap.compiler.view.util.CKeywordsAsync;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;


public class Compiler extends Application {
    public static Stage stage;
    public static Map<String, CodeArea> codeAreas;
    
    @Override
    public void start(Stage primaryStage) throws Exception { 
        codeAreas = new LinkedHashMap<>();
        stage = primaryStage;
        stage.setTitle("Pavo Compiler");
        Parent root = FXMLLoader.load(getClass().getResource("/br/unicap/compiler/view/fxml/FXMLMainScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.getScene().getStylesheets().add(getClass()
                .getResource("/br/unicap/compiler/view/css/dark.css")
                .toExternalForm());
        stage.getScene().getStylesheets().
                add(CKeywordsAsync.class.getResource("/br/unicap/compiler/view/css/c-keywords.css").toExternalForm());
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


