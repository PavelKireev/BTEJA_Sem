package analyzer;

import ast.Statement;
import interpreter.Interpreter;
import structure.Block;

import java.util.Collections;
import java.util.List;

public class ProcedureDeclarationStatementAnalyzer implements StatementAnalyzer<Statement.Procedure> {
    @Override
    public void analyze(Statement.Procedure statement, Block block) {
        String procedureName = statement.getName().lexeme();
        List<Statement.Procedure> declaredProcedures = block.getStatementList()
                                                            .stream()
                                                            .filter(s -> s instanceof Statement.Procedure)
                                                            .map(s -> (Statement.Procedure) s)
                                                            .toList();

        if (Collections.frequency(declaredProcedures, statement) > 1) {
            Interpreter.error(statement.getName().line(),
                              "Procedure declaration",
                              "Procedure " + procedureName + " is already declared");
            System.exit(1);
        }
    }

}
