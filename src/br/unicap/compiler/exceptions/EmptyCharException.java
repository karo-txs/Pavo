package br.unicap.compiler.exceptions;

import br.unicap.compiler.util.Cursor;

public class EmptyCharException extends PersonalizedException{
    private TypeException type = TypeException.EMPTY_CHAR;
    
    public EmptyCharException(String msg, Cursor cs, String filename){
        super("Empty Character Literal: "+msg,filename);
        this.setType(type);
        this.generateMsg(cs);
    }
}
