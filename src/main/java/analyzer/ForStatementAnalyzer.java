package analyzer;

import ast.Statement;
import interpreter.Interpreter;
import structure.Block;
import util.AnalyzerUtils;

public class ForStatementAnalyzer implements StatementAnalyzer<Statement.For> {
    @Override
    public void analyze(Statement.For statement, Block block) {
        AssignmentStatementAnalyzer assignmentStatementAnalyzer = new AssignmentStatementAnalyzer();
        assignmentStatementAnalyzer.analyze((Statement.Assignment) statement.getIndex(), block);
        if (!AnalyzerUtils.getExpressionType(((Statement.Assignment) statement.getIndex()).getExpression(),
                                              block, statement.getLine())
                          .equals("NUMERIC")) {
            Interpreter.error(statement.getLine(),
                              "For statement",
                              "For statement initial value must be a numeric expression");
            System.exit(1);
        }

        if (!AnalyzerUtils.getExpressionType(statement.getTo(), block, statement.getLine())
                          .equals("NUMERIC")) {
            Interpreter.error(statement.getLine(),
                              "For statement",
                              "For statement to value must be a numeric expression");
            System.exit(1);
        }


        if (statement.getBy() != null &&
            !AnalyzerUtils.getExpressionType(statement.getBy(), block, statement.getLine())
            .equals("NUMERIC")) {
            Interpreter.error(statement.getLine(),
                "For statement",
                "For statement by value must be a numeric expression");
        }
    }
}
