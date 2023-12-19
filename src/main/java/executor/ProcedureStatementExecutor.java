package executor;

import ast.Statement;
import context.ProcedureContext;

public class ProcedureStatementExecutor implements Executor<Statement.Procedure> {
    @Override
    public void execute(Statement.Procedure statement, ProcedureContext procedureContext) {
//        statement.getBody()
//                 .forEach(procedureStatement -> StatementExecutor.execute(procedureStatement, procedureContext));
    }
}
