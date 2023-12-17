package interpreter;

import ast.Statement;
import context.ApplicationContext;
import executor.MainStatementExecutor;
import scanner.Scanner;
import scanner.Token;
import structure.Block;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

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

        Block block = Block.readBlock(source);
        ApplicationContext.initialize(block.getStatementList());

        MainStatementExecutor mainStatementExecutor = new MainStatementExecutor();
        mainStatementExecutor.execute(
            (Statement.Main) Objects.requireNonNull(block.getStatementList()
                                                         .stream()
                                                         .filter(statement -> statement instanceof Statement.Main)
                                                         .findFirst()
                                                         .orElse(null))
        );

        // For now, just print the tokens.
//        for (Statement statement : block.getStatementList()) {
//            System.out.println(statement);
//        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
    }

    public static void error(int line, String where, String message) {
        report(line, where, message);
    }

    private static void report(int line, String where,
                               String message) {
        System.err.println(
            "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

}
