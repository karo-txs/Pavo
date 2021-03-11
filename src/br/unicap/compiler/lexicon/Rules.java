package br.unicap.compiler.lexicon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Rules {
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
        return c== '+' || c == '-' || c=='*';
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
               !isDigit(c) && !isRelationalOperator(c) && !isBar(c) && !isSingleQuotes(c) && !isDoubleQuotes(c);
    } 
    
    public static boolean isReserved(String s){
        String filename;
        //filename = "C:\\Users\\karol\\Documents\\NetBeansProjects\\Compavo\\src\\br\\com\\compiler\\arquivos\\reservedWords.txt";
        filename = "C:\\Users\\Luiz\\Documents\\NetBeansProjects\\Compiler\\src\\br\\unicap\\compiler\\files\\keyWords.txt";
        
        Path path = Paths.get(filename); 
        List<String> palavras = null;
        try {
            palavras = Files.readAllLines(path);
        } catch (IOException ex) {
            Logger.getLogger(Rules.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Map<Integer, String> resultsMap = new HashMap<>();
        int key = 0;
        for (String p : palavras) {
            resultsMap.put(key, p);
            key++;
        }
        return resultsMap.containsValue(s);
    }
}
