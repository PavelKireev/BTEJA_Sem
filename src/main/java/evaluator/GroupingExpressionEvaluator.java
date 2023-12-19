package evaluator;

import ast.BasicExpression;
import context.ProcedureContext;

public class GroupingExpressionEvaluator implements Evaluator<BasicExpression.Grouping> {
    @Override
    public Object evaluate(BasicExpression.Grouping expression, ProcedureContext procedureContext) {
        return ExpressionEvaluator.evaluate(expression.expression, procedureContext);
    }
}
