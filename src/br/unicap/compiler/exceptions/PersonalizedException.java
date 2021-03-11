package br.com.compiler.exceptions;

import br.com.compiler.util.Cursor;

public abstract class PersonalizedException {
    private String msg;
    private TypeException type;
    
    public PersonalizedException(String msg){
        this.msg = msg;
        
    }
    
    public void generateMsg(Cursor cs){
        this.msg = "Exception in thread \"main\" br.com.compiler.exceptions."+this.type.getName()+": "+msg+"\n" +
"	at br.com.compiler.arquivos.input.izi(line: "+cs.getLine()+" | col: "+cs.getColun()+")";
    }
    
    public String getMsg(){
        return msg;
    }
    
    public void setType(TypeException type){
        this.type = type;
    }
    
    public abstract void throwException();
}
