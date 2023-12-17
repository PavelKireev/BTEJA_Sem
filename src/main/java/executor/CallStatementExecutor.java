package executor;

import ast.BasicExpression;
import ast.Statement;
import context.ApplicationContext;
import evaluator.ExpressionEvaluator;
import interpreter.Interpreter;
import library.Native;
import library.Terminal2;

import java.util.List;
import java.util.Objects;

public class CallStatementExecutor implements Executor<Statement.Call> {
    @Override
    public void execute(Statement.Call statement) {
        String procedureName = statement.getProcedureName().lexeme();
        if (Native.supportedProcedures.contains(procedureName)) {

        } else if (ApplicationContext.imports.contains(procedureName)) {
            executeTerminal2Procedure(statement);
        } else if (ApplicationContext.globalProcedureList.get(procedureName) != null) {
            ApplicationContext.globalProcedureList.get(procedureName).forEach(StatementExecutor::execute);
        } else {
            Interpreter.error(statement.getProcedureName().line(),
                " at '" + statement.getProcedureName().lexeme() + "'",
                "Procedure not found");
            System.exit(1);
        }
    }

    private void executeNativeProcedure(Statement.Call statement) {
        String procedureName = statement.getProcedureName().lexeme();
        List<String> arguments = statement.getArguments().stream().map(Objects::toString).toList();
        switch (procedureName) {
            case "CHR":
                char character = Native.CHR(Integer.parseInt(arguments.get(0)));
                break;
            case "FLOAT":
                break;
            case "TRUNC":
                break;
            case "ORD":
                break;
            case "CAP":
                break;
            case "VAL":
                break;
            case "INC":
                break;
            case "DEC":
                break;
            case "MIN":
                break;
            case "MAX":
                break;
            default:
                break;

        }
    }

    private void executeTerminal2Procedure(Statement.Call statement) {
        String procedureName = statement.getProcedureName().lexeme();
        List<String> arguments = statement.getArguments().stream().map(expression -> ((BasicExpression.Literal) expression).value.lexeme()).toList();
        switch (procedureName) {
            case "WriteString":
                Terminal2.WriteString(arguments.get(0));
                break;
            case "WriteInt":
                try {
                    Terminal2.WriteInt(Integer.parseInt(arguments.get(0)));
                } catch (Exception e) {
                    Terminal2.WriteInt(0);
                }
                break;
            case "WriteLn":
                Terminal2.WriteLn();
                break;
            case "WriteChar":
                Terminal2.WriteChar(arguments.get(0).charAt(0));
                break;
            case "WriteCard":
                Terminal2.WriteCard(Integer.parseInt(arguments.get(0)));
                break;
            case "WriteReal":
                Terminal2.WriteReal(Double.parseDouble(arguments.get(0)));
                break;
            default:
                break;
        }
    }
}
