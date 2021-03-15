package br.unicap.compiler.view;

import br.unicap.compiler.lexicon.Scanner;
import br.unicap.compiler.lexicon.Token;
import br.unicap.compiler.view.util.NewArchiveBox;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

public class FXMLMainScreenController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private AnchorPane pn;

    private CodeArea codeArea;

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
    private ObservableList<Token> data = FXCollections.observableArrayList();

    @FXML
    TabPane tabPane = new TabPane();

    private String filename;

    @FXML
    private Label newFile;

    @FXML
    private Label openFile;

    @FXML
    private void runLexica(ActionEvent event) {
        ArrayList<Token> tokens = new ArrayList<>();

        resultArea.setText("");
        data = FXCollections.observableArrayList(tokens);
        table.setItems(data);

        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        CodeArea ca = (CodeArea) selectedTab.getContent();

        Scanner sc = new Scanner(ca.getText(), filename);
        Token token;

        do {
            token = sc.nextToken();
            if (token != null) {
                tokens.add(token);
            }
        } while (token != null);
        if (!sc.getException().equals("NULL")) {
            resultArea.setText(sc.getException() + "\n\nCONSTRUCTION FAILURE!");
            resultArea.setStyle("-fx-text-fill: red");
        } else {
            data = FXCollections.observableArrayList(tokens);

            table.setItems(data);
            resultArea.setText(tokens.size() + " identified tokens and no lexical errors found.\n\nSUCCESSFULLY BUILT!");
            resultArea.setStyle("-fx-text-fill: green");
        }

    }
    //******************

    private boolean isDark = false;

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

    @FXML
    private void openArchive() {
        String txtConteudo = "";
        filename = getArchive();

        if (filename != null) {
            String[] aux = filename.split("\\\\");
            String name = aux[aux.length - 1];
            try {
                Path pathToFile = Paths.get(filename);
                txtConteudo = new String(Files.readAllBytes(pathToFile), StandardCharsets.UTF_8);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            codeArea = new CodeArea();
            codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
            Tab tab1 = new Tab(name, codeArea);
            tabPane.getTabs().add(tab1);
            codeArea.replaceText(txtConteudo);
        }
    }

    @FXML
    private void newArchive() {
        filename = NewArchiveBox.display("New File", "name of file: ");
        if (!NewArchiveBox.click) {//entra aqui quando se escreve e nao clica em confirmar?
            if (!filename.contains(".txt")) {
                filename += ".txt";
            }
            codeArea = new CodeArea();
            codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
            Tab tab1 = new Tab(filename, codeArea);
            tabPane.getTabs().add(tab1);
        }

    }

    @FXML
    private void clearEditor() {
        if (codeArea != null) {
            codeArea.replaceText(0, 0, "");
        }
    }

    private String getArchive() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extentionFilter);

        String userDirectoryString = System.getProperty("user.home");
        File userDirectory = new File(userDirectoryString);
        if (!userDirectory.canRead()) {
            userDirectory = new File("c:/");
        }
        fileChooser.setInitialDirectory(userDirectory);

        File chosenFile = fileChooser.showOpenDialog(null);
        String path;
        if (chosenFile != null) {
            path = chosenFile.getPath();
        } else {
            path = null;
        }
        return path;
    }

    //******************
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        table.getColumns().addAll(tokenCol, typeCol);
        tokenCol.setMinWidth(170);
        tokenCol.setCellValueFactory(new PropertyValueFactory<>("token"));
        typeCol.setMinWidth(222);
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setSortable(false);
        tokenCol.setSortable(false);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    }
}
