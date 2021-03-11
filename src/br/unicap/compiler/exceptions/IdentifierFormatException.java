package br.com.compiler.exceptions;

import br.com.compiler.util.Cursor;
import br.com.compiler.util.Printer;

public class IdentifierFormatException extends PersonalizedException{
    private TypeException type = TypeException.IDENTIFIER_FORMAT;
    
    public IdentifierFormatException(String msg, Cursor cs){
        super("Bad Format Identifier: "+msg);
        this.setType(type);
        this.generateMsg(cs);
    }
    
    @Override
    public void throwException(){
        System.out.println(Printer.colorMsg(this.getMsg(), 'r'));
        System.exit(0);
    }
}
