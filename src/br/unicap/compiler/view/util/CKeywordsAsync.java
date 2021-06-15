package br.unicap.compiler.view.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

public class CKeywordsAsync extends Application {

    public static final String[] KEYWORDS = new String[]{
        "asm", "auto", "break", "case", "char", "const", "continue", "default", "do",
        "double", "else", "enum", "extern", "float", "for", "goto", "if", "int", "long",
        "main", "register", "return", "short", "signed", "sizeof", "static", "struct",
        "switch", "typedef", "union", "unsigned", "void", "volatile", "while", "print"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

    public static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
            + "|(?<PAREN>" + PAREN_PATTERN + ")"
            + "|(?<BRACE>" + BRACE_PATTERN + ")"
            + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
            + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
            + "|(?<STRING>" + STRING_PATTERN + ")"
            + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    public static final String sampleCode = String.join("\n", new String[]{
        "// Pavo, a minimalist Compiler"
        , "// Universidade Católica de Pernambuco - ICAM TECH"
        , "// Mini Demo - v1.0.0"
        , ""
        , "int main(){"
        , "    // Criação de variáveis"
        , "    int a = 4;"
        , "    int b = b/a*(a+5)/12*(5-a*2);"
        , "    int c = c*a;"
        , "    float d = (b*b - 4*a*c)*(a+b-c); "
        , "    "
        , "    int fimOp = 1;"
        , "    "
        , "    do{       "
        , "        while(b<=50 && b!=c || a>=30 && c!=0){ //expressões lógicas e relacionais"
        , "              int a;"
        , "              int g;"
        , "              g = a * b; //utilização de variáveis locais e globais"
        , "        }"
        , "        "
        , "        if(a<b){ //Encadeamento de if e else"
        , "            calculaB();"
        , "            print(\"executou o calcularB!\");"
        , "            "
        , "        }else if(a<c || b!=c){ "
        , "            calculaOperacao(); //chama de funções"
        , "            print(\"executou o calculaOperacao!\");"
        , "            "
        , "        }else if(a==b+c && c!=b/a){"
        , "            calculaB();"
        , "            calculaOperacao();"
        , "            print(\"executou calculaB e calculaOperacao\");"
        , "            "
        , "        }else if(a+b+c>=c){"
        , "            print(\"não executou\");"
        , "            "
        , "        }else{"
        , "            print(\"não existe!\");"
        , "        }"
        , "        "
        , "        do{"
        , "            char resp = 'n';"
        , "            print(\"continuar calculando? s - (sim) / n - (não)\");"
        , "            "
        , "            if(resp!='n')resp = 's'; //estruturas sem chaves     "
        , "        }while(resp!='s');"
        , "    }while(fimOp!=0);"
        , "}"
        , ""
        , "void calculaOperacao(){ //criação de métodos"
        , "    int i;"
        , "    int fib1 = 1;"
        , "    int fib2 = fib1;"
        , "    int soma;"
        , "	"
        , "    for(i = 3; i <= soma; i = i + 1){	"
        , "        soma = fib1 + fib2;"
        , "	fib1 = fib2;"
        , "	fib2 = soma;"
        , "	print(\"fib2\");"
        , "    }"
        , "}"
        , ""
        , "int calculaB(){"
        , "    float a = 10.0;"
        , "    float b = a*2-(a+a*2)*a+4;"
        , "    "
        , "    if(a<10 && a!=0 || a*a!=a+a){"
        , "    	float d;"
        , "    	d = a + b;"
        , "    "
        , "        calculaOperacao();"
        , "    }"
        , "}"
    });

    public static void main(String[] args) {
        launch(args);
    }

    private CodeArea codeArea;
    private ExecutorService executor;

    @Override
    public void start(Stage primaryStage) {
        executor = Executors.newSingleThreadExecutor();
        codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        Subscription cleanupWhenDone = codeArea.multiPlainChanges()
                .successionEnds(Duration.ofMillis(500))
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(codeArea.multiPlainChanges())
                .filterMap(t -> {
                    if (t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                })
                .subscribe(this::applyHighlighting);

        // call when no longer need it: `cleanupWhenFinished.unsubscribe();`
        codeArea.replaceText(0, 0, sampleCode);

        Scene scene = new Scene(new StackPane(new VirtualizedScrollPane<>(codeArea)), 600, 400);
        scene.getStylesheets().add(CKeywordsAsync.class.getResource("java-keywords.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Java Keywords Async Demo");
        primaryStage.show();
    }

    @Override
    public void stop() {
        executor.shutdown();
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = codeArea.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(text);
            }
        };
        executor.execute(task);
        return task;
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        codeArea.setStyleSpans(0, highlighting);
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
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
}
