package br.unicap.compiler.codeGen;

import br.unicap.compiler.lexicon.Token;
import br.unicap.compiler.lexicon.TokenType;
import br.unicap.compiler.semantic.Rules;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CodeGenOperation {

    private final CodeGen code;
    private int i, cast1, cast2;
    private Token tk1, tk2, operator, firstVar;
    private TokenType dominantType;
    private boolean firstVerify, secVerify, castAntes, isRelational;
    private List<String> auxStructure;

    public CodeGenOperation(CodeGen code) {
        this.code = code;
        cast1 = cast2 = i = -1;
        this.tk1 = this.tk2 = operator = null;
        dominantType = null;
        castAntes = firstVerify = secVerify = false;
        auxStructure = new LinkedList<>();
    }

    public void storeElement(Token t) {
        if (tk1 == null) {
            tk1 = t;
            firstVerify = true;

        } else {
            tk2 = t;
            if (firstVerify) {
                secVerify = true;
            }
        }
    }

    public void storeOperator(Token t) {
        operator = t;
    }

    public void storeVar(List<Token> scope, Token id, Rules r, boolean isRelational) {
        this.isRelational = isRelational;
        if (firstVar == null) {
            firstVar = id;

            if (firstVar.getType() == TokenType.TK_IDENTIFIER) {

                Map<String, TokenType> tableVar = null;
                for (Token esc : scope) {
                    if (r.verifyExists(esc.getToken(), id.getToken())) {
                        tableVar = r.getTableEscopo().get(esc.getToken());
                    }
                }
                TokenType idType = tableVar.get(id.getToken());
                firstVar.setType(idType);
            }
        }
    }

    public void typeOfIdentifier(List<Token> scope, Token id, Rules r) {
        Map<String, TokenType> tableVar = null;
        for (Token esc : scope) {
            if (r.verifyExists(esc.getToken(), id.getToken())) {
                tableVar = r.getTableEscopo().get(esc.getToken());
            }
        }
        TokenType idType = tableVar.get(id.getToken());

        if (!secVerify) {
            tk1.setType(idType);
        } else {
            tk2.setType(idType);
        }
    }

    public void calculate(List<Token> scope, Token id, Rules r) {
        if (tk1 != null && tk1.getType() == TokenType.TK_IDENTIFIER || tk2 != null && tk2.getType() == TokenType.TK_IDENTIFIER) {
            typeOfIdentifier(scope, id, r);
        }
        dominantType();

        if (tk1.getType() != dominantType) {
            //ex.: T0 = (float) a;
            code.addCode("T" + i + " = (" + dominantType.getText() + ") " + tk1.getToken() + "\n");
            cast1 = i;
            i++;
            tk1.setType(dominantType);
        }

        if (tk2 != null && tk2.getType() != dominantType) {
            //ex.: T1 = (float) b;
            code.addCode("T" + i + " = (" + dominantType.getText() + ") " + tk2.getToken() + "\n");
            cast2 = i;
            i++;
        }

        if (firstVerify && secVerify) {
            code.addCode(cast1 != -1 && !castAntes ? "T" + i + " = T" + cast1
                    : (castAntes || cast2 != -1) && i != 1 ? "T" + i + " = T" + (i - 2)
                            : "T" + i + " = " + tk1.getToken());
            firstVerify = false;
        } else if (secVerify) {
            code.addCode(cast1 != -1 && !castAntes ? "T" + i + " = T" + cast1
                    : castAntes || cast2 != -1 ? "T" + i + " = T" + (i - 2)
                            : "T" + i + " = T" + (i - 1) + "");
        }
        if (tk2 != null) {
            code.addCode(cast2 != -1 ? " " + operator.getToken() + " T" + cast2
                    : " " + operator.getToken() + " " + tk2.getToken());
            firstVerify = false;
        }
        code.addCode("\n");
        i++;

        if (cast1 != -1) {
            cast1 = -1;
            castAntes = true;
        } else {
            castAntes = false;
        }

        if (cast2 != -1) {
            cast2 = -1;
            castAntes = true;
        } else {
            castAntes = false;
        }
    }

    public void calculateAux(List<Token> scope, List<Token> tokens, Rules r) {

        storeVar(scope, tokens.get(0), r, false);
        for (int i = 2; i < tokens.size(); i++) {

            if (tokens.get(i).getType() == TokenType.TK_CHAR
                    || tokens.get(i).getType() == TokenType.TK_FLOAT
                    || tokens.get(i).getType() == TokenType.TK_INT
                    || tokens.get(i).getType() == TokenType.TK_IDENTIFIER) {
                storeElement(tokens.get(i));
            } else if (tokens.get(i).getType() != TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN
                    && tokens.get(i).getType() != TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED) {
                storeOperator(tokens.get(i));
            }
        }
        calculate(scope, tokens.get(0), r);
        closeOperation();
    }

    private void dominantType() {

        if (tk1 != null && tk2 != null) {

            if (tk1.getType() == TokenType.TK_FLOAT || tk2.getType() == TokenType.TK_FLOAT) {
                dominantType = TokenType.TK_FLOAT;
            } else if (tk1.getType() == TokenType.TK_INT || tk2.getType() == TokenType.TK_INT) {
                dominantType = TokenType.TK_INT;
            } else {
                dominantType = TokenType.TK_CHAR;
            }
        } else {
            dominantType = tk1.getType();
        }

    }

    public void closeOperation() {
        if (secVerify == false) {
            if (firstVar.getType() != dominantType) {
                code.addCode("T" + i + " = (" + firstVar.getType().getText() + ") " + tk1.getToken() + "\n");
                i++;
                code.addCode((isRelational ? "T" + i : firstVar.getToken()) + " = T" + (i - 1) + "\n");
            } else {
                code.addCode((isRelational ? "T" + i : firstVar.getToken()) + " = " + tk1.getToken() + "\n");
            }
        } else {
            if (firstVar.getType() != dominantType) {
                dominantType = firstVar.getType();
                code.addCode("T" + i + " = (" + dominantType.getText() + ") T" + (i - 1) + "\n");
                i++;
                code.addCode((isRelational ? "T" + i : firstVar.getToken()) + " = T" + (i - 1) + "\n");
            } else {
                code.addCode((isRelational ? "T" + i : firstVar.getToken()) + " = T" + (i - 1) + "\n");
            }
        }

        tk1 = tk2 = operator = null;
        dominantType = null;
        firstVerify = secVerify = false;
        firstVar = null;

    }

    public int getPos() {
        return i;
    }

}
