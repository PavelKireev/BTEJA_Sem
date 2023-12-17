package executor;

import ast.Statement;

import java.util.HashMap;
import java.util.Map;

public class StatementExecutor {

    private static Map<String, Executor> executorMap;

    static {
        executorMap = new HashMap<>();
        executorMap.put(Statement.Procedure.class.getName(), new ProcedureStatementExecutor());
        executorMap.put(Statement.Call.class.getName(), new CallStatementExecutor());
        executorMap.put(Statement.Main.class.getName(), new MainStatementExecutor());
        executorMap.put(Statement.Var.class.getName(), new VarStatementExecutor());
        executorMap.put(Statement.VarArray.class.getName(), new VarArrayStatementExecutor());
        executorMap.put(Statement.Const.class.getName(), new ConstStatementExecutor());
        executorMap.put(Statement.Assignment.class.getName(), new AssignmentStatementExecutor());
        executorMap.put(Statement.If.class.getName(), new IfStatementExecutor());
        executorMap.put(Statement.While.class.getName(), new WhileStatementExecutor());
        executorMap.put(Statement.For.class.getName(), new ForStatementExecutor());
        executorMap.put(Statement.ElseBranch.class.getName(), new ElseBranchStatementExecutor());
        executorMap.put(Statement.Elsif.class.getName(), new ElsifBranchStatementExecutor());
        executorMap.put(Statement.Case.class.getName(), new CaseStatementExecutor());
        executorMap.put(Statement.CaseBranch.class.getName(), new CaseBranchStatementExecutor());
        executorMap.put(Statement.Return.class.getName(), new ReturnStatementExecutor());
    }

    public static <T extends Statement> void execute(T statement) {
        executorMap.get(statement.getClass().getName()).execute(statement);
    }
}
