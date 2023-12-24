package analyzer;

import ast.Statement;
import context.ProcedureContext;
import structure.Block;

public interface StatementAnalyzer<T extends Statement>{
    void analyze(T statement, Block block);
}
