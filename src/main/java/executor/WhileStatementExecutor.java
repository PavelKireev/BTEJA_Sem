package executor;

import ast.Statement;
import context.ProcedureContext;

public class WhileStatementExecutor implements Executor<Statement.While> {
    @Override
    public void execute(Statement.While statement, ProcedureContext procedureContext) {
        return;
    }

}
