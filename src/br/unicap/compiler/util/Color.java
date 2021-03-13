package br.unicap.compiler.util;

public enum Color {
    BLUE("-fx-text-background-color: blue;"),
    GREEN("-fx-text-background-color: green;"),
    RED("-fx-text-background-color: red;"),
    YELLOW("-fx-text-background-color: yellow;"),
    PINK("-fx-text-background-color: pink;"),
    ORANGE("-fx-text-background-color: orange;"),
    PURPLE("-fx-text-background-color: purple;"),
    BROWN("-fx-text-background-color: brown;"),
    BEIGE("-fx-text-background-color: beige;"),
    GRAY("-fx-text-background-color: gray;");
    
    private String colorName;
    
    private Color(String colorName){
        this.colorName = colorName;
    }
    
    public String getCodColor(){
        return this.colorName;
    }
}
