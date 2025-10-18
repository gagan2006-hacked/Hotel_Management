package DBConnection;

import java.util.HashMap;
import java.util.Map;

public class EncryptionV1 {
    private static final Map<Character, String> objToSymbol = new HashMap<>();
    private static final Map<String, Character> symbolToObj = new HashMap<>();

    public EncryptionV1() {
        setUp();
    }

    private  void setUp() {
        // Define mappings
        objToSymbol.put('a', "1/12"); objToSymbol.put('A', "12/1");
        objToSymbol.put('b', "23/12"); objToSymbol.put('B', "32/12");
        objToSymbol.put('c', "72*98"); objToSymbol.put('C', "89*72");
        objToSymbol.put('d', "6&7");   objToSymbol.put('D', "9&6");
        objToSymbol.put('e', "0-89");  objToSymbol.put('E', "98>0");
        objToSymbol.put('f', "1/:9");  objToSymbol.put('F', "98>76");
        objToSymbol.put('g', "65%4");  objToSymbol.put('G', "1@45");
        objToSymbol.put('h', "198&5%");objToSymbol.put('H', "%3#4");
        objToSymbol.put('i', ")99&");  objToSymbol.put('I', "(8&6");
        objToSymbol.put('j', "}4^");   objToSymbol.put('J', ":1}>");
        objToSymbol.put('k', "->7&");  objToSymbol.put('K', "6}9*");
        objToSymbol.put('l', "1:&}");  objToSymbol.put('L', ":[&6");
        objToSymbol.put('m', "6^7*>"); objToSymbol.put('M', "%4}>");
        objToSymbol.put('n', "&9$<");  objToSymbol.put('N', "*97$<");
        objToSymbol.put('o', "?>");   objToSymbol.put('O', "8>:");
        objToSymbol.put('p', "#2@");   objToSymbol.put('P', "4$?6");
        objToSymbol.put('q', "$61>?"); objToSymbol.put('Q', "21<%4");
        objToSymbol.put('r', "_0:1!"); objToSymbol.put('R', "!=#2");
        objToSymbol.put('s', "~12#");  objToSymbol.put('S', "]$69%");
        objToSymbol.put('t', "%#3$");  objToSymbol.put('T', "67$-2");
        objToSymbol.put('u', "5^7#");  objToSymbol.put('U', ":[-+3");
        objToSymbol.put('v', "&34^~"); objToSymbol.put('V', "1>0%!");
        objToSymbol.put('w', "@12$9&");objToSymbol.put('W', ":0:^5");
        objToSymbol.put('x', "1@98>"); objToSymbol.put('X', "#123%");
        objToSymbol.put('y', "^56*3"); objToSymbol.put('Y', "$23-^");
        objToSymbol.put('z', "7-(&$"); objToSymbol.put('Z', "%~!34");

        // Numbers (must be unique!)
        objToSymbol.put('0', "N6");
        objToSymbol.put('1', "N5");
        objToSymbol.put('2', "N8");
        objToSymbol.put('3', "N3");
        objToSymbol.put('4', "N9");
        objToSymbol.put('5', "N1");
        objToSymbol.put('6', "N7");
        objToSymbol.put('7', "N0");
        objToSymbol.put('8', "N4");
        objToSymbol.put('9', "N2");

        // Basic special chars
        char[] specials = "~`!@#$%^&*()-_+=[]{}|;:'\",.<>?/ ".toCharArray();
        for (char c : specials) {
            objToSymbol.put(c, "S" + ((int) c)); // Unique safe prefix
        }

        // Reverse map
        for (Map.Entry<Character, String> entry : objToSymbol.entrySet()) {
            symbolToObj.put(entry.getValue(), entry.getKey());
        }
    }

    // Encryption using '|' as a separator (safe and uniform)
    public  String encrypt(String s) {
        if (s == null || s.isBlank()) return s;
        StringBuilder b = new StringBuilder();
        for (char ch : s.toCharArray()) {
            String encoded = objToSymbol.getOrDefault(ch, "U" + ((int) ch));
            b.append(encoded).append('|');
        }
        return b.toString();
    }

    // Decrypt safely
    public String decrypt(String s) {
        if (s == null || s.isBlank()) return s;
        StringBuilder b = new StringBuilder();
        String[] tokens = s.split("\\|");
        for (String token : tokens) {
            if (token.isBlank()) continue;
            if (symbolToObj.containsKey(token)) {
                b.append(symbolToObj.get(token));
            } else if (token.startsWith("S")) { // special char pattern
                try {
                    int ascii = Integer.parseInt(token.substring(1));
                    b.append((char) ascii);
                } catch (Exception ignored) {
                    b.append('?');
                }
            } else if (token.startsWith("U")) { // unknown chars
                try {
                    int ascii = Integer.parseInt(token.substring(1));
                    b.append((char) ascii);
                } catch (Exception ignored) {
                    b.append('?');
                }
            } else {
                b.append('?');
            }
        }
        return b.toString();
    }

    public static void main(String[] args) {

    }
}
