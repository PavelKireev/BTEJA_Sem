package structure;

import ast.Parser;
import ast.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import scanner.Scanner;

public class Block {

    private final List<Statement> statementList;

    private Block() {
        this.statementList = new LinkedList<>();
    }

    public static Block readBlock(String source) {
        Block block = new Block();
        Scanner scanner = new Scanner(source);
        Parser parser = new Parser(scanner.scanTokens());
        List<Statement> statements = block.statementList;
        statements.add(parser.readModule());
        statements.add(parser.readImports());
        statements.addAll(parser.readConst());
        List<Statement.Var> vars = new ArrayList<>();
        List<Statement.VarArray> varArrays = new ArrayList<>();
        parser.readVar(vars, varArrays);
        statements.addAll(vars);
        statements.addAll(varArrays);
        List<Statement.Procedure> procedures = new ArrayList<>();
        parser.readProcedures(procedures);
        statements.addAll(procedures);
        statements.add(parser.readMain());
        return block;
    }

    public List<Statement> getStatementList() {
        return statementList;
    }

}
