package br.com.compiler.app;
import br.com.compiler.lexico.Scanner;
import br.com.compiler.lexico.Token;
import br.com.compiler.util.Printer;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws Exception {
        ArrayList<Token> tokens = new ArrayList<>();
        String filename = "C:\\Users\\karol\\Documents\\NetBeansProjects\\Compavo\\src\\br\\com\\compiler\\arquivos\\input.isi";
        Scanner sc = new Scanner(filename);
        Token token;
        
        do{
            token = sc.nextToken();
            if(token!=null){
                tokens.add(token);
            }
        }while(token!=null);
        Printer.printTable(tokens);
    
    }
}

