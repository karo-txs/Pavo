package br.unicap.compiler.syntax;

import br.unicap.compiler.lexicon.TokenType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class First {

    static final List<TokenType> type = Arrays.asList(
            TokenType.TK_KEYWORD_FLOAT,
            TokenType.TK_KEYWORD_INT,
            TokenType.TK_KEYWORD_CHAR,
            TokenType.TK_CHAR_SEQUENCE);

    static final List<TokenType> statement_var = type;

    static final List<TokenType> factor = Arrays.asList(
            TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN,
            TokenType.TK_IDENTIFIER,
            TokenType.TK_INT,
            TokenType.TK_FLOAT,
            TokenType.TK_CHAR);

    static final List<TokenType> term = factor;

    static final List<TokenType> arithmetic_exp = term;

    static final List<TokenType> relational_exp = arithmetic_exp;
    
    static final List<TokenType> logical_exp = arithmetic_exp;

    static final List<TokenType> relational_operator = Arrays.asList(
            TokenType.TK_RELATIONAL_OPERATOR_EQUAL,
            TokenType.TK_RELATIONAL_OPERATOR_LESS,
            TokenType.TK_RELATIONAL_OPERATOR_LESS_EQUAL,
            TokenType.TK_RELATIONAL_OPERATOR_MORE,
            TokenType.TK_RELATIONAL_OPERATOR_MORE_EQUAL,
            TokenType.TK_RELATIONAL_OPERATOR_NOT,
            TokenType.TK_RELATIONAL_OPERATOR_NOT_EQUAL
    );

    static final List<TokenType> assignment = Arrays.asList(
            TokenType.TK_IDENTIFIER);

    static final List<TokenType> while_ = Arrays.asList(
            TokenType.TK_KEYWORD_WHILE);
    
    static final List<TokenType> do_while_ = Arrays.asList(
            TokenType.TK_KEYWORD_DO_WHILE);
    
    static final List<TokenType> for_ = Arrays.asList(
            TokenType.TK_KEYWORD_FOR);

    static final List<TokenType> if_ = Arrays.asList(
            TokenType.TK_KEYWORD_IF);
    
    static final List<TokenType> print = Arrays.asList(
            TokenType.TK_KEYWORD_PRINT);
    
    static final List<TokenType> print_ = concatList(Arrays.asList(
            TokenType.TK_CHAR_SEQUENCE),logical_exp, arithmetic_exp);

    static final List<TokenType> block = Arrays.asList(
            TokenType.TK_SPECIAL_CHARACTER_BRACES_OPEN);

    static final List<TokenType> basic_command = concatList(assignment, block);

    static final List<TokenType> command = concatList(if_, while_, for_,do_while_, basic_command);
    
    static final List<TokenType> command_ = concatList(if_, basic_command);

    static final List<TokenType> main = Arrays.asList(TokenType.TK_KEYWORD_INT);
    
    static final List<TokenType> method = Arrays.asList(TokenType.TK_KEYWORD_VOID, TokenType.TK_KEYWORD_INT);
    
    static final List<TokenType> method_call = Arrays.asList(TokenType.TK_IDENTIFIER);

    static private List<TokenType> concatList(List<TokenType> ... lists) {
        List<TokenType> newList = new ArrayList<TokenType>();
        for (List<TokenType> list: lists){
            newList.addAll(list);
        }
        return newList;
    }
}
