package br.com.compiler.exceptions;

import br.com.compiler.util.Cursor;
import br.com.compiler.util.Printer;

public class InvalidOperatorException extends PersonalizedException{
    private TypeException type = TypeException.INVALID_OPERATOR;
    
    public InvalidOperatorException(String msg,Cursor cs){
        super("Invalid Operator : "+msg);
        this.setType(type);
        this.generateMsg(cs);
    }
    
    @Override
    public void throwException(){
        System.out.println(Printer.colorMsg(this.getMsg(), 'r'));
        System.exit(0);
    }
}
