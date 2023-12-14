package scanner;

public enum TokenType {

    IDENT("ident"),
    NUMBER("number"),
    CONST("CONST"),
    MODULE("MODULE"),
    FROM("FROM"),
    IMPORT("IMPORT"),
    VAR("VAR"),
    OF("OF"),
    PROCEDURE("PROCEDURE"),
    BEGIN("BEGIN"),
    RETURN("RETURN"),
    END("END"),
    CASE("CASE"),
    IF("IF"),
    THEN("THEN"),
    ELSIF("ELSIF"),
    ELSE("ELSE"),
    WHILE("WHILE"),
    FOR("FOR"),
    TO("TO"),
    BY("BY"),
    DO("DO"),
    QUESTION_MARK("?"),
    BANG("!"),
    COMMA(","),
    SEMICOLON(";"),
    AND("AND"),
    OR("OR"),
    NOT("NOT"),
    EQUAL("="),
    ASSIGNMENT(":="),
    NOT_EQUAL("<>"),
    BOOLEAN_EQUAL("#"),
    LESS_EQUAL("<="),
    GREATER_EQUAL(">="),
    LESS("<"),
    GREATER(">"),
    PLUS("+"),
    MINUS("-"),
    STAR("*"),
    SLASH("/"),
    PIPE("|"),
    DIV("DIV"),
    MOD("MOD"),
    OPEN_PARENTHESIS("("),
    CLOSE_PARENTHESIS(")"),
    DOT("."),
    STRING(""),
    CHAR(""),
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
