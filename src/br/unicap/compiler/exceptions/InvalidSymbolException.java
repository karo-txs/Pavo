package br.unicap.compiler.exceptions;

import br.unicap.compiler.util.Cursor;

public class InvalidSymbolException extends PersonalizedException{
    private TypeException type = TypeException.INVALID_SYMBOL;
    
    public InvalidSymbolException(String msg, Cursor cs){
        super("Unrecognized SYMBOL: "+msg);
        this.setType(type);
        this.generateMsg(cs);
    }
}
