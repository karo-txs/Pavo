package br.unicap.compiler.lexicon;

import java.util.HashMap;
import java.util.Map;

public class Rules {
    
    private static final String[] keyWords = {"asm","auto","break","case","char","const","continue","default","do",
                                       "double","else","enum","extern","float","for","goto","if","int","long",
                                       "main","register","return","short","signed","sizeof","static","struct",
                                       "switch","typedef","union","unsigned","void","volatile","while"};
    
    public static boolean isDigit(char c){
        return c>='0' && c<='9';
    }

    public static boolean isChar(char c){
        return (c>='a' && c<='z') || (c>='A' && c<='Z');
    }

    public static boolean isRelationalOperator(char c){
        return c== '>' || c == '<' || c=='!';
    }
    
    public static boolean isArithmeticOperator(char c){
        return c== '+' || c == '-' || c=='*' || c=='^';
    }
    
    public static boolean isEqual(char c){
        return c=='=';
    }
    
    public static boolean isBar(char c){
        return c == '/';
    }
    
    public static boolean isSingleQuotes(char c){
        return c == '\'';
    }
    
    public static boolean isDoubleQuotes(char c){
        return c == '"';
    }
    
    public static boolean isUnderline(char c){
        return c == '_';
    }
    
    public static boolean isPunctuation(char c){
        return c == '.';
    }
    
    public static boolean isSpecialCharacter(char c){
        return c==')' || c=='(' || c=='{' || c=='}' || c=='[' || c==']'|| c==',' || c==';' 
                || c == ':' || c=='#';
    }

    public static boolean isSpace(char c){
        return c==' ' || c=='\t' || c=='\n' || c=='\r';
    }
    
    public static boolean isJumpLine(char c){
        return c=='\n';
    }
    
    public static boolean isUnrecognizableSymbol(char c){
        return !isEqual(c) && !isSpace(c) && !isSpecialCharacter(c) && !isArithmeticOperator(c) && !isChar(c) &&
               !isDigit(c) && !isRelationalOperator(c) && !isBar(c) && !isSingleQuotes(c) && !isDoubleQuotes(c) &&
               !isPunctuation(c);
    } 
    
    public static boolean isReserved(String s){
        Map<Integer, String> resultsMap = new HashMap<>();
        int key = 0;
        for (String p : keyWords) {
            resultsMap.put(key, p);
            key++;
        }
        return resultsMap.containsValue(s);
    }
}
