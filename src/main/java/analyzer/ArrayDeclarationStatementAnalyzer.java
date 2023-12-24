package analyzer;

import ast.Statement;
import context.ApplicationContext;
import context.ProcedureContext;
import interpreter.Interpreter;
import structure.Array;
import structure.Block;

import java.util.Collections;
import java.util.List;

public class ArrayDeclarationStatementAnalyzer implements StatementAnalyzer<Statement.VarArray> {
    @Override
    public void analyze(Statement.VarArray statement, Block block) {
        String arrayName = statement.getName().lexeme();

        List<Statement> arrayDeclarations = block.getStatementList()
                                                 .stream()
                                                 .filter(s -> s instanceof Statement.VarArray &&
                                                         ((Statement.VarArray) s).getName().lexeme().equals(arrayName))
                                                 .toList();

        if (arrayDeclarations.size() > 1) {
            Interpreter.error(statement.getName().line(),
                              "Array declaration",
                              "Array " + arrayName + " is declared more than once");
            System.exit(1);
        }

    }
}
