package executor;

import ast.Statement;
import context.ProcedureContext;

public class ElseBranchStatementExecutor implements Executor<Statement.ElseBranch> {
    @Override
    public void execute(Statement.ElseBranch statement, ProcedureContext procedureContext) {
        statement.getStatements().forEach(elseStatement -> StatementExecutor.execute(elseStatement, procedureContext));
    }
}
