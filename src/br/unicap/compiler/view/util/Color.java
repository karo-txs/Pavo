package br.unicap.compiler.view.util;

public enum Color {
    BLUE("-fx-text-background-color: blue;","-fx-text-background-color: blue;"),
    GREEN(",-fx-text-background-color: green;","-fx-text-background-color: green;"),
    RED("-fx-text-background-color: red;","-fx-text-background-color: red;"),
    YELLOW("-fx-text-background-color: yellow;","-fx-text-background-color: yellow;"),
    PINK("-fx-text-background-color: pink;","-fx-text-background-color: pink;"),
    ORANGE("-fx-text-background-color: orange;","-fx-text-background-color: orange;"),
    PURPLE("-fx-text-background-color: purple;","-fx-text-background-color: purple;"),
    BROWN("-fx-text-background-color: brown;","-fx-text-background-color: brown;"),
    BEIGE("-fx-text-background-color: beige;","-fx-text-background-color: beige;"),
    GRAY("-fx-text-background-color: gray;","-fx-text-background-color: gray;");
    
    private String colorInLightMode;
    private String colorInDarkMode;
    
    private Color(String colorInLightMode,String colorInDarkMode){
        this.colorInLightMode = colorInLightMode;
        this.colorInDarkMode = colorInDarkMode;
    }
    
    public String getColorInLightMode(){
        return this.colorInLightMode;
    }
    
    public String getColorInDarkMode(){
        return this.colorInDarkMode;
    }
}
