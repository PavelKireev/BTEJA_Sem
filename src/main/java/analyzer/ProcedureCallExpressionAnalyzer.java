package analyzer;

import ast.BasicExpression;
import ast.Statement;
import interpreter.Interpreter;
import library.Native;
import library.Terminal2;
import structure.Block;
import util.AnalyzerUtils;

import java.util.ArrayList;
import java.util.List;

public class ProcedureCallExpressionAnalyzer implements ExpressionAnalyzer<BasicExpression.ProcedureCall> {
    @Override
    public void analyze(BasicExpression.ProcedureCall expression, Block block) {
        String procedureName = expression.name.lexeme();
        List<Statement.Procedure> declaredProcedures = extractAllProcedures(block.getStatementList());
        List<String> declaredProcedureNames = declaredProcedures.stream()
                                                                .map(p -> p.getName()
                                                                           .lexeme())
                                                                .toList();

        if (Native.supportedProcedures.contains(procedureName)) {
            for (int i = 0; i <= Native.ARGS_MAP.get(procedureName).size(); i++) {
                try {
                    String passedArgType = AnalyzerUtils.getExpressionType(expression.arguments.get(i),
                                                                           block,
                                                                           expression.name.line());
                    String requiredArgType = Native.ARGS_MAP.get(procedureName).get(i);
                    if (!passedArgType.equals(requiredArgType)) {
                        Interpreter.error(expression.name.line(),
                                          "Procedure Call",
                                          "Procedure " + procedureName +
                                              " requires argument " + (i + 1) + " to be of type " +
                                              requiredArgType + " but " + passedArgType + " was given");
                        System.exit(1);
                    }
                } catch (IndexOutOfBoundsException ignore) {
                    //ignore;
                }
            }
            return;
        }

        if (Terminal2.supportedProcedures.contains(procedureName)) {
            for (int i = 0; i <= Terminal2.ARGS_MAP.get(procedureName).size(); i++) {
                try {
                    String passedArgType = AnalyzerUtils.getExpressionType(expression.arguments.get(i),
                                                                           block,
                                                                           expression.name.line());
                    String requiredArgType = Terminal2.ARGS_MAP.get(procedureName).get(i);
                    if (!passedArgType.equals(requiredArgType)) {
                        Interpreter.error(expression.name.line(),
                            "Procedure Call",
                            "Procedure " + procedureName +
                                " requires argument " + (i + 1) + " to be of type " +
                                requiredArgType + " but " + passedArgType + " was given");
                        System.exit(1);
                    }
                } catch (IndexOutOfBoundsException ignore) {
                    //ignore;
                }
            }
            return;
        }

        if (!declaredProcedureNames.contains(procedureName)) {
            Interpreter.error(expression.name.line(),
                              "Procedure Call",
                              "Procedure " + procedureName + " is not declared");
            System.exit(1);
        }

        List<String> argTypes = expression.arguments
                                           .stream()
                                           .map(arg -> AnalyzerUtils.getExpressionType(arg,
                                                                                        block,
                                                                                        expression.name.line()))
                                           .toList();


        Statement.Procedure procedure = declaredProcedures.stream()
                                                          .filter(p -> p.getName().lexeme().equals(procedureName))
                                                          .findFirst().orElse(null);

        for (int i = 0; i <= procedure.getParameters().size(); i++) {
            if (i == procedure.getParameters().size()) {
                if (argTypes.size() != procedure.getParameters().size()) {
                    Interpreter.error(expression.name.line(),
                                      "Procedure Call",
                                      "Procedure " + procedureName + " requires " + procedure.getParameters().size() +
                                      " arguments but " + argTypes.size() + " were given");
                    System.exit(1);
                }
            } else {
                if (i >= argTypes.size()) {
                    Interpreter.error(expression.name.line(),
                                      "Procedure Call",
                                      "Procedure " + procedureName + " requires " + procedure.getParameters().size() +
                                      " arguments but " + argTypes.size() + " were given");
                    System.exit(1);
                }

                if (!argTypes.get(i).equals(procedure.getParameters().get(i).getType().lexeme())) {
                    Interpreter.error(expression.name.line(),
                                      "Procedure Call",
                                      "Procedure " + procedureName + " requires argument " + (i + 1) + " to be of type " +
                                      procedure.getParameters().get(i).getType().lexeme() + " but " +
                                      argTypes.get(i) + " was given");
                    System.exit(1);
                }
            }
        }
    }

    private List<Statement.Procedure> extractAllProcedures(List<Statement> statements) {
        List<Statement.Procedure> procedures = new ArrayList<>();

        for (Statement statement : statements) {
            if (statement instanceof Statement.Procedure) {
                procedures.add((Statement.Procedure) statement);
                procedures.addAll(extractAllProcedures(((Statement.Procedure) statement).getBody()));
            }
        }

        return procedures;
    }
}
