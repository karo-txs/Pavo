package br.unicap.compiler.exceptions;

import br.unicap.compiler.util.Cursor;

public class NumberFormatException extends PersonalizedException{
    private TypeException type = TypeException.NUMBER_FORMAT;
    
    public NumberFormatException(String msg, Cursor cs){
        super("Bad Format of "+msg);
        this.setType(type);
        this.generateMsg(cs);
    }
}
