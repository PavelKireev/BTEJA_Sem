package evaluator;

import ast.BasicExpression;
import context.ProcedureContext;

import java.util.HashMap;
import java.util.Map;

public class ExpressionEvaluator {

    private static final Map<String, Evaluator> evaluatorMap;

    static {
        evaluatorMap = new HashMap<>();
        evaluatorMap.put(BasicExpression.Empty.class.getName(), new EmptyExpressionEvaluator());
        evaluatorMap.put(BasicExpression.Binary.class.getName(), new BinaryExpressionEvaluator());
        evaluatorMap.put(BasicExpression.Literal.class.getName(), new LiteralExpressionEvaluator());
        evaluatorMap.put(BasicExpression.Grouping.class.getName(), new GroupingExpressionEvaluator());
        evaluatorMap.put(BasicExpression.Negation.class.getName(), new NegationExpressionEvaluator());
        evaluatorMap.put(BasicExpression.ArrayVariable.class.getName(), new ArrayVariableExpressionEvaluator());
        evaluatorMap.put(BasicExpression.ProcedureCall.class.getName(), new ProcedureCallExpressionEvaluator());
    }

    public static <T extends BasicExpression> Object evaluate(T expression, ProcedureContext procedureContext) {
        return evaluatorMap.get(expression.getClass().getName()).evaluate(expression, procedureContext);
    }

}
