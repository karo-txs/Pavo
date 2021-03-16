package br.unicap.compiler.view.util;

public enum Color {
    BLUE("-fx-text-background-color: #2A8FC7;","-fx-text-background-color: #59C3F3;"),
    GREEN("-fx-text-background-color: green;","-fx-text-background-color: #8DE38D;"),
    RED("-fx-text-background-color: #B82524;","-fx-text-background-color: #B7424A;"),
    YELLOW("-fx-text-background-color: #F0C020;","-fx-text-background-color: #DAA82E;"),
    PINK("-fx-text-background-color: #FF69B4;","-fx-text-background-color: pink;"),
    ORANGE("-fx-text-background-color: #F7941E;","-fx-text-background-color: #EF8520;"),
    PURPLE("-fx-text-background-color: #7F0055;","-fx-text-background-color: #A37CC1;"),
    BROWN("-fx-text-background-color: #794F40;","-fx-text-background-color: #D2B48C;"),
    BEIGE("-fx-text-background-color: #E5C9A1;","-fx-text-background-color: #D7AC6A;"),
    GRAY("-fx-text-background-color: #787878;","-fx-text-background-color: #C8C8C8;");
    
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
