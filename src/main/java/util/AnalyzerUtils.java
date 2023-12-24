package util;

import ast.BasicExpression;
import ast.Statement;
import interpreter.Interpreter;
import structure.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class AnalyzerUtils {

    private static final Pattern NUMERIC_PATTERN =
        Pattern.compile("((\\+|-)?([0-9]+)(\\.[0-9]+)?)|((\\+|-)?\\.?[0-9]+)\n");

    public static Map<String, String> OPERATOR_TYPE_MAP = new HashMap<>();

    static {
        OPERATOR_TYPE_MAP.put("<", "BOOLEAN");
        OPERATOR_TYPE_MAP.put("<=", "BOOLEAN");
        OPERATOR_TYPE_MAP.put(">", "BOOLEAN");
        OPERATOR_TYPE_MAP.put(">=", "BOOLEAN");
        OPERATOR_TYPE_MAP.put("=", "BOOLEAN");
        OPERATOR_TYPE_MAP.put("#", "BOOLEAN");
        OPERATOR_TYPE_MAP.put("+", "NUMERIC");
        OPERATOR_TYPE_MAP.put("-", "NUMERIC");
        OPERATOR_TYPE_MAP.put("*", "NUMERIC");
        OPERATOR_TYPE_MAP.put("/", "NUMERIC");
        OPERATOR_TYPE_MAP.put("DIV", "NUMERIC");
        OPERATOR_TYPE_MAP.put("MOD", "NUMERIC");
        OPERATOR_TYPE_MAP.put("AND", "BOOLEAN");
        OPERATOR_TYPE_MAP.put("OR", "BOOLEAN");
    }

    public static final Map<String, Class> MODULA_TYPES = Map.of(
        "INTEGER", Integer.class,
        "CARDINAL", Integer.class,
        "REAL", Double.class,
        "BOOLEAN", Boolean.class,
        "CHAR", Character.class,
        "STRING", String.class
    );

    public static final Map<String, String> NATIVE_RETURN_TYPES = Map.of(
        "MIN", "TYPE",
        "MAX", "TYPE",
        "CHR", "CHAR",
        "FLOAT", "NUMERIC",
        "TRUNC", "NUMERIC",
        "ORD", "NUMERIC",
        "CAP", "STRING",
        "VAL", "NUMERIC",
        "INC", "NUMERIC",
        "DEC", "NUMERIC"
        );


    public static final Map<String, String> NUMERIC_TYPES = Map.of(
        "INTEGER", "NUMERIC",
        "CARDINAL", "NUMERIC",
        "REAL", "NUMERIC",
        "CHAR", "NUMERIC"
    );

    public static final Map<String, String> BOOLEAN_TYPES = Map.of(
        "BOOLEAN", "BOOLEAN"
    );

    public static final Map<String, String> STRING_TYPES = Map.of(
        "CHAR", "STRING",
        "STRING", "STRING"
    );

    public static String getExpressionType(BasicExpression expression, Block block, int line) {
        List<Statement.Var> vars = extractAllVars(block.getStatementList());
        List<String> varNames = vars.stream().map(v -> v.getName().lexeme()).toList();
        List<Statement.VarArray> varArrays = extractAllVarArrays(block.getStatementList());
        List<String> varArrayNames = varArrays.stream().map(v -> v.getName().lexeme()).toList();
        List<Statement.Const> constants = extractAllConsts(block.getStatementList());
        List<String> constNames = constants.stream().map(c -> c.getName().lexeme()).toList();

        if (expression instanceof BasicExpression.Binary) {
            return OPERATOR_TYPE_MAP.get(((BasicExpression.Binary) expression).operator.lexeme());
        } else if (expression instanceof BasicExpression.Grouping grouping) {
            return getExpressionType(grouping.expression, block, line);
        } else if (expression instanceof BasicExpression.Literal literal) {
            if (varNames.contains(literal.value.lexeme())) {
                return vars.stream()
                           .filter(v -> v.getName()
                                         .lexeme()
                                         .equals(literal.value.lexeme()))
                          .findFirst()
                          .get().getType().lexeme();
            }
            if (varArrayNames.contains(literal.value.lexeme())) {
                return varArrays.stream()
                                .filter(v -> v.getName()
                                              .lexeme()
                                              .equals(literal.value.lexeme()))
                                .findFirst()
                                .get().getType().lexeme();
            }
            if (constNames.contains(literal.value.lexeme())) {
                return "NUMERIC";
            }
            if (literal.value.lexeme().equals("TRUE") || literal.value.lexeme().equals("FALSE")) {
                return "BOOLEAN";
            }
            if (literal.value.lexeme().startsWith("'")) {
                return "NUMERIC";
            }

            if (literal.value.lexeme().startsWith("\"") && literal.value.lexeme().endsWith("\"")) {
                return "STRING";
            }
            if(literal.value.lexeme().equals("CARDINAL") || literal.value.lexeme().equals("INTEGER") ||
               literal.value.lexeme().equals("REAL") || literal.value.lexeme().equals("BOOLEAN") ||
               literal.value.lexeme().equals("CHAR") || literal.value.lexeme().equals("STRING")) {
                return "TYPE";
            }
            if (NUMERIC_PATTERN.matcher(literal.value.lexeme()).matches()) {
                return "NUMERIC";
            }
        } else if (expression instanceof BasicExpression.Negation negation) {
            return getExpressionType(negation.getNegatedValue(), block, line);
        } else if (expression instanceof BasicExpression.ArrayVariable) {
            if(!varArrayNames.contains(((BasicExpression.ArrayVariable) expression).name.lexeme())) {
                Interpreter.error(line,
                                  " at '" + expression + "'",
                                  "Variable " + ((BasicExpression.ArrayVariable) expression).name.lexeme()
                                      + " is not declared");
                System.exit(1);
            }
            Statement.VarArray varArray =
                extractAllVarArrays(block.getStatementList()).stream()
                                                             .filter(v -> v.getName()
                                                                           .lexeme()
                                                                           .equals(
                                                                               ((BasicExpression.ArrayVariable)
                                                                                   expression).name.lexeme()))
                                                             .findFirst()
                                                             .get();
            return varArray.getType().lexeme();
        } else if (expression instanceof BasicExpression.ProcedureCall procedure) {
            if (NATIVE_RETURN_TYPES.containsKey(procedure.name.lexeme())) {
                return NATIVE_RETURN_TYPES.get(procedure.name.lexeme());
            }
        } else {
            Interpreter.error(line,
                              " at '" + expression + "'",
                              "Invalid expression type");
            System.exit(1);
            return null;
        }
        return null;
    }


    public static List<Statement.Var> extractAllVars(List<Statement> statements) {
        List<Statement.Var> vars = new ArrayList<>();

        for (Statement statement : statements) {
            if (statement instanceof Statement.Var variable) {
                vars.add(variable);
            }

            if (statement instanceof Statement.Procedure procedure) {
                vars.addAll(extractAllVars(procedure.getBody()));
            }
        }

        return vars;
    }

    public static List<Statement.VarArray> extractAllVarArrays(List<Statement> statements) {
        List<Statement.VarArray> varArrays = new ArrayList<>();
        for (Statement statement : statements) {
            if (statement instanceof Statement.VarArray variable) {
                varArrays.add(variable);
            }
            if (statement instanceof Statement.Procedure procedure) {
                varArrays.addAll(extractAllVarArrays(procedure.getBody()));
            }
        }
        return varArrays;
    }

    public static List<Statement.Const> extractAllConsts(List<Statement> statements) {
        return statements.stream()
                         .filter(s -> s instanceof Statement.Const)
                         .map(s -> (Statement.Const) s)
                         .toList();
    }


    public static List<String> mergeStatements(List<Statement.Var> varStatements,
                                         List<Statement.VarArray> varArrayStatements,
                                         List<Statement.Const> constStatements) {
        List<String> varNames = varStatements.stream()
                                             .map(v -> v.getName().lexeme())
                                             .toList();
        List<String> varArrayNames = varArrayStatements.stream()
                                                       .map(v -> v.getName().lexeme())
                                                       .toList();
        List<String> constNames = constStatements.stream()
            .map(c -> c.getName().lexeme())
            .toList();

        List<String> allNames = new ArrayList<>();
        allNames.addAll(varNames);
        allNames.addAll(varArrayNames);
        allNames.addAll(constNames);

        return allNames;
    }
}
