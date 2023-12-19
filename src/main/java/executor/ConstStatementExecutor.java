package executor;

import ast.Statement;
import context.ApplicationContext;
import context.ProcedureContext;

public class ConstStatementExecutor implements Executor<Statement.Const> {
    @Override
    public void execute(Statement.Const statement, ProcedureContext procedureContext) {
        ApplicationContext.globalConstantList.put(statement.getName().lexeme(), statement.getInitializer());
    }

}
