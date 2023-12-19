package executor;

import ast.BasicExpression;
import ast.Statement;
import context.ProcedureContext;
import evaluator.ExpressionEvaluator;

import java.util.Collections;

public class IfStatementExecutor implements Executor<Statement.If> {

    @Override
    public void execute(Statement.If statement, ProcedureContext procedureContext) {
        if ((boolean) ExpressionEvaluator.evaluate(statement.getCondition(), procedureContext)) {
            statement.getBody().forEach(thenStatement -> StatementExecutor.execute(thenStatement, procedureContext));
        } else {
            Statement.Elsif executableElsifBranch = statement.getElsifBranches()
                                                             .stream()
                                                             .filter(elsif ->
                                                                 (boolean) ExpressionEvaluator.evaluate(
                                                                     elsif.getCondition(), procedureContext))
                                                             .findFirst()
                                                             .orElse(null);

            if (executableElsifBranch != null) {
                executableElsifBranch.getStatements()
                                     .forEach(
                                         elseStatement -> StatementExecutor.execute(elseStatement, procedureContext));
            } else if (statement.getElseBranch() != null) {
                statement.getElseBranch()
                         .getStatements()
                         .forEach(elseStatement -> StatementExecutor.execute(elseStatement, procedureContext));
            }
        }
    }

}
