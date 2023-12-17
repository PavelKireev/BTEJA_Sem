package executor;

import ast.Statement;

public class ProcedureStatementExecutor implements Executor<Statement.Procedure> {
    @Override
    public void execute(Statement.Procedure statement) {
        statement.getBody().forEach(StatementExecutor::execute);
    }
}
