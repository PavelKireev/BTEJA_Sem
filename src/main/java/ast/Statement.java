package ast;

import scanner.Token;

import java.util.List;
import java.util.Set;

public abstract class Statement {

    public static class Module extends Statement {
        final Token name;

        public Module(Token name) {
            this.name = name;
        }

        public Token getName() {
            return name;
        }
    }

    public static class Import extends Statement {
        final Token module;
        final List<String> imports;

        public Import(Token module, List<String> imports) {
            this.module = module;
            this.imports = imports;
        }

        public Token getModule() {
            return module;
        }

        public List<String> getImports() {
            return imports;
        }
    }

    public static class Procedure extends Statement {
        final Token name;
        final Token returnType;
        final List<Var> parameters;
        final List<Statement> body;

        public Procedure(Token name, Token returnType,
                         List<Var> parameters, List<Statement> body) {
            this.name = name;
            this.returnType = returnType;
            this.parameters = parameters;
            this.body = body;
        }

        public Token getName() {
            return name;
        }

        public Token getReturnType() {
            return returnType;
        }

        public List<Var> getParameters() {
            return parameters;
        }

        public List<Statement> getBody() {
            return body;
        }
    }

    public static class Return extends Statement {
        final BasicExpression expression;

        public Return(BasicExpression expression) {
            this.expression = expression;
        }

        public BasicExpression getExpression() {
            return expression;
        }
    }

    public static class If extends Statement {
        final BasicExpression condition;
        final List<Statement> body;
        final List<Elsif> elsifBranches;
        final ElseBranch elseBranch;

        public If(BasicExpression condition, List<Statement> body,
                  List<Elsif> elsifBranches, ElseBranch elseBranch) {
            this.condition = condition;
            this.body = body;
            this.elsifBranches = elsifBranches;
            this.elseBranch = elseBranch;
        }

        public BasicExpression getCondition() {
            return condition;
        }

        public List<Statement> getBody() {
            return body;
        }

        public List<Elsif> getElsifBranches() {
            return elsifBranches;
        }

        public ElseBranch getElseBranch() {
            return elseBranch;
        }
    }

    public static class Elsif extends Statement {
        final BasicExpression condition;
        final List<Statement> statements;

        public Elsif(BasicExpression condition, List<Statement> statements) {
            this.condition = condition;
            this.statements = statements;
        }

        public BasicExpression getCondition() {
            return condition;
        }

        public List<Statement> getStatements() {
            return statements;
        }
    }

    public static class ElseBranch extends Statement {
        final List<Statement> statements;

        public ElseBranch(List<Statement> statements) {
            this.statements = statements;
        }

        public List<Statement> getStatements() {
            return statements;
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

        public Token getName() {
            return name;
        }

        public Token getType() {
            return type;
        }

        public BasicExpression getInitializer() {
            return initializer;
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

        public Token getName() {
            return name;
        }

        public Token getType() {
            return type;
        }

        public int getIndexFrom() {
            return indexFrom;
        }

        public int getIndexTo() {
            return indexTo;
        }

        public BasicExpression getInitializer() {
            return initializer;
        }
    }

    public static class While extends Statement {

        public While(BasicExpression condition, List<Statement> body) {
            this.condition = condition;
            this.body = body;
        }

        final BasicExpression condition;
        final List<Statement> body;

        public BasicExpression getCondition() {
            return condition;
        }

        public List<Statement> getBody() {
            return body;
        }
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

        public Statement getIndex() {
            return index;
        }

        public BasicExpression getTo() {
            return to;
        }

        public BasicExpression getBy() {
            return by;
        }

        public List<Statement> getBody() {
            return body;
        }
    }

    public static class Case extends Statement {

        final Token ident;
        final List<CaseBranch> branches;
        final List<Statement> defaultBranch;

        public Case(Token ident, List<CaseBranch> branches, List<Statement> defaultBranch) {
            this.ident = ident;
            this.branches = branches;
            this.defaultBranch = defaultBranch;
        }

        public Token getIdent() {
            return ident;
        }

        public List<CaseBranch> getBranches() {
            return branches;
        }

        public List<Statement> getDefaultBranch() {
            return defaultBranch;
        }
    }

    public static class CaseBranch extends Statement {
        final Set<Integer> range;
        final List<Statement> body;

        public CaseBranch(Set<Integer> range, List<Statement> body) {
            this.range = range;
            this.body = body;
        }

        public Set<Integer> getRange() {
            return range;
        }

        public List<Statement> getBody() {
            return body;
        }
    }

    public static class Assignment extends Statement {
        final BasicExpression ident;
        final BasicExpression expression;

        public Assignment(BasicExpression ident, BasicExpression expression) {
            this.ident = ident;
            this.expression = expression;
        }

        public BasicExpression getIdent() {
            return ident;
        }

        public BasicExpression getExpression() {
            return expression;
        }
    }

    public static class Call extends Statement {
        final Token procedureName;
        List<BasicExpression> arguments;

        public Call(Token procedureName, List<BasicExpression> arguments) {
            this.procedureName = procedureName;
            this.arguments = arguments;
        }

        public Token getProcedureName() {
            return procedureName;
        }

        public List<BasicExpression> getArguments() {
            return arguments;
        }

        public Call setArguments(List<BasicExpression> arguments) {
            this.arguments = arguments;
            return this;
        }
    }

    public static class Const extends Statement {
        public final Token name;
        public final BasicExpression initializer;

        public Const(Token name, BasicExpression initializer) {
            this.name = name;
            this.initializer = initializer;
        }

        public Token getName() {
            return name;
        }

        public BasicExpression getInitializer() {
            return initializer;
        }
    }

    public static class Read extends Statement {
        final Token ident;
        final BasicExpression arrayIndex;

        public Read(Token ident, BasicExpression arrayIndex) {
            this.ident = ident;
            this.arrayIndex = arrayIndex;
        }

        public Token getIdent() {
            return ident;
        }

        public BasicExpression getArrayIndex() {
            return arrayIndex;
        }
    }

    public static class Write extends Statement {
        final Token ident;
        final BasicExpression arrayIndex;

        public Write(Token ident, BasicExpression arrayIndex) {
            this.ident = ident;
            this.arrayIndex = arrayIndex;
        }

        public Token getIdent() {
            return ident;
        }

        public BasicExpression getArrayIndex() {
            return arrayIndex;
        }
    }

    public static class Main extends Statement {
        final List<Statement> body;

        public Main(List<Statement> body) {
            this.body = body;
        }

        public List<Statement> getBody() {
            return body;
        }
    }

}
