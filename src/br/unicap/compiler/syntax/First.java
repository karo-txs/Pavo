
public class First{
	
	static ArrayList<TokenType> tipo = Arrays.asList(
				TokenType.TK_FLOAT,
				TokenType.TK_INT,
				TokenType.TK_CHAR,
				TokenType.TK_CHAR_SEQUENCE,
    );

	static ArrayList<TokenType> decl_var = tipo;

	static ArrayList<TokenType> fator = concatList(asList(
	            TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN,
				TokenType.TK_IDENTIFIER), 
				tipo);

	static ArrayList<TokenType> termo = fator;

	static ArrayList<TokenType> expr_arit = termo;

	static ArrayList<TokenType> expr_relacional = expr_arit;

	static ArrayList<TokenType> atribuicao = asList(
				TokenType.TK_IDENTIFIER
    );

	static ArrayList<TokenType> atribuicao = asList(
				TokenType.TK_KEYWORD//WHILE
    );

	static ArrayList<TokenType> comando_basico = concatList(atribuicao, bloco);

	static ArrayList<TokenType> comando =  concatList(concatList(asList(
				TokenType.TK_KEYWORD//IF
				),iteracao), comando_basico);

	static ArrayList<TokenType> bloco = asList(
	            TokenType.TK_SPECIAL_CHARACTER_PARENTHESES_OPEN
	}

	static ArrayList<TokenType> programa = asList(
	           TokenType.TK_INT
	}

	private List<TokenType> concatList(List<TokenType> primeiraLista, List<TokenType> segundaLista) {
		List<TokenType> novaLista = new ArrayList<TokenType>(primeiraLista);
		novaLista.addAll(segundaLista);
		return novaLista;
	}
}