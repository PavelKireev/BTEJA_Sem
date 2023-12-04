package evaluator;

import ast.BasicExpression;
import ast.Statement;
import context.ApplicationContext;
import context.ProcedureContext;
import scanner.Token;
import scanner.TokenType;

public class ExpressionEvaluator {

    private ExpressionEvaluator() {
    }

    public static BasicExpression evaluateExpression(BasicExpression expression,
                                                          ApplicationContext applicationContext,
                                                          ProcedureContext procedureContext) {
        if (expression == null) return null;
        switch (expression.getClass().getSimpleName()) {
            case "Literal":
                return getValueFromContext(((BasicExpression.Literal) expression).value,
                                           applicationContext, procedureContext);
            case "Binary":
                BasicExpression.Binary binary = (BasicExpression.Binary) expression;
                return calculate(binary.left, binary.operator.lexeme(), binary.right,
                                 applicationContext, procedureContext);
            case "Grouping":
                BasicExpression.Grouping grouping = (BasicExpression.Grouping) expression;
                BasicExpression biExpresion = grouping.expression;
                while (!(biExpresion instanceof BasicExpression.Binary)) {
                    biExpresion = ((BasicExpression.Grouping) biExpresion).expression;
                }
                return evaluateExpression(biExpresion, applicationContext, procedureContext);
        }
        return null;
    }

    private static BasicExpression.Literal calculate(BasicExpression left, String operator, BasicExpression right,
                                        ApplicationContext applicationContext, ProcedureContext procedureContext) {
        switch (operator) {
            case "+":
                Object resultPlus = plus(left, right, applicationContext, procedureContext);
                return new BasicExpression.Literal(
                    new Token(TokenType.IDENT, resultPlus.toString(), resultPlus.toString(), -1));
            case "-":
                Object resultMinus = minus(left, right, applicationContext, procedureContext);
                return new BasicExpression.Literal(
                    new Token(TokenType.IDENT, resultMinus.toString(), resultMinus.toString(), -1));
            case "*":
                Object resultMultiply = multiply(left, right, applicationContext, procedureContext);
                return new BasicExpression.Literal(
                    new Token(TokenType.IDENT, resultMultiply.toString(), resultMultiply.toString(), -1));
            case "/":
                Object resultDivide = divide(left, right, applicationContext, procedureContext);
                return new BasicExpression.Literal(
                    new Token(TokenType.IDENT, resultDivide.toString(), resultDivide.toString(), -1));
            case ">=":
                Object resultGreaterThenOrEqual = greaterThanOrEqual(left, right, applicationContext, procedureContext);
                return new BasicExpression.Literal(
                    new Token(TokenType.IDENT, resultGreaterThenOrEqual.toString(),
                              resultGreaterThenOrEqual.toString(), -1));
            case "<=":
                Object resultLessThanOrEqual = lessThanOrEqual(left, right, applicationContext, procedureContext);
                return new BasicExpression.Literal(
                    new Token(TokenType.IDENT, resultLessThanOrEqual.toString(),
                              resultLessThanOrEqual.toString(), -1));
            case "=":
                Object resultEqual = equal(left, right, applicationContext, procedureContext);
                return new BasicExpression.Literal(
                    new Token(TokenType.IDENT, resultEqual.toString(), resultEqual.toString(), -1));
            case ">":
                Object resultGreaterThan = greater(left, right, applicationContext, procedureContext);
                return new BasicExpression.Literal(
                    new Token(TokenType.IDENT, resultGreaterThan.toString(), resultGreaterThan.toString(), -1));
            case "<":
                Object resultLessThan = less(left, right, applicationContext, procedureContext);
                return new BasicExpression.Literal(
                    new Token(TokenType.IDENT, resultLessThan.toString(), resultLessThan.toString(), -1));
            default:
                throw new IllegalArgumentException("Unsupported operation");
        }
    }

    private static Object plus(BasicExpression left, BasicExpression right,
                               ApplicationContext applicationContext, ProcedureContext procedureContext) {
        if (!(left instanceof BasicExpression.Literal)) {
            left = evaluateExpression(left, applicationContext, procedureContext);
        }

        if (!(right instanceof BasicExpression.Literal)) {
            right = evaluateExpression(right, applicationContext, procedureContext);
        }

        Token leftToken = ((BasicExpression.Literal) left).value;
        Token rightToken = ((BasicExpression.Literal) right).value;

        if (leftToken.type().equals(TokenType.IDENT) && !rightToken.type().equals(TokenType.IDENT)) {
            Object leftVal = ((BasicExpression.Literal)getValueFromContext(leftToken, applicationContext, procedureContext)).value.lexeme();
            return Double.parseDouble(leftVal.toString()) + Double.parseDouble(rightToken.lexeme());
        } else if (!leftToken.type().equals(TokenType.IDENT) && rightToken.type().equals(TokenType.IDENT)) {
            Object rightVal = getValueFromContext(rightToken, applicationContext, procedureContext);
            return Double.parseDouble(leftToken.lexeme()) + Double.parseDouble(rightVal.toString());
        } else if (leftToken.type().equals(TokenType.IDENT) && rightToken.type().equals(TokenType.IDENT)) {
            Object leftVal = ((BasicExpression.Literal)getValueFromContext(leftToken, applicationContext, procedureContext)).value.lexeme();
            Object rightVal = ((BasicExpression.Literal)getValueFromContext(rightToken, applicationContext, procedureContext)).value.lexeme();
            return Double.parseDouble(leftVal.toString()) + Double.parseDouble(rightVal.toString());
        }
        return Double.parseDouble(leftToken.lexeme()) + Double.parseDouble(rightToken.lexeme());
    }

    private static Double minus(BasicExpression left, BasicExpression right,
                                ApplicationContext applicationContext, ProcedureContext procedureContext) {
        if (!(left instanceof BasicExpression.Literal)) {
            left = evaluateExpression(left, applicationContext, procedureContext);
        }

        if (!(right instanceof BasicExpression.Literal)) {
            right = evaluateExpression(right, applicationContext, procedureContext);
        }

        Token leftToken = ((BasicExpression.Literal) left).value;
        Token rightToken = ((BasicExpression.Literal) right).value;

        if (leftToken.type().equals(TokenType.IDENT) && !rightToken.type().equals(TokenType.IDENT)) {
            Object leftVal = getValueFromContext(leftToken, applicationContext, procedureContext);
            return Double.parseDouble(leftVal.toString()) - Double.parseDouble(rightToken.lexeme());
        } else if (!leftToken.type().equals(TokenType.IDENT) && rightToken.type().equals(TokenType.IDENT)) {
            Object rightVal = getValueFromContext(rightToken, applicationContext, procedureContext);
            return Double.parseDouble(leftToken.lexeme()) - Double.parseDouble(rightVal.toString());
        } else if (leftToken.type().equals(TokenType.IDENT) && rightToken.type().equals(TokenType.IDENT)) {
            Object leftVal = ((BasicExpression.Literal)getValueFromContext(leftToken, applicationContext, procedureContext)).value.lexeme();
            Object rightVal = ((BasicExpression.Literal)getValueFromContext(rightToken, applicationContext, procedureContext)).value.lexeme();
            return Double.parseDouble(leftVal.toString()) - Double.parseDouble(rightVal.toString());
        }
        return Double.parseDouble(leftToken.lexeme()) - Double.parseDouble(rightToken.lexeme());
    }

    private static Double multiply(BasicExpression left, BasicExpression right,
                                   ApplicationContext applicationContext, ProcedureContext procedureContext) {
        if (!(left instanceof BasicExpression.Literal)) {
            left = evaluateExpression(left, applicationContext, procedureContext);
        }

        if (!(right instanceof BasicExpression.Literal)) {
            right = evaluateExpression(right, applicationContext, procedureContext);
        }

        Token leftToken = ((BasicExpression.Literal) left).value;
        Token rightToken = ((BasicExpression.Literal) right).value;

        if (leftToken.type().equals(TokenType.IDENT) && !rightToken.type().equals(TokenType.IDENT)) {
            Object leftVal = getValueFromContext(leftToken, applicationContext, procedureContext);
            return Double.parseDouble(leftVal.toString()) * Double.parseDouble(rightToken.lexeme());
        } else if (!leftToken.type().equals(TokenType.IDENT) && rightToken.type().equals(TokenType.IDENT)) {
            Object rightVal = getValueFromContext(rightToken, applicationContext, procedureContext);
            return Double.parseDouble(leftToken.lexeme()) * Double.parseDouble(rightVal.toString());
        } else if (leftToken.type().equals(TokenType.IDENT) && rightToken.type().equals(TokenType.IDENT)) {
            Object leftVal = ((BasicExpression.Literal)getValueFromContext(leftToken, applicationContext, procedureContext)).value.lexeme();
            Object rightVal = ((BasicExpression.Literal)getValueFromContext(rightToken, applicationContext, procedureContext)).value.lexeme();
            return Double.parseDouble(leftVal.toString()) * Double.parseDouble(rightVal.toString());
        }
        return Double.parseDouble(leftToken.lexeme()) * Double.parseDouble(rightToken.lexeme());
    }

    private static Double divide(BasicExpression left, BasicExpression right,
                                 ApplicationContext applicationContext, ProcedureContext procedureContext) {
        if (!(left instanceof BasicExpression.Literal)) {
            left = evaluateExpression(left, applicationContext, procedureContext);
        }

        if (!(right instanceof BasicExpression.Literal)) {
            right = evaluateExpression(right, applicationContext, procedureContext);
        }

        Token leftToken = ((BasicExpression.Literal) left).value;
        Token rightToken = ((BasicExpression.Literal) right).value;

        if (leftToken.type().equals(TokenType.IDENT) && !rightToken.type().equals(TokenType.IDENT)) {
            Object leftVal = getValueFromContext(leftToken, applicationContext, procedureContext);
            return Double.parseDouble(leftVal.toString()) / Double.parseDouble(rightToken.lexeme());
        } else if (!leftToken.type().equals(TokenType.IDENT) && rightToken.type().equals(TokenType.IDENT)) {
            Object rightVal = getValueFromContext(rightToken, applicationContext, procedureContext);
            return Double.parseDouble(leftToken.lexeme()) / Double.parseDouble(rightVal.toString());
        } else if (leftToken.type().equals(TokenType.IDENT) && rightToken.type().equals(TokenType.IDENT)) {
            Object leftVal = ((BasicExpression.Literal)getValueFromContext(leftToken, applicationContext, procedureContext)).value.lexeme();
            Object rightVal = ((BasicExpression.Literal)getValueFromContext(rightToken, applicationContext, procedureContext)).value.lexeme();
            return Double.parseDouble(leftVal.toString()) / Double.parseDouble(rightVal.toString());
        }
        return Double.parseDouble(leftToken.lexeme()) / Double.parseDouble(rightToken.lexeme());
    }

    private static Boolean greaterThanOrEqual(BasicExpression left, BasicExpression right,
                                              ApplicationContext applicationContext, ProcedureContext procedureContext) {
        if (!(left instanceof BasicExpression.Literal)) {
            left = evaluateExpression(left, applicationContext, procedureContext);
        }

        if (!(right instanceof BasicExpression.Literal)) {
            right = evaluateExpression(right, applicationContext, procedureContext);
        }

        Token leftToken = ((BasicExpression.Literal) left).value;
        Token rightToken = ((BasicExpression.Literal) right).value;

        if (leftToken.type().equals(TokenType.IDENT) && !rightToken.type().equals(TokenType.IDENT)) {
            Object leftVal = getValueFromContext(leftToken, applicationContext, procedureContext);
            return Double.parseDouble(leftVal.toString()) >= Double.parseDouble(rightToken.lexeme());
        } else if (!leftToken.type().equals(TokenType.IDENT) && rightToken.type().equals(TokenType.IDENT)) {
            Object rightVal = getValueFromContext(rightToken, applicationContext, procedureContext);
            return Double.parseDouble(leftToken.lexeme()) >= Double.parseDouble(rightVal.toString());
        } else if (leftToken.type().equals(TokenType.IDENT) && rightToken.type().equals(TokenType.IDENT)) {
            Object leftVal = ((BasicExpression.Literal)getValueFromContext(leftToken, applicationContext, procedureContext)).value.lexeme();
            Object rightVal = ((BasicExpression.Literal)getValueFromContext(rightToken, applicationContext, procedureContext)).value.lexeme();
            return Double.parseDouble(leftVal.toString()) >= Double.parseDouble(rightVal.toString());
        }
        return Double.parseDouble(leftToken.lexeme()) >= Double.parseDouble(rightToken.lexeme());
    }

    private static Boolean lessThanOrEqual(BasicExpression left, BasicExpression right,
                                           ApplicationContext applicationContext, ProcedureContext procedureContext) {
        if (!(left instanceof BasicExpression.Literal)) {
            left = evaluateExpression(left, applicationContext, procedureContext);
        }

        if (!(right instanceof BasicExpression.Literal)) {
            right = evaluateExpression(right, applicationContext, procedureContext);
        }

        Token leftToken = ((BasicExpression.Literal) left).value;
        Token rightToken = ((BasicExpression.Literal) right).value;

        if (leftToken.type().equals(TokenType.IDENT) && !rightToken.type().equals(TokenType.IDENT)) {
            Object leftVal = getValueFromContext(leftToken, applicationContext, procedureContext);
            return Double.parseDouble(leftVal.toString()) <= Double.parseDouble(rightToken.lexeme());
        } else if (!leftToken.type().equals(TokenType.IDENT) && rightToken.type().equals(TokenType.IDENT)) {
            Object rightVal = getValueFromContext(rightToken, applicationContext, procedureContext);
            return Double.parseDouble(leftToken.lexeme()) <= Double.parseDouble(rightVal.toString());
        } else if (leftToken.type().equals(TokenType.IDENT) && rightToken.type().equals(TokenType.IDENT)) {
            Object leftVal = ((BasicExpression.Literal)getValueFromContext(leftToken, applicationContext, procedureContext)).value.lexeme();
            Object rightVal = ((BasicExpression.Literal)getValueFromContext(rightToken, applicationContext, procedureContext)).value.lexeme();
            return Double.parseDouble(leftVal.toString()) <= Double.parseDouble(rightVal.toString());
        }
        return Double.parseDouble(leftToken.lexeme()) <= Double.parseDouble(rightToken.lexeme());

    }

    private static Boolean equal(BasicExpression left, BasicExpression right,
                                 ApplicationContext applicationContext, ProcedureContext procedureContext) {
        if (!(left instanceof BasicExpression.Literal)) {
            left = evaluateExpression(left, applicationContext, procedureContext);
        }

        if (!(right instanceof BasicExpression.Literal)) {
            right = evaluateExpression(right, applicationContext, procedureContext);
        }

        Token leftToken = ((BasicExpression.Literal) left).value;
        Token rightToken = ((BasicExpression.Literal) right).value;

        if (leftToken.type().equals(TokenType.IDENT) && !rightToken.type().equals(TokenType.IDENT)) {
            Object leftVal = getValueFromContext(leftToken, applicationContext, procedureContext);
            return Double.parseDouble(leftVal.toString()) == Double.parseDouble(rightToken.lexeme());
        } else if (!leftToken.type().equals(TokenType.IDENT) && rightToken.type().equals(TokenType.IDENT)) {
            Object rightVal = getValueFromContext(rightToken, applicationContext, procedureContext);
            return Double.parseDouble(leftToken.lexeme()) == Double.parseDouble(rightVal.toString());
        } else if (leftToken.type().equals(TokenType.IDENT) && rightToken.type().equals(TokenType.IDENT)) {
            Object leftVal = ((BasicExpression.Literal)getValueFromContext(leftToken, applicationContext, procedureContext)).value.lexeme();
            Object rightVal = ((BasicExpression.Literal)getValueFromContext(rightToken, applicationContext, procedureContext)).value.lexeme();
            return Double.parseDouble(leftVal.toString()) == Double.parseDouble(rightVal.toString());
        }
        return Double.parseDouble(leftToken.lexeme()) == Double.parseDouble(rightToken.lexeme());
    }

    private static Boolean greater(BasicExpression left, BasicExpression right,
                                   ApplicationContext applicationContext, ProcedureContext procedureContext) {
        if (!(left instanceof BasicExpression.Literal)) {
            left = evaluateExpression(left, applicationContext, procedureContext);
        }

        if (!(right instanceof BasicExpression.Literal)) {
            right = evaluateExpression(right, applicationContext, procedureContext);
        }

        Token leftToken = ((BasicExpression.Literal) left).value;
        Token rightToken = ((BasicExpression.Literal) right).value;

        if (leftToken.type().equals(TokenType.IDENT) && !rightToken.type().equals(TokenType.IDENT)) {
            Object leftVal = getValueFromContext(leftToken, applicationContext, procedureContext);
            return Double.parseDouble(leftVal.toString()) > Double.parseDouble(rightToken.lexeme());
        } else if (!leftToken.type().equals(TokenType.IDENT) && rightToken.type().equals(TokenType.IDENT)) {
            Object rightVal = ((BasicExpression.Literal)getValueFromContext(rightToken, applicationContext, procedureContext)).value.lexeme();
            return Double.parseDouble(leftToken.lexeme()) > Double.parseDouble(rightVal.toString());
        } else if (leftToken.type().equals(TokenType.IDENT) && rightToken.type().equals(TokenType.IDENT)) {
            Object leftVal = ((BasicExpression.Literal)getValueFromContext(leftToken, applicationContext, procedureContext)).value.lexeme();
            Object rightVal = ((BasicExpression.Literal)getValueFromContext(rightToken, applicationContext, procedureContext)).value.lexeme();
            return Double.parseDouble(leftVal.toString()) > Double.parseDouble(rightVal.toString());
        }
        return Double.parseDouble(leftToken.lexeme()) > Double.parseDouble(rightToken.lexeme());
    }

    private static Boolean less(BasicExpression left, BasicExpression right,
                                ApplicationContext applicationContext, ProcedureContext procedureContext) {
        if (!(left instanceof BasicExpression.Literal)) {
            left = evaluateExpression(left, applicationContext, procedureContext);
        }

        if (!(right instanceof BasicExpression.Literal)) {
            right = evaluateExpression(right, applicationContext, procedureContext);
        }

        Token leftToken = ((BasicExpression.Literal) left).value;
        Token rightToken = ((BasicExpression.Literal) right).value;

        if (leftToken.type().equals(TokenType.IDENT) && !rightToken.type().equals(TokenType.IDENT)) {
            Object leftVal = getValueFromContext(leftToken, applicationContext, procedureContext);
            return Double.parseDouble(leftVal.toString()) < Double.parseDouble(rightToken.lexeme());
        } else if (!leftToken.type().equals(TokenType.IDENT) && rightToken.type().equals(TokenType.IDENT)) {
            Object rightVal = getValueFromContext(rightToken, applicationContext, procedureContext);
            return Double.parseDouble(leftToken.lexeme()) < Double.parseDouble(rightVal.toString());
        } else if (leftToken.type().equals(TokenType.IDENT) && rightToken.type().equals(TokenType.IDENT)) {
            Object leftVal = ((BasicExpression.Literal)getValueFromContext(leftToken, applicationContext, procedureContext)).value.lexeme();
            Object rightVal = ((BasicExpression.Literal)getValueFromContext(rightToken, applicationContext, procedureContext)).value.lexeme();
            return Double.parseDouble(leftVal.toString()) < Double.parseDouble(rightVal.toString());
        }
        return Double.parseDouble(leftToken.lexeme()) < Double.parseDouble(rightToken.lexeme());
    }

    private static Boolean notEqual(BasicExpression left, BasicExpression right,
                                    ApplicationContext applicationContext, ProcedureContext procedureContext) {
        if (!(left instanceof BasicExpression.Literal)) {
            left = evaluateExpression(left, applicationContext, procedureContext);
        }

        if (!(right instanceof BasicExpression.Literal)) {
            right = evaluateExpression(right, applicationContext, procedureContext);
        }

        Token leftToken = ((BasicExpression.Literal) left).value;
        Token rightToken = ((BasicExpression.Literal) right).value;

        if (leftToken.type().equals(TokenType.IDENT) && !rightToken.type().equals(TokenType.IDENT)) {
            Object leftVal = getValueFromContext(leftToken, applicationContext, procedureContext);
            return Double.parseDouble(leftVal.toString()) != Double.parseDouble(rightToken.lexeme());
        } else if (!leftToken.type().equals(TokenType.IDENT) && rightToken.type().equals(TokenType.IDENT)) {
            Object rightVal = getValueFromContext(rightToken, applicationContext, procedureContext);
            return Double.parseDouble(leftToken.lexeme()) != Double.parseDouble(rightVal.toString());
        } else if (leftToken.type().equals(TokenType.IDENT) && rightToken.type().equals(TokenType.IDENT)) {
            Object leftVal = ((BasicExpression.Literal)getValueFromContext(leftToken, applicationContext, procedureContext)).value.lexeme();
            Object rightVal = ((BasicExpression.Literal)getValueFromContext(rightToken, applicationContext, procedureContext)).value.lexeme();
            return Double.parseDouble(leftVal.toString()) != Double.parseDouble(rightVal.toString());
        }
        return Double.parseDouble(leftToken.lexeme()) != Double.parseDouble(rightToken.lexeme());
    }

    public static BasicExpression getValueFromContext(Token ident, ApplicationContext applicationContext,
                                                      ProcedureContext procedureContext) {
        BasicExpression variable = applicationContext.getConstant(ident.lexeme());
        if (variable == null) variable = applicationContext.getVariable(ident.lexeme());
        if (procedureContext != null && variable == null) variable = procedureContext.getVariable(ident.lexeme());

        return variable == null ? new BasicExpression.Literal(ident) : variable;
    }
}
