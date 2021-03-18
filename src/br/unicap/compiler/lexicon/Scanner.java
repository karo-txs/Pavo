package br.unicap.compiler.lexicon;

import br.unicap.compiler.exceptions.EmptyCharException;
import br.unicap.compiler.exceptions.NumberFormatException;
import br.unicap.compiler.exceptions.IdentifierFormatException;
import br.unicap.compiler.exceptions.InvalidOperatorException;
import br.unicap.compiler.exceptions.InvalidSymbolException;
import br.unicap.compiler.exceptions.PersonalizedException;
import br.unicap.compiler.exceptions.TypeException;
import br.unicap.compiler.exceptions.UnclosedException;
import br.unicap.compiler.util.Cursor;

public class Scanner {

    private char[] content;
    private int state;
    private int pos;
    private static Cursor cs;
    private String exception;
    private String nameArchive;

    public Scanner(String filename, String nameArchive) {
        cs = new Cursor();
        pos = 0;
        content = filename.toCharArray();
        exception = "NULL";
        this.nameArchive = nameArchive;
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
                antColCursor = cs.getColun();
                cs.moveCursorFront(currentChar);
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
                            return new Token(TokenType.returnSubtype(term), term);
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
                            state = 13;
                        }
                    } else if (Rules.isSpecialCharacter(currentChar)) {
                        term += currentChar;
                        return new Token(TokenType.returnSubtype(term), term);
                    } else if (Rules.isPunctuation(currentChar)) {
                        term += currentChar;
                        state = 11;
                        if (isEOF()) {
                            exception = throwException(TypeException.INVALID_SYMBOL, term);
                            return null;
                        }
                    } else if (Rules.isBar(currentChar)) {
                        state = 16;
                        if (isEOF()) {
                            term += currentChar;
                            pause = true;
                            state = 13;
                        }
                    } else if (Rules.isDoubleQuotes(currentChar)) {
                        term += currentChar;
                        state = 18;
                        if (isEOF()) {
                            exception = throwException(TypeException.UNCLOSED, "Char Sequence: " + term);
                            return null;
                        }
                    } else if (Rules.isSingleQuotes(currentChar)) {
                        term += currentChar;
                        state = 19;
                        if (isEOF()) {
                            exception = throwException(TypeException.UNCLOSED, "Char: " + term);
                            return null;
                        }
                    } else {
                        term += currentChar;
                        exception = throwException(TypeException.INVALID_SYMBOL, term);
                        return null;
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
                            exception = throwException(TypeException.IDENTIFIER_FORMAT, term);
                            return null;
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
                            exception = throwException(TypeException.NUMBER_FORMAT, "Float Number :" + term);
                            return null;
                        }
                    } else if (!Rules.isChar(currentChar)) {
                        pause = true;
                        state = 7;
                        if (isEOF() && Rules.isUnrecognizableSymbol(currentChar)) {
                            term = "" + currentChar;
                            exception = throwException(TypeException.INVALID_SYMBOL, term);
                            return null;
                        }
                    } else {
                        term += currentChar;
                        state = 4;
                        if (isEOF()) {
                            pause = true;
                        }
                    }
                    break;
                case 4:
                    if (pause == false) {
                        cs.moveCursorBack(currentChar, antColCursor);
                    }
                    if (isEOF()) {
                        if (Rules.isChar(currentChar) && pause == false) {
                            term += currentChar;
                        }
                        exception = throwException(TypeException.NUMBER_FORMAT, "Integer Number : " + term);
                        return null;
                    } else if (!Rules.isChar(currentChar)) {
                        exception = throwException(TypeException.NUMBER_FORMAT, "Integer Number : " + term);
                        return null;
                    } else {
                        term += currentChar;
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
                            exception = throwException(TypeException.NUMBER_FORMAT, "Float Number : " + term);
                            return null;
                        }
                    } else {
                        cs.moveCursorBack(currentChar, antColCursor);
                        exception = throwException(TypeException.NUMBER_FORMAT, "Float Number : " + term);
                        return null;
                        /*
                        pause = true;
                        state = 7;
                         */
                    }
                    break;
                case 6:
                    if (Rules.isChar(currentChar)) {
                        term += currentChar;
                        state = 6;
                        if (isEOF()) {
                            exception = throwException(TypeException.NUMBER_FORMAT, "Float Number : " + term);
                            return null;
                        }
                    } else {
                        cs.moveCursorBack(currentChar, antColCursor);
                        exception = throwException(TypeException.NUMBER_FORMAT, "Float Number : " + term);
                        return null;
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
                        } else {
                            term += currentChar;
                        }
                        state = 9;
                    }
                    break;
                case 9:
                    if (pause == false) {
                        cs.moveCursorBack(currentChar, antColCursor);
                    }
                    if (isEOF() && Rules.isChar(currentChar)) {
                        term += currentChar;
                    }
                    if (!Rules.isChar(currentChar) || isEOF()) {
                        exception = throwException(TypeException.NUMBER_FORMAT, "Float Number : " + term);
                        return null;
                    } else {
                        term += currentChar;
                        state = 9;
                    }
                    break;
                case 10:
                    return new Token(TokenType.TK_FLOAT, term);
                /*
                  PONTUAÇÃO
                 */
                case 11:
                    if (Rules.isJumpLine(currentChar)) {
                        cs.moveCursorBack(currentChar, antColCursor);
                        pause = true;
                        state = 21;
                    } else if (Rules.isChar(currentChar)) {
                        term += currentChar;
                        state = 22;
                        if (isEOF()) {
                            pause = true;
                        }
                    } else if (Rules.isDigit(currentChar)) {
                        term += currentChar;
                        state = 23;
                        if (isEOF()) {
                            pause = true;
                        }
                    } else {
                        cs.moveCursorBack(currentChar, antColCursor);
                        pause = true;
                        state = 21;
                    }
                    break;
                case 21:
                    exception = throwException(TypeException.INVALID_SYMBOL, term);
                    return null;
                case 22:
                    if (!Rules.isChar(currentChar) && !Rules.isDigit(currentChar)) {
                        cs.moveCursorBack(currentChar, antColCursor);
                        exception = throwException(TypeException.IDENTIFIER_FORMAT, term);
                        return null;
                    } else if (isEOF()) {
                        if ((Rules.isChar(currentChar) || Rules.isDigit(currentChar)) && pause == false) {
                            term += currentChar;
                        }
                        exception = throwException(TypeException.IDENTIFIER_FORMAT, term);
                        return null;
                    } else {
                        term += currentChar;
                        cs.moveCursorBack(currentChar, antColCursor);
                        state = 22;
                    }
                    break;
                case 23:
                    if (!Rules.isChar(currentChar) && !Rules.isDigit(currentChar)) {
                        cs.moveCursorBack(currentChar, antColCursor);
                        exception = throwException(TypeException.NUMBER_FORMAT, "Float Number : " + term);
                        return null;
                    } else if (isEOF()) {
                        if (Rules.isChar(currentChar) || Rules.isDigit(currentChar)) {
                            term += currentChar;
                        }
                        exception = throwException(TypeException.NUMBER_FORMAT, "Float Number : " + term);
                        return null;
                    } else {
                        term += currentChar;
                        cs.moveCursorBack(currentChar, antColCursor);
                        state = 23;
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
                    return new Token(TokenType.returnSubtype(term), term);
                /*
                  OPERADORES ARITMETICOS
                 */
                case 13:
                    return new Token(TokenType.returnSubtype(term), term);
                /*
                  IGUAL COMO OPERADOR RELACIONAL OU ARITMETICO
                 */
                case 14:
                    if (Rules.isEqual(currentChar)) {
                        term += currentChar;
                        state = 15;
                        if (isEOF()) {
                            return new Token(TokenType.returnSubtype(term), term);
                        }
                    } else {
                        pause = true;
                        back();
                        cs.moveCursorBack(currentChar, antColCursor);
                        state = 13;
                    }
                    break;
                case 15:
                    if (!Rules.isEqual(currentChar)) {
                        back();
                        cs.moveCursorBack(currentChar, antColCursor);
                        return new Token(TokenType.returnSubtype(term), term);
                    } else {
                        term += currentChar;
                        exception = throwException(TypeException.INVALID_OPERATOR, term);
                        return null;
                    }
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
                        return new Token(TokenType.returnSubtype(term), term);
                    }
                    break;
                case 17:
                    if (currentChar == '\n') {
                        state = 0;
                    } else {
                        state = 17;
                        if (isEOF()) {
                            return null;
                        }
                    }
                    break;
                /*
                  CHAR SEQUENCE
                 */
                case 18:
                    if (Rules.isDoubleQuotes(currentChar)) {
                        term += currentChar;
                        return new Token(TokenType.TK_CHAR_SEQUENCE, term);
                    } else if (isEOF()) {
                        if (currentChar != '\n') {
                            term += currentChar;
                        } else {
                            cs.moveCursorBack(currentChar, antColCursor);
                        }
                        exception = throwException(TypeException.UNCLOSED, "Char Sequence: " + term);
                        return null;
                    } else if (currentChar == '\n') {
                        cs.moveCursorBack(currentChar, antColCursor);
                        exception = throwException(TypeException.UNCLOSED, "Char Sequence: " + term);
                        return null;
                    } else {
                        term += currentChar;
                        state = 18;
                    }
                    break;
                /*
                  CHAR
                 */
                case 19:
                    if (Rules.isSingleQuotes(currentChar)) {
                        term += currentChar;
                        exception = throwException(TypeException.EMPTY_CHAR, term);
                        return null;
                    } else if (isEOF()) {
                        if (currentChar != '\n') {
                            term += currentChar;
                        } else {
                            cs.moveCursorBack(currentChar, antColCursor);
                        }
                        exception = throwException(TypeException.UNCLOSED, "Char: " + term);
                        return null;
                    } else if (Rules.isJumpLine(currentChar)) {
                        cs.moveCursorBack(currentChar, antColCursor);
                        exception = throwException(TypeException.UNCLOSED, "Char: " + term);
                        return null;
                    } else {
                        term += currentChar;
                        state = 20;
                    }
                    break;
                case 20:
                    if (Rules.isSingleQuotes(currentChar)) {
                        term += currentChar;
                        return new Token(TokenType.TK_CHAR, term);
                    } else {
                        cs.moveCursorBack(currentChar, antColCursor);
                        exception = throwException(TypeException.UNCLOSED, "Char: " + term);
                        return null;
                    }
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

    public String getException() {
        return this.exception;
    }

    private String throwException(TypeException type, String msg) {
        PersonalizedException ex = null;

        switch (type) {
            case NUMBER_FORMAT:
                ex = new NumberFormatException(msg, cs, nameArchive);
                break;
            case IDENTIFIER_FORMAT:
                ex = new IdentifierFormatException(msg, cs, nameArchive);
                break;
            case EMPTY_CHAR:
                ex = new EmptyCharException(msg, cs, nameArchive);
                break;
            case INVALID_OPERATOR:
                ex = new InvalidOperatorException(msg, cs, nameArchive);
                break;
            case INVALID_SYMBOL:
                ex = new InvalidSymbolException(msg, cs, nameArchive);
                break;
            case UNCLOSED:
                ex = new UnclosedException(msg, cs, nameArchive);
                break;
        }
        return ex.throwException();
    }
}
