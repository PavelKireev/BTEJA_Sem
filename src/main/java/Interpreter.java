import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Interpreter {

    static boolean hadError = false;

    public static void main(String[] args) {
        System.out.println("Usage: PL/0 [script]");
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.print("Enter path to file: ");
        String path = scanner.nextLine();
        try {
            runFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);
//        Block block = Block.readBlock(source);
//
//        System.out.println(block);
        List<Token> tokens = scanner.scanTokens();

        // For now, just print the tokens.
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
    }

    static void error(int line, String where, String message) {
        report(line, where, message);
    }

    private static void report(int line, String where,
                               String message) {
        System.err.println(
            "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

}
