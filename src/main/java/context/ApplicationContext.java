package context;

import ast.BasicExpression;
import ast.Statement;

import java.util.*;
import java.util.stream.Collectors;

public class ApplicationContext {

    public static String moduleName = "";
    public static final List<String> imports = new ArrayList<>();
    public static final List<Statement> mainProcudure = new ArrayList<>();
    public static final Map<String, BasicExpression> globalVariableList = new HashMap<>();
    public static final Map<String, BasicExpression> globalVariableArrayList = new HashMap<>();
    public static final Map<String, BasicExpression> globalConstantList = new HashMap<>();
    public static final Map<String, List<Statement>> globalProcedureList = new HashMap<>();

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

        Map<String, BasicExpression> constList =
            statements.stream()
                      .filter(statement -> statement instanceof Statement.Const)
                      .collect(Collectors.toMap(statement -> ((Statement.Const) statement).name.lexeme(),
                                                statement -> ((Statement.Const) statement).initializer));
        Map<String, BasicExpression> varList =
            statements.stream()
                      .filter(statement -> statement instanceof Statement.Var)
                      .collect(
                          Collectors.toMap(statement -> ((Statement.Var) statement).name.lexeme(),
                                           statement -> {
                                              if (((Statement.Var) statement).initializer == null)
                                                  return new BasicExpression.Empty();
                                              return ((Statement.Var) statement).initializer;})
                      );
        Map<String, BasicExpression> varArrayList =
            statements.stream()
                      .filter(statement -> statement instanceof Statement.VarArray)
                      .collect(
                          Collectors.toMap(statement -> ((Statement.VarArray) statement).name.lexeme(),
                                           statement -> {
                                              if (((Statement.VarArray) statement).initializer == null)
                                                  return new BasicExpression.Empty();
                                              return ((Statement.VarArray) statement).initializer;})
                      );

        List<Statement> extractedMainProcedure =
            statements.stream()
                      .filter(statement -> statement instanceof Statement.Main)
                      .map(statement -> ((Statement.Main) statement).getBody())
                      .findFirst()
                      .orElse(Collections.emptyList());

        Map<String, List<Statement>> procedureList =
            statements.stream()
                .filter(statement -> statement instanceof Statement.Procedure)
                .collect(Collectors.toMap(statement -> ((Statement.Procedure) statement).getName().lexeme(),
                                          statement -> ((Statement.Procedure) statement).getBody()));

        moduleName = extractedModuleName;
        imports.addAll(extractedImports);
        globalVariableList.putAll(varList);
        globalVariableArrayList.putAll(varArrayList);
        globalConstantList.putAll(constList);
        mainProcudure.addAll(extractedMainProcedure);
        globalProcedureList.putAll(procedureList);

    }

    public void setVariableValue(String key, BasicExpression value) {
        if (globalVariableList.containsKey(key)) {
            globalVariableList.replace(key, value);
        }
    }

    public static BasicExpression getConstant(String key) {
        return globalConstantList.getOrDefault(key, null);
    }

    public static BasicExpression getVariable(String key) {
        return globalVariableList.getOrDefault(key, null);
    }

    public List<Statement> getProcedure(String key) {
        return globalProcedureList.getOrDefault(key, Collections.emptyList());
    }

}
