package scanner;

import java.util.Objects;

public final class Token {
    private final TokenType type;
    private final String lexeme;
    private final Object literal;
    private final int line;

    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString() {
        return "type: " + type + " lexeme: " + lexeme + " literal: " + literal + " line: " + line;
    }

    public TokenType type() {
        return type;
    }

    public String lexeme() {
        return lexeme;
    }

    public Object literal() {
        return literal;
    }

    public int line() {
        return line;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Token) obj;
        return Objects.equals(this.type, that.type) &&
            Objects.equals(this.lexeme, that.lexeme) &&
            Objects.equals(this.literal, that.literal) &&
            this.line == that.line;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, lexeme, literal, line);
    }

}
