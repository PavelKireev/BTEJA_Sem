package analyzer;

import ast.Statement;
import interpreter.Interpreter;
import structure.Block;
import util.AnalyzerUtils;

import java.util.Collections;
import java.util.List;

public class ConstDeclarationStatementAnalyzer implements StatementAnalyzer<Statement.Const> {
    @Override
    public void analyze(Statement.Const statement, Block block) {
        List<Statement.Var> varStatements = AnalyzerUtils.extractAllVars(block.getStatementList());
        List<Statement.VarArray> varArrayStatements = AnalyzerUtils.extractAllVarArrays(block.getStatementList());
        List<Statement.Const> constStatements = AnalyzerUtils.extractAllConsts(block.getStatementList());
        List<String> allNames = AnalyzerUtils.mergeStatements(varStatements, varArrayStatements, constStatements);

        if (Collections.frequency(allNames, statement.getName().lexeme()) > 1) {
            Interpreter.error(statement.getName().line(),
                              "Constant declaration",
                              "Constant " + statement.getName().lexeme() + " is already declared");
            System.exit(1);
        }
    }
}
