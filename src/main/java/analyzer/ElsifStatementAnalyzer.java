package analyzer;

import ast.Statement;
import interpreter.Interpreter;
import structure.Block;
import util.AnalyzerUtils;

public class ElsifStatementAnalyzer implements StatementAnalyzer<Statement.Elsif> {
    @Override
    public void analyze(Statement.Elsif statement, Block block) {
        if (!AnalyzerUtils.getExpressionType(statement.getCondition(), block, statement.getLine())
                           .equals("BOOLEAN")) {
            Interpreter.error(statement.getLine(),
                              "Elsif statement",
                              "Elsif statement condition must be a boolean expression");
        }
    }
}
