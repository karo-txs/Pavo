package br.com.compiler.lexico;

import br.com.compiler.exceptions.InvalidSymbolException;
import br.com.compiler.exceptions.PersonalizedException;
import br.com.compiler.util.Cursor;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Scanner {

    private char[] content;
    private int estado;
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
        estado = 0;
        int antColCursor = 0;
        String term = "";
        Token token;
        boolean pause = false;
        PersonalizedException exception;

        if (isEOF()) {
            return null;
        }

        while (true) {
            if (!pause) {
                currentChar = nextChar();
                cs.moveCursorFront(currentChar);
                antColCursor = cs.getColun() - 1;
            }

            switch (estado) {
                /*
                  ESTADO INICIAL
                 */
                case 0:
                    if (Rules.isSpace(currentChar)) {
                        estado = 0;
                        if (isEOF()) {
                            return null;
                        }
                    } else if (Rules.isChar(currentChar) || currentChar == '_') {
                        term += currentChar;
                        estado = 1;
                        if (isEOF()) {
                            pause = true;
                            estado = 2;
                        }
                    } else if (Rules.isDigit(currentChar)) {
                        term += currentChar;
                        estado = 3;
                        if (isEOF()) {
                            pause = true;
                            estado = 7;
                        }
                    } else if (Rules.isRelationalOperator(currentChar)) {
                        term += currentChar;
                        estado = 10;
                        if (isEOF()) {
                            token = new Token(TokenType.TK_OPERATOR_RELATIONAL, term);
                            return token;
                        }
                    } else if (Rules.isArithmeticOperator(currentChar)) {
                        term += currentChar;
                        estado = 11;
                        if (isEOF()) {
                            pause = true;
                        } else {
                            back();
                            cs.moveCursorBack(currentChar, antColCursor);
                        }
                    } else if (Rules.isEqual(currentChar)) {
                        term += currentChar;
                        estado = 12;
                        if (isEOF()) {
                            pause = true;
                            estado = 13;
                        }
                    } else if (Rules.isSpecialCharacter(currentChar)) {
                        term += currentChar;
                        token = new Token(TokenType.TK_CHARACTER_SPECIAL, term);
                        return token;
                    } else if (currentChar == '.') {
                        term += currentChar;
                        token = new Token(TokenType.TK_PUNCTUATION, term);
                        return token;
                    } else if (currentChar == '/') {
                        estado = 14;
                    } else {
                        term += currentChar;
                        exception = new InvalidSymbolException("Unrecognized SYMBOL: " + term, cs);
                        exception.throwException();
                    }
                    break;
                /*
                  IDENTIFICADOR 
                 */
                case 1:
                    if (Rules.isChar(currentChar) || Rules.isDigit(currentChar) || currentChar == '_') {
                        term += currentChar;
                        estado = 1;
                        if (isEOF()) {
                            pause = true;
                            estado = 2;
                        }
                    } else {
                        pause = true;
                        back();
                        cs.moveCursorBack(currentChar, antColCursor);
                        estado = 2;
                    }
                    break;
                case 2:
                    if (Rules.isReserved(term)) {
                        token = new Token(TokenType.TK_RESERVED, term);
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
                        estado = 3;
                        if (isEOF()) {
                            pause = true;
                            estado = 7;
                        }
                    } else if (currentChar == '.') {
                        term += currentChar;
                        estado = 5;
                        if (isEOF()) {
                            term += currentChar;
                            exception = new InvalidSymbolException("Bad Format of Integer Number : " + term, cs);
                            exception.throwException();
                        }
                    } else if (!Rules.isChar(currentChar)) {
                        pause = true;
                        estado = 7;
                    } else {
                        term += currentChar;
                        estado = 4;
                    }
                    break;
                case 4:
                    if (!Rules.isChar(currentChar)) {
                        exception = new InvalidSymbolException("Bad Format of Integer Number : " + term, cs);
                        exception.throwException();
                    } else {
                        cs.moveCursorBack(currentChar, antColCursor);
                        term += currentChar;
                        estado = 4;
                    }
                    break;
                case 5:
                    if (Rules.isDigit(currentChar)) {
                        term += currentChar;
                        estado = 8;
                        if (isEOF()) {
                            pause = true;
                            estado = 9;
                        }
                    } else if (Rules.isChar(currentChar)) {
                        term += currentChar;
                        estado = 6;
                        if (isEOF()) {
                            exception = new InvalidSymbolException("Bad Format of Float Number : " + term, cs);
                            exception.throwException();
                        }
                    } else {
                        pause = true;
                        estado = 7;
                    }
                    break;
                case 6:
                    if (Rules.isChar(currentChar)) {
                        term += currentChar;
                        estado = 6;
                        if (isEOF()) {
                            exception = new InvalidSymbolException("Bad Format of Float Number : " + term, cs);
                            exception.throwException();
                        }
                    } else {
                        exception = new InvalidSymbolException("Bad Format of Float Number : " + term, cs);
                        exception.throwException();
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
                        estado = 8;
                        if (isEOF()) {
                            pause = true;
                            estado = 9;
                        }
                    } else if (!Rules.isChar(currentChar)) {
                        back();
                        cs.moveCursorBack(currentChar, antColCursor);
                        pause = true;
                        estado = 9;
                    } else {
                        term += currentChar;
                        exception = new InvalidSymbolException("Bad Format of Float Number : " + term, cs);
                        exception.throwException();
                    }
                    break;
                case 9:
                    token = new Token(TokenType.TK_FLOAT, term);
                    return token;
                /*
                  OPERADORES RELACIONAIS
                 */
                case 10:
                    if (Rules.isEqual(currentChar)) {
                        term += currentChar;
                    } else {
                        back();
                        cs.moveCursorBack(currentChar, antColCursor);
                    }
                    token = new Token(TokenType.TK_OPERATOR_RELATIONAL, term);
                    return token;
                /*
                  OPERADORES ARITMETICOS
                 */
                case 11:
                    token = new Token(TokenType.TK_OPERATOR_ARITHMETIC, term);
                    return token;
                /*
                  IGUAL COMO OPERADOR RELACIONAL OU ARITMETICO
                 */
                case 12:
                    if (Rules.isEqual(currentChar)) {
                        term += currentChar;
                        estado = 13;
                        if (isEOF()) {
                            token = new Token(TokenType.TK_OPERATOR_RELATIONAL, term);
                            return token;
                        }
                    } else {
                        pause = true;
                        estado = 11;
                    }
                    break;
                case 13:
                    if (!Rules.isEqual(currentChar)) {
                        back();
                        cs.moveCursorBack(currentChar, antColCursor);
                        token = new Token(TokenType.TK_OPERATOR_RELATIONAL, term);
                        return token;
                    } else if (Rules.isEqual(currentChar) && isEOF()) {
                        term += currentChar;
                        exception = new InvalidSymbolException("Invalid Operator : " + term, cs);
                        exception.throwException();
                    }
                    break;
                /*
                  CONSUMIR COMENTARIOS (// exemplo de comentario consumido) 
                 */
                case 14:
                    if (currentChar == '/') {
                        estado = 15;
                    } else {
                        term += '/';
                        back();
                        cs.moveCursorBack(currentChar, antColCursor);
                        token = new Token(TokenType.TK_OPERATOR_ARITHMETIC, term);
                        return token;
                    }
                    break;
                case 15:
                    if (currentChar == '\n') {
                        estado = 0;
                    } else {
                        estado = 15;
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
}
