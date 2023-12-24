package analyzer;

import ast.Statement;
import context.ProcedureContext;
import structure.Block;

public class MainDeclarationStatementAnalyzer implements StatementAnalyzer<Statement.Main> {
    @Override
    public void analyze(Statement.Main statement, Block block) {
        statement.getBody().forEach(bodyStatement -> SemanticAnalyzer.analyze(bodyStatement, block));
    }
}
