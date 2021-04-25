package br.unicap.compiler.syntax;

import br.unicap.compiler.exceptions.syntactic.SyntaxException;
import br.unicap.compiler.lexicon.Scanner;
import br.unicap.compiler.lexicon.Token;
import br.unicap.compiler.lexicon.TokenType;
import java.util.List;

public class Parser {

    /*
                1º testes
		2º implementar outras funções (for, do while, ...)

		extra: branch nova: Design de ide
     */
    private Scanner scan;
    private Token token;
    private String nameArchive;
    private String exception;

    public Parser(Scanner scan, String nameArchive) {
        this.scan = scan;
        this.nameArchive = nameArchive;
        exception = "NULL";
    }

    public void runParser() {
        scan();
        if (!scan.isEOF()) {
            programa();
            if (!scan.isEOF()) {
                //parserError(CodigosToken.EOF);
            }
        }//else parserError(First.programa);
    }

    public void programa() {
        //int main"("")" <bloco>
        if (verificacao(TokenType.TK_KEYWORD_INT)) {
            scan();
            if (verificacao(TokenType.TK_KEYWORD_MAIN)) {
                scan();
                if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN)) {
                    scan();
                    if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                        scan();
                        bloco();
                    } else {
                        exception = throwException("não fechou parenteses");
                    }
                } else {
                    exception = throwException("nao abriu parenteses");
                }
            } else {
                exception = throwException("falta main");
            }
        } else {
            exception = throwException("falta int");
        }
    }

    /* ------------------------------------------- *
                        BLOCO
	   <bloco> ::= “{“ {<decl_var>}* {<comando>}* “}”
     * ------------------------------------------- */
    public void bloco() {
        if (verificacao(TokenType.TK_SPECIAL_CHARACTER_BRACES_OPEN)) {
            scan();

            if (verificacao(TokenType.TK_SPECIAL_CHARACTER_BRACES_CLOSED)) {
                scan();
                return;
            }
            if (scan.isEOF() && token != null && verificacao(TokenType.TK_SPECIAL_CHARACTER_BRACES_CLOSED)
                    || token == null) {
                exception = throwException("não fechou bloco");
                return;
            }
            while (exception.equals("NULL") && token != null && !scan.isEOF() && (token.getType() != TokenType.TK_SPECIAL_CHARACTER_BRACES_CLOSED)) {
//first(First.decl_var);
                if (first(First.decl_var)) {
                    while (first(First.decl_var)) {
                        decl_var();
                    }
                } else if (first(First.comando)) {
                    while (first(First.comando)) {
                        comando();
                    }
                } else {
                    exception = throwException("não reconhecido");
                }
            }
            if (scan.isEOF() && token != null && verificacao(TokenType.TK_SPECIAL_CHARACTER_BRACES_CLOSED)
                    || token == null) {
                exception = throwException("não fechou bloco");
                return;
            }
        } else {
            exception = throwException("não abriu bloco");
        }
    }

    /* ------------------------------------------- *
                       comando
		     <comando> ::=	<comando_básico>
 						  |	<iteração>
						  | if "("<expr_relacional>")" <comando> {else <comando>}?
     * ------------------------------------------- */
    public void comando() {
        if (first(First.comando_basico)) {
            comando_basico();
        } else if (first(First.iteracao)) {
            iteracao();
        } else if (first(First.if_)) {
            if_();
        }
    }

    public void comando_() {
        if (first(First.comando_basico)) {
            comando_basico();
        } else if (first(First.if_)) {
            if_();
        }
    }

    /* ------------------------------------------- *
                       comando_basico
		     <comando_básico> ::= <atribuição>
								| <bloco>
     * ------------------------------------------- */
    public void comando_basico() {
        if (first(First.atribuicao)) {
            atribuicao();
        } else if (first(First.bloco)) {
            bloco();
        }
    }

    /* ------------------------------------------- *
		     iteração
	while "("<expr_relacional>")" <comando>
     * ------------------------------------------- */
    public void iteracao() {
        scan();
        if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN)) {
            scan();
            if (first(First.expr_relacional)) {
                expr_relacional();
                if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                    scan();
                    if (first(First.comando)) {
                        comando();
                    } else {
                        exception = throwException("não abriu bloco e ta sem comando");
                    }
                } else {
                    exception = throwException("Parenteses não fechado");
                }
            } else {
                exception = throwException("Falta a relacional");
            }
        } else {
            exception = throwException("não abriu parenteses");
        }
    }

    /* ------------------------------------------- *
			atribuição
		     <id> "=" <expr_arit> ";"
     * ------------------------------------------- */
    public void atribuicao() {
        if (verificacao(TokenType.TK_IDENTIFIER)) {
            scan();
            if (verificacao(TokenType.TK_ARITHMETIC_OPERATOR_ASSIGN)) {
                scan();
                if (first(First.expr_arit)) {
                    expr_arit();
                    if (verificacao(TokenType.TK_SPECIAL_CHARACTER_SEMICOLON)) {
                        scan();
                        return; //esta ok
                    } else {
                        exception = throwException("nao fechou ;");
                    }
                } else {
                    exception = throwException("Sem expressão arit ou valor");
                }
            } else {
                exception = throwException("sem operador = ");
            }
        }
    }

    /* ------------------------------------------- *
						expr_relacional
		<expr_relacional> ::= <expr_arit> <op_relacional> <expr_arit>
     * ------------------------------------------- */
    public void expr_relacional() {

        if (first(First.expr_arit)) {
            expr_arit();
            if (first(First.op_relacional)) {
                op_relacional();
                if (first(First.expr_arit)) {
                    expr_arit();
                } else {
                    exception = throwException("Faltou expressão");
                }
            } else {
                exception = throwException("Faltou operador rel");
            }
        } else {
            exception = throwException("Faltou expressão");
        }
    }

    /* ------------------------------------------- *
		op_relacional
     * ------------------------------------------- */
    public void op_relacional() {
        if (token.getType() == TokenType.TK_RELATIONAL_OPERATOR_EQUAL
                || token.getType() == TokenType.TK_RELATIONAL_OPERATOR_LESS
                || token.getType() == TokenType.TK_RELATIONAL_OPERATOR_LESS_EQUAL
                || token.getType() == TokenType.TK_RELATIONAL_OPERATOR_MORE
                || token.getType() == TokenType.TK_RELATIONAL_OPERATOR_MORE_EQUAL
                || token.getType() == TokenType.TK_RELATIONAL_OPERATOR_NOT
                || token.getType() == TokenType.TK_RELATIONAL_OPERATOR_NOT_EQUAL) {
            scan();
            return;//ok
        } else {
            //erro
        }
    }

    /* ------------------------------------------- *
			expr_arit
    antes>>
		<expr_arit> ::= <expr_arit> "+" <termo>
                              | <expr_arit> "-" <termo>
  			      | <termo>
    depois>>
                <expr_arit> ::= <termo> <expr_arit_>
     * ------------------------------------------- */
    public void expr_arit() {
        if (first(First.termo)) {
            termo();
            expr_arit_();
        }
    }

    /* ------------------------------------------- *
		<expr_arit_> ::= "+" <termo> <expr_arit_>
                                | "+" <termo> <expr_arit_>
                                | vazio
     * ------------------------------------------- */
    public void expr_arit_() {
        if (verificacao(TokenType.TK_ARITHMETIC_OPERATOR_PLUS)) {
            scan();
            if (first(First.termo)) {
                termo();
                expr_arit_();
            } else {
                exception = throwException("ta errado 1+ nada");
            }

        } else if (verificacao(TokenType.TK_ARITHMETIC_OPERATOR_MINUS)) {
            scan();
            if (first(First.termo)) {
                termo();
                expr_arit_();
            } else {
                exception = throwException("ta errado 1- nada");
            }
        } else {
            return;
        }

    }

    /* ------------------------------------------- *
			termo
    antes>>
		<termo> ::= <termo> "*" <fator>
                      | <termo> "\" <fator>
  		      | <fator>
    depois>>
                <termo> ::= <fator> <termo_>
     * ------------------------------------------- */
    public void termo() {
        if (first(First.fator)) {
            fator();
            termo_();
        }
    }

    /* ------------------------------------------- *
			termo_
		<termo> ::= "*" <fator> <termo_>
                           |"\" <fator> <termo_>
  		           | vazio
     * ------------------------------------------- */
    public void termo_() {
        if (verificacao(TokenType.TK_ARITHMETIC_OPERATOR_MULTIPLICATION)) {
            scan();
            if (first(First.fator)) {
                fator();
                termo_();
            } else {
                exception = throwException("ta errado 1* nadao");
            }
        } else if (verificacao(TokenType.TK_ARITHMETIC_OPERATOR_DIVISION)) {
            scan();
            if (first(First.fator)) {
                fator();
                termo_();
            } else {
                exception = throwException("ta errado 1/ por nada");
            }
        } else {
            return;
        }
    }

    /* ------------------------------------------- *
	            	fator		
		<fator> ::= “(“ <expr_arit> “)”
				  | <id>
				  | <float>
				  | <inteiro>
				  | <char>
     * ------------------------------------------- */
    public void fator() {
        if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN)) {
            scan();
            if (first(First.expr_arit)) {
                expr_arit();

                if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                    scan();
                    return; //ok
                } else {
                    exception = throwException("Parenteses não fechado");
                }
            } else if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                exception = throwException("sem nada dentro-> ilegal");
            }
        } else if (scan.getException().equals("NULL") && (token.getType() == TokenType.TK_IDENTIFIER
                || token.getType() == TokenType.TK_INT
                || token.getType() == TokenType.TK_CHAR
                || token.getType() == TokenType.TK_FLOAT
                || token.getType() == TokenType.TK_CHAR_SEQUENCE)) {
            scan();
            return; //ok
        }
    }

    /* ------------------------------------------- *
	            	declaração var		
		    <decl_var> ::= <tipo> <id> ";"
     * ------------------------------------------- */
    public void decl_var() {
        if (first(First.tipo)) {
            scan();
            if (verificacao(TokenType.TK_IDENTIFIER)) {
                scan();
                if (verificacao(TokenType.TK_SPECIAL_CHARACTER_SEMICOLON)) {
                    scan();
                    return;
                } else {
                    exception = throwException("não fechou ;");
                }
            } else {
                exception = throwException("Identificador Esperado");
            }
        }
    }

    /* ------------------------------------------- *
	            	    if		
   if "("<expr_relacional>")" <comando> {else <comando_>}?
     * ------------------------------------------- */
    public void if_() {
        scan();
        if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN)) {
            scan();
            if (first(First.expr_relacional)) {
                expr_relacional();
                if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                    scan();
                    if (first(First.comando)) {
                        comando();
                        //scan();
                        System.out.println("Token " + token);
                        if (token != null
                                && verificacao(TokenType.TK_KEYWORD_ELSE)) {
                            scan();
                            if (first(First.comando_)) {
                                comando_();
                            }
                        } else if (token == null) {
                            exception = throwException("não fechou bloco main");
                        }
                    } else {
                        exception = throwException("Não é um comando...");
                    }
                } else {
                    exception = throwException("Não fechou parenteses");
                }
            } else {
                exception = throwException("Não é exp rel");
            }
        } else {
            exception = throwException("Não abriu parenteses");
        }
    }

    /* ------------------------------------------- *
	            	OUTROS METODOS AUXILIARES
     * ------------------------------------------- */
    private void scan() {
        token = scan.nextToken();
    }

    private String throwException(String msg) {
        SyntaxException ex = new SyntaxException(msg, scan.getCursor(), nameArchive);
        return ex.throwException();
    }

    public String getException() {
        return this.exception;
    }

    private boolean verificacao(TokenType tokenType) {
        return scan.getException().equals("NULL")
                && token.getType() == tokenType;
    }

    private boolean first(List first) {
        return token != null && first.contains(token.getType());
    }
    
    
}
