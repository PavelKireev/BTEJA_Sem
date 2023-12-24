package library;

import ast.BasicExpression;
import context.ProcedureContext;
import evaluator.ExpressionEvaluator;
import interpreter.Interpreter;

import java.util.List;
import java.util.Map;

public class Native {

    public static final List<String> supportedProcedures = List.of("CHR", "FLOAT", "TRUNC", "ORD",
                                                                   "CAP", "VAL", "INC", "DEC", "MIN", "MAX");

    public static final Map<String, List<String>> ARGS_MAP = Map.of(
        "CHR", List.of("NUMERIC"),
        "FLOAT", List.of("NUMERIC"),
        "TRUNC", List.of("NUMERIC"),
        "ORD", List.of("NUMERIC"),
        "CAP", List.of("NUMERIC"),
        "VAL", List.of("STRING", "NUMERIC"),
        "INC", List.of("NUMERIC"),
        "DEC", List.of("NUMERIC"),
        "MIN", List.of("TYPE"),
        "MAX", List.of("TYPE")
    );

    public static Object executeNativeProcedure(BasicExpression.ProcedureCall expression, ProcedureContext procedureContext) {
        String procedureName = expression.name.lexeme();
        List<Object> arguments = expression.arguments
                                           .stream()
                                           .map(argument -> ExpressionEvaluator.evaluate(argument, procedureContext))
                                           .toList();
        switch (procedureName) {
            case "CHR":
                return Native.CHR((Integer) arguments.get(0));
            case "FLOAT":
                return Native.FLOAT((Integer) arguments.get(0));
            case "TRUNC":
                return Native.TRUNC((Double) arguments.get(0));
            case "ORD":
                return Native.ORD(String.valueOf(arguments.get(0)));
            case "CAP":
                return Native.CAP(String.valueOf(arguments.get(0)));
            case "VAL":
                return Native.VAL(String.valueOf(arguments.get(0)), arguments.get(1));
            case "INC":
                return Native.INC(arguments.get(0), arguments.size() > 1 ? (Integer) arguments.get(1) : 0);
            case "DEC":
                return Native.DEC(arguments.get(0), arguments.size() > 1 ? (Integer) arguments.get(1) : 0);
            case "MIN":
                return Native.MIN(String.valueOf(arguments.get(0)));
            case "MAX":
                return Native.MAX(String.valueOf(arguments.get(0)));
            default:
                Interpreter.error(0, " at '" + procedureName + "'", "Procedure not found");
                System.exit(1);
                break;
        }
        return null;
    }

    public static double FLOAT(Integer number) {
        return number.floatValue();
    }

    public static int TRUNC(Double number) {
        return number.intValue();
    }

    public static Object VAL(String type, Object value) {
        return switch (type) {
            case "INTEGER" -> Integer.parseInt(value.toString());
            case "CARDINAL" -> Integer.parseUnsignedInt(value.toString());
            case "REAL" -> Double.parseDouble(value.toString());
            case "CHAR" -> value.toString().charAt(0);
            default -> null;
        };
    }

    public static Object INC(Object number) {
        return INC(number, 1);
    }

    public static Integer INC(Object number, Integer increment) {
        number = (Integer) number + increment;
        return (Integer) number;
    }

    public static Integer DEC(Object number) {
        return DEC(number, 1);
    }

    public static Integer DEC(Object number, Integer decrement) {
        number = (Integer) number - decrement;
        return (Integer) number;
    }

    public static Object MIN(String type) {
        return switch (type) {
            case "INTEGER" -> Integer.MIN_VALUE;
            case "CARDINAL" -> 0;
            case "REAL" -> Double.MIN_VALUE;
            case "CHAR" -> Character.MIN_VALUE;
            default -> 0;
        };
    }

    public static Object MAX(String type) {
        return switch (type) {
            case "INTEGER" -> Integer.MAX_VALUE;
            case "CARDINAL" -> 65535;
            case "REAL" -> Double.MAX_VALUE;
            case "CHAR" -> Character.MAX_VALUE;
            default -> 0;
        };
    }

    public static String CAP(String text) {
        return text.toUpperCase();
    }

    public static Integer ORD(String character) {
        return (int) character.charAt(0);
    }

    public static char CHR(Integer ordinal) {
        return (char) ordinal.intValue();
    }
}
