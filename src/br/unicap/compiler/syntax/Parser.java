package br.unicap.compiler.syntax;

import br.unicap.compiler.exceptions.syntactic.SyntaxException;
import br.unicap.compiler.lexicon.Scanner;
import br.unicap.compiler.lexicon.Token;
import br.unicap.compiler.lexicon.TokenType;
import java.util.List;

public class Parser {

    /*
                1º testes
                2º refatoraçao do codigo (ingles e padronização)
                3º documentação pavo github

		extra: branch nova: Design de ide
     */
    private Scanner scan;
    private Token token;
    private String nameArchive;
    private String exception;
    private boolean mainCriado = false;

    public Parser(Scanner scan, String nameArchive) {
        this.scan = scan;
        this.nameArchive = nameArchive;
        exception = "NULL";
    }

    public void runParser() {
        scan();
        if (scan.isEOF()) {
            throwException("ta sem nada");
        }
        while (!scan.isEOF() && exception.equals("NULL")) {
            if (verificacao(TokenType.TK_KEYWORD_INT)) {
                scan();
                programa_();
            } else if (verificacao(TokenType.TK_KEYWORD_VOID)) {
                scan();
                if (verificacao(TokenType.TK_IDENTIFIER)) {
                    scan();
                    metodo();
                } else {
                    throwException("metodo errado");
                }
            } else {
                throwException("irreconhecido");
            }
        }
    }

    public void programa_() {
        if (verificacao(TokenType.TK_KEYWORD_MAIN)) {
            if (mainCriado) {
                throwException("metodo main duplicado");
            } else {
                mainCriado = true;
                scan();
                main();
            }
        } else if (verificacao(TokenType.TK_IDENTIFIER)) {
            scan();
            metodo();
        } else {
            throwException("metodo errado");
        }
    }

    public void metodo() {
        if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN)) {
            scan();
            if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                scan();
                bloco();
            } else {
                throwException("não fechou parenteses");
            }
        } else {
            throwException("nao abriu parenteses");
        }
    }

    public void main() {
        if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN)) {
            scan();
            if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                scan();
                bloco();
            } else {
                throwException("não fechou parenteses");
            }
        } else {
            throwException("nao abriu parenteses");
        }
    }

    /* ------------------------------------------- *
                        BLOCO
	   <bloco> ::= “{“ {<decl_var>}* {<comando>}* “}”
     * ------------------------------------------- */
    public void bloco() {
        if (verificacao(TokenType.TK_SPECIAL_CHARACTER_BRACES_OPEN)) {
            scan();

            if (token != null && verificacao(TokenType.TK_SPECIAL_CHARACTER_BRACES_CLOSED)) {
                scan();
                return;
            }
            if (scan.isEOF() && token != null && verificacao(TokenType.TK_SPECIAL_CHARACTER_BRACES_CLOSED)
                    || token == null) {
                throwException("não fechou bloco");
                return;
            }
            while (exception.equals("NULL") && token != null && !scan.isEOF() && (token.getType() != TokenType.TK_SPECIAL_CHARACTER_BRACES_CLOSED)) {
                if (first(First.decl_var)) {
                    while (first(First.decl_var)) {
                        decl_var();
                    }
                } else if (first(First.comando)) {
                    while (first(First.comando)) {
                        comando();
                    }
                } else if (first(First.print)) {
                    while (first(First.print)) {
                        print();
                    }
                } else {
                    throwException("não reconhecido");
                }
            }
            if (scan.isEOF() && token != null && !verificacao(TokenType.TK_SPECIAL_CHARACTER_BRACES_CLOSED)
                    || token == null) {
                throwException("não fechou bloco");
                return;
            }
            if (verificacao(TokenType.TK_SPECIAL_CHARACTER_BRACES_CLOSED)) {
                scan();
            } else {
                throwException("não fechou bloco");
            }
        } else {
            throwException("não abriu bloco");
        }
    }

    /* ------------------------------------------- *
                       comando
		     <comando> ::=	<comando_básico>
 			|	<iteração>
			| <if_>
     * ------------------------------------------- */
    public void comando() {
        if (first(First.comando_basico)) {
            comando_basico();
        } else if (first(First.while_)) {
            while_();
        } else if (first(First.do_while_)) {
            do_while_();
        } else if (first(First.for_)) {
            for_();
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
                        throwException("nao fechou ;");
                    }
                } else {
                    throwException("Sem expressão arit ou valor");
                }
            } else if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN)) {
                chamada_metodo();
            } else {
                throwException("sem operador =");

            }
        }
    }

    /* ------------------------------------------- *
			atribuição
		     <id> "=" <expr_arit>
     * ------------------------------------------- */
    public void atribuicao_() {
        if (verificacao(TokenType.TK_IDENTIFIER)) {
            scan();
            if (verificacao(TokenType.TK_ARITHMETIC_OPERATOR_ASSIGN)) {
                scan();
                if (first(First.expr_arit)) {
                    expr_arit();
                    return;
                } else {
                    throwException("Sem expressão arit ou valor");
                }
            } else {
                throwException("sem operador = ");
            }
        }
    }

    /* ------------------------------------------- *
		      chamada_metodo
	    <chamada_metodo>::= <id> "(" ")" ";"
     * ------------------------------------------- */
    public void chamada_metodo() {
//        scan();
        if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN)) { //não precisa
            scan();
            if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                scan();
                if (verificacao(TokenType.TK_SPECIAL_CHARACTER_SEMICOLON)) {
                    scan();
                } else {
                    throwException("Não fechou ;");
                }
            } else {
                throwException("Não fechou parenteses");
            }
        } else {
            throwException("Não abriu parenteses");
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
                    throwException("Faltou expressão");
                }
            } else {
                throwException("Faltou operador rel");
            }
        } else {
            throwException("Faltou expressão");
        }
    }

    /* ------------------------------------------- *
		expr_logica
      <expr_logica> ::= <expr_relacional> <logica> 
     * ------------------------------------------- */
    public void expr_logica() {
        if (first(First.expr_relacional)) {
            expr_relacional();
            logica();
        } else {
            throwException("Faltou expressão");
        }
    }

    /* ------------------------------------------- *
            <logica> ::=   “||” <expr_logica>
                            | “&&” <expr_logica>
                            | vazio
     * ------------------------------------------- */
    public void logica() {
        if (!scan.getException().equals("NULL")) {
            throwException(scan.getException());
        } else if (token.getType() == TokenType.TK_LOGIC_AND || token.getType() == TokenType.TK_LOGIC_OR) {
            scan();
            if (first(First.expr_logica)) {
                expr_logica();
            }
        } else {
            return;
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
                throwException("ta errado 1+ nada");
            }

        } else if (verificacao(TokenType.TK_ARITHMETIC_OPERATOR_MINUS)) {
            scan();
            if (first(First.termo)) {
                termo();
                expr_arit_();
            } else {
                throwException("ta errado 1- nada");
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
                throwException("ta errado 1* nadao");
            }
        } else if (verificacao(TokenType.TK_ARITHMETIC_OPERATOR_DIVISION)) {
            scan();
            if (first(First.fator)) {
                fator();
                termo_();
            } else {
                throwException("ta errado 1/ por nada");
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
                    throwException("Parenteses não fechado");
                }
            } else if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                throwException("sem nada dentro-> ilegal");
            } else {
                throwException("ilegal start of expression");
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
                    throwException("não fechou ;");
                }
            } else {
                throwException("Identificador Esperado");
            }
        }
    }

    /* ------------------------------------------- *
	            	    if		
   if "("<expr_logica>")" <comando> {else <comando_>}?
     * ------------------------------------------- */
    public void if_() {
        scan();
        if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN)) {
            scan();
            if (first(First.expr_logica)) {
                expr_logica();
                if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                    scan();
                    if (first(First.comando)) {
                        comando();
                        if (token != null
                                && verificacao(TokenType.TK_KEYWORD_ELSE)) {
                            scan();
                            if (first(First.comando_)) {
                                comando_();
                            }
                        } else if (token == null) {
                            throwException("não fechou bloco main");
                        }
                    } else if (verificacao(TokenType.TK_SPECIAL_CHARACTER_BRACES_CLOSED)) {
                        throwException("Não abriu bloco...");
                    } else {
                        throwException("Não é um comando...");
                    }
                } else {
                    throwException("Não fechou parenteses");
                }
            } else {
                throwException("Não é exp rel");
            }
        } else {
            throwException("Não abriu parenteses");
        }
    }

    /* ------------------------------------------- *
		     iteração
	while "("<expr_logica>")" <comando>
     * ------------------------------------------- */
    public void while_() {
        scan();
        if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN)) {
            scan();
            if (first(First.expr_logica)) {
                expr_logica();
                if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                    scan();
                    if (first(First.comando)) {
                        comando();
                    } else {
                        throwException("não abriu bloco e ta sem comando");
                    }
                } else {
                    throwException("Parenteses não fechado");
                }
            } else {
                throwException("Falta a relacional");
            }
        } else {
            throwException("não abriu parenteses");
        }
    }

    /* ------------------------------------------- *
            <do_while> ::= do <comando> while “(“ <expr_logica> “)” “;”
     * ------------------------------------------- */
    public void do_while_() {
        scan();
        if (first(First.comando)) {
            comando();
            if (first(First.while_)) {
                scan();
                if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN)) {
                    scan();
                    if (first(First.expr_logica)) {
                        expr_logica();
                        if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                            scan();
                            if (verificacao(TokenType.TK_SPECIAL_CHARACTER_SEMICOLON)) {
                                scan();
                            } else {
                                throwException("Não fechou ;");
                            }
                        } else {
                            throwException("Não fechou parenteses");
                        }
                    } else {
                        throwException("Faltou exp logica");
                    }
                } else {
                    throwException("Não abriu parenteses");
                }
            } else {
                throwException("Faltou while");
            }
        } else {
            throwException("Faltou comando...");
        }

    }

    /* ------------------------------------------- *
	<for_> ::= for “(“ <atribuicao_>  ";" <exp_logica>  “;” <atribuicao_> “)” <comando>
     * ------------------------------------------- */
    public void for_() {
        scan();
        if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN) {
            scan();
            if (first(First.atribuicao)) {
                atribuicao_();
                if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_SEMICOLON) {
                    scan();
                    if (first(First.expr_logica)) {
                        expr_logica();
                        if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_SEMICOLON) {
                            scan();
                            if (first(First.atribuicao)) {
                                atribuicao_();
                                if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED) {
                                    scan();
                                    if (first(First.comando)) {
                                        comando();
                                    } else {
                                        throwException("Não é um comando...");
                                    }
                                } else {
                                    throwException("Não fechou parenteses");
                                }
                            } else {
                                throwException("Não é atribuicao");
                            }
                        } else {
                            throwException("Não fechou ;");
                        }
                    } else {
                        throwException("Não é exp logica");
                    }
                } else {
                    throwException("Não fechou ;");
                }
            } else {
                throwException("Não é atribuicao");
            }
        } else {
            throwException("Não abriu parenteses");
        }
    }

    /* ------------------------------------------- *
	 <print>::= “(“ <print_> “)” “;”
     * ------------------------------------------- */
    public void print() {
        scan();
        if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN)) {
            scan();
            if (first(First.print_)) {
                print_();
                if (verificacao(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                    scan();
                    if (verificacao(TokenType.TK_SPECIAL_CHARACTER_SEMICOLON)) {
                        scan();
                        return; //esta ok
                    } else {
                        throwException("nao fechou ;");
                    }
                } else {
                    throwException("Não fechou parenteses");
                }
            } else if (token.getType() != TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED) {
                throwException("valor invalido");
            } else if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED) {
                scan();
                if (verificacao(TokenType.TK_SPECIAL_CHARACTER_SEMICOLON)) {
                    scan();
                    return; //esta ok
                } else {
                    throwException("nao fechou ;");
                }
            }
        } else {
            throwException("Não abriu parenteses");
        }
    }

    /* ------------------------------------------- *
	  <print_> ::= <char_sequence> 
		| <expr_logica> 
                | <expr_arit>
     * ------------------------------------------- */
    public void print_() {
        if (token.getType() == TokenType.TK_CHAR_SEQUENCE) {
            scan();
        } else if (first(First.expr_logica)) {
            expr_logica();
        } else if (first(First.expr_arit)) {
            expr_arit();
        }
    }

    /* ------------------------------------------- *
	       OUTROS METODOS AUXILIARES
     * ------------------------------------------- */
    private void scan() {
        token = scan.nextToken();
    }

    private void throwException(String msg) {
        if (exception.equals("NULL")) {
            SyntaxException ex = new SyntaxException(msg, scan.getCursor(), nameArchive);
            exception = ex.throwException();
        }
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
