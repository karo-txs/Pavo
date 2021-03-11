package br.unicap.compiler.lexicon;

public enum TokenType{
    TK_IDENTIFIER("Identifier", 0), 
    TK_FLOAT("Var_Float", 1),
    TK_INT("Var_Int", 2),
    TK_OPERATOR_RELATIONAL("Operator_Relational", 3),
    TK_OPERATOR_ARITHMETIC("Operator_Arithmetic", 4),
    TK_CHARACTER_SPECIAL("Character_Special", 6),
    TK_KEYWORD("Key_Word",7),
    TK_STRING("String",8),
    TK_CHAR("Char",9),
    TK_PUNCTUATION("Punctuation",10)
    ;

    private String text;
    private int id;

    private TokenType(String text, int id){
        this.text=text;
        this.id =id;
    }

    public String getText(){
        return this.text;
    }
    
    public int getId(){
        return this.id;
    }
}