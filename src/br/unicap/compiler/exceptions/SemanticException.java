package br.unicap.compiler.exceptions;

import br.unicap.compiler.util.Cursor;

public class SemanticException implements PersonalizedException{
    private String msg;
    private TypeException type = TypeException.SEMANTIC;
      
        public SemanticException(String msg, Cursor cs, String filename){
        this.msg = "Exception in thread \"main\" br.com.compiler.exceptions."+this.type.getName()+": "+msg+"\n" +
"	at "+filename+"(line: "+cs.getLine()+" | col: "+cs.getColun()+")";
    }
    
    @Override
    public String throwException(){
        return this.msg;
    }
}
  

