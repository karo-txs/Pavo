package br.unicap.compiler.codeGen;

public class CodeGen {

    static String codeText;

    public CodeGen() {
        codeText = " ";
    }

    public void addCode(String code) {
        codeText += code;
    }

    public String printCode() {
        return codeText;
    }
}
