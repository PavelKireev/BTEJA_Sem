package context;

import ast.BasicExpression;
import ast.Statement;
import evaluator.ExpressionEvaluator;
import structure.Array;
import structure.Variable;
import java.util.*;
import java.util.stream.Collectors;

public class ProcedureContext {

    private final Map<String, Variable> variables;
    private final Map<String, Array> varArrayList;
    private final Map<String, ProcedureContext> procedureContexts;
    private final List<Statement> body;

    private final List<String> parameters;


    public ProcedureContext(Map<String, Variable> variables,
                            Map<String, Array> varArrayList,
                            Map<String, ProcedureContext> procedureContexts,
                            List<Statement> body) {
        this.variables = variables;
        this.varArrayList = varArrayList;
        this.procedureContexts = procedureContexts;
        this.body = body;
        this.parameters = new ArrayList<>();
    }

    public ProcedureContext() {
        this.variables = new HashMap<>();
        this.varArrayList = new HashMap<>();
        this.procedureContexts = new HashMap<>();
        this.body = new ArrayList<>();
        this.parameters = new ArrayList<>();
    }

    public ProcedureContext(Map<String, Variable> variables,
                            Map<String, Array> varArrayList,
                            Map<String, ProcedureContext> procedureContexts,
                            List<Statement> body, List<String> parameters) {
        this.variables = variables;
        this.varArrayList = varArrayList;
        this.procedureContexts = procedureContexts;
        this.body = body;
        this.parameters = parameters;
    }

    public static ProcedureContext initialize(Statement.Procedure procedure) {
        if (procedure == null) {
            return null;
        }
        List<String> parameters = new ArrayList<>();
        List<Statement> statements = procedure.getBody();
        Map<String, Variable> varMap =
            statements.stream()
                .filter(statement -> statement instanceof Statement.Var)
                .collect(HashMap::new,
                         (m, v) -> m.put(((Statement.Var) v).getName().lexeme(),
                                         new Variable(((Statement.Var) v).getName().lexeme(), null,
                                         ((Statement.Var) v).getType().lexeme())),
                         HashMap::putAll
                );

        procedure.getParameters()
                 .forEach(parameter -> {
                     varMap.put(parameter.getName().lexeme(),
                                new Variable(parameter.getName().lexeme(),
                                null, parameter.getType().lexeme()));
                     parameters.add(parameter.getName().lexeme());
                 });

        Map<String, Array> varArrayMap =
            statements.stream()
                .filter(statement -> statement instanceof Statement.VarArray)
                .collect(
                    Collectors.toMap(
                        statement -> ((Statement.VarArray) statement).name.lexeme(),
                        statement -> new Array(((Statement.VarArray) statement).name.lexeme(),
                                               ((Statement.VarArray) statement).getDimensionRanges(),
                                               ((Statement.VarArray) statement).getType().lexeme(),
                                               new HashMap<>()))
                );
        Map<String, ProcedureContext> subProcedures =
            statements.stream()
                      .filter(statement -> statement instanceof Statement.Procedure)
                      .collect(
                          Collectors.toMap(statement -> ((Statement.Procedure) statement).getName().lexeme(),
                                           statement -> ProcedureContext.initialize(((Statement.Procedure) statement)))
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

        return new ProcedureContext(varMap, varArrayMap, subProcedures, body, parameters);
    }

    public Variable getVariable(String key) {
        return variables.getOrDefault(key, null);
    }
    public boolean containsVariable(String key) {
        return variables.containsKey(key);
    }

    public ProcedureContext getProcedure(String key) {
        return procedureContexts.getOrDefault(key, null);
    }

    public Object getArrayVariable(String key, Integer... index) {
        return varArrayList.getOrDefault(key, null)
                           .getIndexValueMap()
                           .get(new Array.CompositeKey(index));
    }

    public void setVariable(String key, Object value) {
        variables.get(key).setValue(value);
    }

    public void setArrayVariable(String key, Object value, Integer... index) {
        varArrayList.getOrDefault(key, null)
                    .getIndexValueMap()
                    .put(new Array.CompositeKey(index), value);
    }

    public Map<String, ProcedureContext> getProcedureContexts() {
        return procedureContexts;
    }

    public List<Statement> getBody() {
        return body;
    }

    public boolean arrayExists(String key) {
        return varArrayList.containsKey(key);
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

    public List<String> getParameters() {
        return parameters;
    }

    public void updateVariable(String key, Object value) {
        if (variables.containsKey(key)) {
            variables.getOrDefault(key, null)
                     .setValue(value);
        }
    }

    public void setVariableValue(String key, BasicExpression value) {
        if (variables.containsKey(key)) {
            variables.getOrDefault(key, null)
                     .setValue(ExpressionEvaluator.evaluate(value, this));
        }
    }

    public Array getArray(String key) {
        return varArrayList.getOrDefault(key, null);
    }

    public Map<String, Variable> getVariables() {
        return variables;
    }

    public Map<String, Array> getVarArrayList() {
        return varArrayList;
    }
}
