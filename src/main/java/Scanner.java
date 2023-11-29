import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Scanner {

    private int start = 0;
    private int current = 0;
    private int line = 1;

    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    public Scanner(String source) {
        this.source = source;
        this.start = 0;
        this.current = 0;
        this.line = 0;
    }

    public static List<Token> getTokens(String source) {
        return new Scanner(source).scanTokens();
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            start = current;
            scanToken();
        }

        tokens.add(new Token(
            TokenType.END,
            TokenType.END.getKey(),
            TokenType.END.getKey(),
            line)
        );

        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(':
                if (match('*')) {
                    // A comment goes until the end of the line.
                    while (peek() != '*' && !isAtEnd()) advance();
                    advance();
                    if (advance() != ')') {
                        Interpreter.error(line, "", "Unterminated comment.");
                    }

                } else {
                    addToken(TokenType.OPEN_PARENTHESIS);
                }
                break;
            case ')':
                addToken(TokenType.CLOSE_PARENTHESIS);
                break;
            case '[':
                addToken(TokenType.OPEN_BRACKET);
                break;
            case ']':
                addToken(TokenType.CLOSE_BRACKET);
                break;
            case ',':
                addToken(TokenType.COMMA);
                break;
            case '.':
                addToken(TokenType.DOT);
                break;
            case '-':
                addToken(TokenType.MINUS);
                break;
            case '+':
                addToken(TokenType.PLUS);
                break;
            case ';':
                addToken(TokenType.SEMICOLON);
                break;
            case '*':
                addToken(TokenType.STAR);
                break;
            case '!':
                addToken(TokenType.BANG);
                break;
            case '=':
                addToken(TokenType.EQUAL);
                break;
            case '?':
                addToken(TokenType.QUESTION_MARK);
                break;
            case ':':
                if(match('='))
                    addToken(TokenType.ASSIGNMENT);
                else
                    addToken(TokenType.COLON);
                break;
            case '<':
                if (match('=')) {
                    addToken(TokenType.LESS_EQUAL);
                } else if (match('>')) {
                    addToken(TokenType.NOT_EQUAL);
                }
                break;
            case '>':
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;
            case '/':
                if (match('/')) {
                    // A comment goes until the end of the line.
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(TokenType.SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;
            case '\n':
                line++;
                break;
            case '"', '\'':
                string();
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Interpreter.error(line, "","Unexpected character.");
                }
                break;
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();
        String text = source.substring(start, current);
        TokenType type = TokenType.findByKey(text);
        if (type == null) type = TokenType.IDENT;
        addToken(type);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
            (c >= 'A' && c <= 'Z') ||
            c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private void number() {
        while (isDigit(peek())) advance();

        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance();

            while (isDigit(peek())) advance();
        }

        addToken(TokenType.NUMBER,
            Double.parseDouble(source.substring(start, current)));
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void string() {
        char peeked = peek();
        while (!Objects.equals(peeked, '"') &&
               !Objects.equals(peeked, '\'') && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
            peeked = peek();
        }

        if (isAtEnd()) {
            Interpreter.error(line, "", "Unterminated string.");
            return;
        }

        // The closing ".
        advance();

        // Trim the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        addToken(TokenType.STRING, value);
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, type.getKey());
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    public int getStart() {
        return start;
    }

    public Scanner setStart(int start) {
        this.start = start;
        return this;
    }

    public int getCurrent() {
        return current;
    }

    public Scanner setCurrent(int current) {
        this.current = current;
        return this;
    }

    public int getLine() {
        return line;
    }

    public Scanner setLine(int line) {
        this.line = line;
        return this;
    }
}
