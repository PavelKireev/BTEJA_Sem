package executor;

import ast.Statement;

public class ElseBranchStatementExecutor implements Executor<Statement.ElseBranch> {
    @Override
    public void execute(Statement.ElseBranch statement) {
        statement.getStatements().forEach(StatementExecutor::execute);
    }
}
