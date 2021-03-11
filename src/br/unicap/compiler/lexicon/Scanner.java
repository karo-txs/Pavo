package br.com.compiler.lexico;

import br.com.compiler.exceptions.EmptyCharException;
import br.com.compiler.exceptions.NumberFormatException;
import br.com.compiler.exceptions.IdentifierFormatException;
import br.com.compiler.exceptions.InvalidOperatorException;
import br.com.compiler.exceptions.InvalidSymbolException;
import br.com.compiler.exceptions.PersonalizedException;
import br.com.compiler.exceptions.TypeException;
import br.com.compiler.exceptions.UnclosedException;

import br.com.compiler.util.Cursor;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Scanner {

    private char[] content;
    private int state;
    private int pos;
    private static Cursor cs;

    public Scanner(String filename) {
        cs = new Cursor();
        pos = 0;
        try {
            Path pathToFile = Paths.get(filename);
            String txtConteudo = new String(Files.readAllBytes(pathToFile), StandardCharsets.UTF_8);
            content = txtConteudo.toCharArray();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Token nextToken() {

        char currentChar = 0;
        state = 0;
        int antColCursor = 0;
        String term = "";
        Token token;
        boolean pause = false;

        if (isEOF()) {
            return null;
        }

        while (true) {
            if (!pause) {
                currentChar = nextChar();
                cs.moveCursorFront(currentChar);
                antColCursor = cs.getColun() - 1;
            }

            switch (state) {
                /*
                  ESTADO INICIAL
                 */
                case 0:
                    if (Rules.isSpace(currentChar)) {
                        state = 0;
                        if (isEOF()) {
                            return null;
                        }
                    } else if (Rules.isChar(currentChar) || Rules.isUnderline(currentChar)) {
                        term += currentChar;
                        state = 1;
                        if (isEOF()) {
                            pause = true;
                            state = 2;
                        }
                    } else if (Rules.isDigit(currentChar)) {
                        term += currentChar;
                        state = 3;
                        if (isEOF()) {
                            pause = true;
                            state = 7;
                        }
                    } else if (Rules.isRelationalOperator(currentChar)) {
                        term += currentChar;
                        state = 12;
                        if (isEOF()) {
                            return new Token(TokenType.TK_OPERATOR_RELATIONAL, term);
                        }
                    } else if (Rules.isArithmeticOperator(currentChar)) {
                        term += currentChar;
                        state = 13;
                        if (isEOF()) {
                            pause = true;
                        } else {
                            back();
                            cs.moveCursorBack(currentChar, antColCursor);
                        }
                    } else if (Rules.isEqual(currentChar)) {
                        term += currentChar;
                        state = 14;
                        if (isEOF()) {
                            pause = true;
                            state = 15;
                        }
                    } else if (Rules.isSpecialCharacter(currentChar)) {
                        term += currentChar;
                        return new Token(TokenType.TK_CHARACTER_SPECIAL, term);
                    } else if (Rules.isPunctuation(currentChar)) {
                        term += currentChar;
                        state = 11;
                        if (isEOF()) {
                            throwException(TypeException.INVALID_SYMBOL,term);
                        }
                    } else if (Rules.isBar(currentChar)) {
                        state = 16;
                        if (isEOF()) {
                            term += currentChar;
                            pause = true;
                            state = 13;
                        }
                    } else if (Rules.isDoubleQuotes(currentChar)){
                        term += currentChar;
                        state = 18;
                        if(isEOF()){
                            throwException(TypeException.UNCLOSED,"String Literal: "+term);
                        }
                    } else if(Rules.isSingleQuotes(currentChar)){
                        term += currentChar;
                        state = 19;
                        if(isEOF()){
                            throwException(TypeException.UNCLOSED,"Character Literal: "+term);
                        }
                    } else if(Rules.isPunctuation(currentChar)){
                        term += currentChar;
                        return new Token(TokenType.TK_PUNCTUATION,term);
                    }
                    else {
                        term += currentChar;
                        throwException(TypeException.INVALID_SYMBOL, term);
                    }
                    break;
                /*
                  IDENTIFICADOR 
                 */
                case 1:
                    if (Rules.isChar(currentChar) || Rules.isDigit(currentChar) || Rules.isUnderline(currentChar)) {
                        term += currentChar;
                        state = 1;
                        if (isEOF()) {
                            pause = true;
                            state = 2;
                        }
                    } else {
                        if (Rules.isUnrecognizableSymbol(currentChar)) {
                            term += currentChar;
                            throwException(TypeException.IDENTIFIER_FORMAT, term);
                        } else {
                            pause = true;
                            back();
                            cs.moveCursorBack(currentChar, antColCursor);
                            state = 2;
                        }
                    }
                    break;
                case 2:
                    if (Rules.isReserved(term)) {
                        token = new Token(TokenType.TK_KEYWORD, term);
                    } else {
                        token = new Token(TokenType.TK_IDENTIFIER, term);
                    }
                    return token;
                /*
                  DIGITOS
                 */
                case 3:
                    if (Rules.isDigit(currentChar)) {
                        term += currentChar;
                        state = 3;
                        if (isEOF()) {
                            pause = true;
                            state = 7;
                        }
                    } else if (Rules.isPunctuation(currentChar)) {
                        term += currentChar;
                        state = 5;
                        if (isEOF()) {
                            term += currentChar;
                            throwException(TypeException.NUMBER_FORMAT, "Float Number :"+term);
                        }
                    } else if (!Rules.isChar(currentChar)) {
                        pause = true;
                        state = 7;
                    } else {
                        term += currentChar;
                        state = 4;
                        if (isEOF()) {
                            pause = true;
                        }
                    }
                    break;
                case 4:
                    if (!Rules.isChar(currentChar)) {
                        throwException(TypeException.NUMBER_FORMAT, "Integer Number : "+term);
                    } else if (isEOF()) {
                        if (Rules.isChar(currentChar)) {
                            term += currentChar;
                        }
                        throwException(TypeException.NUMBER_FORMAT, "Integer Number : "+term);
                    } else {
                        term += currentChar;
                        cs.moveCursorBack(currentChar, antColCursor);
                        state = 4;
                    }
                    break;
                case 5:
                    if (Rules.isDigit(currentChar)) {
                        term += currentChar;
                        state = 8;
                        if (isEOF()) {
                            pause = true;
                            state = 10;
                        }
                    } else if (Rules.isChar(currentChar)) {
                        term += currentChar;
                        state = 6;
                        if (isEOF()) {
                            throwException(TypeException.NUMBER_FORMAT, "Float Number : "+term);
                        }
                    } else {
                        pause = true;
                        state = 7;
                    }
                    break;
                case 6:
                    if (Rules.isChar(currentChar)) {
                        term += currentChar;
                        state = 6;
                        if (isEOF()) {
                            throwException(TypeException.NUMBER_FORMAT, "Float Number : "+term);
                        }
                    } else {
                        throwException(TypeException.NUMBER_FORMAT, "Float Number : "+term);
                    }
                    break;
                case 7:
                    if (!isEOF()) {
                        back();
                        cs.moveCursorBack(currentChar, antColCursor);
                    }
                    token = new Token(TokenType.TK_INT, term);
                    return token;
                case 8:
                    if (Rules.isDigit(currentChar)) {
                        term += currentChar;
                        state = 8;
                        if (isEOF()) {
                            pause = true;
                            state = 10;
                        }
                    } else if (!Rules.isChar(currentChar)) {
                        back();
                        cs.moveCursorBack(currentChar, antColCursor);
                        pause = true;
                        state = 10;
                    } else {
                        if (isEOF()) {
                            pause = true;
                        }
                        term += currentChar;
                        state = 9;
                    }
                    break;
                case 9:
                    if (isEOF() && Rules.isChar(currentChar)) {
                        term += currentChar;
                    }
                    if (!Rules.isChar(currentChar) || isEOF()) {
                        throwException(TypeException.NUMBER_FORMAT, "Float Number : "+term);
                    } else {
                        term += currentChar;
                        cs.moveCursorBack(currentChar, antColCursor);
                        state = 9;
                    }
                    break;
                case 10:
                    return new Token(TokenType.TK_FLOAT, term);
                case 11:
                    if (!Rules.isChar(currentChar) && !Rules.isDigit(currentChar)) {
                        throwException(TypeException.NUMBER_FORMAT, "Float Number : "+term);
                    } else if (isEOF()) {
                        if (Rules.isChar(currentChar) || Rules.isDigit(currentChar)) {
                            term += currentChar;
                        }
                        throwException(TypeException.NUMBER_FORMAT, "Float Number : "+term);
                    } else {
                        term += currentChar;
                        cs.moveCursorBack(currentChar, antColCursor);
                        state = 11;
                    }
                    break;
                /*
                  OPERADORES RELACIONAIS
                 */
                case 12:
                    if (Rules.isEqual(currentChar)) {
                        term += currentChar;
                    } else {
                        back();
                        cs.moveCursorBack(currentChar, antColCursor);
                    }
                    return new Token(TokenType.TK_OPERATOR_RELATIONAL, term);
                /*
                  OPERADORES ARITMETICOS
                 */
                case 13:
                    return  new Token(TokenType.TK_OPERATOR_ARITHMETIC, term);
                /*
                  IGUAL COMO OPERADOR RELACIONAL OU ARITMETICO
                 */
                case 14:
                    if (Rules.isEqual(currentChar)) {
                        term += currentChar;
                        state = 15;
                        if (isEOF()) {
                            return new Token(TokenType.TK_OPERATOR_RELATIONAL, term);
                        }
                    } else {
                        pause = true;
                        state = 13;
                    }
                    break;
                case 15:
                    if (!Rules.isEqual(currentChar)) {
                        back();
                        cs.moveCursorBack(currentChar, antColCursor);
                        return new Token(TokenType.TK_OPERATOR_RELATIONAL, term);
                    } else if (Rules.isEqual(currentChar) && isEOF()) {
                        term += currentChar;
                        throwException(TypeException.INVALID_OPERATOR, term);
                    }
                    break;
                /*
                  CONSUMIR COMENTARIOS  // exemplo de comentario consumido 
                */
                case 16:
                    if (Rules.isBar(currentChar)) {
                        state = 17;
                    } else {
                        term += '/';
                        back();
                        cs.moveCursorBack(currentChar, antColCursor);
                        return new Token(TokenType.TK_OPERATOR_ARITHMETIC, term);
                    }
                    break;
                case 17:
                    if (currentChar == '\n') {
                        state = 0;
                    } else {
                        state = 17;
                        if(isEOF()){
                            return null;
                        }
                    }
                    break;
                /*
                  STRING
                */
                case 18:
                    term += currentChar;
                    if(Rules.isDoubleQuotes(currentChar)){
                        return new Token(TokenType.TK_STRING, term);
                    }else if(isEOF()){
                        throwException(TypeException.UNCLOSED, "String Literal: "+term);
                    }else{
                        state = 18;
                    }
                    break;
                /*
                  CHAR
                */
                case 19:
                    term += currentChar;
                    if(Rules.isSingleQuotes(currentChar)){
                        throwException(TypeException.EMPTY_CHAR, term);
                    }else if(isEOF()){
                         throwException(TypeException.UNCLOSED, "Character Literal: "+term);
                    }else{
                        state = 20;
                    }
                    break;
                case 20:
                    term += currentChar;
                    if(Rules.isSingleQuotes(currentChar)){
                        return new Token(TokenType.TK_CHAR,term);
                    }else{
                        throwException(TypeException.UNCLOSED, "Character Literal: "+term);
                    }
                    break;
            }
        }
    }

    private char nextChar() {
        return content[pos++];
    }

    private boolean isEOF() {
        return pos == content.length;
    }

    private void back() {
        pos--;
    }
    
    private void throwException(TypeException type,String msg){
        PersonalizedException ex = null;
        
        switch(type){
            case NUMBER_FORMAT:
                ex = new NumberFormatException(msg, cs);
                break;
            case IDENTIFIER_FORMAT:
                ex = new IdentifierFormatException(msg, cs);
                break;
            case EMPTY_CHAR:
                ex = new EmptyCharException(msg, cs);
                break;
            case INVALID_OPERATOR:
                ex = new InvalidOperatorException(msg, cs);
                break;
            case INVALID_SYMBOL:
                ex = new InvalidSymbolException(msg, cs);
                break;
            case UNCLOSED:
                ex = new UnclosedException(msg, cs);
                break;
        }
        ex.throwException();
    } 
}
