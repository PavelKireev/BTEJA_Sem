package analyzer;

import ast.Statement;
import interpreter.Interpreter;
import structure.Block;
import util.AnalyzerUtils;

import java.util.Collections;
import java.util.List;

public class VariableDeclarationStatementAnalyzer implements StatementAnalyzer<Statement.Var> {
    @Override
    public void analyze(Statement.Var statement, Block block) {
        String variableName = statement.getName().lexeme();

        List<String> occupiedNames =
            AnalyzerUtils.mergeStatements(AnalyzerUtils.extractAllVars(block.getStatementList()),
                                           AnalyzerUtils.extractAllVarArrays(block.getStatementList()),
                                           AnalyzerUtils.extractAllConsts(block.getStatementList()));

        if (Collections.frequency(occupiedNames, variableName) > 1) {
            Interpreter.error(statement.getName().line(),
                              "Variable declaration",
                              "Variable " + variableName + " is already declared");
            System.exit(1);
        }
    }
}
