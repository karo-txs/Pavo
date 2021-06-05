package br.unicap.compiler.exceptions;

import br.unicap.compiler.exceptions.*;
import br.unicap.compiler.util.Cursor;

public class SyntaxException {
    private String msg;
    private TypeException type = TypeException.SYNTAX;
    
    public SyntaxException(String msg, Cursor cs, String filename){
        this.msg = "Exception in thread \"main\" br.com.compiler.exceptions."+this.type.getName()+": "+msg+"\n" +
"	at "+filename+"(line: "+cs.getLine()+" | col: "+cs.getColun()+")";
    }
    
    public String throwException(){
        return this.msg;
    }
}
