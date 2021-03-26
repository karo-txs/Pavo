package br.unicap.compiler.exceptions.syntactic;

import br.unicap.compiler.exceptions.*;

public class SyntaxException {
    private String msg;
    private TypeException type;
    private String filename;
    
    public SyntaxException(String msg){
        this.msg = msg;
        this.filename = "teste.txt";
    }
    
    public void generateMsg(){
        this.msg = "Exception in thread \"main\" br.com.compiler.exceptions."+this.type.getName()+": "+msg+"\n" +
"	at ";
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
