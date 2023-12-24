package evaluator;

import ast.BasicExpression;
import context.ApplicationContext;
import context.ProcedureContext;

import java.util.Arrays;

public class ArrayVariableExpressionEvaluator implements Evaluator<BasicExpression.ArrayVariable> {
    @Override
    public Object evaluate(BasicExpression.ArrayVariable expression, ProcedureContext procedureContext) {
        Integer[] indices = Arrays.stream(expression.index).map(index ->
            (Integer) ExpressionEvaluator.evaluate(new BasicExpression.Literal(index), procedureContext)
        ).toList().toArray(Integer[]::new);

        Object value = ApplicationContext.getArrayVariable(expression.name.lexeme(), indices);

        if (value == null && procedureContext != null) {
            value = procedureContext.getArrayVariable(expression.name.lexeme(), indices);
        }

        return value;
    }
}
