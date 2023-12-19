package executor;

import ast.Statement;
import context.ProcedureContext;

public class VarStatementExecutor implements Executor<Statement.Var> {
    @Override
    public void execute(Statement.Var statement, ProcedureContext procedureContext) {
        return;
    }

}
