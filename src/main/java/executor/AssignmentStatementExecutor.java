package executor;

import ast.BasicExpression;
import ast.Statement;
import context.ApplicationContext;
import context.ProcedureContext;
import evaluator.ExpressionEvaluator;
import structure.Variable;

public class AssignmentStatementExecutor implements Executor<Statement.Assignment> {
    @Override
    public void execute(Statement.Assignment statement, ProcedureContext procedureContext) {
        Object value = ExpressionEvaluator.evaluate(statement.getExpression(), procedureContext);

        if (statement.getIdent() instanceof BasicExpression.Literal) {
            if (procedureContext == null) {
                ApplicationContext.updateVariable(((BasicExpression.Literal) statement.getIdent()).value.lexeme(), value);
            } else {
                procedureContext.updateVariable(((BasicExpression.Literal) statement.getIdent()).value.lexeme(), value);
            }
            return;
        }

        if (statement.getIdent() instanceof BasicExpression.ArrayVariable) {
            if (ApplicationContext.arrayExists(((BasicExpression.ArrayVariable) statement.getIdent()).name.lexeme())) {
                ApplicationContext.setArrayVariable(
                    ((BasicExpression.ArrayVariable) statement.getIdent()).name.lexeme(),
                    (Integer) ExpressionEvaluator.evaluate(
                        new BasicExpression.Literal(((BasicExpression.ArrayVariable) statement.getIdent()).index),
                        procedureContext),
                    value
                );
            } else if (procedureContext != null) {
                procedureContext.setArrayVariable(
                    ((BasicExpression.ArrayVariable) statement.getIdent()).name.lexeme(),
                    (Integer) ExpressionEvaluator.evaluate(
                        new BasicExpression.Literal(((BasicExpression.ArrayVariable) statement.getIdent()).index),
                        procedureContext),
                    value
                );
            }
        }

    }
}
