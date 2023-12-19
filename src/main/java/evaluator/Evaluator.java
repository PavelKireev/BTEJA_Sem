package evaluator;

import ast.BasicExpression;
import context.ProcedureContext;

public interface Evaluator<T extends BasicExpression> {
    Object evaluate(T statement, ProcedureContext procedureContext);

}
