package structure;

import ast.Statement;
import context.ProcedureContext;

import java.util.List;
import java.util.Map;

public class Procedure {

    private final ProcedureContext procedureContext;
    private final List<Statement> statements;

    public Procedure(ProcedureContext procedureContext, List<Statement> statements) {
        this.procedureContext = procedureContext;
        this.statements = statements;
    }

    public ProcedureContext getProcedureContext() {
        return procedureContext;
    }

    public List<Statement> getStatements() {
        return statements;
    }
}
