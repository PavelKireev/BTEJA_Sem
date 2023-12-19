package evaluator;

import ast.BasicExpression;
import context.ProcedureContext;
import interpreter.Interpreter;

public class BinaryExpressionEvaluator implements Evaluator<BasicExpression.Binary> {
    @Override
    public Object evaluate(BasicExpression.Binary expression, ProcedureContext procedureContext) {
        Object left = ExpressionEvaluator.evaluate(expression.left, procedureContext);
        String operator = expression.operator.lexeme();
        Object right = ExpressionEvaluator.evaluate(expression.right, procedureContext);
        return operate(left, operator, right);
    }

    private Object operate(Object left, String operator, Object right) {
        switch (operator) {
            case "+":
                try {
                    return (double) left + (double) right;
                } catch (Exception ignored) {
                    // ignored
                }
                try {
                    return (int) left + (int) right;
                } catch (Exception ignored) {
                    // ignored
                }
                try {
                    return (String) left + (String) right;
                } catch (Exception ignored) {
                    // ignored
                }
                Interpreter.error(0, " at '" + operator + "'", "Incompatible types");
                System.exit(1);
                return null;
            case "-":
                try {
                    return (double) left - (double) right;
                } catch (Exception ignored) {
                    // ignored
                }
                try {
                    return (int) left - (int) right;
                } catch (Exception ignored) {
                    // ignored
                }
                Interpreter.error(0, " at '" + operator + "'", "Incompatible types");
                System.exit(1);
                return null;
            case "*":
                try {
                    return (double) left * (double) right;
                } catch (Exception ignored) {
                    // ignored
                }
                try {
                    return (int) left * (int) right;
                } catch (Exception ignored) {
                    // ignored
                }
                Interpreter.error(0, " at '" + operator + "'", "Incompatible types");
                System.exit(1);
                return null;
            case "DIV", "/":
                try {
                    return (double) left / (double) right;
                } catch (Exception ignored) {
                    // ignored
                }
                try {
                    return (int) left / (int) right;
                } catch (Exception ignored) {
                    // ignored
                }
                Interpreter.error(0, " at '" + operator + "'", "Incompatible types");
                System.exit(1);
                return null;
            case "MOD":
                try {
                    return (double) left % (double) right;
                } catch (Exception ignored) {
                    // ignored
                }
                try {
                    return (int) left % (int) right;
                } catch (Exception ignored) {
                    // ignored
                }
                Interpreter.error(0, " at '" + operator + "'", "Incompatible types");
                System.exit(1);
                return null;
            case "<":
                try {
                    return (double) left < (double) right;
                } catch (Exception ignored) {
                    // ignored
                }
                try {
                    return (int) left < (int) right;
                } catch (Exception ignored) {
                    // ignored
                }
                Interpreter.error(0, " at '" + operator + "'", "Incompatible types");
                System.exit(1);
                return null;
            case "<=":
                try {
                    return (double) left <= (double) right;
                } catch (Exception ignored) {
                    // ignored
                }
                try {
                    return (int) left <= (int) right;
                } catch (Exception ignored) {
                    // ignored
                }
                Interpreter.error(0, " at '" + operator + "'", "Incompatible types");
                System.exit(1);
                return null;
            case ">":
                try {
                    return (double) left > (double) right;
                } catch (Exception ignored) {
                    // ignored
                }
                try {
                    return (int) left > (int) right;
                } catch (Exception ignored) {
                    // ignored
                }
                Interpreter.error(0, " at '" + operator + "'", "Incompatible types");
                System.exit(1);
                return null;
            case ">=":
                try {
                    return (double) left >= (double) right;
                } catch (Exception ignored) {
                    // ignored
                }
                try {
                    return (int) left >= (int) right;
                } catch (Exception ignored) {
                    // ignored
                }
                Interpreter.error(0, " at '" + operator + "'", "Incompatible types");
                System.exit(1);
                return null;
            case "=":
                return left.equals(right);
            case "#":
                return !left.equals(right);
            case "AND":
                return (boolean) left && (boolean) right;
            case "OR":
                return (boolean) left || (boolean) right;
            default:
                Interpreter.error(0, " at '" + operator + "'", "Unknown operator");
                System.exit(1);
                return null;
        }
    }

    public Object cast(Object value) {
        if (value instanceof Double) {
            return (double) value;
        } else if (value instanceof Integer) {
            return (int) value;
        } else if (value instanceof Boolean) {
            return (boolean) value;
        } else {
            return value;
        }
    }
}
