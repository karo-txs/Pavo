package br.unicap.compiler.lexicon;

public class Token {
    private String type;
    private String token;
    
    public Token(TokenType type, String token){
        super();
        this.type = type.getText();
        this.token = token;
    }

    public Token(){
        super();
    }
    
    public String getType(){
        return this.type;
    }
    
    public String getToken(){
        return this.token;
    }

    public void setToken(String token){
        this.token= token;
    }

    @Override
    public String toString(){
        return "Type = "+this.type+", Text = "+this.token;
    }
}
