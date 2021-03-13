package br.unicap.compiler.view.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NewArchiveBox {

    public static boolean click = true;
    static String filename = "untitled.txt";

    public static String display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(300);

        GridPane p = new GridPane();
        p.maxWidth(300);
        p.minWidth(300);
        p.maxHeight(100);
        p.minHeight(100);
        p.setPrefSize(300, 100);
        p.setBackground(new Background(new BackgroundFill(Color.DARKGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        p.setVisible(true);

        Label label = new Label();
        label.setText(message);

        TextField nomeArquivo = new TextField();

        nomeArquivo.setMaxWidth(200);

        Button confirmar = new Button("Confirmar");

        confirmar.setOnAction(e -> {
            filename = nomeArquivo.getText();
            click = false;
            window.close();
        }
        );
        
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, nomeArquivo, confirmar);
        layout.setAlignment(Pos.CENTER);
        window.setResizable(false);

        p.getChildren().addAll(layout);
        p.setAlignment(Pos.CENTER);
        Scene scene = new Scene(p);
        window.setScene(scene);
        window.showAndWait();
        return filename;
    }

}
