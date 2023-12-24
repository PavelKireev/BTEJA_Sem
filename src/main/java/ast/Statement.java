package ast;

import scanner.Token;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Module module = (Module) o;
            return Objects.equals(getName(), module.getName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName());
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Import anImport = (Import) o;
            return Objects.equals(getModule(), anImport.getModule())
                && Objects.equals(getImports(), anImport.getImports());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getModule(), getImports());
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Procedure procedure = (Procedure) o;
            return Objects.equals(getName(), procedure.getName())
                && Objects.equals(getReturnType(), procedure.getReturnType())
                && Objects.equals(getParameters(), procedure.getParameters())
                && Objects.equals(getBody(), procedure.getBody());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName(), getReturnType(), getParameters(), getBody());
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Return aReturn = (Return) o;
            return Objects.equals(getExpression(), aReturn.getExpression());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getExpression());
        }
    }

    public static class If extends Statement {
        final BasicExpression condition;
        final List<Statement> body;
        final List<Elsif> elsifBranches;
        final ElseBranch elseBranch;

        final int line;

        public If(BasicExpression condition,
                  List<Statement> body,
                  List<Elsif> elsifBranches,
                  ElseBranch elseBranch,
                  int line
        ) {
            this.condition = condition;
            this.body = body;
            this.elsifBranches = elsifBranches;
            this.elseBranch = elseBranch;
            this.line = line;
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

        public int getLine() {
            return line;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            If anIf = (If) o;
            return Objects.equals(getCondition(), anIf.getCondition()) && Objects.equals(getBody(), anIf.getBody()) && Objects.equals(getElsifBranches(), anIf.getElsifBranches()) && Objects.equals(getElseBranch(), anIf.getElseBranch());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getCondition(), getBody(), getElsifBranches(), getElseBranch());
        }
    }

    public static class Elsif extends Statement {
        final BasicExpression condition;
        final List<Statement> statements;

        final int line;

        public Elsif(BasicExpression condition, List<Statement> statements, int line) {
            this.condition = condition;
            this.statements = statements;
            this.line = line;
        }

        public BasicExpression getCondition() {
            return condition;
        }
        public List<Statement> getStatements() {
            return statements;
        }

        public int getLine() {
            return line;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Elsif elsif = (Elsif) o;
            return getLine() == elsif.getLine()
                && Objects.equals(getCondition(), elsif.getCondition())
                && Objects.equals(getStatements(), elsif.getStatements());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getCondition(), getStatements(), getLine());
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ElseBranch that = (ElseBranch) o;
            return Objects.equals(getStatements(), that.getStatements());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getStatements());
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Var var = (Var) o;
            return Objects.equals(getName(), var.getName())
                && Objects.equals(getType(), var.getType())
                && Objects.equals(getInitializer(), var.getInitializer());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName(), getType(), getInitializer());
        }
    }

    public static class VarArray extends Statement {
        public final Token name;
        final Token type;
        final Map<Integer, Set<Integer>> dimensionRanges;
        public final BasicExpression initializer;


        public VarArray(Token name, Token type, Map<Integer, Set<Integer>> dimensionRanges, BasicExpression initializer) {
            this.name = name;
            this.type = type;
            this.dimensionRanges = dimensionRanges;
            this.initializer = initializer;
        }

        public Token getName() {
            return name;
        }

        public Token getType() {
            return type;
        }

        public Map<Integer, Set<Integer>> getDimensionRanges() {
            return dimensionRanges;
        }

        public BasicExpression getInitializer() {
            return initializer;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            VarArray varArray = (VarArray) o;
            return Objects.equals(getName(), varArray.getName()) &&
                Objects.equals(getType(), varArray.getType()) &&
                Objects.equals(getDimensionRanges(), varArray.getDimensionRanges()) &&
                Objects.equals(getInitializer(), varArray.getInitializer());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName(), getType(), getDimensionRanges(), getInitializer());
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            While aWhile = (While) o;
            return Objects.equals(getCondition(), aWhile.getCondition())
                && Objects.equals(getBody(), aWhile.getBody());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getCondition(), getBody());
        }
    }

    public static class For extends Statement {
        final Statement index;
        final BasicExpression to;
        final BasicExpression by;
        final List<Statement> body;
        final int line;

        public For(Statement index, BasicExpression to, BasicExpression by, List<Statement> body, int line) {
            this.index = index;
            this.to = to;
            this.by = by;
            this.body = body;
            this.line = line;
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

        public int getLine() {
            return line;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            For aFor = (For) o;
            return Objects.equals(getIndex(), aFor.getIndex())
                && Objects.equals(getTo(), aFor.getTo())
                && Objects.equals(getBy(), aFor.getBy())
                && Objects.equals(getBody(), aFor.getBody());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getIndex(), getTo(), getBy(), getBody());
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Case aCase = (Case) o;
            return Objects.equals(getIdent(), aCase.getIdent())
                && Objects.equals(getBranches(), aCase.getBranches())
                && Objects.equals(getDefaultBranch(), aCase.getDefaultBranch());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getIdent(), getBranches(), getDefaultBranch());
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CaseBranch that = (CaseBranch) o;
            return Objects.equals(getRange(), that.getRange())
                && Objects.equals(getBody(), that.getBody());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getRange(), getBody());
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Assignment that = (Assignment) o;
            return Objects.equals(getIdent(), that.getIdent())
                && Objects.equals(getExpression(), that.getExpression());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getIdent(), getExpression());
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Call call = (Call) o;
            return Objects.equals(getProcedureName(), call.getProcedureName())
                && Objects.equals(getArguments(), call.getArguments());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getProcedureName(), getArguments());
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Const aConst = (Const) o;
            return Objects.equals(getName(), aConst.getName())
                && Objects.equals(getInitializer(), aConst.getInitializer());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName(), getInitializer());
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Read read = (Read) o;
            return Objects.equals(getIdent(), read.getIdent())
                && Objects.equals(getArrayIndex(), read.getArrayIndex());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getIdent(), getArrayIndex());
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Write write = (Write) o;
            return Objects.equals(getIdent(), write.getIdent())
                && Objects.equals(getArrayIndex(), write.getArrayIndex());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getIdent(), getArrayIndex());
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Main main = (Main) o;
            return Objects.equals(getBody(), main.getBody());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getBody());
        }
    }

}
