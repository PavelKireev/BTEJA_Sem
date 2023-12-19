package executor;

import ast.Statement;
import context.ProcedureContext;

public class MainStatementExecutor implements Executor<Statement.Main> {
    @Override
    public void execute(Statement.Main statement, ProcedureContext procedureContext) {
        statement.getBody().forEach(mainStatement -> StatementExecutor.execute(mainStatement, null));
    }
}
