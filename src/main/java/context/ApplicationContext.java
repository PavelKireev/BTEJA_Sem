package context;

import ast.BasicExpression;
import ast.Statement;
import evaluator.ExpressionEvaluator;
import structure.Array;
import structure.Variable;

import java.util.*;
import java.util.stream.Collectors;

public class ApplicationContext {

    public static String moduleName = "";
    public static final List<String> imports = new ArrayList<>();
    public static final List<Statement> mainProcudure = new ArrayList<>();
    public static final Map<String, Variable> globalVariableList = new HashMap<>();
    public static final Map<String, Array> globalVariableArrayList = new HashMap<>();
    public static final Map<String, Object> globalConstantList = new HashMap<>();
    public static final Map<String, Statement.Procedure> globalProcedureList = new HashMap<>();
    public static final Map<String, String> procedureArgumentMap = new HashMap<>();

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
        Map<String, Variable> varMap =
            statements.stream()
                      .filter(statement -> statement instanceof Statement.Var)
                      .collect(HashMap::new,
                               (m, v) -> m.put(((Statement.Var) v).getName().lexeme(),
                                               new Variable(((Statement.Var) v).getName().lexeme(), null,
                                                            ((Statement.Var) v).getType().lexeme())),
                                               HashMap::putAll
                      );

        Map<String, Array> varArrayMap =
            statements.stream()
                      .filter(statement -> statement instanceof Statement.VarArray)
                      .map(statement -> (Statement.VarArray) statement)
                      .collect(
                          Collectors.toMap(
                              statement -> statement.name.lexeme(),
                              statement -> new Array(statement.name.lexeme(),
                                                     statement.getDimensionRanges(),
                                                     statement.getType().lexeme(),
                                                     new HashMap<>())
                          )
                      );

        List<Statement> extractedMainProcedure =
            statements.stream()
                      .filter(statement -> statement instanceof Statement.Main)
                      .map(statement -> ((Statement.Main) statement).getBody())
                      .findFirst()
                      .orElse(Collections.emptyList());

        Map<String, Statement.Procedure> procedureMap =
            statements.stream()
                .filter(statement -> statement instanceof Statement.Procedure)
                .collect(Collectors.toMap(statement -> ((Statement.Procedure) statement).getName().lexeme(),
                                          statement -> ((Statement.Procedure) statement)));

        statements.stream()
                .filter(statement -> statement instanceof Statement.Procedure)
                .forEach(statement ->
                    procedureArgumentMap.put(((Statement.Procedure) statement).getName().lexeme(),
                                              ((Statement.Procedure) statement).getParameters()
                                                                                .stream()
                                                                                .map(parameter ->
                                                                                    (parameter).getName().lexeme())
                                                                                .collect(Collectors.joining(", "))));

        statements.stream()
                  .filter(statement -> statement instanceof Statement.Procedure)
                  .forEach(statement -> ((Statement.Procedure) statement).getParameters()
                                                   .forEach(param -> varMap.put(param.getName().lexeme(),
                                                                                new Variable(param.getName().lexeme(),
                                                                                             null,
                                                                                             param.getType().lexeme())))
                  );

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
            globalVariableList.get(key).setValue(ExpressionEvaluator.evaluate(value, null));
        }
    }

    public static Object getConstant(String key) {
        return globalConstantList.getOrDefault(key, null);
    }

    public static Variable getVariable(String key) {
        return globalVariableList.getOrDefault(key, null);
    }

    public static Statement.Procedure getProcedure(String key) {
        return globalProcedureList.getOrDefault(key, null);
    }

    public static Object getConst(String key) {
        return globalConstantList.getOrDefault(key, Collections.emptyMap());
    }

    public static Object getArrayVariable(String key, Integer... index) {
        return globalVariableArrayList.getOrDefault(key, null)
                                      .getIndexValueMap().get(new Array.CompositeKey(index));
    }

    public static String getProcedureArguments(String key) {
        return procedureArgumentMap.getOrDefault(key, null);
    }

    public static void setVariable(String key, Object value) {
        globalVariableList.put(key, new Variable(key, value, null));
    }

    public static void updateVariable(String key, Object value) {
        if (globalVariableList.containsKey(key)) {
            globalVariableList.get(key).setValue(value);
        }
    }

    public static Array getArray(String key) {
        return globalVariableArrayList.getOrDefault(key, null);
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

    public static void setArrayVariable(String key, Object value, Integer... index) {
        Array.CompositeKey compositeKey = new Array.CompositeKey(index);
        globalVariableArrayList.getOrDefault(key, null)
                               .getIndexValueMap()
                               .put(compositeKey, value);
    }

}
