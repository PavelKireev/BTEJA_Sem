package ast;

import scanner.Token;

import java.util.List;

public abstract class BasicExpression {

    public static class Binary extends BasicExpression {

        public final BasicExpression left;
        public final Token operator;
        public final BasicExpression right;
        public final int line;

        public Binary(BasicExpression left, Token operator, BasicExpression right, int line) {
            this.left = left;
            this.operator = operator;
            this.right = right;
            this.line = line;
        }

        public BasicExpression getLeft() {
            return left;
        }

        public Token getOperator() {
            return operator;
        }

        public BasicExpression getRight() {
            return right;
        }

        public int getLine() {
            return line;
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

    public static class Negation extends BasicExpression {

        public Negation(BasicExpression negatedValue) {
            this.negatedValue = negatedValue;
        }

        private final BasicExpression negatedValue;

        public BasicExpression getNegatedValue() {
            return negatedValue;
        }
    }

    public static class ArrayVariable extends BasicExpression {
        public final Token name;
        public final Token[] index;

        public ArrayVariable(Token name, Token[] index) {
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
