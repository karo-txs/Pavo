package br.unicap.compiler.view.util;

import br.unicap.compiler.view.FXMLMainScreenController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class NewArchiveBox {

    public static boolean click = true;
    static String filename = "untitled.txt";
    

    public static String display(String title, String message, FXMLMainScreenController fx) {
        Stage window = new Stage();
        window.initStyle(StageStyle.UNDECORATED);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(100);
        

        GridPane p = new GridPane();
        p.maxWidth(160);
        p.minWidth(160);
        p.maxHeight(30);
        p.minHeight(30);
       
        p.setPrefSize(160, 30);
        Label label = new Label();
        Button confirmar = new Button();
        
        Image img = new Image("/br/unicap/compiler/view/images/check.png");
        ImageView view = new ImageView(img);
        confirmar.setGraphic(view);
        
        p.setBackground(new Background(new BackgroundFill(Color.rgb(69, 67, 67), CornerRadii.EMPTY, Insets.EMPTY)));
        label.setStyle("-fx-text-fill: white;");
        confirmar.setStyle(" -fx-text-fill: white; -fx-base: rgb(50, 50, 50);");

        p.setVisible(true);

        label.setText(message);

        TextField nomeArquivo = new TextField();

        nomeArquivo.setMaxWidth(200);

        confirmar.setOnAction(e -> {
            filename = nomeArquivo.getText();
            click = false;
            window.close();
        }
        );

        HBox layout = new HBox(10);
        layout.getChildren().addAll(nomeArquivo, confirmar);
        layout.setAlignment(Pos.CENTER_LEFT);

        window.setResizable(false);
        window.setY(fx.getAdd().getLayoutY()+46);
        window.setX(fx.getAdd().getLayoutX()+105);

        p.getChildren().addAll(layout);
        p.setAlignment(Pos.CENTER_LEFT);
        
        Scene scene = new Scene(p);
        Image icon = new Image("/br/unicap/compiler/view/images/add-file.png", 32, 32, false, false);
        window.getIcons().add(icon);
        window.setScene(scene);
        window.showAndWait();
        return filename;
    }

}
