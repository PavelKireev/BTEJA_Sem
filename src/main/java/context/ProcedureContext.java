package context;

import ast.BasicExpression;
import ast.Statement;
import scanner.Token;
import scanner.TokenType;

import java.util.*;
import java.util.stream.Collectors;

public class ProcedureContext {

    private final Map<String, Object> variables;
    private final Map<String, Map<Integer, Object>> varArrayList;
    private final Map<String, ProcedureContext> procedureContexts;
    private final List<Statement> body;


    public ProcedureContext(Map<String, Object> variables,
                            Map<String, Map<Integer, Object>> varArrayList,
                            Map<String, ProcedureContext> procedureContexts,
                            List<Statement> body) {
        this.variables = variables;
        this.varArrayList = varArrayList;
        this.procedureContexts = procedureContexts;
        this.body = body;
    }

    public ProcedureContext() {
        this.variables = new HashMap<>();
        this.varArrayList = new HashMap<>();
        this.procedureContexts = new HashMap<>();
        this.body = new ArrayList<>();
    }

    public static ProcedureContext initialize(List<Statement> statements) {
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
        Map<String, ProcedureContext> subProcedures =
            statements.stream()
                      .filter(statement -> statement instanceof Statement.Procedure)
                      .collect(
                          Collectors.toMap(statement -> ((Statement.Procedure) statement).getName().lexeme(),
                                           statement -> ProcedureContext.initialize(((Statement.Procedure) statement)
                                                                                                    .getBody()))
                      );

        for (ProcedureContext procedureContext : subProcedures.values()) {
            subProcedures.putAll(procedureContext.getProcedureContexts());
        }

        List<Statement> body =
            statements.stream()
                      .filter(statement -> !(statement instanceof Statement.Var) &&
                                           !(statement instanceof Statement.VarArray) &&
                                           !(statement instanceof Statement.Procedure))
                      .toList();

        return new ProcedureContext(varMap, varArrayMap, subProcedures, body);
    }

    public Object getVariable(String key) {
        return variables.getOrDefault(key, null);
    }

    public ProcedureContext getProcedure(String key) {
        return procedureContexts.getOrDefault(key, null);
    }

    public Object getArrayVariable(String key, int index) {
        return varArrayList.getOrDefault(key, Collections.emptyMap())
                           .getOrDefault(index, null);
    }

    public void setVariable(String key, Object value) {
        variables.put(key, value);
    }

    public void setArrayVariable(String key, int index, Object value) {
        varArrayList.getOrDefault(key, Collections.emptyMap())
                    .put(index, value);
    }

    public Map<String, ProcedureContext> getProcedureContexts() {
        return procedureContexts;
    }

    public List<Statement> getBody() {
        return body;
    }


    public ProcedureContext getProcedureContext(String key) {
        return procedureContexts.getOrDefault(key, null);
    }

    public List<Statement> getStatements() {
        return body;
    }

    public void addProcedureContext(String key, ProcedureContext procedureContext) {
        procedureContexts.put(key, procedureContext);
    }

    public void updateVariable(String key, Object value) {
        if (variables.containsKey(key)) {
            variables.replace(key, value);
        }
    }

    public void setVariableValue(String key, BasicExpression value) {
        if (variables.containsKey(key)) {
            variables.replace(key, value);
        }
    }
}
