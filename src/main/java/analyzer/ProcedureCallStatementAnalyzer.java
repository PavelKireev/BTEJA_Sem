package analyzer;

import ast.Statement;
import context.ProcedureContext;
import interpreter.Interpreter;
import library.Native;
import library.Terminal2;
import structure.Block;
import util.AnalyzerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProcedureCallStatementAnalyzer implements StatementAnalyzer<Statement.Call> {

    private static final Map<String, String> typeGroupMap = Map.of("INTEGER", "NUMERIC",
                                                                   "CARDINAL", "NUMERIC",
                                                                   "NUMERIC", "NUMERIC",
                                                                   "REAL", "NUMERIC",
                                                                   "STRING", "STRING",
                                                                   "BOOLEAN", "BOOLEAN",
                                                                   "CHAR", "CHAR");
    @Override
    public void analyze(Statement.Call statement, Block block) {
        String procedureName = statement.getProcedureName().lexeme();
        String imports = block.getStatementList().stream().filter(s -> s instanceof Statement.Import)
                              .map(s -> (Statement.Import) s)
                              .map(s -> s.getImports())
                              .flatMap(List::stream)
                              .toList()
                              .toString();


        if (!extractAllProcedureNames(block.getStatementList()).contains(procedureName) &&
            (!imports.contains("Terminal2") && !Terminal2.supportedProcedures.contains(procedureName)) &&
            !Native.supportedProcedures.contains(procedureName)) {
            Interpreter.error(statement.getProcedureName().line(),
                              "Procedure call",
                              "Procedure " + procedureName + " is not declared");
        }

        if (Native.supportedProcedures.contains(procedureName)) {
            for (int i = 0; i <= Native.ARGS_MAP.get(procedureName).size(); i++) {
                try {
                    String passedArgType = AnalyzerUtils.getExpressionType(statement.getArguments().get(i),
                                                                           block,
                                                                           statement.getProcedureName().line());
                    String requiredArgType = Native.ARGS_MAP.get(procedureName).get(i);
                    passedArgType = typeGroupMap.get(passedArgType);
                    requiredArgType = typeGroupMap.get(requiredArgType);
                    if (!passedArgType.equals(requiredArgType)) {
                        Interpreter.error(statement.getProcedureName().line(),
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
        }

        if (Terminal2.supportedProcedures.contains(procedureName)) {
            for (int i = 0; i <= Terminal2.ARGS_MAP.get(procedureName).size(); i++) {
                try {
                    String passedArgType = AnalyzerUtils.getExpressionType(statement.getArguments().get(i),
                                                                           block,
                                                                           statement.getProcedureName().line());
                    String requiredArgType = Terminal2.ARGS_MAP.get(procedureName).get(i);
                    passedArgType = typeGroupMap.get(passedArgType);
                    requiredArgType = typeGroupMap.get(requiredArgType);
                    if (!passedArgType.equals(requiredArgType)) {
                        Interpreter.error(statement.getProcedureName().line(),
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
        }
    }

    private List<String> extractAllProcedureNames(List<Statement> list) {
        List<String> procedureNames = new ArrayList<>();
        List<Statement.Procedure> procedures = list.stream().filter(s -> s instanceof Statement.Procedure)
                                                   .map(s -> (Statement.Procedure) s)
                                                   .toList();
        procedures.forEach(procedure -> {
            procedureNames.add(procedure.getName().lexeme());
            procedureNames.addAll(extractAllProcedureNames(procedure.getBody()));
        });
        return procedureNames;
    }
}
