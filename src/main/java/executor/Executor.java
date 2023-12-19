package executor;

import context.ProcedureContext;

public interface Executor<T> {
    void execute(T statement, ProcedureContext procedureContext);
}
