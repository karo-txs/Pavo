package br.unicap.compiler.exceptions;

import br.unicap.compiler.util.Cursor;

public class EmptyCharException extends PersonalizedException{
    private TypeException type = TypeException.IDENTIFIER_FORMAT;
    
    public EmptyCharException(String msg, Cursor cs){
        super("Empty Character Literal: "+msg);
        this.setType(type);
        this.generateMsg(cs);
    }
}
