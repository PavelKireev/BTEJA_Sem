package executor;

import ast.BasicExpression;
import ast.Statement;
import context.ApplicationContext;
import context.ProcedureContext;
import evaluator.ExpressionEvaluator;
import interpreter.Interpreter;
import library.Native;
import library.Terminal2;

import java.util.List;

public class CallStatementExecutor implements Executor<Statement.Call> {
    @Override
    public void execute(Statement.Call statement, ProcedureContext procedureContext) {
        String procedureName = statement.getProcedureName().lexeme();
        if (ApplicationContext.imports.contains(procedureName)) {
            executeTerminal2Procedure(statement, procedureContext);
        } else if (Native.supportedProcedures.contains(procedureName)) {
            BasicExpression.ProcedureCall procedureCall = new BasicExpression.ProcedureCall(
                statement.getProcedureName(),
                statement.getArguments()
            );
            ApplicationContext.updateVariable(procedureName,
                                              Native.executeNativeProcedure(procedureCall, procedureContext));
            if (procedureContext != null) {
                procedureContext.updateVariable(procedureName,
                                                Native.executeNativeProcedure(procedureCall, procedureContext));
            }
        } else if (ApplicationContext.globalProcedureList
                                     .get(procedureName) != null) {
            ProcedureContext innerProcedureContext =
                ProcedureContext.initialize(ApplicationContext.globalProcedureList
                                                              .get(procedureName));

            for (int i = 0; i < statement.getArguments().size(); i++) {
                innerProcedureContext.updateVariable(
                    innerProcedureContext.getParameters().get(i),
                    ExpressionEvaluator.evaluate(statement.getArguments().get(i), innerProcedureContext)
                );
                ApplicationContext.updateVariable(
                    ApplicationContext.getProcedureArguments(statement.getProcedureName().lexeme()),
                    ExpressionEvaluator.evaluate(statement.getArguments().get(i), innerProcedureContext)
                );
            }
            if (procedureContext == null) {
                procedureContext = innerProcedureContext;
            }

            procedureContext.addProcedureContext(procedureName, innerProcedureContext);
            procedureContext.getProcedureContexts().putAll(innerProcedureContext.getProcedureContexts());
            ProcedureContext finalProcedureContext1 = procedureContext;
            ApplicationContext.globalProcedureList
                              .get(procedureName)
                              .getBody()
                              .forEach(procedureStatement ->
                                  StatementExecutor.execute(procedureStatement, finalProcedureContext1));
        } else if (procedureContext != null && procedureContext.getProcedureContext(procedureName) != null) {
            ProcedureContext innerProcedureContext = procedureContext.getProcedureContext(procedureName);
            procedureContext.addProcedureContext(procedureName, innerProcedureContext);
            procedureContext.getProcedureContexts().putAll(innerProcedureContext.getProcedureContexts());
            ProcedureContext finalProcedureContext1 = procedureContext;
            procedureContext.getProcedureContext(procedureName).getStatements().forEach(procedureStatement ->
                StatementExecutor.execute(procedureStatement, finalProcedureContext1));
        } else {
            Interpreter.error(statement.getProcedureName().line(),
                " at '" + statement.getProcedureName().lexeme() + "'",
                "Procedure not found");
            System.exit(1);
        }
    }

    private void executeTerminal2Procedure(Statement.Call statement, ProcedureContext procedureContext) {
        String procedureName = statement.getProcedureName().lexeme();
        List<Object> arguments = statement.getArguments()
                                          .stream()
                                          .map(expression ->
                                              ExpressionEvaluator.evaluate(expression, procedureContext))
                                          .toList();
        switch (procedureName) {
            case "WriteString":
                Terminal2.WriteString((String) arguments.get(0));
                break;
            case "WriteInt":
                try {
                    Terminal2.WriteInt((Integer) arguments.get(0), arguments.get(1) != null ? (Integer) arguments.get(1)
                                                                                            : 0);
                } catch (Exception e) {
                    Interpreter.error(statement.getProcedureName().line(),
                        " at '" + statement.getProcedureName().lexeme() + "'",
                        "Incompatible types");
                }
                break;
            case "WriteLn":
                Terminal2.WriteLn();
                break;
            case "WriteChar":
                char arg = arguments.get(0) instanceof Integer ? (char) ((Integer) arguments.get(0)).intValue()
                                                               : (char) arguments.get(0);
                Terminal2.WriteChar(arg);
                break;
            case "WriteCard":
                Terminal2.WriteCard((Integer) arguments.get(0), arguments.get(1) != null ? (Integer) arguments.get(1)
                                                                                         : 0);
                break;
            case "WriteReal":
                Terminal2.WriteReal((Double) arguments.get(0), arguments.get(1) != null ? (Integer) arguments.get(1)
                                                                                        : 0);
                break;
            default:
                break;
        }
    }
}
