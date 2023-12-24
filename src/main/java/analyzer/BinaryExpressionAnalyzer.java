package analyzer;

import ast.BasicExpression;
import context.ProcedureContext;
import interpreter.Interpreter;
import structure.Block;
import util.AnalyzerUtils;

import java.util.List;

public class BinaryExpressionAnalyzer implements ExpressionAnalyzer<BasicExpression.Binary>{

    private static final List<String> BOOLEAN_OPERATORS = List.of("=", "#", ">", ">=", "<", "<=" ,"AND", "OR");
    private static final List<String> NUMERIC_OPERATORS = List.of("+", "-", "*", "DIV", "MOD");

    @Override
    public void analyze(BasicExpression.Binary expression, Block block) {
        String leftType = AnalyzerUtils.getExpressionType(expression.left, block, expression.getLine());
        String rightType = AnalyzerUtils.getExpressionType(expression.right, block, expression.getLine());

        if (!leftType.equals(rightType)) {
            Interpreter.error(expression.getLine(),
                              "Binary expression",
                              "Binary expression operands must be of the same type");
            System.exit(1);
        }

        if (leftType.equals("BOOLEAN") && !BOOLEAN_OPERATORS.contains(expression.operator.lexeme())) {
            Interpreter.error(expression.getLine(),
                              "Binary expression",
                              "Binary expression operator must be =, #, AND, or OR for boolean operands");
            System.exit(1);
        }

        if (leftType.equals("NUMERIC") && !NUMERIC_OPERATORS.contains(expression.operator.lexeme())) {
            Interpreter.error(expression.getLine(),
                              "Binary expression",
                              "Binary expression operator must be +, -, *, DIV or MOD for numeric operands");
            System.exit(1);
        }

    }
}
