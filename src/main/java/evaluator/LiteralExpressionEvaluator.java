package evaluator;

import ast.BasicExpression;
import context.ApplicationContext;
import context.ProcedureContext;

public class LiteralExpressionEvaluator implements Evaluator<BasicExpression.Literal> {
    @Override
    public Object evaluate(BasicExpression.Literal expression, ProcedureContext procedureContext) {
        String value = expression.value.lexeme();

        if (value.equals("TRUE")) {
            return true;
        } else if (value.equals("FALSE")) {
            return false;
        }


        if (procedureContext != null && procedureContext.getVariable(value) != null) {
            return procedureContext.getVariable(value)
                .getValue();
        }

        if (ApplicationContext.varExists(value)) {
            return ApplicationContext.getVariable(value)
                                     .getValue();
        }

        if (ApplicationContext.constantExists(value)) {
            return ApplicationContext.getConst(value);
        }

        try {
            return Integer.parseInt(value);
        } catch (Exception ignored) {
            // ignored
        }

        try {
            return Double.parseDouble(value);
        } catch (Exception ignored) {
            // ignored
        }

        try {
            if (value.length() == 2 && value.charAt(0) == '\'') {
                return value.charAt(1);
            }
        } catch (Exception ignored) {
            // ignored
        }


        try {
            if (value.length() == 3 && value.charAt(0) == '\"') {
                return value.charAt(1);
            }
        } catch (Exception ignored) {
            // ignored
        }

        return value.replace("'", "");
    }
}
