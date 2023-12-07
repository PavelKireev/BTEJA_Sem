package ast;

import scanner.Token;

import java.util.List;

public abstract class Statement {

    public static class Block extends Statement {
        final List<Statement> statements;

        Block(List<Statement> statements) {
            this.statements = statements;
        }
    }

    public static class Module extends Statement {
        final Token name;

        public Module(Token name) {
            this.name = name;
        }
    }

    public static class Import extends Statement {
        final Token module;
        final List<String> imports;

        public Import(Token module, List<String> imports) {
            this.module = module;
            this.imports = imports;
        }
    }

    public static class Procedure extends Statement {
        public final Token name;
        public final List<Statement> body;

        public Procedure(Token name, List<Statement> body) {
            this.name = name;
            this.body = body;
        }
    }

    public static class If extends Statement {
        final BasicExpression condition;
        final Statement thenBranch;

        public If(BasicExpression condition, Statement thenBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
        }
    }

    public static class ThenBranch extends Statement {
        List<Statement> statements;

        public ThenBranch(List<Statement> statements) {
            this.statements = statements;
        }

    }

    public static class ElseBranch extends Statement {
        List<Statement> statements;

        public ElseBranch(List<Statement> statements) {
            this.statements = statements;
        }

    }

    public static class Var extends Statement {
        public final Token name;
        final Token type;
        public final BasicExpression initializer;

        public Var(Token name, Token type, BasicExpression initializer) {
            this.name = name;
            this.type = type;
            this.initializer = initializer;
        }
    }

    public static class VarArray extends Statement {
        public final Token name;
        final Token type;
        final int indexFrom;
        final int indexTo;
        public final BasicExpression initializer;

        public VarArray(Token name, Token type, int indexFrom, int indexTo,
                        BasicExpression initializer) {
            this.name = name;
            this.type = type;
            this.indexFrom = indexFrom;
            this.indexTo = indexTo;
            this.initializer = initializer;
        }
    }

    public static class While extends Statement {

        public While(BasicExpression condition, List<Statement> body) {
            this.condition = condition;
            this.body = body;
        }

        final BasicExpression condition;
        final List<Statement> body;

    }

    public static class For extends Statement {
        final Statement index;
        final BasicExpression to;
        final BasicExpression by;
        final List<Statement> body;

        public For(Statement index, BasicExpression to,
                   BasicExpression by, List<Statement> body) {
            this.index = index;
            this.to = to;
            this.by = by;
            this.body = body;
        }
    }

    public static class Switch extends Statement {
        final BasicExpression expression;
        final List<Statement.Case> cases;
        final Statement.Case defaultCase;

        public Switch(BasicExpression expression, List<Case> cases, Case defaultCase) {
            this.expression = expression;
            this.cases = cases;
            this.defaultCase = defaultCase;
        }
    }

    public static class Case extends Statement {
        final BasicExpression expression;
        final List<Statement> body;

        public Case(BasicExpression expression, List<Statement> body) {
            this.expression = expression;
            this.body = body;
        }
    }

    public static class Assignment extends Statement {
        final Token ident;
        final BasicExpression expression;
        final Statement.Call call;

        public Assignment(Token ident, BasicExpression expression) {
            this.ident = ident;
            this.expression = expression;
            this.call = null;
        }

        public Assignment(Token ident, Call call) {
            this.ident = ident;
            this.call = call;
            this.expression = null;
        }
    }

    public static class Call extends Statement {
        final Token procedureName;
        List<BasicExpression> arguments;

        public Call(Token procedureName, List<BasicExpression> arguments) {
            this.procedureName = procedureName;
            this.arguments = arguments;
        }
    }

    public static class Const extends Statement {
        public final Token name;
        public final BasicExpression initializer;

        public Const(Token name, BasicExpression initializer) {
            this.name = name;
            this.initializer = initializer;
        }
    }

    public static class Read extends Statement {
        final Token ident;
        final BasicExpression arrayIndex;

        public Read(Token ident, BasicExpression arrayIndex) {
            this.ident = ident;
            this.arrayIndex = arrayIndex;
        }
    }

    public static class Write extends Statement {
        final Token ident;
        final BasicExpression arrayIndex;

        public Write(Token ident, BasicExpression arrayIndex) {
            this.ident = ident;
            this.arrayIndex = arrayIndex;
        }
    }

    public static class Main extends Statement {
        final List<Statement> body;

        public Main(List<Statement> body) {
            this.body = body;
        }
    }

}
