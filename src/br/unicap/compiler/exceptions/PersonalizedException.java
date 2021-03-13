package br.unicap.compiler.exceptions;

import br.unicap.compiler.util.Cursor;

public abstract class PersonalizedException {
    private String msg;
    private TypeException type;
    private String filename;
    
    public PersonalizedException(String msg,String filename){
        this.msg = msg;
        this.filename = filename;
    }
    
    public void generateMsg(Cursor cs){
        this.msg = "Exception in thread \"main\" br.com.compiler.exceptions."+this.type.getName()+": "+msg+"\n" +
"	at "+filename+"(line: "+cs.getLine()+" | col: "+cs.getColun()+")";
    }
    
    public String getMsg(){
        return msg;
    }
    
    public void setType(TypeException type){
        this.type = type;
    }
    
    public String throwException(){
        return this.getMsg();
    }
}
