package br.unicap.compiler.exceptions;

public enum TypeException {
    LEXICAL("EmptyChar"),
    SYNTAX("Syntax"),
    SEMANTIC("Semantic"),
    ;

    private String name;

    private TypeException(String name) {
        this.name = name;
    }
    
    public String getName(){
        return this.name;
    }
}
