package executor;

import ast.Statement;
import context.ProcedureContext;

public class ReturnStatementExecutor implements Executor<Statement.Return> {
    @Override
    public void execute(Statement.Return statement, ProcedureContext procedureContext) {
        return;
    }
}
