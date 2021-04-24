package br.unicap.compiler.syntax;

import br.unicap.compiler.lexicon.Scanner;
import br.unicap.compiler.lexicon.Token;
import br.unicap.compiler.lexicon.TokenType;

public class Parser {

    /*
		1º Testar e ajustar (finalizar o basico)
		2º exceções
		3º Analizar recusão a esquerda 
		4º implementar outras funções (for, do while, ...)

		extra: branch nova: Design de ide
     */
    private Scanner scan;
    private Token token;

    public Parser(Scanner scan) {
        this.scan = scan;
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
        if (token.getType() == TokenType.TK_KEYWORD_INT) {
            scan();
            if (token.getType() == TokenType.TK_KEYWORD_MAIN) {
                scan();
                if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN) {
                    scan();
                    if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED) {
                        scan();
                        bloco();
                    } else {
                        System.out.println("parserError(CodigosToken.FECHA_PARENTESES)");
                    }
                } else {
                    System.out.println("parserError(CodigosToken.ABRE_PARENTESES)");
                }
            } else {
                System.out.println("main");
            }
        } else {
            System.out.println("int");
        }
    }

    /* ------------------------------------------- *
                       BLOCK <BLK>
	   <bloco> ::= “{“ {<decl_var>}* {<comando>}* “}”
     * ------------------------------------------- */
    public void bloco() {
        if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_BRACES_OPEN) {
            scan();
            if (scan.isEOF() && token != null && token.getType() != TokenType.TK_SPECIAL_CHARACTER_BRACES_CLOSED
                    || token == null) {
                System.out.println("2Não fechou o bloco");
                return;
            }
            while (token != null && !scan.isEOF() && (token.getType() != TokenType.TK_SPECIAL_CHARACTER_BRACES_CLOSED)) {
                while (token != null && First.decl_var.contains(token.getType())) {
                    decl_var();
                }

                while (token != null && First.comando.contains(token.getType())) {
                    comando();
                    scan();
                }
            }
            if (scan.isEOF() && token != null && token.getType() != TokenType.TK_SPECIAL_CHARACTER_BRACES_CLOSED
                    || token==null) {
                System.out.println("2Não fechou o bloco");
                return;
            }
        } else {
            System.out.println("3Não abriu o bloco");
        }
    }

    /* ------------------------------------------- *
                       comando
		     <comando> ::=	<comando_básico>
 						  |	<iteração>
						  | if "("<expr_relacional>")" <comando> {else <comando>}?
     * ------------------------------------------- */
    public void comando() {
        if (First.comando_basico.contains(token.getType())) {
            comando_basico();
            scan();
        } else if (First.iteracao.contains(token.getType())) {
            iteracao();
            scan();
        } else if (First.if_.contains(token.getType())) {
            if_();
            scan();
        } else {
            //erro
        }
    }

    /* ------------------------------------------- *
                       comando_basico
		     <comando_básico> ::= <atribuição>
								| <bloco>
     * ------------------------------------------- */
    public void comando_basico() {
        if (First.atribuicao.contains(token.getType())) {
            atribuicao();
            scan();
        } else if (First.bloco.contains(token.getType())) {
            bloco();
            scan();
        }
    }

    /* ------------------------------------------- *
		     iteração
	while "("<expr_relacional>")" <comando>
     * ------------------------------------------- */
    public void iteracao() {

        if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN) {
            scan();
            if (First.expr_relacional.contains(token.getType())) {
                expr_relacional();
                scan();
                if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED) {
                    scan();
                    if (First.comando.contains(token.getType())) {
                        comando();
                        scan();
                    }
                }
            }
        }
    }

    /* ------------------------------------------- *
			atribuição
		     <id> "=" <expr_arit> ";"
     * ------------------------------------------- */
    public void atribuicao() {
        if (token.getType()==TokenType.TK_IDENTIFIER) {
            scan();

            if (token.getType() == TokenType.TK_RELATIONAL_OPERATOR_EQUAL) {
                scan();

                if (First.expr_arit.contains(token.getType())) {
                    expr_arit();
                    scan();

                    if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_SEMICOLON) {
                        return; //esta ok
                    }
                }
            }
        }
    }

    /* ------------------------------------------- *
						expr_relacional
		<expr_relacional> ::= <expr_arit> <op_relacional> <expr_arit>
     * ------------------------------------------- */
    public void expr_relacional() {

        if (First.expr_arit.contains(token.getType())) {
            expr_arit();
            scan();
            if (First.op_relacional.contains(token.getType())) {
                op_relacional();
                scan();
                if (First.expr_arit.contains(token.getType())) {
                    expr_arit();
                    scan();
                }
            }
        }
    }

    /* ------------------------------------------- *
		expr_relacional
	<expr_relacional> ::= <expr_arit> <op_relacional> <expr_arit>
     * ------------------------------------------- */
    public void op_relacional() {
        if (token.getType() == TokenType.TK_RELATIONAL_OPERATOR_EQUAL
                || token.getType() == TokenType.TK_RELATIONAL_OPERATOR_LESS
                || token.getType() == TokenType.TK_RELATIONAL_OPERATOR_LESS_EQUAL
                || token.getType() == TokenType.TK_RELATIONAL_OPERATOR_MORE
                || token.getType() == TokenType.TK_RELATIONAL_OPERATOR_MORE_EQUAL
                || token.getType() == TokenType.TK_RELATIONAL_OPERATOR_NOT
                || token.getType() == TokenType.TK_RELATIONAL_OPERATOR_NOT_EQUAL) {
            return;//ok
        } else {
            //erro
        }
    }

    /* ------------------------------------------- *
						expr_arit
		<expr_arit> ::= <expr_arit> "+" <termo>
                      | <expr_arit> "-" <termo>
  				  	  | <termo>
     * ------------------------------------------- */
    public void expr_arit() {
        if (First.expr_arit.contains(token.getType())) {
            expr_arit();
            scan();
            if (token.getType() == TokenType.TK_ARITHMETIC_OPERATOR_PLUS) {
                scan();
                if (First.termo.contains(token.getType())) {
                    termo();
                    scan();
                }
            } else if (token.getType() == TokenType.TK_ARITHMETIC_OPERATOR_MINUS) {
                scan();
                if (First.termo.contains(token.getType())) {
                    termo();
                    scan();
                }
            }
        } else if (First.termo.contains(token.getType())) {
            termo();
            scan();
        }
    }

    /* ------------------------------------------- *
						termo
		<expr_arit> ::= <termo> "*" <fator>
                      | <termo> "\" <fator>
  				  	  | <fator>
     * ------------------------------------------- */
    public void termo() {
        if (First.termo.contains(token.getType())) {
            termo();
            scan();
            if (token.getType() == TokenType.TK_ARITHMETIC_OPERATOR_MULTIPLICATION) {
                scan();
                if (First.fator.contains(token.getType())) {
                    fator();
                    scan();
                }
            } else if (token.getType() == TokenType.TK_ARITHMETIC_OPERATOR_DIVISION) {
                scan();
                if (First.fator.contains(token.getType())) {
                    fator();
                    scan();
                }
            }
        } else if (First.fator.contains(token.getType())) {
            fator();
            scan();
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
        if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN) {
            scan();
            if (First.expr_arit.contains(token.getType())) {
                expr_arit();
                scan();
                if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN) {
                    return; //ok
                }
            }
        } else if (token.getType() == TokenType.TK_IDENTIFIER
                || token.getType() == TokenType.TK_INT
                || token.getType() == TokenType.TK_CHAR
                || token.getType() == TokenType.TK_FLOAT
                || token.getType() == TokenType.TK_CHAR_SEQUENCE) {
            scan();
            return; //ok
        }
    }

    /* ------------------------------------------- *
	            	declaração var		
		    <decl_var> ::= <tipo> <id> ";"
     * ------------------------------------------- */
    public void decl_var() {
        if (First.tipo.contains(token.getType())) {
            scan();
            if (token.getType() == TokenType.TK_IDENTIFIER) {
                scan();
                if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_SEMICOLON) {
                    scan();
                    return;
                } else {
                    System.out.println("não fechou ;");
                }
            } else {
                System.out.println("Identificador Esperado");
            }
        } else {
            System.out.println("Tipo esperado");
        }
    }
         

    /* ------------------------------------------- *
	            	declaração tipo		
		     <tipo> ::= int | float | char
     * ------------------------------------------- */
//    public void tipo() {
//        if (token.getType() == TokenType.TK_KEYWORD_INT
//                || token.getType() == TokenType.TK_KEYWORD_CHAR
//                || token.getType() == TokenType.TK_KEYWORD_FLOAT
//                || token.getType() == TokenType.TK_CHAR_SEQUENCE) {//ver isso aqui <-
//            scan();
//        }
//    }

    /* ------------------------------------------- *
	            	    if		
   if "("<expr_relacional>")" <comando> {else <comando>}?
     * ------------------------------------------- */
    public void if_() {
        if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN) {
            scan();
            if (First.expr_relacional.contains(token.getType())) {
                expr_relacional();
                scan();
                if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED) {
                    scan();
                    if (First.comando.contains(token.getType())) {
                        comando();
                        scan();
                        // while
                        if (token.getType() == TokenType.TK_KEYWORD) {//else
                            scan();
                            if (First.comando.contains(token.getType())) {
                                comando();
                                scan();
                            }
                        }
                    }
                }
            }
        }
    }

    /* ------------------------------------------- *
	            	OUTROS METODOS AUXILIARES
     * ------------------------------------------- */
    private void scan() {
        token = scan.nextToken();
    }
}
