package br.unicap.compiler.view;

import br.unicap.compiler.lexicon.Scanner;
import br.unicap.compiler.lexicon.Token;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class FXMLMainScreenController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private AnchorPane pn;

    @FXML
    private TextArea codigoArea;

    @FXML
    private TextArea resultArea;

    @FXML
    private ImageView iconMoon;

    @FXML
    private ImageView iconSun;

    @FXML
    private TableView table = new TableView();
    TableColumn typeCol = new TableColumn("Type");
    TableColumn tokenCol = new TableColumn("Token");

    private boolean isFirst = true;

    @FXML
    private void runLexica(ActionEvent event) {
        ArrayList<Token> tokens = new ArrayList<>();
        //String filename = "C:\\Users\\karol\\Documents\\NetBeansProjects\\Compavo\\src\\br\\com\\compiler\\arquivos\\input.isi";

        Scanner sc = new Scanner(codigoArea.getText(), true);
        Token token;

        do {
            token = sc.nextToken();
            if (token != null) {
                tokens.add(token);
            }
        } while (token != null);
        if (!sc.getException().equals("NULL")) {
            resultArea.setText(sc.getException() + "\n\nFALHA NA CONSTRUÇÃO");
            resultArea.setStyle("-fx-text-fill: red");
        } else {
            //resultArea.setText(Printer.printTable(tokens)");
            if (isFirst) {
                table.getColumns().addAll(tokenCol, typeCol);
                tokenCol.setMinWidth(161);
                tokenCol.setCellValueFactory(new PropertyValueFactory<>("token"));
                typeCol.setMinWidth(161);
                typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
                isFirst = false;
            }
            
            ObservableList<Token> data= FXCollections.observableArrayList(tokens);

            table.setItems(data);
            //resultArea.setStyle("-fx-text-fill: green");
        }

    }
    //******************

    boolean isDark = false;

    @FXML
    private void change() {
        if (isDark) {
            Compiler.stage.getScene().getStylesheets().clear();
            Compiler.stage.getScene().setUserAgentStylesheet(null);
            Compiler.stage.getScene().getStylesheets()
                    .add(getClass()
                            .getResource("/br/unicap/compiler/view/css/classic.css")
                            .toExternalForm());
            isDark = !isDark;

            iconMoon.setVisible(true);
            iconSun.setVisible(false);
        } else {
            Compiler.stage.getScene().getStylesheets().clear();
            Compiler.stage.getScene().setUserAgentStylesheet(null);
            Compiler.stage.getScene().getStylesheets()
                    .add(getClass()
                            .getResource("/br/unicap/compiler/view/css/dark.css")
                            .toExternalForm());
            isDark = !isDark;
            iconSun.setVisible(true);
            iconMoon.setVisible(false);
        }
    }

    //******************
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
