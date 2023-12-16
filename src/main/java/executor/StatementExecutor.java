package executor;

import ast.Statement;

import java.util.HashMap;
import java.util.Map;

public class StatementExecutor {

    private static Map<Class, Executor> executorMap;

    static {
        executorMap = new HashMap<>();
        executorMap.put(Statement.Main.class, new MainStatementExecutor());
        executorMap.put(Statement.Var.class, new VarStatementExecutor());
        executorMap.put(Statement.VarArray.class, new VarArrayStatementExecutor());
        executorMap.put(Statement.Const.class, new ConstStatementExecutor());
        executorMap.put(Statement.Assignment.class, new AssignmentStatementExecutor());
        executorMap.put(Statement.If.class, new IfStatementExecutor());
        executorMap.put(Statement.While.class, new WhileStatementExecutor());
        executorMap.put(Statement.For.class, new ForStatementExecutor());
        executorMap.put(Statement.ElseBranch.class, new ElseBranchStatementExecutor());
        executorMap.put(Statement.Elsif.class, new ElsifBranchStatementExecutor());
        executorMap.put(Statement.Case.class, new CaseStatementExecutor());
        executorMap.put(Statement.CaseBranch.class, new CaseBranchStatementExecutor());
        executorMap.put(Statement.Return.class, new ReturnStatementExecutor());
    }

    public static void execute(Statement statement) {
        executorMap.get(statement.getClass()).execute(statement);
    }
}
