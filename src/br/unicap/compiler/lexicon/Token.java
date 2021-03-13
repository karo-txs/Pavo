package br.unicap.compiler.lexicon;

import javafx.scene.control.Label;

public class Token {

    private Label type;
    private String token;

    public Token(TokenType type, String token) {
        super();
        this.type = new Label(type.getText());
        this.type.setStyle(type.getColor().getCodColor());
        this.token = token;
    }

    public Token() {
        super();
    }

    public Label getType() {
        return this.type;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
