package br.com.compiler.exceptions;

import br.com.compiler.util.Cursor;
import br.com.compiler.util.Printer;

public class EmptyCharException extends PersonalizedException{
    private TypeException type = TypeException.IDENTIFIER_FORMAT;
    
    public EmptyCharException(String msg, Cursor cs){
        super("Empty Character Literal: "+msg);
        this.setType(type);
        this.generateMsg(cs);
    }
    
    @Override
    public void throwException(){
        System.out.println(Printer.colorMsg(this.getMsg(), 'r'));
        System.exit(0);
    }
}
