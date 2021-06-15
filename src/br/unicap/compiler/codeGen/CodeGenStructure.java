package br.unicap.compiler.codeGen;

import br.unicap.compiler.lexicon.Token;
import java.util.LinkedList;
import java.util.List;
import br.unicap.compiler.lexicon.TokenType;

public class CodeGenStructure extends CodeGen {

    private final CodeGen code;
    private final CodeGenOp codeOp;
    private int pos;
    private String operator;
    private boolean structureOpen, isWhile, isFor, isIf;
    private List<Token> auxFor;

    public CodeGenStructure(CodeGen code, CodeGenOp codeOp) {
        this.code = code;
        this.codeOp = codeOp;
        this.pos = 0;
        this.operator = null;
        this.structureOpen = this.isWhile = false;
    }

    public void generateIf(List<Token> scope, List<Token> tokens) {
        if (!isWhile) {
            isIf = true;
            code.addCode("//IF\n");
        }
        String t1 = "", t2 = "";
        boolean first = true;

        List<Token> operation = new LinkedList<>();
        for (int i = 0; i < tokens.size(); i++) {

            if (TokenType.returnMasterType(tokens.get(i)).equals("OTHER") && i != tokens.size() - 1) {
                operation.add(tokens.get(i));
            } else {
                if (i == tokens.size() - 1) {
                    operation.add(tokens.get(i));
                }
                codeOp.calculateMaster(null, scope, operation);
                operation = new LinkedList<>();
                if (first) {
                    t1 = "T" + (codeOp.getPos() - 1);
                    first = false;
                } else {
                    t2 = "T" + (codeOp.getPos() - 1);
                }
            }
        }
        pos++;
        code.addCode("L" + pos + ": if " + t1 + " " + operator + " " + t2
                + " goto " + "L" + (pos + 1) + "\n");//L1: T1 < T2 goto L2
        pos++;
        structureOpen = true;
    }

    public void generateWhile(List<Token> scope, List<Token> tokens) {
        code.addCode("//WHILE\n");
        if (operator != null) {
            isWhile = true;
            generateIf(scope, tokens);
        }
    }

    public void generateFor(List<Token> scope, List<Token> tokens) {
        isFor = true;
        structureOpen = true;
        auxFor = new LinkedList<>();
        boolean first = true, second = false, third = false, aux = true;
        Token id = null;
        String t1 = "", t2 = "";
        System.out.println("chegoou no for");

        List<Token> operation = new LinkedList<>();

        code.addCode("//FOR\n");

        for (int i = 0; i < tokens.size(); i++) {
            if (first) {
                if (i == 0) {
                    id = tokens.get(i);
                } else if (tokens.get(i).getType() != TokenType.TK_SPECIAL_CHARACTER_SEMICOLON) {
                    if (tokens.get(i).getType() != TokenType.TK_ARITHMETIC_OPERATOR_ASSIGN) {
                        operation.add(tokens.get(i));
                    }
                } else {
                    codeOp.calculateMaster(id, scope, operation);
                    operation = new LinkedList<>();
                    first = false;
                    second = true;
                }
            } else if (second) {
                if (TokenType.returnMasterType(tokens.get(i)).equals("OTHER") 
                        && tokens.get(i).getType() != TokenType.TK_SPECIAL_CHARACTER_SEMICOLON) {
                    operation.add(tokens.get(i));
                } else {
//                    if (tokens.get(i).getType() != TokenType.TK_SPECIAL_CHARACTER_SEMICOLON) {
//                        operation.add(tokens.get(i));
//                    }
                    
                    codeOp.calculateMaster(null, scope, operation);
                    operation = new LinkedList<>();
                    if (aux) {
                        t1 = "T" + (codeOp.getPos() - 1);
                        aux = false;
                    } else {
                        t2 = "T" + (codeOp.getPos() - 1);
                        second = false;
                        third = true;

                        pos++;
                        code.addCode("L" + pos + ": if " + t1 + " " + operator + " " + t2
                                + " goto " + "L" + (pos + 1) + "\n");//L1: T1 < T2 goto L2
                        pos++;
                    }
                }
            } else if (third) {
                this.auxFor.add(tokens.get(i));
            }
        }
    }

    public void addOperator(Token op) {
        operator = invertOperation(op);
    }

    private String invertOperation(Token op) {
        switch (op.getType()) {
            case TK_RELATIONAL_OPERATOR_EQUAL:
                return "!=";
            case TK_RELATIONAL_OPERATOR_MORE:
                return "<=";
            case TK_RELATIONAL_OPERATOR_MORE_EQUAL:
                return "<";
            case TK_RELATIONAL_OPERATOR_LESS:
                return ">=";
            case TK_RELATIONAL_OPERATOR_LESS_EQUAL:
                return ">";
            case TK_RELATIONAL_OPERATOR_NOT_EQUAL:
                return "==";
        }
        return null;
    }

    public void closeStructure(List<Token> scope) {
        if (structureOpen) {
            if (isWhile) {
                code.addCode("goto L" + (pos - 1) + "\n");
                code.addCode("L" + pos + ":\n");
                isWhile = false;
            } 
            if (isFor) {
                System.out.println("FIRST");
                    for (Token t : auxFor) {
                        System.out.println(t.getToken());
                    }
                Token id = auxFor.get(0);
                List<Token> operation = auxFor.subList(2, auxFor.size());
                
                codeOp.calculateMaster(id, scope, operation);
                
                code.addCode("goto L" + (pos - 1) + "\n");
                code.addCode("L" + pos + ":");
                isFor = false;
            } if(isIf) {
                code.addCode("L" + pos + ":\n");
                isIf = false;
            }
            structureOpen = false;
        }
    }

    public int getPos() {
        return pos;
    }
}
