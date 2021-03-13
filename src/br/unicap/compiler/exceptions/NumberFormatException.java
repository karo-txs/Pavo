package br.unicap.compiler.exceptions;

import br.unicap.compiler.util.Cursor;

public class NumberFormatException extends PersonalizedException{
    private TypeException type = TypeException.NUMBER_FORMAT;
    
    public NumberFormatException(String msg, Cursor cs, String filename){
        super("Bad Format of "+msg,filename);
        this.setType(type);
        this.generateMsg(cs);
    }
}
