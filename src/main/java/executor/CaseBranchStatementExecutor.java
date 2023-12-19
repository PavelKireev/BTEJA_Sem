package executor;

import ast.Statement;
import context.ProcedureContext;
import evaluator.ExpressionEvaluator;

public class CaseBranchStatementExecutor implements Executor<Statement.CaseBranch> {
    @Override
    public void execute(Statement.CaseBranch statement, ProcedureContext procedureContext) {
//        if (ExpressionEvaluator.evaluate(statement.getRange().contains()))
//        statement.getBody().forEach(caseStatement -> StatementExecutor.execute(caseStatement, procedureContext));
    }

}
