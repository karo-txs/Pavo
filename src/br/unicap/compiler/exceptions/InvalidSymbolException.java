package br.unicap.compiler.exceptions;

import br.unicap.compiler.util.Cursor;

public class InvalidSymbolException extends PersonalizedException{
    private TypeException type = TypeException.INVALID_SYMBOL;
    
    public InvalidSymbolException(String msg, Cursor cs, String filename){
        super("Unrecognized Symbol: "+msg,filename);
        this.setType(type);
        this.generateMsg(cs);
    }
}
