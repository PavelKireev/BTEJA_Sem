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
        final Token ident;
        final BasicExpression from;
        final BasicExpression to;
        final List<Statement> body;

        public For(Token ident, BasicExpression from,
                   BasicExpression to, List<Statement> body) {
            this.ident = ident;
            this.from = from;
            this.to = to;
            this.body = body;
        }
    }

    public static class Switch extends Statement {
        final BasicExpression expression;
        final List<Statement> cases;
        final List<Statement> defaultCase;

        public Switch(BasicExpression expression, List<Statement> cases, List<Statement> defaultCase) {
            this.expression = expression;
            this.cases = cases;
            this.defaultCase = defaultCase;
        }
    }

    public static class Assignment extends Statement {
        final Token ident;
        final BasicExpression expression;

        public Assignment(Token ident, BasicExpression expression) {
            this.ident = ident;
            this.expression = expression;
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
