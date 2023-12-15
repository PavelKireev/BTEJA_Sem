package ast;

import scanner.Token;

import java.util.List;

public abstract class BasicExpression {

    public static class Binary extends BasicExpression {

        public final BasicExpression left;
        public final Token operator;
        public final BasicExpression right;

        public Binary(BasicExpression left, Token operator, BasicExpression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

    }

    public static class Grouping extends BasicExpression {
        public Grouping(BasicExpression expression) {
            this.expression = expression;
        }

        public final BasicExpression expression;
    }

    public static class Literal extends BasicExpression {
        public Literal(Token value) {
            this.value = value;
        }

        public final Token value;
    }

    public static class Numeric extends BasicExpression {
        public Numeric(Token value) {
            this.value = value;
        }

        final Token value;
    }

    public static class Negation extends BasicExpression {

        public Negation(BasicExpression negatedValue) {
            this.negatedValue = negatedValue;
        }

        final BasicExpression negatedValue;
    }

    public static class ArrayVariable extends BasicExpression {
        public final Token name;
        public final Token index;

        public ArrayVariable(Token name, Token index) {
            this.name = name;
            this.index = index;
        }
    }

    public static class ProcedureCall extends BasicExpression {
        public final Token name;
        public final List<BasicExpression> arguments;

        public ProcedureCall(Token name, List<BasicExpression> arguments) {
            this.name = name;
            this.arguments = arguments;
        }
    }

    public static class Empty extends BasicExpression {
        public Empty() { }
    }

}
