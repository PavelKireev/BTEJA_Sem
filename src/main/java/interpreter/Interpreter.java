package interpreter;

import analyzer.SemanticAnalyzer;
import ast.Statement;
import context.ApplicationContext;
import executor.MainStatementExecutor;
import structure.Block;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class Interpreter {

    static boolean hadError = false;

    public static void main(String[] args) {
        System.out.println("Modula-2 Interpreter");
        System.out.print("Enter path to file: ");
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String path = scanner.nextLine();
        try {
            runFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void run(String source) {
        Block block = Block.readBlock(source);
        ApplicationContext.initialize(block.getStatementList());

        for (Statement statement : block.getStatementList()) {
            if (statement != null) {
                SemanticAnalyzer.analyze(statement, block);
            }
        }

        MainStatementExecutor mainStatementExecutor = new MainStatementExecutor();
        mainStatementExecutor.execute(
            (Statement.Main) Objects.requireNonNull(block.getStatementList()
                                                         .stream()
                                                         .filter(statement -> statement instanceof Statement.Main)
                                                         .findFirst()
                                                         .orElse(null)), null
        );

        System.exit(0);
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
