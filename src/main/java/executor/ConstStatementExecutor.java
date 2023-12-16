package executor;

import ast.Statement;

public class ConstStatementExecutor implements Executor<Statement.Const> {
    @Override
    public void execute(Statement.Const statement) {
        return;
    }

}
