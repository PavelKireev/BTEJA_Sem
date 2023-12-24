package analyzer;

import ast.BasicExpression;
import ast.Statement;
import context.ProcedureContext;
import evaluator.ExpressionEvaluator;
import interpreter.Interpreter;
import structure.Block;

public class IfStatementAnalyzer implements StatementAnalyzer<Statement.If> {
    @Override
    public void analyze(Statement.If statement, Block block) {
        BasicExpression condition = statement.getCondition();
        if (condition == null) {
            Interpreter.error(statement.getLine(),
                              "If statement",
                              "If statement condition must not be empty");
            System.exit(1);
        }


        statement.getBody()
                 .forEach(bodyStatement -> SemanticAnalyzer.analyze(bodyStatement, block));
        statement.getElsifBranches()
                 .forEach(elsif -> SemanticAnalyzer.analyze(elsif, block));
        statement.getElseBranch()
                 .getStatements()
                 .forEach(bodyStatement -> SemanticAnalyzer.analyze(bodyStatement, block));
    }
}
