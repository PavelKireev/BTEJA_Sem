package analyzer;

import ast.Statement;
import interpreter.Interpreter;
import structure.Block;
import util.AnalyzerUtils;

public class CaseStatementStatementAnalyzer implements StatementAnalyzer<Statement.Case> {

    @Override
    public void analyze(Statement.Case statement, Block block) {
        String ident = statement.getIdent().lexeme();

        Statement.Var variable = AnalyzerUtils.extractAllVars(block.getStatementList())
                                               .stream()
                                               .filter(v -> v.getName().lexeme().equals(ident))
                                               .findFirst()
                                               .orElse(null);

        if (variable == null) {
            Interpreter.error(statement.getIdent().line(),
                              "Case statement",
                              "Variable " + ident + " is not declared");
            System.exit(1);
        }

        String variableType = variable.getType().lexeme();

        if (!variableType.equals("NUMERIC")) {
            Interpreter.error(statement.getIdent().line(),
                              "Case statement",
                              "Variable " + ident + " is not of type NUMERIC");
            System.exit(1);
        }
    }
}
