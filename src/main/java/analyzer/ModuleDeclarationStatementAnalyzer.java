package analyzer;

import ast.Statement;
import context.ProcedureContext;
import interpreter.Interpreter;
import structure.Block;

public class ModuleDeclarationStatementAnalyzer implements StatementAnalyzer<Statement.Module> {
    @Override
    public void analyze(Statement.Module statement, Block block) {
        if (statement.getName().lexeme().isBlank()) {
            Interpreter.error(statement.getName().line(),
                              "Module declaration",
                              "Module " + statement.getName().lexeme() + " is already declared");
            System.exit(1);
        }
    }
}
