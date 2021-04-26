package br.unicap.compiler.syntax;

import br.unicap.compiler.lexicon.TokenType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class First {

    static final List<TokenType> tipo = Arrays.asList(
            TokenType.TK_KEYWORD_FLOAT,
            TokenType.TK_KEYWORD_INT,
            TokenType.TK_KEYWORD_CHAR,
            TokenType.TK_CHAR_SEQUENCE);

    static final List<TokenType> decl_var = tipo;

    static final List<TokenType> fator = Arrays.asList(
            TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN,
            TokenType.TK_IDENTIFIER,
            TokenType.TK_INT,
            TokenType.TK_FLOAT,
            TokenType.TK_CHAR);

    static final List<TokenType> termo = fator;

    static final List<TokenType> expr_arit = termo;

    static final List<TokenType> expr_relacional = expr_arit;
    
    static final List<TokenType> expr_logica= expr_arit;

    static final List<TokenType> op_relacional = Arrays.asList(
            TokenType.TK_RELATIONAL_OPERATOR_EQUAL,
            TokenType.TK_RELATIONAL_OPERATOR_LESS,
            TokenType.TK_RELATIONAL_OPERATOR_LESS_EQUAL,
            TokenType.TK_RELATIONAL_OPERATOR_MORE,
            TokenType.TK_RELATIONAL_OPERATOR_MORE_EQUAL,
            TokenType.TK_RELATIONAL_OPERATOR_NOT,
            TokenType.TK_RELATIONAL_OPERATOR_NOT_EQUAL
    );

    static final List<TokenType> atribuicao = Arrays.asList(
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
            TokenType.TK_CHAR_SEQUENCE),expr_logica, expr_arit);

    static final List<TokenType> bloco = Arrays.asList(
            TokenType.TK_SPECIAL_CHARACTER_BRACES_OPEN);

    static final List<TokenType> comando_basico = concatList(atribuicao, bloco);

    static final List<TokenType> comando = concatList(if_, while_, for_,do_while_, comando_basico);
    
    static final List<TokenType> comando_ = concatList(if_, comando_basico);

    static final List<TokenType> main = Arrays.asList(TokenType.TK_KEYWORD_INT);
    
    static final List<TokenType> metodo = Arrays.asList(TokenType.TK_KEYWORD_VOID, TokenType.TK_KEYWORD_INT);
    
    static final List<TokenType> chamada_metodo = Arrays.asList(TokenType.TK_IDENTIFIER);

    static private List<TokenType> concatList(List<TokenType> ... listas) {
        List<TokenType> novaLista = new ArrayList<TokenType>();
        for (List<TokenType> lista: listas){
            novaLista.addAll(lista);
        }
        return novaLista;
    }
}
