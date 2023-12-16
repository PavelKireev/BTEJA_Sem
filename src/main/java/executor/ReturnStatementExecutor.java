package executor;

import ast.Statement;

public class ReturnStatementExecutor implements Executor<Statement.Return> {
    @Override
    public void execute(Statement.Return statement) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
