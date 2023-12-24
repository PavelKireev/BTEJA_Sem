package analyzer;

import ast.BasicExpression;
import ast.Statement;
import interpreter.Interpreter;
import structure.Block;
import util.AnalyzerUtils;

import java.util.List;
import java.util.regex.Pattern;

public class AssignmentStatementAnalyzer implements StatementAnalyzer<Statement.Assignment> {

    @Override
    public void analyze(Statement.Assignment statement, Block block) {
        if (statement.getIdent() instanceof BasicExpression.ArrayVariable identExpression) {
            analyzeArrayVariableAssignment(identExpression, statement.getExpression(), block);
        } else {
            analyzeVariableAssignment((BasicExpression.Literal) statement.getIdent(),
                                      statement.getExpression(), block);
        }
    }

    private void analyzeArrayVariableAssignment(BasicExpression.ArrayVariable identExpression,
                                                BasicExpression initializerExpression,
                                                Block block) {
        String ident = identExpression.name.lexeme();

        List<Statement> arrayDeclarations = block.getStatementList()
                                                 .stream()
                                                 .filter(s -> s instanceof Statement.VarArray varStatement &&
                                                              varStatement.getName().lexeme().equals(ident))
                                                 .toList();


        if (arrayDeclarations.isEmpty()) {
            Interpreter.error(identExpression.name.line(),
                              "Variable Value Assignment",
                              "Variable " + ident + " is not declared");
            System.exit(1);
        }

        Statement.VarArray varArray = (Statement.VarArray) arrayDeclarations.get(0);

        String arrayType = varArray.getType().lexeme();
        String expressionType = AnalyzerUtils.getExpressionType(initializerExpression, block,
                                                                identExpression.name.line());

        if (expressionType.equals("NUMERIC") && !AnalyzerUtils.NUMERIC_TYPES.containsKey(arrayType)) {
            Interpreter.error(identExpression.name.line(),
                              "Variable Value Assignment",
                              "Variable " + ident + " is of type " + arrayType +
                              " but is being assigned a value of type " + expressionType);
            System.exit(1);
        }

        if (expressionType.equals("BOOLEAN") && !AnalyzerUtils.BOOLEAN_TYPES.containsKey(arrayType)) {
            Interpreter.error(identExpression.name.line(),
                              "Variable Value Assignment",
                              "Variable " + ident + " is of type " + arrayType +
                              " but is being assigned a value of type " + expressionType);
            System.exit(1);
        }

    }


    private void analyzeVariableAssignment(BasicExpression.Literal identExpression,
                                           BasicExpression initializerExpression, Block block) {
        String ident = identExpression.value.lexeme();

        List<Statement> varDeclarations = block.getStatementList()
                                              .stream()
                                              .filter(s -> s instanceof Statement.Var &&
                                                  ((Statement.Var) s).getName().lexeme().equals(ident))
                                              .toList();

        if (varDeclarations.isEmpty()) {
            Interpreter.error(identExpression.value.line(),
                              "Variable Value Assignment",
                              "Variable " + ident + " is not declared");
            System.exit(1);
        }

        Statement.Var varArray = (Statement.Var) varDeclarations.get(0);

        String varType = varArray.getType().lexeme();
        String expressionType = AnalyzerUtils.getExpressionType(initializerExpression, block,
                                                                 identExpression.value.line());

        if (expressionType.equals("NUMERIC") && !AnalyzerUtils.NUMERIC_TYPES.containsKey(varType)) {
            Interpreter.error(identExpression.value.line(),
                "Variable Value Assignment",
                "Variable " + ident + " is of type " + varType +
                    " but is being assigned a value of type " + expressionType);
            System.exit(1);
        }

        if (expressionType.equals("BOOLEAN") && !AnalyzerUtils.BOOLEAN_TYPES.containsKey(varType)) {
            Interpreter.error(identExpression.value.line(),
                              "Variable Value Assignment",
                              "Variable " + ident + " is of type " + varType +
                              " but is being assigned a value of type " + expressionType);
            System.exit(1);
        }
    }
}
