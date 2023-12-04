package context;

import ast.BasicExpression;
import ast.Statement;
import scanner.Token;
import scanner.TokenType;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProcedureContext {

    private final Map<String, BasicExpression> variables;
    private final Map<String, BasicExpression> variablesArrayList;
    private final Map<String, ProcedureContext> procedureContexts;


    public ProcedureContext(Map<String, BasicExpression> variables,
                            Map<String, BasicExpression> variablesArrayList,
                            Map<String, ProcedureContext> procedureContexts) {
        this.variables = variables;
        this.variablesArrayList = variablesArrayList;
        this.procedureContexts = procedureContexts;
    }

    public static ProcedureContext initialize(List<Statement> statements) {
        Map<String, BasicExpression> varList =
            statements.stream()
                .filter(statement -> statement instanceof Statement.Var)
                .collect(
                    Collectors.toMap(statement -> ((Statement.Var) statement).name.lexeme(),
                        statement -> Objects.requireNonNullElseGet(((Statement.Var) statement).initializer,
                            () -> new BasicExpression.Literal(new Token(TokenType.NUMBER,
                                                                 "0", 0, -1))))
                );

        return new ProcedureContext(varList, null, null);
    }

    public BasicExpression getVariable(String key) {
        return variables.getOrDefault(key, null);
    }

    public void setVariableValue(String key, BasicExpression value) {
        if (variables.containsKey(key)) {
            variables.replace(key, value);
        }
    }
}
