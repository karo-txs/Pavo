package br.unicap.compiler.lexicon;
import br.unicap.compiler.util.Color;

public enum TokenType{
    TK_IDENTIFIER("Identifier",Color.BROWN), 
    TK_FLOAT("Var_Float",Color.BLUE),
    TK_INT("Var_Int",Color.GREEN),
    TK_KEYWORD("Key_Word",Color.PINK),
    TK_STRING("String",Color.ORANGE),
    TK_CHAR("Char",Color.YELLOW),
    TK_PUNCTUATION("Punctuation",Color.BEIGE),
    
    TK_OPERATOR_ARITHMETIC_PLUS("Operator_Arithmetic_Plus",Color.RED),
    TK_OPERATOR_ARITHMETIC_MINUS("Operator_Arithmetic_Minus",Color.RED),
    TK_OPERATOR_ARITHMETIC_MULTIPLICATION("Operator_Arithmetic_Multiplication",Color.RED),
    TK_OPERATOR_ARITHMETIC_DIVISION("Operator_Arithmetic_Division",Color.RED),
    TK_OPERATOR_ARITHMETIC_ASSIGN("Operator_Arithmetic_Assign",Color.RED),
    
    TK_OPERATOR_RELATIONAL_EQUAL("Operator_Relational_Equal",Color.PURPLE),
    TK_OPERATOR_RELATIONAL_MORE("Operator_Relational_More",Color.PURPLE),
    TK_OPERATOR_RELATIONAL_MORE_EQUAL("Operator_Relational_More_Equal",Color.PURPLE),
    TK_OPERATOR_RELATIONAL_LESS("Operator_Relational_Less",Color.PURPLE),
    TK_OPERATOR_RELATIONAL_LESS_EQUAL("Operator_Relational_Less_Equal",Color.PURPLE),
    TK_OPERATOR_RELATIONAL_NOT("Operator_Relational_Not",Color.PURPLE),
    TK_OPERATOR_RELATIONAL_NOT_EQUAL("Operator_Relational_Not_Equal",Color.PURPLE),
    
    TK_CHARACTER_SPECIAL_PARENTHESES("Character_Special_Parentheses",Color.GRAY),
    TK_CHARACTER_SPECIAL_BRACKETS("Character_Special_Brackets",Color.GRAY),
    TK_CHARACTER_SPECIAL_BRACES("Character_Special_Braces",Color.GRAY),
    TK_CHARACTER_SPECIAL_COMMA("Character_Special_Comma",Color.GRAY),
    TK_CHARACTER_SPECIAL_SEMICOLON("Character_Special_Semicolon",Color.GRAY),
    TK_CHARACTER_SPECIAL_TWO_POINTS("Character_Special_Two_Points",Color.GRAY),
    TK_CHARACTER_SPECIAL_HASH("Character_Special_Hash",Color.GRAY),
    ;

    private String text;
    private Color color;

    private TokenType(String text, Color color){
        this.text=text;
        this.color = color;
    }

    public String getText(){
        return this.text;
    }
    
    public Color getColor(){
        return this.color;
    }
    
    public static TokenType returnSubtype(String term){
        switch (term) {
            case "+":
                return TokenType.TK_OPERATOR_ARITHMETIC_PLUS;
            case "-":
                return TokenType.TK_OPERATOR_ARITHMETIC_MINUS;
            case "*":
                return TokenType.TK_OPERATOR_ARITHMETIC_MULTIPLICATION;
            case "/":
                return TokenType.TK_OPERATOR_ARITHMETIC_DIVISION;
            case "=":
                return TokenType.TK_OPERATOR_ARITHMETIC_ASSIGN;
                
                
            case "==":
                return TokenType.TK_OPERATOR_RELATIONAL_EQUAL;
            case ">":
                return TokenType.TK_OPERATOR_RELATIONAL_MORE;
            case ">=":
                return TokenType.TK_OPERATOR_RELATIONAL_MORE_EQUAL;
            case "<":
                return TokenType.TK_OPERATOR_RELATIONAL_LESS;
            case "<=":
                return TokenType.TK_OPERATOR_RELATIONAL_LESS_EQUAL;
            case "!":
                return TokenType.TK_OPERATOR_RELATIONAL_NOT;
            case "!=":
                return TokenType.TK_OPERATOR_RELATIONAL_NOT_EQUAL;
                
                
            case "(":
                return TokenType.TK_CHARACTER_SPECIAL_PARENTHESES; 
            case ")":
                return TokenType.TK_CHARACTER_SPECIAL_PARENTHESES; 
            case "{":
                return TokenType.TK_CHARACTER_SPECIAL_BRACES;  
            case "}":
                return TokenType.TK_CHARACTER_SPECIAL_BRACES; 
            case "[":
                return TokenType.TK_CHARACTER_SPECIAL_BRACKETS;  
            case "]":
                return TokenType.TK_CHARACTER_SPECIAL_BRACKETS;
            case ",":
                return TokenType.TK_CHARACTER_SPECIAL_COMMA; 
            case ";":
                return TokenType.TK_CHARACTER_SPECIAL_SEMICOLON;  
            case ":":
                return TokenType.TK_CHARACTER_SPECIAL_TWO_POINTS;  
            case "#":
                return TokenType.TK_CHARACTER_SPECIAL_HASH;  
            
            default:
                return null;
        }
    }
}