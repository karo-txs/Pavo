package br.unicap.compiler.exceptions;

import br.unicap.compiler.util.Cursor;

public class SemanticException {
    private String msg;
    private TypeException type = TypeException.SEMANTIC;
    
    public SemanticException(String msg){
        String filename = "unlited.txt";
        
        this.msg = "Exception in thread \"main\" br.com.compiler.exceptions."+this.type.getName()+": "+msg+"\n" +
"	at "+filename+"(line: 0 | col: 0)";
    }
    
    public String throwException(){
        return this.msg;
    }
}
  

