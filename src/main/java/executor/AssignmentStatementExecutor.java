package executor;

import ast.Statement;

public class AssignmentStatementExecutor implements Executor<Statement.Assignment> {
    @Override
    public void execute(Statement.Assignment statement) {
        return;
    }
}
