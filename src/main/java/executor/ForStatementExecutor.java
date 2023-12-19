package executor;

import ast.BasicExpression;
import ast.Statement;
import context.ApplicationContext;
import context.ProcedureContext;
import evaluator.ExpressionEvaluator;
import interpreter.Interpreter;

public class ForStatementExecutor implements Executor<Statement.For> {
    @Override
    public void execute(Statement.For statement, ProcedureContext procedureContext) {
        StatementExecutor.execute(statement.getIndex(), procedureContext);
        Object index = ApplicationContext.getVariable(
            ExpressionEvaluator.evaluate(
                ((Statement.Assignment) statement.getIndex()).getIdent(),
                procedureContext
            ).toString());

        if (index == null && procedureContext != null) {
            index =
                procedureContext.getVariable(ExpressionEvaluator.evaluate(
                    ((Statement.Assignment) statement.getIndex()).getIdent(),
                    procedureContext
                ).toString());
        }

        if (index == null) {
            index = ExpressionEvaluator.evaluate(
                ((Statement.Assignment) statement.getIndex()).getExpression(),
                procedureContext
            );
        }

        if (index == null) {
            Interpreter.error(0, " at 'FOR'", "Variable not found");
            System.exit(1);
        }

        Object to = ExpressionEvaluator.evaluate(statement.getTo(), procedureContext);
        Object by = statement.getBy() != null ? ExpressionEvaluator.evaluate(statement.getBy(), procedureContext)
                                              : null;

        String ident = ((BasicExpression.Literal)((Statement.Assignment) statement.getIndex()).getIdent()).value
                                                                                              .lexeme();

        if (index instanceof Integer) {
            for (int i = (int) index;
                 i != (int) to;
                 i += by != null ? (Integer) by : 1) {
                for (Statement forStatement : statement.getBody()) {
                    ApplicationContext.updateVariable(ident, i);
                    if (procedureContext != null) {
                        procedureContext.updateVariable(ident, i);
                    }
                    StatementExecutor.execute(forStatement, procedureContext);
                }
            }
        } else if (index instanceof Double) {
            for (double i = (double) index;
                 i == (double) to;
                 i += by != null ? (Double) by : 1.0) {
                for (Statement forStatement : statement.getBody()) {
                    ApplicationContext.updateVariable(ident, i);
                    procedureContext.updateVariable(ident, i);
                    StatementExecutor.execute(forStatement, procedureContext);
                }
            }
        } else if (index instanceof Character) {
            for (char i = (char) index;
                 to.equals(i);
                 i += by != null ? (Character) by : 1) {
                for (Statement forStatement : statement.getBody()) {
                    ApplicationContext.updateVariable(ident, i);
                    procedureContext.updateVariable(ident, i);
                    StatementExecutor.execute(forStatement, procedureContext);
                }
            }
        } else {
            Interpreter.error(0, " at 'FOR'", "Incompatible types");
            System.exit(1);
        }
    }
}
