package evaluator;

import ast.BasicExpression;
import context.ProcedureContext;
import interpreter.Interpreter;

public class NegationExpressionEvaluator implements Evaluator<BasicExpression.Negation> {
    @Override
    public Object evaluate(BasicExpression.Negation expression, ProcedureContext procedureContext) {
        BasicExpression negatedValue = expression.getNegatedValue();
        Object value = ExpressionEvaluator.evaluate(negatedValue, procedureContext);

        if (value instanceof Boolean) {
            return !(Boolean) value;
        } else if (value instanceof Double) {
            return -(Double) value;
        } else if (value instanceof Integer) {
            return -(Integer) value;
        } else {
            Interpreter.error(-1, "", "Unknown type for negation");
            System.exit(1);
            return null;
        }
    }
}
