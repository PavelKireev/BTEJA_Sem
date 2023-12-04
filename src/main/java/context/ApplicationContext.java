package context;

import ast.BasicExpression;
import ast.Statement;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ApplicationContext {

    private final String moduleName;
    private final List<String> imports;
    private final Map<String, BasicExpression> globalVariableList;
    private final Map<String, BasicExpression> globalVariableArrayList;
    private final Map<String, BasicExpression> globalConstantList;
    private final Map<String, List<Statement>> procedureList;

    public ApplicationContext(String moduleName,
                              List<String> imports,
                              Map<String, BasicExpression> globalVariableList,
                              Map<String, BasicExpression> globalVariableArrayList,
                              Map<String, BasicExpression> globalConstantList,
                              Map<String, List<Statement>> procedureList) {
        this.moduleName = moduleName;
        this.imports = imports;
        this.globalVariableList = globalVariableList;
        this.globalVariableArrayList = globalVariableArrayList;
        this.globalConstantList = globalConstantList;
        this.procedureList = procedureList;
    }

    public static ApplicationContext initialize(List<Statement> statements) {
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

        Map<String, List<Statement>> procedureList =
            statements.stream()
                .filter(statement -> statement instanceof Statement.Procedure)
                .collect(Collectors.toMap(statement -> ((Statement.Procedure) statement).name.lexeme(),
                                          statement -> ((Statement.Procedure) statement).body));

        return new ApplicationContext(null, null, varList, varArrayList, constList, procedureList);
    }

    public void setVariableValue(String key, BasicExpression value) {
        if (globalVariableList.containsKey(key)) {
            globalVariableList.replace(key, value);
        }
    }

    public BasicExpression getConstant(String key) {
        return globalConstantList.getOrDefault(key, null);
    }

    public BasicExpression getVariable(String key) {
        return globalVariableList.getOrDefault(key, null);
    }

    public List<Statement> getProcedure(String key) {
        return procedureList.getOrDefault(key, Collections.emptyList());
    }

}
