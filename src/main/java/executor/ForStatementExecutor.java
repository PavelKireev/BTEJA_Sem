package executor;

import ast.Statement;

public class ForStatementExecutor implements Executor<Statement.For> {

    private Integer index;
    private Integer limit;
    private Integer increment;

    @Override
    public void execute(Statement.For statement) {

    }
}
