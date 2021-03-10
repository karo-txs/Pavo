
package br.com.compiler.util;

/**
 * Adaptado de <https://qastack.com.br/programming/5762491/how-to-print-color-in-console-using-system-out-println>
 * @author karol
 */
public enum Color {
    ANSI_RESET("reset","\u001B[0m"),
    BLACK ("k","\u001B[30m"),
    RED("r","\u001B[31m"),
    GREEN("g","\u001B[32m"),
    YELLOW("y","\u001B[33m"),
    BLUE("b","\u001B[34m"),
    PURPLE("p","\u001B[35m"),
    CYAN("c","\u001B[36m"),
    WHITE("w","\u001B[37m");
    
    private String cod;
    private String name;
    
    private Color(String name, String cod){
        this.name =name;
        this.cod = cod;
    }
    
    public String getCod(){
        return this.cod;
    }
    
    public static String getCodColor(char name){
        switch(Character.toLowerCase(name)){
            case 'k': return BLACK.getCod(); 
            case 'r': return RED.getCod(); 
            case 'g': return GREEN.getCod(); 
            case 'y': return YELLOW.getCod();
            case 'b': return BLUE.getCod(); 
            case 'p': return PURPLE.getCod(); 
            case 'c': return CYAN.getCod(); 
            case 'w': return WHITE.getCod();
            default: return null;
        }
    }
    public static String getCodColorToId(int id){
        switch(id){
            case 1: return BLACK.getCod(); 
            case 2: return RED.getCod(); 
            case 3: return GREEN.getCod(); 
            case 4: return YELLOW.getCod();
            case 5: return BLUE.getCod(); 
            case 6: return PURPLE.getCod(); 
            case 7: return CYAN.getCod(); 
            case 8: return WHITE.getCod();
            case 9: return GREEN.getCod();
            default: return null;
        }
    }
}
