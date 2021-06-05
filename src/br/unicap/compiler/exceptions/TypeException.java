package br.unicap.compiler.exceptions;

public enum TypeException {
    INVALID_SYMBOL("InvalidSymbol"),
    INVALID_OPERATOR("InvalidOperator"),
    NUMBER_FORMAT("NumberFormat"),
    IDENTIFIER_FORMAT("IdentifierFormat"),
    UNCLOSED("Unclosed"),
    EMPTY_CHAR("EmptyChar"),
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
