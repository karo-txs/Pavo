package br.unicap.compiler.exceptions;

import br.unicap.compiler.util.Cursor;

public class InvalidOperatorException extends PersonalizedException{
    private TypeException type = TypeException.INVALID_OPERATOR;
    
    public InvalidOperatorException(String msg,Cursor cs, String filename){
        super("Invalid Operator : "+msg,filename);
        this.setType(type);
        this.generateMsg(cs);
    }
}
