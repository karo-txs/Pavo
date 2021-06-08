package br.unicap.compiler.semantic;

import br.unicap.compiler.exceptions.SemanticException;
import br.unicap.compiler.lexicon.Scanner;
import br.unicap.compiler.lexicon.Token;
import br.unicap.compiler.lexicon.TokenType;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Rules {

    private Map<String, Map<String, TokenType>> tableScope;
    private LinkedList<String> tableMethod;
    private LinkedList<String> tableMethodAux;

    private String exception;
    private Scanner scan;
    private String nameArchive;

    public Rules(Scanner scan, String nameArchive) {
        exception = "NULL";
        tableScope = new LinkedHashMap<>();
        tableMethod = new LinkedList<>();
        tableMethodAux = new LinkedList<>();
        this.scan = scan;
        this.nameArchive = nameArchive;
    }

    /*
        FUNCIONALIDADES
     */
    public void assignment(List<Token> escopo, Token token) {
        boolean exists = false;

        for (int i = 0; i < escopo.size(); i++) {
            if (verifyExists(escopo.get(i).getToken(), token.getToken())) {
                exists = true;
            }
        }

        if (!exists) {
            throwException("Variable was not created / Cannot find symbol");
        }
    }

    public void addStatement(Token escopo, Token token, TokenType value) {
        if (!verifyExists(escopo.getToken(), token.getToken())) {
            value = value == TokenType.TK_KEYWORD_INT ? TokenType.TK_INT
                    : value == TokenType.TK_KEYWORD_FLOAT ? TokenType.TK_FLOAT
                            : value == TokenType.TK_KEYWORD_CHAR ? TokenType.TK_CHAR : value;

            tableScope.get(escopo.getToken()).put(token.getToken(), value);
        } else {
            throwException("Variable is already defined!");
        }
    }

    public void accumulateMethod(Token token) {
        if (!methodExists(token)) {
            tableMethod.add(token.getToken());
        } else {
            throwException("Method is already defined!");
        }

    }

    public void addMethod(Token token) {
        if (!methodExists(token)) {
            tableMethodAux.add(token.getToken());
        } else {
            throwException("Method is already defined!");
        }
    }

    public void resetTable() {
        this.tableMethodAux = new LinkedList<>();
    }

    /*
        VERIFICAÇÕES
     */
    public boolean verifyExists(String escopo, String name) {

        if (tableScope.containsKey(escopo)) {
            Map<String, TokenType> tableVar = tableScope.get(escopo);
            return tableVar.containsKey(name);
        } else {
            tableScope.put(escopo, new LinkedHashMap<>());
            return false;
        }
    }

    public boolean methodExists(Token token) {
        return tableMethodAux.contains(token.getToken());
    }

    public void callExists(Token token) {
        if (!tableMethod.contains(token.getToken())) {
            throwException("Method was not created / Cannot find symbol");
        }
    }

    /*
        VERIFICAÇÕES DE TIPOS
     */
    //Compatibilidade de tipos
    public boolean verifyCompatibility(List<Token> scope, Token id, List<Token> list) {
        Map<String, TokenType> tableVar = null;
        for (Token esc : scope) {
            System.out.println("estou aqui");
            if (verifyExists(esc.getToken(), id.getToken())) {
                tableVar = tableScope.get(esc.getToken());
            }
        }
        boolean correctOperation = true;
        TokenType idType = tableVar.get(id.getToken());
        System.out.println("id"+idType);
        boolean isString = idType == TokenType.TK_CHAR_SEQUENCE;
        boolean concatString = false;
        TokenType previous = null;
        Token lastVar = null;

        for (Token var : list) {
            System.out.println(list.size());
            if (var.getType() == TokenType.TK_INT
                    || var.getType() == TokenType.TK_FLOAT
                    || var.getType() == TokenType.TK_CHAR
                    || var.getType() == TokenType.TK_CHAR_SEQUENCE
                    || var.getType() == TokenType.TK_IDENTIFIER) {

                if (var.getType() == TokenType.TK_IDENTIFIER && tableVar.containsKey(var.getToken())) {
                    var.setType(tableVar.get(var.getToken()));
                }
                    System.out.println(idType + "->" + var.getType());
                    if (idType == TokenType.TK_INT && var.getType() != TokenType.TK_INT
                            && var.getType() != TokenType.TK_CHAR) { // int recebe int e char
                        correctOperation = false;
                        lastVar = var;
                        break;

                    } else if (idType == TokenType.TK_CHAR && var.getType() != idType) { //char so recebe char
                        correctOperation = false;
                        lastVar = var;
                        break;

                    } else if (idType == TokenType.TK_FLOAT && var.getType() != idType //float recebe float, int e char
                            && var.getType() != TokenType.TK_INT && var.getType() != TokenType.TK_CHAR) {
                        correctOperation = false;
                        lastVar = var;
                        break;

                    } else if (previous == TokenType.TK_ARITHMETIC_OPERATOR_PLUS
                            && isString && var.getType() == idType) {//string recebe tudo, se tiver somado a uma string
                        concatString = true;
                    }

                    previous = var.getType();
                
            }
        }
        if (!correctOperation && lastVar!=null && idType!=null) {
            throwException("Expected type " + idType.getText() + ", found " + lastVar.getType().getText());
        }

        return isString ? concatString : correctOperation;
    }

    /*
        EXEÇÕES
     */
    public String getException() {
        return this.exception;
    }

    private void throwException(String msg) {
        if (exception.equals("NULL")) {
            SemanticException ex = new SemanticException(msg, scan.getCursor(), nameArchive);
            exception = ex.throwException();
        }
    }

}
