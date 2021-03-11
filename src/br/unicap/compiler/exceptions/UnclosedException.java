package br.unicap.compiler.exceptions;

import br.unicap.compiler.util.Cursor;

public class UnclosedException extends PersonalizedException{
    private TypeException type = TypeException.UNCLOSED;
    
    public UnclosedException(String msg, Cursor cs){
        super("Unclosed "+msg);
        this.setType(type);
        this.generateMsg(cs);
    }
}
