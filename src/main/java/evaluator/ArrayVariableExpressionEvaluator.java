package evaluator;

import ast.BasicExpression;
import context.ApplicationContext;
import context.ProcedureContext;

public class ArrayVariableExpressionEvaluator implements Evaluator<BasicExpression.ArrayVariable> {
    @Override
    public Object evaluate(BasicExpression.ArrayVariable expression, ProcedureContext procedureContext) {
        Object value = ApplicationContext.getArrayVariable(
            expression.name.lexeme(),
            (Integer) ExpressionEvaluator.evaluate(
                new BasicExpression.Literal(expression.index),
                procedureContext)
        );

        if (value == null) {
            value = procedureContext.getArrayVariable(
                expression.name.lexeme(),
                (Integer) ExpressionEvaluator.evaluate(
                    new BasicExpression.Literal(expression.index),
                    procedureContext)
            );
        }

        return value;
    }
}
