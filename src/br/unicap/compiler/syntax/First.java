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

    static final List<TokenType> iteracao = Arrays.asList(
            TokenType.TK_KEYWORD_WHILE
    );

    static final List<TokenType> if_ = Arrays.asList(
            TokenType.TK_KEYWORD_IF
    );

    static final List<TokenType> bloco = Arrays.asList(
            TokenType.TK_SPECIAL_CHARACTER_BRACES_OPEN);

    static final List<TokenType> comando_basico = concatList(atribuicao, bloco);

    static final List<TokenType> comando = concatList(concatList(Arrays.asList(
            TokenType.TK_KEYWORD_IF
    ), iteracao), comando_basico);
    
    static final List<TokenType> comando_ = concatList(Arrays.asList(
            TokenType.TK_KEYWORD_IF), comando_basico);

    static final List<TokenType> programa = Arrays.asList(
            TokenType.TK_KEYWORD_INT);

    static private List<TokenType> concatList(List<TokenType> primeiraLista, List<TokenType> segundaLista) {
        List<TokenType> novaLista = new ArrayList<TokenType>(primeiraLista);
        novaLista.addAll(segundaLista);
        return novaLista;
    }
}
