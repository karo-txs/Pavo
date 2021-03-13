package br.unicap.compiler.exceptions;

import br.unicap.compiler.util.Cursor;

public class IdentifierFormatException extends PersonalizedException{
    private TypeException type = TypeException.IDENTIFIER_FORMAT;
    
    public IdentifierFormatException(String msg, Cursor cs, String filename){
        super("Bad Format Identifier: "+msg,filename);
        this.setType(type);
        this.generateMsg(cs);
    }
}
