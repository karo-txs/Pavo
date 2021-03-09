package br.com.compiler.exceptions;

import br.com.compiler.util.Cursor;
import br.com.compiler.util.Printer;

public class FloatFormatException extends PersonalizedException{
    private TypeException type = TypeException.NUMBER_FORMAT;
    
    public FloatFormatException(String msg, Cursor cs){
        super("Bad Format of Float Number : "+msg);
        this.setType(type);
        this.generateMsg(cs);
    }
    
    @Override
    public void throwException(){
        System.out.println(Printer.colorMsg(this.getMsg(), 'r'));
        System.exit(0);
    }
}
