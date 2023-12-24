package analyzer;

import ast.BasicExpression;
import context.ProcedureContext;
import structure.Block;

public interface ExpressionAnalyzer<T extends BasicExpression> {
    void analyze(T expression, Block block);
}
