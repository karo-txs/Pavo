package br.unicap.compiler.codeGen;

import br.unicap.compiler.lexicon.Token;
import br.unicap.compiler.lexicon.TokenType;
import br.unicap.compiler.semantic.Rules;
import java.util.*;

public class CodeGenOp {

    private final CodeGen code;
    private int pos = 1;
    private Rules rules;

    public CodeGenOp(CodeGen code, Rules rules) {
        this.rules = rules;
        this.code = code;
    }

    private void addCast(String cast, Token token) {
        String element = "T" + pos;
        pos++;
        code.addCode(element + "= (" + cast + ") " + token.getToken() + "\n");
    }

    private Token addOperation(Token identifier, Token op, List<Token> tokens) {
        Token elem = new Token();
        String element, elementAnt, elementAntAnt;

        boolean castElement1 = false, castElement2 = false;

        // Adiciona a primeira operação encontrada com determinado operador
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getType() == op.getType()) {

                //Verificando a necessidade de cast noa valores antes e depois do operador
                if (identifier != null && identifier.getType() == TokenType.TK_INT) {
                    if (tokens.get(i - 1).getType() == TokenType.TK_CHAR) {
                        addCast("int", tokens.get(i - 1));
                        castElement1 = true;
                    }
                    if (tokens.get(i + 1).getType() == TokenType.TK_CHAR) {
                        addCast("int", tokens.get(i + 1));
                        castElement2 = true;
                    }
                } else if (identifier != null && identifier.getType() == TokenType.TK_FLOAT) {
                    if (tokens.get(i - 1).getType() == TokenType.TK_CHAR || tokens.get(i - 1).getType() == TokenType.TK_INT) {
                        addCast("float", tokens.get(i - 1));
                        castElement1 = true;
                    }
                    if (tokens.get(i + 1).getType() == TokenType.TK_CHAR || tokens.get(i + 1).getType() == TokenType.TK_INT) {
                        addCast("float", tokens.get(i + 1));
                        castElement2 = true;
                    }
                }

                elementAntAnt = pos > 2 ? "T" + (pos - 2) : "";
                elementAnt = pos > 1 ? "T" + (pos - 1) : "";
                element = "T" + pos;
                elem.setToken(element);
                pos++;

                if (castElement1 && castElement2) {
                    code.addCode(element + "=" + elementAntAnt + " " + tokens.get(i).getToken() + " " + elementAnt + "\n");
                } else if (castElement1) {
                    code.addCode(element + "=" + elementAnt + " " + tokens.get(i).getToken() + " " + tokens.get(i + 1).getToken() + "\n");
                } else if (castElement2) {
                    code.addCode(element + "=" + tokens.get(i - 1).getToken() + " " + tokens.get(i).getToken() + " " + elementAnt + "\n");
                } else {
                    code.addCode(element + "=" + tokens.get(i - 1).getToken() + " " + tokens.get(i).getToken() + " " + tokens.get(i + 1).getToken() + "\n");
                }
                tokens.remove(i - 1);
                tokens.remove(i - 1);
                tokens.remove(i - 1);
                tokens.add(i - 1, elem);

                break;
            }
        }
        return elem;
    }

    private void addIdentifier(Token identifier, Token lastValue) {
        String result = lastValue.getToken();
        if (identifier != null && identifier.getType() == TokenType.TK_INT) {
            if (lastValue.getType() == TokenType.TK_CHAR) {
                result = "(int)"+ lastValue.getToken();
            }
        } else if (identifier != null && identifier.getType() == TokenType.TK_FLOAT) {
            if (lastValue.getType() == TokenType.TK_CHAR || lastValue.getType() == TokenType.TK_INT) {
                result = "(float)"+ lastValue.getToken();
            }
        }

        code.addCode(identifier.getToken() + "=" + result + "\n");
    }
    
    private void addSimpleOp(Token lastValue) {
        code.addCode("T" + pos + "=" + lastValue.getToken() + "\n");
        pos++;
    }

    private void trataParenteses(Token identifier, List<Token> tokens) {
        List<Token> operation;
        Object[] results;
        int i, j;
        int parentesesMaisInterno = 0;

        for (i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getType() == TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN) {
                parentesesMaisInterno = i;
            }
        }

        for (i = 0; i < tokens.size(); i++) {

            if (tokens.get(i).getType() == TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN
                    && i == parentesesMaisInterno) {
                operation = new ArrayList<>();
                tokens.remove(i);

                j = i;
                while (tokens.get(j).getType() != TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED) {
                    operation.add(tokens.get(j));
                    tokens.remove(j);
                }
                tokens.remove(j);

                results = calculate(identifier, operation);
                tokens.add(j, (Token) results[0]);
            }
        }
    }

    private Token convertIdentifierToType(List<Token> scope, Token id) {
        Map<String, TokenType> tableVar = null;
        boolean exists = false;
        for (Token esc : scope) {
            if (rules.verifyExists(esc.getToken(), id.getToken())) {
                tableVar = rules.getTableEscopo().get(esc.getToken());
                exists = true;
            }
        }
        if (exists) {
            TokenType idType = tableVar.get(id.getToken());
            return new Token(idType, id.getToken());
        } else {
            return null;
        }
    }

    private void convertTokensToType(List<Token> scope, List<Token> tokens) {
        Map<String, TokenType> tableVar = null;
        TokenType idType;
        boolean exist;

        for (Token t : tokens) {
            System.out.println(t.getType());
            if (t.getType() == TokenType.TK_IDENTIFIER) {
                exist = false;
                for (Token esc : scope) {
                    if (rules.verifyExists(esc.getToken(), t.getToken())) {
                        tableVar = rules.getTableEscopo().get(esc.getToken());
                        exist = true;
                    }
                }
                if (exist) {
                    idType = tableVar.get(t.getToken());
                    System.out.println(idType);
                    t.setType(idType);
                }
            }
        }
    }

    public void calculateMaster(Token identifier, List<Token> scope, List<Token> tokens) {
        System.out.println("size"+tokens.size());
        if(identifier!=null)identifier = convertIdentifierToType(scope, identifier);
        //convertTokensToType(scope, tokens);
        if (tokens.size() > 1) {
            Object[] results;

            // Trata parenteses
            int qtdeParenteses = 0;
            for (Token token : tokens) {
                if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN) {
                    qtdeParenteses++;
                }
            }

            for (int i = 0; i < qtdeParenteses; i++) {
                trataParenteses(identifier, tokens);
            }

            results = calculate(identifier, tokens);

            //Se tem um identificador
            if (identifier != null) {
                addIdentifier(identifier, (Token) results[0]);
            }

            code.addCode("\n");
        } else {
            if(identifier!=null){addIdentifier(identifier, tokens.get(0));}else{
                addSimpleOp(tokens.get(0));
            }
        }
    }

    public Object[] calculate(Token identifier, List<Token> tokens) {
        Object[] result = new Object[2];

        //Define a ordem em que os operadores se encontram
        List<Token> ordem = new ArrayList<>();

        for (Token token : tokens) {
            if (token.getType() != null) {
                switch (token.getType()) {
                    case TK_ARITHMETIC_OPERATOR_MULTIPLICATION:
                        ordem.add(token);
                        break;
                    case TK_ARITHMETIC_OPERATOR_DIVISION:
                        ordem.add(token);
                        break;
                    case TK_ARITHMETIC_OPERATOR_MINUS:
                        ordem.add(token);
                        break;
                    case TK_ARITHMETIC_OPERATOR_PLUS:
                        ordem.add(token);
                        break;
                }
            }
        }
        // Reordenando operadores
        List<Token> ordemCorreta = new ArrayList<>();
        for (Token op : ordem) {
            if (op.getType() == TokenType.TK_ARITHMETIC_OPERATOR_MULTIPLICATION) {
                ordemCorreta.add(op);
            } else if (op.getType() == TokenType.TK_ARITHMETIC_OPERATOR_DIVISION) {
                ordemCorreta.add(op);
            }
        }
        for (Token op : ordem) {
            if (op.getType() == TokenType.TK_ARITHMETIC_OPERATOR_MINUS) {
                ordemCorreta.add(op);
            } else if (op.getType() == TokenType.TK_ARITHMETIC_OPERATOR_PLUS) {
                ordemCorreta.add(op);
            }
        }

        // Adicionando operaçoes
        Token lastElement = new Token();
        for (Token op : ordemCorreta) {
            lastElement = addOperation(identifier, op, tokens);
        }

        result[0] = lastElement;
        result[1] = tokens;
        return result;
    }
    
    public int getPos() {
        return pos;
    }
}
//for for away.. 