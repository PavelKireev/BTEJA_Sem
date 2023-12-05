package structure;

import ast.BasicExpression;
import ast.Statement;
import exception.IllegalTokenException;
import interpreter.Interpreter;
import scanner.Scanner;
import scanner.Token;
import scanner.TokenType;

import java.util.*;

public class Program {

    private static final String CONST = "CONST";
    private static final String VAR = "VAR";
    private static final String ILLEGAL_TOKEN_ERROR_MESSAGE = "Illegal token (%s) during %s reading, line: %d";
    private static final List<TokenType> BOOLEAN_OPERATION_TYPE_LIST = Arrays.asList(TokenType.GREATER,
                                                                                     TokenType.LESS,
                                                                                     TokenType.GREATER_EQUAL,
                                                                                     TokenType.LESS_EQUAL,
                                                                                     TokenType.EQUAL);

    private static final List<TokenType> MATH_OPERATION_TYPE_LIST = Arrays.asList(TokenType.PLUS, TokenType.MINUS,
                                                                                  TokenType.STAR, TokenType.SLASH);
    private final List<Token> tokenList;

    private final List<Statement> statementList;
    private int tokenIndex;

    private Program(String source) {
        this.statementList = new LinkedList<>();
        this.tokenList = Scanner.getTokens(source);
        this.tokenIndex = 0;
    }

    public static Program readBlock(String source) {
        Program program = new Program(source);
        List<Statement> statements = program.statementList;
        statements.add(program.readModule());
        statements.add(program.readImports());
        statements.addAll(program.readConst());
        List<Statement.Var> vars = new ArrayList<>();
        List<Statement.VarArray> varArrays = new ArrayList<>();
        program.readVar(vars, varArrays);
        statements.addAll(vars);
        statements.addAll(varArrays);
        List<Statement.Procedure> procedures = new ArrayList<>();
        program.readProcedures(procedures);
        statements.addAll(procedures);
        statements.add(program.readMain());
        return program;
    }

    public Statement.Module readModule() {
        Token currentToken = peekToken();

        if (!TokenType.MODULE.equals(currentToken.type())) {
            throw new IllegalTokenException(String.format(ILLEGAL_TOKEN_ERROR_MESSAGE,
                currentToken.type(), "module name", currentToken.line()));
        }

        currentToken = peekToken();
        peekToken();
        return new Statement.Module(currentToken);
    }

    public Statement.Import readImports() {
        Token currentToken = peekToken();
        List<String> imports = new ArrayList<>();

        if (!TokenType.FROM.equals(currentToken.type())) {
            throw new IllegalTokenException(String.format(ILLEGAL_TOKEN_ERROR_MESSAGE,
                currentToken.type(), "import", currentToken.line()));
        }

        currentToken = peekToken();

        if (!TokenType.IDENT.equals(currentToken.type())) {
            throw new IllegalTokenException(String.format(ILLEGAL_TOKEN_ERROR_MESSAGE,
                currentToken.type(), "import", currentToken.line()));
        }

        Token module = currentToken;

        currentToken = peekToken();

        if (!TokenType.IMPORT.equals(currentToken.type())) {
            throw new IllegalTokenException(String.format(ILLEGAL_TOKEN_ERROR_MESSAGE,
                currentToken.type(), "import", currentToken.line()));
        }

        currentToken = peekToken();

        while (!TokenType.SEMICOLON.equals(currentToken.type())) {
            if (!TokenType.IDENT.equals(currentToken.type())) {
                throw new IllegalTokenException(String.format(ILLEGAL_TOKEN_ERROR_MESSAGE,
                    currentToken.type(), "import", currentToken.line()));
            }
            imports.add(currentToken.lexeme());
            currentToken = peekToken();

            if (TokenType.COMMA.equals(currentToken.type())) {
                currentToken = peekToken();
            } else if (TokenType.SEMICOLON.equals(currentToken.type())) {
                break;
            }

        }

        return new Statement.Import(module, imports);
    }

    public List<Statement.Const> readConst() {
        if (!TokenType.CONST.equals(tokenList.get(tokenIndex).type())) {
            return Collections.emptyList();
        }

        peekToken();
        List<Statement.Const> constList = new ArrayList<>();
        Token currentToken = peekToken();

        while (!TokenType.SEMICOLON.equals(currentToken.type())) {
            if (!TokenType.IDENT.equals(currentToken.type())) {
                throw new IllegalTokenException(String.format(ILLEGAL_TOKEN_ERROR_MESSAGE,
                    currentToken.type(), CONST, currentToken.line()));
            }
            Token constName = currentToken;

            currentToken = peekToken();

            if (!TokenType.EQUAL.equals(currentToken.type())) {
                throw new IllegalTokenException(String.format(ILLEGAL_TOKEN_ERROR_MESSAGE,
                    currentToken.type(), CONST, currentToken.line()));
            }

            currentToken = peekToken();
            BasicExpression initializer = readExpression(new BasicExpression.Literal(currentToken));
            constList.add(new Statement.Const(constName, initializer));

            if (TokenType.SEMICOLON.equals(currentToken.type())) {
                break;
            }
            currentToken = peekToken();

            if (TokenType.COMMA.equals(currentToken.type())) {
                currentToken = peekToken();
            } else if (TokenType.SEMICOLON.equals(currentToken.type())) {
                break;
            }

        }

        return constList;
    }

   public void readVar(List<Statement.Var> vars, List<Statement.VarArray> varArrays) {

        if (!TokenType.VAR.equals(tokenList.get(tokenIndex).type())) {
            return;
        }

        peekToken();
        Token currentToken = peekToken();

        while (!TokenType.BEGIN.equals(currentToken.type()) && !TokenType.PROCEDURE.equals(currentToken.type())) {
            if (!TokenType.IDENT.equals(currentToken.type())) {
                throw new IllegalTokenException(String.format(ILLEGAL_TOKEN_ERROR_MESSAGE,
                                                              currentToken.type(), VAR, currentToken.line()));
            }
            Token varName = currentToken;
            currentToken = peekToken();

            if (!TokenType.COLON.equals(currentToken.type())) {
                throw new IllegalTokenException(String.format(ILLEGAL_TOKEN_ERROR_MESSAGE,
                                                              currentToken.type(), VAR, currentToken.line()));
            }

            currentToken = peekToken();

            if (!TokenType.IDENT.equals(currentToken.type())) {
                throw new IllegalTokenException(String.format(ILLEGAL_TOKEN_ERROR_MESSAGE,
                                                              currentToken.type(), VAR, currentToken.line()));
            }

            Token type = currentToken;

            currentToken = peekToken();

            if (TokenType.EQUAL.equals(currentToken.type())) {
                currentToken = peekToken();
                BasicExpression initializer =
                    readExpression(new BasicExpression.Literal(currentToken));

                vars.add(new Statement.Var(varName, type, initializer));
                currentToken = peekToken();
            } else if (TokenType.SEMICOLON.equals(currentToken.type())) {
                vars.add(new Statement.Var(varName, type, null));
                currentToken = peekToken();
            } else if (TokenType.OPEN_BRACKET.equals(currentToken.type())) {
                try {
                    int indexFrom = Integer.parseInt(peekToken().lexeme());
                    peekToken();
                    peekToken();
                    int indexTo = Integer.parseInt(peekToken().lexeme());
                    varArrays.add(new Statement.VarArray(varName, type, indexFrom, indexTo, null));
                } catch (NumberFormatException e) {
                    Interpreter.error(currentToken.line(), "Invalid array size", e.getMessage());
                    throw new IllegalTokenException(String.format(ILLEGAL_TOKEN_ERROR_MESSAGE,
                        currentToken.type(), VAR, currentToken.line()));
                }
                peekToken();
                currentToken = peekToken();
            }

        }
    }

    public void readProcedures(List<Statement.Procedure> procedures) {
        while (TokenType.PROCEDURE.equals(tokenList.get(tokenIndex).type())) {
            Token currentToken = peekToken();
            Token name;
            List<Statement> body = new ArrayList<>();
            List<Statement.Var> vars = new ArrayList<>();
            List<Statement.VarArray> varArrays = new ArrayList<>();

            if (TokenType.PROCEDURE.equals(currentToken.type())) {
                name = peekToken();
            } else {
                throw new IllegalTokenException(String.format("Illegal token (%s) during ast.Statement reading, line: %d }",
                    currentToken.type(), currentToken.line()));
            }
            currentToken = peekToken();

            if (TokenType.VAR.equals(currentToken.type())) {
                readVar(vars, varArrays);
                body.addAll(vars);
                body.addAll(varArrays);
            }

            if (TokenType.SEMICOLON.equals(currentToken.type())) {
                currentToken = tokenList.get(tokenIndex);
            }

            while (TokenType.PROCEDURE.equals(currentToken.type())) {
                List<Statement.Procedure> subProcedures = new ArrayList<>();
                readProcedures(subProcedures);
                body.addAll(subProcedures);
                currentToken = peekToken();
            }

            while (!TokenType.END.equals(currentToken.type())) {
                if (TokenType.SEMICOLON.equals(tokenList.get(tokenIndex).type())) {
                    peekToken();
                }

                if (TokenType.BEGIN.equals(tokenList.get(tokenIndex).type())) {
                    peekToken();
                }

                if (!TokenType.PROCEDURE.equals(tokenList.get(tokenIndex).type())) {
                    body.add(readStatement());
                }
                currentToken = tokenList.get(tokenIndex);

            }
            peekToken();
            peekToken();
            peekToken();
            procedures.add(new Statement.Procedure(name, body));
        }
    }

    private Statement readMain() {
        List<Statement> body = new ArrayList<>();
        List<Statement.Var> vars = new ArrayList<>();
        List<Statement.VarArray> varArrays = new ArrayList<>();
        Token currentToken = tokenList.get(tokenIndex);
        if (TokenType.VAR.equals(currentToken.type())) {
            readVar(vars, varArrays);
        }

        body.addAll(vars);
        body.addAll(varArrays);

        if(TokenType.BEGIN.equals(tokenList.get(tokenIndex).type())) {
            currentToken = peekToken();
        }

        while (!TokenType.END.equals(currentToken.type())) {
            body.add(readStatement());
            currentToken = tokenList.get(tokenIndex);

        }

        return new Statement.Main(body);
    }

    public Statement readStatement() {
        Token token = tokenList.get(tokenIndex);
        return switch (token.type()) {
            case IF -> readIfStatement();
            case WHILE -> readWhileStatement();
            case FOR -> readForStatement();
            case IDENT -> readAssignmentOrCallStatement();
            case QUESTION_MARK -> readReadStatement();
            case BANG -> readWriteStatement();
            case BEGIN -> readMain();
            default -> throw new IllegalTokenException(
                String.format("Illegal token (%s) during Statement reading, line: %d ",
                token.type(), token.line()));
        };
    }

    private Statement.If readIfStatement() {
        peekToken();
        Statement body = null;
        Token currentToken = peekToken();
        BasicExpression condition = readExpression(new BasicExpression.Literal(currentToken));
        Token beginToken = peekToken();

        if (!TokenType.BEGIN.equals(beginToken.type())) {
            throw new IllegalTokenException(
                String.format("Illegal token during if statement read. scanner.Token: %s Index: %d",
                              beginToken, tokenIndex));
        }

        body = readStatement();

        peekToken();
        return new Statement.If(condition, body);
    }

    private Statement.While readWhileStatement() {
        List<Statement> body = new ArrayList<>();
        peekToken();

        BasicExpression condition = readExpression(new BasicExpression.Literal(peekToken()));

        while (!TokenType.END.equals(tokenList.get(tokenIndex).type())) {
            body.add(readStatement());
        }
        peekToken();
        peekToken();
        return new Statement.While(condition, body);
    }

    public Statement.For readForStatement() {
        List<Statement> body = new ArrayList<>();
        peekToken();

        if (TokenType.OPEN_PARENTHESIS.equals(tokenList.get(tokenIndex).type())) {
            peekToken();
        }

        Statement index = readAssignmentOrCallStatement();

        if (!TokenType.TO.equals(peekToken().type())) {
            throw new IllegalTokenException("Invalid for condition");
        }

        Token currentToken = peekToken();
        if (TokenType.OPEN_PARENTHESIS.equals(currentToken.type())) {
            peekToken();
        }
        BasicExpression toExpression = readExpression(new BasicExpression.Literal(currentToken));

        BasicExpression byExpression = null;
        if (TokenType.BY.equals(tokenList.get(tokenIndex).type())) {
            peekToken();
            byExpression = readExpression(new BasicExpression.Literal(peekToken()));
        }

        if (!TokenType.DO.equals(tokenList.get(tokenIndex).type())) {
            peekToken();
        }

        if (!TokenType.DO.equals(tokenList.get(tokenIndex).type())) {
            throw new IllegalTokenException("Invalid FOR condition");
        }

        peekToken();

        while (!TokenType.END.equals(tokenList.get(tokenIndex).type())) {
            body.add(readStatement());
        }
        peekToken();
        peekToken();
        return new Statement.For(index, toExpression, byExpression, body);
    }

    private Statement readAssignmentOrCallStatement() {
        Token currentToken = tokenList.get(tokenIndex);
        Token ident;
        BasicExpression expression = null;

        if (currentToken == null) {
            throw new IllegalTokenException(String.format(
                "Error during assignment statement reading, token with index: %d is null",
                this.tokenIndex
            ));
        }

        if (TokenType.IDENT.equals(currentToken.type())) {
            ident = currentToken;
        } else {
            throw new IllegalTokenException(String.format(
                "Illegal token during assignment statement reading. TokenType: %s, literal: %s, line: %d, index: %d",
                currentToken.type(), currentToken.lexeme(), currentToken.line(), tokenIndex
            ));
        }

        peekToken();
        currentToken = peekToken();

        if (TokenType.ASSIGNMENT.equals(currentToken.type())) {
            currentToken = peekToken();
            if (TokenType.OPEN_PARENTHESIS.equals(currentToken.type())) {
                currentToken = peekToken();
            }
            expression = readExpression(new BasicExpression.Literal(currentToken));
            if (TokenType.SEMICOLON.equals(tokenList.get(tokenIndex).type())) {
                peekToken();
            }
            return new Statement.Assignment(ident, expression);
        } else if (TokenType.SEMICOLON.equals(currentToken.type())) {
            return new Statement.Call(ident, Collections.emptyList());
        } else if (TokenType.OPEN_PARENTHESIS.equals(currentToken.type())) {
            currentToken = peekToken();
            List<BasicExpression> argumentExpressions = new ArrayList<>();
            while (!TokenType.CLOSE_PARENTHESIS.equals(tokenList.get(tokenIndex).type())) {
                argumentExpressions.add(readExpression(new BasicExpression.Literal(currentToken)));
                peekToken();
            }
            peekToken();
            if (TokenType.SEMICOLON.equals(tokenList.get(tokenIndex).type())) {
                peekToken();
            }
            return new Statement.Call(ident, argumentExpressions);
        } else {
            throw new IllegalTokenException(String.format(
                "Illegal token during assignment statement reading. TokenType: %s, literal: %s, line: %d, index: %d",
                currentToken.type(), currentToken.lexeme(), currentToken.line(), tokenIndex
            ));
        }
    }

    private BasicExpression readExpression(BasicExpression expression) {
        if (expression instanceof BasicExpression.Literal &&
            ((BasicExpression.Literal) expression).value.lexeme().equals("-")) {
            Integer number = ((Double) peekToken().literal()).intValue() * -1;

            expression = new BasicExpression.Literal(new Token(TokenType.NUMBER,
                                                               number.toString(),
                                                               number,
                                                               ((BasicExpression.Literal) expression).value.line()));
        }

        if (!MATH_OPERATION_TYPE_LIST.contains(tokenList.get(tokenIndex).type()) &&
            !BOOLEAN_OPERATION_TYPE_LIST.contains(tokenList.get(tokenIndex).type())) {
            return expression;
        }
        Token currentToken = peekToken();

        while (MATH_OPERATION_TYPE_LIST.contains(currentToken.type()) ||
               BOOLEAN_OPERATION_TYPE_LIST.contains(currentToken.type())) {
            BasicExpression left = expression;
            Token operator = currentToken;
            currentToken = peekToken();
            Token right = currentToken;

            if (TokenType.OPEN_PARENTHESIS.equals(currentToken.type())) {
                currentToken = peekToken();
                BasicExpression groupExpression = new BasicExpression.Literal(currentToken);
                currentToken = peekToken();
                while (!TokenType.CLOSE_PARENTHESIS.equals(currentToken.type()) &&
                       MATH_OPERATION_TYPE_LIST.contains(currentToken.type())) {
                    Token groupedOperator = currentToken;
                    currentToken = peekToken();
                    BasicExpression groupedRight = new BasicExpression.Literal(currentToken);
                    groupExpression = new BasicExpression.Binary(groupExpression, groupedOperator, groupedRight);
                    if (MATH_OPERATION_TYPE_LIST.contains(tokenList.get(tokenIndex).type())) {
                        currentToken = peekToken();
                    }
                }
                currentToken = peekToken();
                expression = new BasicExpression.Binary(expression, operator,
                                                        new BasicExpression.Grouping(groupExpression));
            } else {
                currentToken = peekToken();
                expression = new BasicExpression.Binary(left, operator, new BasicExpression.Literal(right));
            }
        }

        return expression;
    }

    private Statement.Read readReadStatement() {
        peekToken();
        Token currentToken = peekToken();
        Token variableName = currentToken;
        Statement.Read statement = new Statement.Read(variableName, null);
        if (TokenType.OPEN_BRACKET.equals(peekToken().type())) {
            currentToken = peekToken();
            BasicExpression expression = readExpression(new BasicExpression.Literal(currentToken));
            statement = new Statement.Read(variableName, expression);
            peekToken();
        }

        return statement;
    }

    private Statement.Write readWriteStatement() {
        peekToken();
        Token currentToken = peekToken();
        Token variableName = currentToken;
        Statement.Write statement = new Statement.Write(variableName, null);
        if (TokenType.OPEN_BRACKET.equals(peekToken().type())) {
            currentToken = peekToken();
            BasicExpression expression = readExpression(new BasicExpression.Literal(currentToken));
            statement = new Statement.Write(variableName, expression);
            peekToken();
        }

        return statement;
    }

    private Token peekToken() {
        Token token = this.tokenList.get(tokenIndex);

        if (token == null) {
            throw new IllegalTokenException(String.format(
                "Error during assignment statement reading, token with index: %d is null",
                this.tokenIndex
            ));
        }

        tokenIndex++;
        return token;
    }

    public List<Statement> getStatementList() {
        return statementList;
    }

    public List<Token> getTokenList() {
        return tokenList;
    }

    public int getTokenIndex() {
        return tokenIndex;
    }
}
