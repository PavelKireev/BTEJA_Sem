package evaluator;

import ast.BasicExpression;
import context.ProcedureContext;

public class EmptyExpressionEvaluator implements Evaluator<BasicExpression.Empty> {
    @Override
    public Object evaluate(BasicExpression.Empty expression, ProcedureContext procedureContext) {
        return null;
    }
}
