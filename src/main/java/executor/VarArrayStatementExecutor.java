package executor;

import ast.Statement;
import context.ProcedureContext;

public class VarArrayStatementExecutor implements Executor<Statement.VarArray> {
    @Override
    public void execute(Statement.VarArray statement, ProcedureContext procedureContext) {
        return;
    }
}
