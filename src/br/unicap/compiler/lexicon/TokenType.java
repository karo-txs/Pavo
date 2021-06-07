package br.unicap.compiler.lexicon;

import br.unicap.compiler.view.FXMLMainScreenController;
import br.unicap.compiler.view.util.Color;

public enum TokenType {
    TK_IDENTIFIER("Identifier", Color.BROWN),
    TK_FLOAT("Float", Color.BLUE),
    TK_INT("Integer", Color.GREEN),
    TK_CHAR_SEQUENCE("Char_Sequence", Color.ORANGE),
    TK_CHAR("Char", Color.YELLOW),
    TK_ARITHMETIC_OPERATOR_PLUS("Arithmetic_Operator_Plus", Color.RED),
    TK_ARITHMETIC_OPERATOR_MINUS("Arithmetic_Operator_Minus", Color.RED),
    TK_ARITHMETIC_OPERATOR_POWER("Arithmetic_Operator_Power", Color.RED),
    TK_ARITHMETIC_OPERATOR_MULTIPLICATION("Arithmetic_Operator_Multiplication", Color.RED),
    TK_ARITHMETIC_OPERATOR_DIVISION("Arithmetic_Operator_Division", Color.RED),
    TK_ARITHMETIC_OPERATOR_ASSIGN("Arithmetic_Operator_Assign", Color.RED),
    TK_RELATIONAL_OPERATOR_EQUAL("Relational_Operator_Equal", Color.PURPLE),
    TK_RELATIONAL_OPERATOR_MORE("Relational_Operator_More", Color.PURPLE),
    TK_RELATIONAL_OPERATOR_MORE_EQUAL("Relational_Operator_More_Equal", Color.PURPLE),
    TK_RELATIONAL_OPERATOR_LESS("Relational_Operator_Less", Color.PURPLE),
    TK_RELATIONAL_OPERATOR_LESS_EQUAL("Relational_Operator_Less_Equal", Color.PURPLE),
    TK_RELATIONAL_OPERATOR_NOT("Relational_Operator_Not", Color.PURPLE),
    TK_RELATIONAL_OPERATOR_NOT_EQUAL("Relational_Operator_Not_Equal", Color.PURPLE),
    TK_SPECIAL_CHARACTER_PARENTHESES_OPEN("Special_Character_Parentheses_Open", Color.GRAY),
    TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED("Special_Character_Parentheses_Closed", Color.GRAY),
    TK_SPECIAL_CHARACTER_BRACKETS_OPEN("Special_Character_Brackets_Open", Color.GRAY),
    TK_SPECIAL_CHARACTER_BRACKETS_CLOSED("Special_Character_Brackets_Closed", Color.GRAY),
    TK_SPECIAL_CHARACTER_BRACES_OPEN("Special_Character_Braces_Open", Color.GRAY),
    TK_SPECIAL_CHARACTER_BRACES_CLOSED("Special_Character_Braces_Closed", Color.GRAY),
    TK_SPECIAL_CHARACTER_COMMA("Special_Character_Comma", Color.GRAY),
    TK_SPECIAL_CHARACTER_SEMICOLON("Special_Character_Semicolon", Color.GRAY),
    TK_SPECIAL_CHARACTER_TWO_POINTS("Special_Character_Two_Points", Color.GRAY),
    TK_SPECIAL_CHARACTER_HASH("Special_Character_Hash", Color.GRAY),
    TK_SPECIAL_CHARACTER_ADDRESS("Special_Character_Address", Color.GRAY),
    TK_LOGIC_AND("Logic_And", Color.GRAY),
    TK_LOGIC_OR("Logic_Or", Color.GRAY),
    TK_KEYWORD("KeyWord", Color.PINK),
    TK_KEYWORD_MAIN("Main", Color.PINK),
    TK_KEYWORD_VOID("Void", Color.PINK),
    TK_KEYWORD_IF("If", Color.PINK),
    TK_KEYWORD_ELSE("Else", Color.PINK),
    TK_KEYWORD_PRINT("Print", Color.PINK),
    TK_KEYWORD_WHILE("While", Color.PINK),
    TK_KEYWORD_DO_WHILE("Do_While", Color.PINK),
    TK_KEYWORD_FOR("For", Color.PINK),
    TK_KEYWORD_INT("Int", Color.PINK),
    TK_KEYWORD_FLOAT("float", Color.PINK),
    TK_KEYWORD_CHAR("char", Color.PINK),;

    private String text;
    private Color color;

    private TokenType(String text, Color color) {
        this.text = text;
        this.color = color;
    }

    public String getText() {
        return this.text;
    }

    public String getColor() {
        if (FXMLMainScreenController.isDark) {
            return this.color.getColorInDarkMode();
        } else {
            return this.color.getColorInLightMode();
        }
    }

    public static TokenType returnSubtype(String term) {
        switch (term) {
            // Arithmetic
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

            // Relacional
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

            // Special Character
            case "(":
                return TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN;
            case ")":
                return TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED;
            case "{":
                return TokenType.TK_SPECIAL_CHARACTER_BRACES_OPEN;
            case "}":
                return TokenType.TK_SPECIAL_CHARACTER_BRACES_CLOSED;
            case "[":
                return TokenType.TK_SPECIAL_CHARACTER_BRACKETS_OPEN;
            case "]":
                return TokenType.TK_SPECIAL_CHARACTER_BRACKETS_CLOSED;
            case ",":
                return TokenType.TK_SPECIAL_CHARACTER_COMMA;
            /*case ";":
                return TokenType.TK_SPECIAL_CHARACTER_SEMICOLON;*/
            case ":":
                return TokenType.TK_SPECIAL_CHARACTER_TWO_POINTS;
            case "#":
                return TokenType.TK_SPECIAL_CHARACTER_HASH;
            case "&":
                return TokenType.TK_SPECIAL_CHARACTER_ADDRESS;

            // Keyword
            case "if":
                return TokenType.TK_KEYWORD_IF;
            case "else":
                return TokenType.TK_KEYWORD_ELSE;
            case "main":
                return TokenType.TK_KEYWORD_MAIN;
            case "void":
                return TokenType.TK_KEYWORD_VOID;
            case "print":
                return TokenType.TK_KEYWORD_PRINT;
            case "while":
                return TokenType.TK_KEYWORD_WHILE;
            case "do":
                return TokenType.TK_KEYWORD_DO_WHILE;
            case "for":
                return TokenType.TK_KEYWORD_FOR;
            case "int":
                return TokenType.TK_KEYWORD_INT;
            case "float":
                return TokenType.TK_KEYWORD_FLOAT;
            case "char":
                return TokenType.TK_KEYWORD_CHAR;
            default:
                return null;
        }
    }

    public static TokenType returnType(String term) {
        char[] termChar = term.toCharArray();
        if (term.matches("[0-9]+")) {
            return TK_INT;
        } else if (term.matches("[0-9]+.[0-9]+")) {
            return TK_FLOAT;
        } else if (termChar[0] == '\'' && termChar[2] == '\'') {
            return TK_CHAR;
        } else if (termChar[0] == '\"' && termChar[termChar.length - 1] == '\"') {
            return TK_CHAR_SEQUENCE;
        } else if (returnSubtype(term) != null) {
            return returnSubtype(term);
        } else if (Character.isLetter(termChar[0])) {
            return TK_IDENTIFIER;
        } else if (termChar.length == 2 && termChar[0] == '&' && termChar[1] == '&') {
            return TK_LOGIC_AND;
        } else if (termChar.length == 2 && termChar[0] == '|' && termChar[1] == '|') {
            return TK_LOGIC_OR;
        } else {
            return null;
        }
    }
}
