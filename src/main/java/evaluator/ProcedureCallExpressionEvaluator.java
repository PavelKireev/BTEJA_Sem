package evaluator;

import ast.BasicExpression;
import context.ApplicationContext;
import context.ProcedureContext;
import executor.StatementExecutor;
import interpreter.Interpreter;
import library.Native;

import java.util.List;

public class ProcedureCallExpressionEvaluator implements Evaluator<BasicExpression.ProcedureCall> {
    @Override
    public Object evaluate(BasicExpression.ProcedureCall expression, ProcedureContext procedureContext) {
        if (!ApplicationContext.getProcedure(expression.name.lexeme()).isEmpty()) {
            ApplicationContext.getProcedure(expression.name.lexeme())
                              .forEach(statement -> StatementExecutor.execute(statement, procedureContext));
        } else if (procedureContext != null && procedureContext.getProcedure(expression.name.lexeme()) != null) {
            procedureContext.getProcedure(expression.name.lexeme())
                            .getBody()
                            .forEach(statement -> StatementExecutor.execute(statement, procedureContext));
        } else if (Native.supportedProcedures.contains(expression.name.lexeme())) {
            return Native.executeNativeProcedure(expression, procedureContext);
        } else {
            Interpreter.error(0, " at '" + expression.name.lexeme() + "'", "Procedure not found");
            System.exit(1);
            return null;
        }
        return null;
    }
}
