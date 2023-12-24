package executor;

import ast.BasicExpression;
import ast.Statement;
import context.ApplicationContext;
import context.ProcedureContext;
import evaluator.ExpressionEvaluator;
import structure.Variable;

import java.util.Arrays;

public class AssignmentStatementExecutor implements Executor<Statement.Assignment> {
    @Override
    public void execute(Statement.Assignment statement, ProcedureContext procedureContext) {
        Object value = ExpressionEvaluator.evaluate(statement.getExpression(), procedureContext);

        if (statement.getIdent() instanceof BasicExpression.Literal) {

            ApplicationContext.updateVariable(((BasicExpression.Literal) statement.getIdent()).value.lexeme(), value);

            if (procedureContext != null) {
                procedureContext.updateVariable(((BasicExpression.Literal) statement.getIdent()).value.lexeme(), value);
            }
            return;
        }

        if (statement.getIdent() instanceof BasicExpression.ArrayVariable) {
            if (ApplicationContext.arrayExists(((BasicExpression.ArrayVariable) statement.getIdent()).name.lexeme())) {
                Integer[] indicies =
                    Arrays.stream(((BasicExpression.ArrayVariable) statement.getIdent()).index)
                          .map(index ->
                              (Integer) ExpressionEvaluator.evaluate(new BasicExpression.Literal(index),
                                                                   procedureContext))
                          .toList().toArray(Integer[]::new);

                ApplicationContext.setArrayVariable(
                    ((BasicExpression.ArrayVariable) statement.getIdent()).name.lexeme(),
                    value,
                    indicies
                );
            } else if (procedureContext != null) {
                procedureContext.setArrayVariable(
                    ((BasicExpression.ArrayVariable) statement.getIdent()).name.lexeme(), value,
                    Arrays.stream(((BasicExpression.ArrayVariable) statement.getIdent()).index)
                        .map(index ->
                            (Integer) ExpressionEvaluator.evaluate(new BasicExpression.Literal(index),
                                procedureContext))
                        .toList().toArray(Integer[]::new)
                );
            }
        }

    }
}
