package executor;

import ast.Statement;
import context.ProcedureContext;
import evaluator.ExpressionEvaluator;

public class CaseStatementExecutor implements Executor<Statement.Case> {
    @Override
    public void execute(Statement.Case statement, ProcedureContext procedureContext) {
//        statement.getBranches()
//                 .forEach(caseBranch -> {
//                     if (caseBranch.getRange().contains(ExpressionEvaluator.evaluate(statement., procedureContext))) {
//                         caseBranch.getBody().forEach(caseStatement -> StatementExecutor.execute(caseStatement, procedureContext));
//                     }
//                 });
    }
}
