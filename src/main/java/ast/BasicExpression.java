package ast;

import scanner.Token;

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
        Numeric(Token value) {
            this.value = value;
        }

        final Token value;
    }

    public static class Empty extends BasicExpression {
        public Empty() { }
    }

}
