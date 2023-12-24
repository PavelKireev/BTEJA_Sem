package executor;

import ast.Statement;
import context.ProcedureContext;
import evaluator.ExpressionEvaluator;

public class ElsifBranchStatementExecutor implements Executor<Statement.Elsif> {
    @Override
    public void execute(Statement.Elsif statement, ProcedureContext procedureContext) {
        boolean condition = (boolean) ExpressionEvaluator.evaluate(statement.getCondition(), procedureContext);
        if (condition) {
            statement.getStatements()
                     .forEach(elseStatement -> StatementExecutor.execute(elseStatement, procedureContext));
        }
    }
}
