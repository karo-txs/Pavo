package br.com.compiler.exceptions;

import br.com.compiler.util.Cursor;
import br.com.compiler.util.Printer;

public class InvalidSymbolException extends PersonalizedException{
    private TypeException type = TypeException.INVALID_SYMBOL;
    
    public InvalidSymbolException(String msg, Cursor cs){
        super("Unrecognized SYMBOL: "+msg);
        this.setType(type);
        this.generateMsg(cs);
    }
    
    @Override
    public void throwException(){
        System.out.println(Printer.colorMsg(this.getMsg(), 'r'));
        System.exit(0);
    }
}
