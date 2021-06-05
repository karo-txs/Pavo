package br.unicap.compiler.semantic;

import br.unicap.compiler.exceptions.SemanticException;
import br.unicap.compiler.lexicon.Scanner;
import br.unicap.compiler.lexicon.Token;
import br.unicap.compiler.lexicon.TokenType;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class Rules {

    /*  Variáveis:
        - Nao pode repetir variaveis (sem verificar escopo)
        - Não pode atribuir um valor de tipo diferente do declarado int a = "ola";
     */
    private Map<String, LinkedList<Object>> tableVar;
    //'a': [tipo, valor] //, qtd, contexto

    private String exception;
    private Scanner scan;
    private String nameArchive;

    public Rules() {
        tableVar = new LinkedHashMap<>();
    }

    /*
        FUNCIONALIDADES
     */
    public void add(Token token, String[]... values) {
        if (!verificarExiste(token.getToken())) {
            tableVar.put(token.getToken(), new LinkedList(Arrays.asList(token.getType(), values[0])));
        } else {
            throwException("variável já existe!");
        }
    }

    public void remove(Token token) {
        tableVar.remove(token.getToken());
    }

    public void atualizaValor(Token token, String value) {
        if (verificarExiste(token.getToken())) {
            LinkedList<Object> antiga = tableVar.get(token.getToken());
            antiga.set(1, value);
            tableVar.replace(token.getToken(), antiga);
        } else {
            throwException("variável não existe!");
        }
    }

    /*
        VERIFICAÇÕES
     */
    public boolean verificarExiste(String name) {
        return tableVar.containsKey(name);
    }

    private boolean verificaTipo(Token token, String value) {
        TokenType type = token.getType();
        if(type == TokenType.TK_INT && value.matches("[0-9]+")){
            
        }else if(type == TokenType.TK_FLOAT && value.matches("[0-9]+.[0-9]+")){
            
        }else if(type == TokenType.TK_CHAR){
            //errado
        }

        //verifica tipo
        return true;
    }

    /*
        EXEÇÕES
     */
    public String getException() {
        return "NULL";
    }

    private void throwException(String msg) {
        if (exception.equals("NULL")) {
            SemanticException ex = new SemanticException(msg);
            exception = ex.throwException();
        }
    }

}
