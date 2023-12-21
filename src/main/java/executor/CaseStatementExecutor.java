package executor;

import ast.BasicExpression;
import ast.Statement;
import context.ProcedureContext;
import evaluator.ExpressionEvaluator;

public class CaseStatementExecutor implements Executor<Statement.Case> {
    @Override
    public void execute(Statement.Case statement, ProcedureContext procedureContext) {
        Integer ident = (Integer) ExpressionEvaluator.evaluate(
            new BasicExpression.Literal(statement.getIdent()), procedureContext);
        boolean executed = false;
        for (Statement.CaseBranch caseBranch : statement.getBranches()) {
            if (caseBranch.getRange().contains(ident)) {
                caseBranch.getBody()
                    .forEach(caseStatement -> StatementExecutor.execute(caseStatement,
                        procedureContext));
                executed = true;
            }
        }

        if (!executed) {
            statement.getDefaultBranch()
                .forEach(caseStatement -> StatementExecutor.execute(caseStatement,
                                                                    procedureContext));
        }
    }
}
