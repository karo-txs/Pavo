package br.unicap.compiler.lexicon;
import br.unicap.compiler.view.util.Color;

public enum TokenType{
    TK_IDENTIFIER("Identifier",Color.BROWN), 
    TK_FLOAT("Float",Color.BLUE),
    TK_INT("Integer",Color.GREEN),
    TK_KEYWORD("KeyWord",Color.PINK),
    TK_CHAR_SEQUENCE("Char_Sequence",Color.ORANGE),
    TK_CHAR("Char",Color.YELLOW),
    TK_PUNCTUATION("Punctuation",Color.BEIGE),
    
    TK_ARITHMETIC_OPERATOR_PLUS("Arithmetic_Operator_Plus",Color.RED),
    TK_ARITHMETIC_OPERATOR_MINUS("Arithmetic_Operator_Minus",Color.RED),
    TK_ARITHMETIC_OPERATOR_POWER("Arithmetic_Operator_Power",Color.RED),
    TK_ARITHMETIC_OPERATOR_MULTIPLICATION("Arithmetic_Operator_Multiplication",Color.RED),
    TK_ARITHMETIC_OPERATOR_DIVISION("Arithmetic_Operator_Division",Color.RED),
    TK_ARITHMETIC_OPERATOR_ASSIGN("Arithmetic_Operator_Assign",Color.RED),
    
    TK_RELATIONAL_OPERATOR_EQUAL("Relational_Operator_Equal",Color.PURPLE),
    TK_RELATIONAL_OPERATOR_MORE("Relational_Operator_More",Color.PURPLE),
    TK_RELATIONAL_OPERATOR_MORE_EQUAL("Relational_Operator_More_Equal",Color.PURPLE),
    TK_RELATIONAL_OPERATOR_LESS("Relational_Operator_Less",Color.PURPLE),
    TK_RELATIONAL_OPERATOR_LESS_EQUAL("Relational_Operator_Less_Equal",Color.PURPLE),
    TK_RELATIONAL_OPERATOR_NOT("Relational_Operator_Not",Color.PURPLE),
    TK_RELATIONAL_OPERATOR_NOT_EQUAL("Relational_Operator_Not_Equal",Color.PURPLE),
    
    TK_SPECIAL_CHARACTER_PARENTHESES("Special_Character_Parentheses",Color.GRAY),
    TK_SPECIAL_CHARACTER_BRACKETS("Special_Character_Brackets",Color.GRAY),
    TK_SPECIAL_CHARACTER_BRACES("Special_Character_Braces",Color.GRAY),
    TK_SPECIAL_CHARACTER_COMMA("Special_Character_Comma",Color.GRAY),
    TK_SPECIAL_CHARACTER_SEMICOLON("Special_Character_Semicolon",Color.GRAY),
    TK_SPECIAL_CHARACTER_TWO_POINTS("Special_Character_Two_Points",Color.GRAY),
    TK_SPECIAL_CHARACTER_HASH("Special_Character_Hash",Color.GRAY),
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
                return TokenType.TK_ARITHMETIC_OPERATOR_PLUS;
            case "-":
                return TokenType.TK_ARITHMETIC_OPERATOR_MINUS;
            case "^":
                return TokenType.TK_ARITHMETIC_OPERATOR_POWER;
            case "*":
                return TokenType.TK_ARITHMETIC_OPERATOR_MULTIPLICATION;
            case "/":
                return TokenType.TK_ARITHMETIC_OPERATOR_DIVISION;
            case "=":
                return TokenType.TK_ARITHMETIC_OPERATOR_ASSIGN;
                
            case "==":
                return TokenType.TK_RELATIONAL_OPERATOR_EQUAL;
            case ">":
                return TokenType.TK_RELATIONAL_OPERATOR_MORE;
            case ">=":
                return TokenType.TK_RELATIONAL_OPERATOR_MORE_EQUAL;
            case "<":
                return TokenType.TK_RELATIONAL_OPERATOR_LESS;
            case "<=":
                return TokenType.TK_RELATIONAL_OPERATOR_LESS_EQUAL;
            case "!":
                return TokenType.TK_RELATIONAL_OPERATOR_NOT;
            case "!=":
                return TokenType.TK_RELATIONAL_OPERATOR_NOT_EQUAL;
                
            case "(":
                return TokenType.TK_SPECIAL_CHARACTER_PARENTHESES; 
            case ")":
                return TokenType.TK_SPECIAL_CHARACTER_PARENTHESES; 
            case "{":
                return TokenType.TK_SPECIAL_CHARACTER_BRACES;  
            case "}":
                return TokenType.TK_SPECIAL_CHARACTER_BRACES; 
            case "[":
                return TokenType.TK_SPECIAL_CHARACTER_BRACKETS;  
            case "]":
                return TokenType.TK_SPECIAL_CHARACTER_BRACKETS;
            case ",":
                return TokenType.TK_SPECIAL_CHARACTER_COMMA; 
            case ";":
                return TokenType.TK_SPECIAL_CHARACTER_SEMICOLON;  
            case ":":
                return TokenType.TK_SPECIAL_CHARACTER_TWO_POINTS;  
            case "#":
                return TokenType.TK_SPECIAL_CHARACTER_HASH;  
            
            default:
                return null;
        }
    }
}