package br.unicap.compiler.view;

import br.unicap.compiler.lexicon.Scanner;
import br.unicap.compiler.lexicon.Token;
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
import br.unicap.compiler.syntax.Parser;
import br.unicap.compiler.view.util.CKeywordsAsync;
import br.unicap.compiler.view.util.NewArchiveBox;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.collection.ListModification;

public class FXMLMainScreenController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private AnchorPane pn;

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

    @FXML
    private TextField nameArchive;

    private String filename;

    @FXML
    private Label newFile;

    @FXML
    private Label openFile;

    @FXML //barra de rolagem do codeArea
    VirtualizedScrollPane scroll;

    public static boolean isDark = false;
    ArrayList<Token> tokens = new ArrayList<>();

    @FXML
    private Pane paneArchive = new Pane();

    @FXML
    private Button play = new Button();

    @FXML
    private Button clear = new Button();

    @FXML
    private Button add = new Button();

    @FXML
    private Button search = new Button();

    @FXML
    private Button lexical = new Button();

    @FXML
    private Button syntax = new Button();

    @FXML
    private void run(ActionEvent event) {
        runSintatica(event);
    }

    @FXML
    private void runLexica(ActionEvent event) {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab != null) {
            //cast pra receber o codeArea que esta dentro de scroll
            scroll = (VirtualizedScrollPane) selectedTab.getContent();
            //CodeArea ca = (CodeArea) selectedTab.getContent();
            CodeArea ca = (CodeArea) scroll.getContent();
            if (ca != null) {
                padraoLexica();
                tokens = new ArrayList<>();
                resultArea.setText("");
                data = FXCollections.observableArrayList(tokens);
                table.setItems(data);

                filename = selectedTab.getText();

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
                    if (isDark) {
                        resultArea.setStyle("-fx-text-fill: #FF2800");
                    } else {
                        resultArea.setStyle("-fx-text-fill: #B82524");
                    }
                } else {
                    resultArea.setText(tokens.size() + " identified tokens and no lexical errors found.\n\nSUCCESSFULLY BUILT!");
                    if (isDark) {
                        resultArea.setStyle("-fx-text-fill: #8DE38D");
                    } else {
                        resultArea.setStyle("-fx-text-fill: green");

                    }
                }
                data = FXCollections.observableArrayList(tokens);
                table.setItems(data);

            }
        }
    }

    @FXML
    private void runSintatica(ActionEvent event) {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab != null) {
            //cast pra receber o codeArea que esta dentro de scroll
            scroll = (VirtualizedScrollPane) selectedTab.getContent();
            //CodeArea ca = (CodeArea) selectedTab.getContent();
            CodeArea ca = (CodeArea) scroll.getContent();
            if (ca != null) {
                padrao();
                tokens = new ArrayList<>();
                resultArea.setText("");
                data = FXCollections.observableArrayList(tokens);
                table.setItems(data);

                filename = selectedTab.getText();

                Scanner sc = new Scanner(ca.getText(), filename);
                Parser ps = new Parser(sc, filename);
                ps.runParser();
                if (!sc.getException().equals("NULL")) {
                    resultArea.setText(sc.getException() + "\n\nCONSTRUCTION FAILURE!");
                    if (isDark) {
                        resultArea.setStyle("-fx-text-fill: #FF2800");
                    } else {
                        resultArea.setStyle("-fx-text-fill: #B82524");
                    }
                } else {
                    if (!ps.getException().equals("NULL")) {
                        resultArea.setText(ps.getException() + "\n\nCONSTRUCTION FAILURE!");
                        if (isDark) {
                            resultArea.setStyle("-fx-text-fill: #FF2800");
                        } else {
                            resultArea.setStyle("-fx-text-fill: #B82524");
                        }
                    } else {
                        resultArea.setText("No errors found.\n\nSUCCESSFULLY BUILT!");
                        if (isDark) {
                            resultArea.setStyle("-fx-text-fill: #8DE38D");
                        } else {
                            resultArea.setStyle("-fx-text-fill: green");

                        }
                    }

                }
            }
        }
    }
    //******************

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
            updateColors();
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
            updateColors();
        }
    }

    private void updateColors() {

        tokens.forEach((t) -> {
            t.updateColor();
        });

        data = FXCollections.observableArrayList(tokens);
        table.setItems(data);

        if (isDark) {
            if (resultArea.getText().contains("SUCCESSFULLY")) {
                resultArea.setStyle("-fx-text-fill: #8DE38D");
            } else {
                resultArea.setStyle("-fx-text-fill: #FF2800");
            }
        } else {
            if (resultArea.getText().contains("SUCCESSFULLY")) {
                resultArea.setStyle("-fx-text-fill: green");
            } else {
                resultArea.setStyle("-fx-text-fill: #B82524");
            }
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
            startCodeArea(filename);
            //cria uma ScrollBar, coloca o codeArea e adiciona na tab1 o scroll ao inves do codeArea
            scroll = new VirtualizedScrollPane(Compiler.codeAreas.get(filename));
            Tab tab1 = new Tab(name, scroll);

            tabPane.getTabs().add(tab1);
            Compiler.codeAreas.get(filename).replaceText(txtConteudo);
            resultArea.setVisible(true);
        }
    }

    @FXML
    private void newArchive() {
        this.nameArchive.setText(NewArchiveBox.display("Novo arquivo", "Digite o nome do arquivo", this));

        if (this.nameArchive.getText().equals("")) {
            this.filename = "Untitled.c";
        } else {
            if (!this.nameArchive.getText().contains(".c")) {
                this.filename = nameArchive.getText() + ".c";
            } else {
                this.filename = nameArchive.getText();
            }
        }
        startCodeArea(filename);
        //cria uma ScrollBar, coloca o codeArea e adiciona na tab1 o scroll ao inves do codeArea
        scroll = new VirtualizedScrollPane(Compiler.codeAreas.get(filename));
        Tab tab1 = new Tab(this.filename, scroll);
        tabPane.getTabs().add(tab1);
        nameArchive.setText("");
        Compiler.codeAreas.get(filename).requestFocus();
        resultArea.setVisible(true);
    }

    @FXML
    private void clearEditor() {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab != null) {
            scroll = (VirtualizedScrollPane) selectedTab.getContent();
            filename = selectedTab.getText();

            if (Compiler.codeAreas.get(filename) != null) {
                Compiler.codeAreas.get(filename).replaceText("");
            }
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

    public void padrao() {
        tabPane.setPrefWidth(1222);
        table.setVisible(false);
    }

    public void padraoLexica() {
        tabPane.setPrefWidth(1222 - (table.getWidth() + 10));
        table.setVisible(true);
    }

    public Button getAdd() {
        return this.add;
    }

    //******************
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        table.setPlaceholder(new Label("No tokens read so far"));
        table.getColumns().addAll(tokenCol, typeCol);
        tokenCol.setMinWidth(170);
        tokenCol.setCellValueFactory(new PropertyValueFactory<>("token"));
        typeCol.setMinWidth(222);
        typeCol.setCellValueFactory(new PropertyValueFactory<>("typeColor"));
        typeCol.setSortable(false);
        tokenCol.setSortable(false);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        play.setTooltip(new Tooltip("Compile"));
        clear.setTooltip(new Tooltip("Clear"));
        add.setTooltip(new Tooltip("Add file"));
        search.setTooltip(new Tooltip("Search file"));
        lexical.setTooltip(new Tooltip("Lexical Analysis"));
        syntax.setTooltip(new Tooltip("Syntax Analysis"));
        paneArchive.setId("pn");
        resultArea.setVisible(false);
    }

    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = CKeywordsAsync.PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass
                    = matcher.group("KEYWORD") != null ? "keyword"
                    : matcher.group("PAREN") != null ? "paren"
                    : matcher.group("BRACE") != null ? "brace"
                    : matcher.group("BRACKET") != null ? "bracket"
                    : matcher.group("SEMICOLON") != null ? "semicolon"
                    : matcher.group("STRING") != null ? "string"
                    : matcher.group("COMMENT") != null ? "comment"
                    : null;
            /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    public void startCodeArea(String filename) {
        CodeArea codeArea = new CodeArea();

        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.setId("codearea");
        codeArea.getVisibleParagraphs().addModificationObserver(
                new FXMLMainScreenController.VisibleParagraphStyler<>(codeArea, this::computeHighlighting)
        );

        // auto-indent: insert previous line's indents on enter
        final Pattern whiteSpace = Pattern.compile("^\\s+");
        codeArea.addEventHandler(KeyEvent.KEY_PRESSED, KE
                -> {
            if (KE.getCode() == KeyCode.ENTER) {
                int caretPosition = codeArea.getCaretPosition();
                int currentParagraph = codeArea.getCurrentParagraph();
                Matcher m0 = whiteSpace.matcher(codeArea.getParagraph(currentParagraph - 1).getSegments().get(0));
                if (m0.find()) {
                    Platform.runLater(() -> codeArea.insertText(caretPosition, m0.group()));
                }
            }
        });

        codeArea.replaceText(0, 0, CKeywordsAsync.sampleCode);
        Compiler.codeAreas.put(filename, codeArea);
    }

    private class VisibleParagraphStyler<PS, SEG, S> implements Consumer<ListModification<? extends Paragraph<PS, SEG, S>>> {

        private final GenericStyledArea<PS, SEG, S> area;
        private final Function<String, StyleSpans<S>> computeStyles;
        private int prevParagraph, prevTextLength;

        public VisibleParagraphStyler(GenericStyledArea<PS, SEG, S> area, Function<String, StyleSpans<S>> computeStyles) {
            this.computeStyles = computeStyles;
            this.area = area;
        }

        @Override
        public void accept(ListModification<? extends Paragraph<PS, SEG, S>> lm) {
            if (lm.getAddedSize() > 0) {
                int paragraph = Math.min(area.firstVisibleParToAllParIndex() + lm.getFrom(), area.getParagraphs().size() - 1);
                String text = area.getText(paragraph, 0, paragraph, area.getParagraphLength(paragraph));

                if (paragraph != prevParagraph || text.length() != prevTextLength) {
                    int startPos = area.getAbsolutePosition(paragraph, 0);
                    Platform.runLater(() -> area.setStyleSpans(startPos, computeStyles.apply(text)));
                    prevTextLength = text.length();
                    prevParagraph = paragraph;
                }
            }
        }
    }
}
