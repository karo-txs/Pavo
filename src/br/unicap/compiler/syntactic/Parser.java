package br.unicap.compiler.syntactic;

import br.unicap.compiler.exceptions.syntactic.SyntaxException;
import br.unicap.compiler.lexicon.Scanner;
import br.unicap.compiler.lexicon.Token;
import br.unicap.compiler.lexicon.TokenType;

public class Parser {

    private Scanner scan;
    private Token token;

    
    public Parser(Scanner scan) {
        this.scan = scan;
    }
    
    /*
    public void runParser(){
        scan();
        if(!scanner.eof()){
            programa();
            if(!scanner.eof()){
                parserError(CodigosToken.EOF);
            }
        }else parserError(First.programa);
    }
    
    public void program(){
        //int main"("")" <bloco>
        
        if(token.getCodigo() == CodigosToken.INT){
            scan();
            if(token.getCodigo() == CodigosToken.MAIN){
                scan();
                if(token.getCodigo() == CodigosToken.ABRE_PARENTESES){
                    scan();
                    if(token.getCodigo() == CodigosToken.FECHA_PARENTESES){
                        scan();
                        bloco();
                    }else parserError(CodigosToken.FECHA_PARENTESES);//retorna o erro
                }else parserError(CodigosToken.ABRE_PARENTESES);
            } else parserError(CodigosToken.MAIN);
        } else parserError(CodigosToken.INT);
    }*/
    /* ------------------------------------------- *
                       BLOCK <BLK>
     * ------------------------------------------- */ 
    public void BLK_S(){
        token = scan();
        System.out.println("TK: "+token.getType());
        if(token.getType() == TokenType.TK_SPECIAL_CHARACTER_BRACES_OPEN){
            System.out.println("Bloco aberto");
            BLK_OP();
        }else{
            //erro não abriu o bloco
        }
    }
    
    private void BLK_OP(){
        do{
        AO_S();
        token = scan();
        }while(token.getType()!=TokenType.TK_SPECIAL_CHARACTER_BRACES_CLOSED);
        System.out.println("Bloco fechado");// so pega {123-123;} (uma expressão apenas)
    }
    
    /* ------------------------------------------- *
                Arithmetic Operation <AO>
     * ------------------------------------------- */
    public void AO_S() {
        AO_T();
        AO_Sl();
    }

    private void AO_Sl() {
        token = scan();
        System.out.println("TK: "+token.getType());
        if (token.getType() != TokenType.TK_PUNCTUATION) { // se for diferente e nao for um OP nem T ? dar erro de fechamento
            AO_OP();
            AO_T();
            AO_Sl();
        }
    }

    private void AO_T() {
        token = scan();
        System.out.println("Text: "+token.getToken());
        System.out.println("TK: "+token.getType());
        if (token.getType() != TokenType.TK_IDENTIFIER
                && token.getType() != TokenType.TK_INT
                && token.getType() != TokenType.TK_FLOAT) {
            //Parar programa
            SyntaxException exception = new SyntaxException("Identifier or Number Expected, found "+
                                                            token.getType().getText()+"("+token.getToken()+")");
            System.out.println(exception.throwException());
        }
    }

    private void AO_OP() {
        if (token.getType() != TokenType.TK_ARITHMETIC_OPERATOR_ASSIGN
                && token.getType() != TokenType.TK_ARITHMETIC_OPERATOR_DIVISION
                && token.getType() != TokenType.TK_ARITHMETIC_OPERATOR_MINUS
                && token.getType() != TokenType.TK_ARITHMETIC_OPERATOR_MULTIPLICATION
                && token.getType() != TokenType.TK_ARITHMETIC_OPERATOR_PLUS
                && token.getType() != TokenType.TK_ARITHMETIC_OPERATOR_POWER) {
            SyntaxException exception = new SyntaxException("Operator Expected, found "+
                                                            token.getType().getText()+"("+token.getToken()+")");
            System.out.println(exception.throwException());
        }
    }
    
    /*
        OUTROS METODOS AUXILIARES
    */
    
    private Token scan(){
        return scan.nextToken();
    }
}
