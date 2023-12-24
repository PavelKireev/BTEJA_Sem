package analyzer;

import ast.BasicExpression;
import ast.Statement;
import context.ProcedureContext;
import structure.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SemanticAnalyzer {

    private static Map<String, StatementAnalyzer> statementAnalyzerMap;
    private static Map<String, ExpressionAnalyzer> expressionAnalyzerMap;

    static {
        statementAnalyzerMap = new HashMap<>();
        statementAnalyzerMap.put(Statement.Import.class.getName(), new ImportStatementAnalyzer());
        statementAnalyzerMap.put(Statement.Procedure.class.getName(), new ProcedureDeclarationStatementAnalyzer());
        statementAnalyzerMap.put(Statement.Call.class.getName(), new ProcedureCallStatementAnalyzer());
        statementAnalyzerMap.put(Statement.Var.class.getName(), new VariableDeclarationStatementAnalyzer());
        statementAnalyzerMap.put(Statement.VarArray.class.getName(), new ArrayDeclarationStatementAnalyzer());
        statementAnalyzerMap.put(Statement.Const.class.getName(), new ConstDeclarationStatementAnalyzer());
        statementAnalyzerMap.put(Statement.Assignment.class.getName(), new AssignmentStatementAnalyzer());
        statementAnalyzerMap.put(Statement.If.class.getName(), new IfStatementAnalyzer());
        statementAnalyzerMap.put(Statement.For.class.getName(), new ForStatementAnalyzer());
        statementAnalyzerMap.put(Statement.Elsif.class.getName(), new ElsifStatementAnalyzer());
        statementAnalyzerMap.put(Statement.Case.class.getName(), new CaseStatementStatementAnalyzer());
        statementAnalyzerMap.put(Statement.Module.class.getName(), new ModuleDeclarationStatementAnalyzer());
        statementAnalyzerMap.put(Statement.Main.class.getName(), new MainDeclarationStatementAnalyzer());

        expressionAnalyzerMap = Map.of(
            BasicExpression.Binary.class.getName(), new BinaryExpressionAnalyzer()
        );
    }

    public static void analyze(Statement statement, Block block) {
        statementAnalyzerMap.get(statement.getClass().getName()).analyze(statement, block);
    }
    public static void analyze(BasicExpression expression, Block block) {
        expressionAnalyzerMap.get(expression.getClass().getName()).analyze(expression, block);
    }
}
