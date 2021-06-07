package br.unicap.compiler.lexicon;

import javafx.scene.control.Label;

public class Token {

    private Label typeColor;
    private String token;
    private TokenType type;

    public Token(TokenType type, String token) {
        super();
        this.typeColor = new Label(type.getText());
        this.typeColor.setStyle(type.getColor());
        this.token = token;
        this.type = type;
    }

    public Token() {
        super();
    }
    
    public void updateColor(){
        this.typeColor.setStyle(type.getColor());
    }

    public Label getTypeColor() {
        return this.typeColor;
    }
    
    public TokenType getType() {
        return this.type;
    }
    
    public void setType(TokenType type) {
        this.type = type;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
