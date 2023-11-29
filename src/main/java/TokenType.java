public enum TokenType {

    IDENT("ident"),
    NUMBER("number"),
    CONST("CONST"),
    VAR("VAR"),
    PROCEDURE("PROC"),
    CALL("call"),
    BEGIN("BEGIN"),
    END("END"),
    IF("IF"),
    THEN("THEN"),
    ELSIF("ELSIF"),
    ELSE("ELSE"),
    WHILE("WHILE"),
    FOR("FOR"),
    DO("DO"),
    QUESTION_MARK("?"),
    BANG("!"),
    COMMA(","),
    SEMICOLON(";"),
    EQUAL("="),
    ASSIGNMENT(":="),
    NOT_EQUAL("<>"),
    LESS_EQUAL("<="),
    GREATER_EQUAL(">="),
    LESS("<"),
    GREATER(">"),
    PLUS("+"),
    MINUS("-"),
    STAR("*"),
    SLASH("/"),
    OPEN_PARENTHESIS("("),
    CLOSE_PARENTHESIS(")"),
    DOT("."),
    STRING(" "),
    COLON(":"),
    OPEN_BRACKET("["),
    CLOSE_BRACKET("]");

    public static TokenType findByKey(String key) {
        for (TokenType token : values()) {
            if (token.getKey().equals(key)) {
                return token;
            }
        }
        return null;
    }

    private final String key;

    TokenType(String token) {
        this.key = token;
    }

    public String getKey() {
        return key;
    }
}
