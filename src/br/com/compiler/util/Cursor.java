package br.com.compiler.util;

public class Cursor {
    private int line;
    private int colun;
    
    public Cursor(){
        this.line = 1;
        this.colun = 0;
    }
    
    public void moveCursorFront(char c){
        if(c=='\n'){
            this.colun = 0;
            this.line++;
        }else{
            this.colun++;
        }
    }
    
    public void moveCursorBack(char c, int colAnt){
        if(c=='\n'){
            this.colun=colAnt;
            this.line--;
        }else{
            this.colun--;
        }
    }
    
    public int getLine(){
        return this.line;
    }
    
    public int getColun(){
        return this.colun;
    }
}
