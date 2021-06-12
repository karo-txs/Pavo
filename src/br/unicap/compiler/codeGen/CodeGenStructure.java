package br.unicap.compiler.codeGen;

import br.unicap.compiler.lexicon.Token;
import br.unicap.compiler.semantic.Rules;
import java.util.List;

public class CodeGenStructure extends CodeGen {

    private final CodeGen code;
    private final CodeGenOperation codeOp;
    private int pos;
    private String operator;
    private boolean structureOpen, isWhile, isFor;

    public CodeGenStructure(CodeGen code, CodeGenOperation codeOp) {
        this.code = code;
        this.codeOp = codeOp;
        this.pos = 0;
        this.operator = null;
        this.structureOpen = this.isWhile = false;
    }
    
    public void generateIf() {
        if(operator!=null){
            String t1 = "T"+(codeOp.getPos()-1);
            String t2 = "T"+codeOp.getPos();
            
            pos++;
            code.addCode("L" + pos + ": if " + t1 + " " + operator + " " + t2
                    + " goto " + "L" + (pos + 1)+"\n");//L1: T1 < T2 goto L2
            pos++;
            structureOpen = true;
        }
    }
    
    public void generateWhile() {
        if(operator!=null){
            generateIf();
            isWhile = true;
        }
    }
    
    public void generateFor() {
        if(operator!=null){
            generateIf();
            isFor = true;
        }
    }
    
    public void addOperator(Token op){
        operator = invertOperation(op);
    }

    private String invertOperation(Token op) {
        switch (op.getType()) {
            case TK_RELATIONAL_OPERATOR_EQUAL:
                return "!=";
            case TK_RELATIONAL_OPERATOR_MORE:
                return "<=";
            case TK_RELATIONAL_OPERATOR_MORE_EQUAL:
                return "<";
            case TK_RELATIONAL_OPERATOR_LESS:
                return ">=";
            case TK_RELATIONAL_OPERATOR_LESS_EQUAL:
                return ">";
            case TK_RELATIONAL_OPERATOR_NOT_EQUAL:
                return "==";
        }
        return null;
    }
    
    public void closeStructure(Rules r, List<Token> scope, List<Token> ... aux){
        if(structureOpen){
            if(isWhile){
                code.addCode("goto L"+(pos-1)+"\n");  
                code.addCode("L" + pos + ":");  
            }else if(isFor){
                codeOp.calculateAux(scope, aux[0], r);
                code.addCode("goto L"+(pos-1)+"\n");  
                code.addCode("L" + pos + ":");  
            }else{
                code.addCode("L" + pos + ":");
            }
            structureOpen = false; 
        }
    }
    
    public int getPos(){
        return pos;
    }
}
