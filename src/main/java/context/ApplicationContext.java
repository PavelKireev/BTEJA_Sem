package context;

import ast.BasicExpression;
import ast.Statement;
import evaluator.ExpressionEvaluator;

import java.util.*;
import java.util.stream.Collectors;

public class ApplicationContext {

    public static String moduleName = "";
    public static final List<String> imports = new ArrayList<>();
    public static final List<Statement> mainProcudure = new ArrayList<>();
    public static final Map<String, Object> globalVariableList = new HashMap<>();
    public static final Map<String, Map<Integer, Object>> globalVariableArrayList = new HashMap<>();
    public static final Map<String, Object> globalConstantList = new HashMap<>();
    public static final Map<String, List<Statement>> globalProcedureList = new HashMap<>();
    public static final Map<String, String> procedureArgumentList = new HashMap<>();

    public static void initialize(List<Statement> statements) {
        String extractedModuleName = statements.stream()
                                      .filter(statement -> statement instanceof Statement.Module)
                                      .map(statement -> ((Statement.Module) statement).getName().lexeme())
                                      .findFirst()
                                      .orElse(null);

        List<String> extractedImports = statements.stream()
                                         .filter(statement -> statement instanceof Statement.Import)
                                         .flatMap(statement -> ((Statement.Import) statement).getImports().stream())
                                         .toList();

        Map<String, Object> constList =
            statements.stream()
                      .filter(statement -> statement instanceof Statement.Const)
                      .collect(Collectors.toMap(statement -> ((Statement.Const) statement).name.lexeme(),
                                                statement -> ExpressionEvaluator.evaluate(
                                                    ((Statement.Const) statement).initializer,
                                                    null))
                      );
        Map<String, Object> varMap =
            statements.stream()
                      .filter(statement -> statement instanceof Statement.Var)
                      .collect(HashMap::new,
                               (m, v) -> m.put(((Statement.Var) v).getName().lexeme(), null),
                               HashMap::putAll
                      );
        Map<String, Map<Integer, Object>> varArrayMap =
            statements.stream()
                      .filter(statement -> statement instanceof Statement.VarArray)
                      .collect(
                          Collectors.toMap(
                              statement -> ((Statement.VarArray) statement).name.lexeme(),
                              statement -> new HashMap<>())
                      );

        List<Statement> extractedMainProcedure =
            statements.stream()
                      .filter(statement -> statement instanceof Statement.Main)
                      .map(statement -> ((Statement.Main) statement).getBody())
                      .findFirst()
                      .orElse(Collections.emptyList());

        Map<String, List<Statement>> procedureMap =
            statements.stream()
                .filter(statement -> statement instanceof Statement.Procedure)
                .collect(Collectors.toMap(statement -> ((Statement.Procedure) statement).getName().lexeme(),
                                          statement -> ((Statement.Procedure) statement).getBody()));

        statements.stream()
                .filter(statement -> statement instanceof Statement.Procedure)
                .forEach(statement ->
                    procedureArgumentList.put(((Statement.Procedure) statement).getName().lexeme(),
                                              ((Statement.Procedure) statement).getParameters()
                                                                                .stream()
                                                                                .map(parameter ->
                                                                                    (parameter).getName().lexeme())
                                                                                .collect(Collectors.joining(", "))));

        statements.stream()
                  .filter(statement -> statement instanceof Statement.Procedure)
                  .forEach(statement -> {
                      List<String> argumentList =
                          ((Statement.Procedure) statement).getParameters()
                                                           .stream()
                                                           .map(parameter -> ((Statement.Var) parameter).getName().lexeme())
                                                           .toList();
                        varMap.put(((Statement.Procedure) statement).getName().lexeme(), argumentList.stream()
                                                              .collect(HashMap::new,
                                                                       (m, v) -> m.put(v, null),
                                                                       HashMap::putAll));
                  });

        moduleName = extractedModuleName;
        imports.addAll(extractedImports);
        globalVariableList.putAll(varMap);
        globalVariableArrayList.putAll(varArrayMap);
        globalConstantList.putAll(constList);
        mainProcudure.addAll(extractedMainProcedure);
        globalProcedureList.putAll(procedureMap);

    }

    public void setVariableValue(String key, BasicExpression value) {
        if (globalVariableList.containsKey(key)) {
            globalVariableList.replace(key, value);
        }
    }

    public static Object getConstant(String key) {
        return globalConstantList.getOrDefault(key, null);
    }

    public static Object getVariable(String key) {
        return globalVariableList.getOrDefault(key, null);
    }

    public static List<Statement> getProcedure(String key) {
        return globalProcedureList.getOrDefault(key, Collections.emptyList());
    }

    public static Object getConst(String key) {
        return globalConstantList.getOrDefault(key, Collections.emptyMap());
    }

    public static Object getArrayVariable(String key, int index) {
        return globalVariableArrayList.getOrDefault(key, Collections.emptyMap())
                                      .getOrDefault(index, null);
    }

    public static String getProcedureArguments(String key) {
        return procedureArgumentList.getOrDefault(key, null);
    }

    public static void setVariable(String key, Object value) {
        globalVariableList.put(key, value);
    }

    public static void updateVariable(String key, Object value) {
        if (globalVariableList.containsKey(key)) {
            globalVariableList.replace(key, value);
        }
    }

    public static boolean varExists(String key) {
        return globalVariableList.containsKey(key);
    }

    public static boolean arrayExists(String key) {
        return globalVariableArrayList.containsKey(key);
    }

    public static boolean constantExists(String key) {
        return globalConstantList.containsKey(key);
    }

    public static void setArrayVariable(String key, int index, Object value) {
        globalVariableArrayList.getOrDefault(key, Collections.emptyMap())
                               .put(index, value);
    }

}
