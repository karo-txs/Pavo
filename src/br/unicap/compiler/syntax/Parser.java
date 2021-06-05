package br.unicap.compiler.syntax;

import br.unicap.compiler.exceptions.SyntaxException;
import br.unicap.compiler.lexicon.Scanner;
import br.unicap.compiler.lexicon.Token;
import br.unicap.compiler.lexicon.TokenType;
import br.unicap.compiler.semantic.Rules;
import java.util.List;

public class Parser {
    private Scanner scan;
    private Token token;
    private String nameArchive;
    private String exception;
    private Rules rulesSemantic;
    private boolean createdMain = false;

    public Parser(Scanner scan, String nameArchive) {
        this.scan = scan;
        this.nameArchive = nameArchive;
        exception = "NULL";
    }
   
    public void runParser() {
        rulesSemantic = new Rules();
        
        scan();
        if (scan.isEOF()) {
            throwException("It does not have a main method");
        }
        while (!scan.isEOF() && exception.equals("NULL")) {
            if (verification(TokenType.TK_KEYWORD_INT)) {
                scan();
                program_();
            } else if (verification(TokenType.TK_KEYWORD_VOID)) {
                scan();
                if (verification(TokenType.TK_IDENTIFIER)) {
                    scan();
                    method();
                } else {
                    throwException("<identifier> expected");
                }
            } else {
                throwException("Cannot find symbol");
            }
        }
        if (createdMain == false) {
            throwException("It does not have a main method");
        }
    }

    public void program_() {
        if (verification(TokenType.TK_KEYWORD_MAIN)) {
            if (createdMain) {
                throwException("Duplicate main method");
            } else {
                createdMain = true;
                scan();
                main();
            }
        } else if (verification(TokenType.TK_IDENTIFIER)) {
            scan();
            method();
        } else {
            throwException("<identifier> expected");
        }
    }

    public void method() {
        if (verification(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN)) {
            scan();
            if (verification(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                scan();
                block();
            } else {
                throwException("')' expected");
            }
        } else {
            throwException("'(' expected");
        }
    }

    public void main(){
        if (verification(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN)) {
            scan();
            if (verification(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                scan();
                block();
            } else {
                throwException("')' expected");
            }
        } else {
            throwException("'(' expected");
        }
    }

    public void block() {
        if (verification(TokenType.TK_SPECIAL_CHARACTER_BRACES_OPEN)) {
            scan();

            if (token != null && verification(TokenType.TK_SPECIAL_CHARACTER_BRACES_CLOSED)) {
                scan();
                return;
            }
            if (scan.isEOF() && token != null && verification(TokenType.TK_SPECIAL_CHARACTER_BRACES_CLOSED)
                    || token == null) {
                throwException("'}' expected");
                return;
            }
            while (exception.equals("NULL") && token != null && !scan.isEOF() && (token.getType() != 
                    TokenType.TK_SPECIAL_CHARACTER_BRACES_CLOSED)) {
                if (first(First.statement_var)) {
                    while (first(First.statement_var)) {
                        statement_var();
                    }
                } else if (first(First.command)) {
                    while (first(First.command)) {
                        command();
                    }
                } else if (first(First.print)) {
                    while (first(First.print)) {
                        print();
                    }
                } else {
                    throwException("Illegal start of type");
                }
            }
            if (scan.isEOF() && token != null && !verification(TokenType.TK_SPECIAL_CHARACTER_BRACES_CLOSED)
                    || token == null) {
                throwException("'}' expected");
                return;
            }
            if (verification(TokenType.TK_SPECIAL_CHARACTER_BRACES_CLOSED)) {
                scan();
            } else {
                throwException("'}' expected");
            }
        } else {
            throwException("'{' expected");
        }
    }
    
    public void command() {
        if (first(First.basic_command)) {
            basic_command();
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

    public void command_() {
        if (first(First.basic_command)) {
            basic_command();
        } else if (first(First.if_)) {
            if_();
        }
    }

    public void basic_command() {
        if (first(First.assignment)) {
            assignment();
        } else if (first(First.block)) {
            block();
        }
    }

    public void assignment() {
        if (verification(TokenType.TK_IDENTIFIER)) {
            scan();
            if (verification(TokenType.TK_ARITHMETIC_OPERATOR_ASSIGN)) {
                scan();
                if (first(First.arithmetic_exp)) {
                    arithmetic_exp();
                    if (verification(TokenType.TK_SPECIAL_CHARACTER_SEMICOLON)) {
                        scan();
                    } else {
                        throwException("';' expected");
                    }
                } else {
                    throwException("Illegal start of expression");
                }
            } else if (verification(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN)) {
                method_call();
            } else {
                throwException("';' expected");
            }
        }
    }

    public void assignment_() {
        if (verification(TokenType.TK_IDENTIFIER)) {
            scan();
            if (verification(TokenType.TK_ARITHMETIC_OPERATOR_ASSIGN)) {
                scan();
                if (first(First.arithmetic_exp)) {
                    arithmetic_exp();
                } else {
                    throwException("Illegal start of expression");
                }
            } else {
                throwException("'=' expected");
            }
        }
    }

    public void method_call() {
        if (verification(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN)) { //não precisa
            scan();
            if (verification(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                scan();
                if (verification(TokenType.TK_SPECIAL_CHARACTER_SEMICOLON)) {
                    scan();
                } else {
                    throwException("';' expected");
                }
            } else {
                throwException("')' expected");
            }
        } else {
            throwException("'(' expected");
        }
    }

    public void relational_exp() {
        if (first(First.arithmetic_exp)) {
            arithmetic_exp();
            if (first(First.relational_operator)) {
                relational_operator();
                if (first(First.arithmetic_exp)) {
                    arithmetic_exp();
                } else {
                    throwException("Illegal start of expression");
                }
            } else {
                throwException("<relational_operator> expected");
            }
        } else {
            throwException("Illegal start of expression");
        }
    }
    
    public void logical_exp() {
        if (first(First.relational_exp)) {
            relational_exp();
            logical();
        } else {
            throwException("Illegal start of expression");
        }
    }

    public void logical() {
        if (!scan.getException().equals("NULL")) {
            throwException(scan.getException());
        } else if (token.getType() == TokenType.TK_LOGIC_AND || token.getType() == TokenType.TK_LOGIC_OR) {
            scan();
            if (first(First.logical_exp)) {
                logical_exp();
            }
        } 
    }

    public void relational_operator() {
        if (token.getType() == TokenType.TK_RELATIONAL_OPERATOR_EQUAL
                || token.getType() == TokenType.TK_RELATIONAL_OPERATOR_LESS
                || token.getType() == TokenType.TK_RELATIONAL_OPERATOR_LESS_EQUAL
                || token.getType() == TokenType.TK_RELATIONAL_OPERATOR_MORE
                || token.getType() == TokenType.TK_RELATIONAL_OPERATOR_MORE_EQUAL
                || token.getType() == TokenType.TK_RELATIONAL_OPERATOR_NOT
                || token.getType() == TokenType.TK_RELATIONAL_OPERATOR_NOT_EQUAL) {
            scan();
        } 
    }

    public void arithmetic_exp() {
        if (first(First.term)) {
            term();
            arithmetic_exp_();
        }
    }

    public void arithmetic_exp_() {
        if (verification(TokenType.TK_ARITHMETIC_OPERATOR_PLUS)) {
            scan();
            if (first(First.term)) {
                term();
                arithmetic_exp_();
            } else {
                throwException("Illegal start of expression");
            }
        } else if (verification(TokenType.TK_ARITHMETIC_OPERATOR_MINUS)) {
            scan();
            if (first(First.term)) {
                term();
                arithmetic_exp_();
            } else {
               throwException("Illegal start of expression");
            }
        }
    }

    public void term() {
        if (first(First.factor)) {
            factor();
            term_();
        }
    }

    public void term_() {
        if (verification(TokenType.TK_ARITHMETIC_OPERATOR_MULTIPLICATION)) {
            scan();
            if (first(First.factor)) {
                factor();
                term_();
            } else {
                throwException("Illegal start of expression");
            }
        } else if (verification(TokenType.TK_ARITHMETIC_OPERATOR_DIVISION)) {
            scan();
            if (first(First.factor)) {
                factor();
                term_();
            } else {
                throwException("Illegal start of expression");
            }
        } 
    }

    public void factor() {
        if (verification(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN)) {
            scan();
            if (first(First.arithmetic_exp)) {
                arithmetic_exp();
                if (verification(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                    scan();
                } else {
                    throwException("')' expected");
                }
            } else if (verification(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                throwException("Illegal start of expression");
            } else {
                throwException("Illegal start of expression");
            }
        } else if (scan.getException().equals("NULL") && (token.getType() == TokenType.TK_IDENTIFIER
                || token.getType() == TokenType.TK_INT
                || token.getType() == TokenType.TK_CHAR
                || token.getType() == TokenType.TK_FLOAT
                || token.getType() == TokenType.TK_CHAR_SEQUENCE)) {
            scan();
        }
    }

    public void statement_var() {
        if (first(First.type)) {
            scan();
            if (verification(TokenType.TK_IDENTIFIER)) {
                rulesSemantic.add(token);
                scan();
                if (verification(TokenType.TK_SPECIAL_CHARACTER_SEMICOLON)) {
                    scan();
                } else {
                    throwException("';' expected");
                }
            } else {
                throwException("<identifier> expected");
            }
        }
    }

    public void if_() {
        scan();
        if (verification(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN)) {
            scan();
            if (first(First.logical_exp)) {
                logical_exp();
                if (verification(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                    scan();
                    if (first(First.command)) {
                        command();
                        if (token != null
                                && verification(TokenType.TK_KEYWORD_ELSE)) {
                            scan();
                            if (first(First.command_)) {
                                command_();
                            }
                        } else if (token == null) {
                            throwException("'}' expected");
                        }
                    } else if (verification(TokenType.TK_SPECIAL_CHARACTER_BRACES_CLOSED)) {
                        throwException("'{' expected");
                    } else {
                        
                        throwException("Not a command");
                    }
                } else {
                    throwException("')' expected");
                }
            } else {
                throwException("Illegal start of expression");
            }
        } else {
            throwException("'(' expected");
        }
    }

    public void while_() {
        scan();
        if (verification(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN)) {
            scan();
            if (first(First.logical_exp)) {
                logical_exp();
                if (verification(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                    scan();
                    if (first(First.command)) {
                        command();
                    } else {
                        throwException("Illegal start of expression");
                    }
                } else {
                    throwException("')' expected");
                }
            } else {
                throwException("Illegal start of expression");
            }
        } else {
            throwException("'(' expected");
        }
    }

    public void do_while_() {
        scan();
        if (first(First.command)) {
            command();
            if (first(First.while_)) {
                scan();
                if (verification(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN)) {
                    scan();
                    if (first(First.logical_exp)) {
                        logical_exp();
                        if (verification(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                            scan();
                            if (verification(TokenType.TK_SPECIAL_CHARACTER_SEMICOLON)) {
                                scan();
                            } else {
                                throwException("';' expected");
                            }
                        } else {
                            throwException("')' expected");
                        }
                    } else {
                        throwException("Illegal start of expression");
                    }
                } else {
                    throwException("'(' expected");
                }
            } else {
                throwException("<while> expected");
            }
        } else {
            throwException("'{' expected");
        }
    }

    public void for_() {
        scan();
        if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN) {
            scan();
            if (first(First.assignment)) {
                assignment_();
                if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_SEMICOLON) {
                    scan();
                    if (first(First.logical_exp)) {
                        logical_exp();
                        if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_SEMICOLON) {
                            scan();
                            if (first(First.assignment)) {
                                assignment_();
                                if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED) {
                                    scan();
                                    if (first(First.command)) {
                                        command();
                                    } else {
                                        throwException("'{' expected");
                                    }
                                } else {
                                    throwException("')' expected");
                                }
                            } else {
                                throwException("Not a statement");
                            }
                        } else {
                            throwException("';' expected");
                        }
                    } else {
                        throwException("<logical_expression> expected");
                    }
                } else {
                    throwException("';' expected");
                }
            } else {
                throwException("Not a statement");
            }
        } else {
            throwException("'(' expected");
        }
    }

    public void print() {
        scan();
        if (verification(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN)) {
            scan();
            if (first(First.print_)) {
                print_();
                if (verification(TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED)) {
                    scan();
                    if (verification(TokenType.TK_SPECIAL_CHARACTER_SEMICOLON)) {
                        scan();
                    } else {
                        throwException("';' expected");
                    }
                } else {
                    throwException("')' expected");
                }
            } else if (token.getType() != TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED) {
                throwException("Illegal start of expression");
            } else if (token.getType() == TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_CLOSED) {
                scan();
                if (verification(TokenType.TK_SPECIAL_CHARACTER_SEMICOLON)) {
                    scan();
                } else {
                    throwException("';' expected");
                }
            }
        } else {
            throwException("'(' expected");
        }
    }

    public void print_() {
        if (token.getType() == TokenType.TK_CHAR_SEQUENCE) {
            scan();
        } else if (first(First.logical_exp)) {
            logical_exp();
        } else if (first(First.arithmetic_exp)) {
            arithmetic_exp();
        }
    }

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

    private boolean verification(TokenType tokenType) {
        return scan.getException().equals("NULL") 
                && rulesSemantic.getException().equals("NULL")
                && token.getType() == tokenType;
    }

    private boolean first(List first) {
        return token != null && first.contains(token.getType());
    }
}
