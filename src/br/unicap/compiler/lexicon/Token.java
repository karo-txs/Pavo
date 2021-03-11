package br.com.compiler.lexico;

public class Token {
    private TokenType type;
    private String token;
    
    public Token(TokenType type, String token){
        super();
        this.type = type;
        this.token = token;
    }

    public Token(){
        super();
    }
    
    public TokenType getType(){
        return this.type;
    }

    public void setType(TokenType type){
        this.type = type;
    }

    public String getToken(){
        return this.token;
    }

    public void setToken(String token){
        this.token= token;
    }

    @Override
    public String toString(){
        return "Type = "+this.type.getText()+", Text = "+this.token;
    }
}
