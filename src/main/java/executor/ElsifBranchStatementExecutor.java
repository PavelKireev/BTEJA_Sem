package executor;

import ast.Statement;
import context.ProcedureContext;

public class ElsifBranchStatementExecutor implements Executor<Statement.Elsif> {
    @Override
    public void execute(Statement.Elsif statement, ProcedureContext procedureContext) {
//        TODO: evaluate expression
//        boolean condition = statement.getCondition();
        boolean condition = true;
        if (condition) {
            statement.getStatements().forEach(elseStatement -> StatementExecutor.execute(elseStatement, procedureContext));
        }
    }
}
