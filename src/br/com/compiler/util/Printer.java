package br.com.compiler.util;

import br.com.compiler.lexico.Token;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Printer {

    private static List<List<String>> table;

    /**
     * Adaptado de:
     * <https://codereview.stackexchange.com/questions/213208/printing-out-a-table-in-console>
     *
     * @param token
     */
    public static void printTable(ArrayList<Token> token) {
        List<String> tokens = new ArrayList<>();
        List<String> types = new ArrayList<>();

        tokens.add("Token");
        types.add("Types");

        for (Token t : token) {
            tokens.add(t.getToken());
            if (t.getType().getId() != 0) {
                types.add(Color.getCodColorToId(t.getType().getId()) + t.getType().getText() + Color.ANSI_RESET.getCod());
            } else {
                types.add(t.getType().getText());
            }
        }
        table = Arrays.asList(tokens, types);

        List<Integer> maxLengths = findMaxLengths();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < table.get(0).size(); i++) {
            for (int j = 0; j < table.size(); j++) {
                String currentValue = table.get(j).get(i);
                sb.append(currentValue);
                for (int k = 0; k < (maxLengths.get(j) - currentValue.length() + 2); k++) {
                    sb.append(' ');
                }
            }
            sb.append('\n');
        }
        System.out.println("-----------------------------");
        System.out.print(sb);
        System.out.println("-----------------------------");
    }

    private static List<Integer> findMaxLengths() {
        List<Integer> maxLengths = new ArrayList<>();
        for (List<String> row : table) {
            int maxLength = 0;
            for (String value : row) {
                if (value.length() > maxLength) {
                    maxLength = value.length();
                }
            }
            maxLengths.add(maxLength);
        }
        return maxLengths;
    }

    public static String colorMsg(String msg, char color) {
        return Color.getCodColor(color) + msg + Color.ANSI_RESET.getCod();
    }
}
