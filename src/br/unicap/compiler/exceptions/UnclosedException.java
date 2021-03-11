package br.com.compiler.exceptions;

import br.com.compiler.util.Cursor;
import br.com.compiler.util.Printer;

public class UnclosedException extends PersonalizedException{
    private TypeException type = TypeException.UNCLOSED;
    
    public UnclosedException(String msg, Cursor cs){
        super("Unclosed "+msg);
        this.setType(type);
        this.generateMsg(cs);
    }
    
    @Override
    public void throwException(){
        System.out.println(Printer.colorMsg(this.getMsg(), 'r'));
        System.exit(0);
    }
}
