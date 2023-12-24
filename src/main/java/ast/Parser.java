package ast;

import context.ApplicationContext;
import exception.IllegalTokenException;
import interpreter.Interpreter;
import scanner.Token;
import scanner.TokenType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Parser {

    private static final String CONST = "CONST";
    private static final String VAR = "VAR";
    private static final String ILLEGAL_TOKEN_ERROR_MESSAGE = "Illegal token (%s) during %s reading, line: %d";
    private static final List<TokenType> BOOLEAN_OPERATION_TYPE_LIST = Arrays.asList(TokenType.GREATER,
                                                                                     TokenType.LESS,
                                                                                     TokenType.GREATER_EQUAL,
                                                                                     TokenType.LESS_EQUAL,
                                                                                     TokenType.EQUAL,
                                                                                     TokenType.NOT_EQUAL,
                                                                                     TokenType.NOT,
                                                                                     TokenType.AND,
                                                                                     TokenType.OR);

    private static final List<TokenType> MATH_OPERATION_TYPE_LIST = Arrays.asList(TokenType.PLUS, TokenType.MINUS,
                                                                                  TokenType.STAR, TokenType.SLASH,
                                                                                  TokenType.DIV, TokenType.MOD);

    private final List<Token> tokenList;
    private int tokenIndex;

    public Parser(List<Token> tokenList) {
        this.tokenList = tokenList;
        this.tokenIndex = 0;
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
        if (!TokenType.FROM.equals(tokenList.get(tokenIndex).type())) {
            return null;
        }

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

        if (TokenType.SEMICOLON.equals(tokenList.get(tokenIndex).type())) {
            peekToken();
        }

        return constList;
    }

    public void readVar(List<Statement.Var> vars, List<Statement.VarArray> varArrays) {

        if (!TokenType.VAR.equals(tokenList.get(tokenIndex).type())) {
            return;
        }

        peekToken();
        Token currentToken = peekToken();

        while (!TokenType.BEGIN.equals(currentToken.type()) &&
               !TokenType.PROCEDURE.equals(tokenList.get(tokenIndex).type())) {
            if (!TokenType.IDENT.equals(currentToken.type())) {
                throw new IllegalTokenException(String.format(ILLEGAL_TOKEN_ERROR_MESSAGE,
                    currentToken.type(), VAR, currentToken.line()));
            }

            List<Token> names = new ArrayList<>();

            names.add(currentToken);
            currentToken = peekToken();

            while (TokenType.COMMA.equals(currentToken.type())) {
                currentToken = peekToken();
                if (!TokenType.IDENT.equals(currentToken.type())) {
                    throw new IllegalTokenException(String.format(ILLEGAL_TOKEN_ERROR_MESSAGE,
                        currentToken.type(), VAR, currentToken.line()));
                }
                names.add(currentToken);
                currentToken = peekToken();
            }

            if (!TokenType.COLON.equals(currentToken.type())) {
                throw new IllegalTokenException(String.format(ILLEGAL_TOKEN_ERROR_MESSAGE,
                    currentToken.type(), VAR, currentToken.line()));
            }

            currentToken = peekToken();

            if (!TokenType.IDENT.equals(currentToken.type())) {
                throw new IllegalTokenException(String.format(ILLEGAL_TOKEN_ERROR_MESSAGE,
                    currentToken.type(), VAR, currentToken.line()));
            }

            if (TokenType.OPEN_BRACKET.equals(tokenList.get(tokenIndex).type())) {
                peekToken();
                try {
                    Map<Integer, Set<Integer>> dimensionRange = new HashMap<>();
                    Integer dimension = 0;
                    int indexFrom = Integer.parseInt(peekToken().lexeme());
                    peekToken();
                    peekToken();
                    int indexTo = Integer.parseInt(peekToken().lexeme());
                    dimensionRange.put(dimension, IntStream.range(indexFrom, indexTo + 1)
                                  .boxed()
                                  .collect(Collectors.toSet()));
                    peekToken();
                    currentToken = peekToken();
                    while (TokenType.OPEN_BRACKET.equals(currentToken.type())) {
                        dimension++;
                        int rangeIndexFrom = Integer.parseInt(peekToken().lexeme());
                        peekToken();
                        peekToken();
                        int rangeIndexTo = Integer.parseInt(peekToken().lexeme());
                        dimensionRange.put(dimension, IntStream.range(rangeIndexFrom, rangeIndexTo + 1)
                                      .boxed()
                                      .collect(Collectors.toSet()));
                        peekToken();
                        currentToken = peekToken();
                    }
                    if (!TokenType.OF.equals(currentToken.type())) {
                        throw new IllegalTokenException(String.format(ILLEGAL_TOKEN_ERROR_MESSAGE,
                            currentToken.type(), "VarArray declaration", currentToken.line()));
                    }
                    Token type = peekToken();
                    for (Token name : names) {
                        varArrays.add(new Statement.VarArray(name, type, dimensionRange, null));
                    }
                    peekToken();
                    currentToken = peekToken();
                } catch (NumberFormatException e) {
                    Interpreter.error(currentToken.line(), "Invalid array size", e.getMessage());
                    System.exit(1);
                }
            } else {
                Token type = currentToken;
                currentToken = peekToken();
                if (TokenType.SEMICOLON.equals(currentToken.type())) {
                    for (Token name : names) {
                        vars.add(new Statement.Var(name, type, null));
                    }
                    if(!TokenType.PROCEDURE.equals(tokenList.get(tokenIndex).type())) {
                        currentToken = peekToken();
                    }
                }
            }
        }
    }

    public void readProcedures(List<Statement.Procedure> procedures) {
        while (TokenType.PROCEDURE.equals(tokenList.get(tokenIndex).type())) {
            Token currentToken = peekToken();
            Token name;
            Token returnType = null;
            List<Statement.Var> parameters = new ArrayList<>();
            List<Statement> body = new ArrayList<>();
            List<Statement.Var> vars = new ArrayList<>();
            List<Statement.VarArray> varArrays = new ArrayList<>();

            if (TokenType.PROCEDURE.equals(currentToken.type())) {
                currentToken = peekToken();
                name = currentToken;
                if (TokenType.OPEN_PARENTHESIS.equals(tokenList.get(tokenIndex).type())) {
                    currentToken = peekToken();
                }
            } else {
                throw new IllegalTokenException(String.format("Illegal token (%s) during ast.Statement reading, line: %d }",
                    currentToken.type(), currentToken.line()));
            }

            if (TokenType.OPEN_PARENTHESIS.equals(currentToken.type())) {
                while (!TokenType.CLOSE_PARENTHESIS.equals(currentToken.type())) {
                    currentToken = peekToken();
                    if (TokenType.IDENT.equals(currentToken.type())) {
                        Token parameterName = currentToken;
                        currentToken = peekToken();
                        if (TokenType.COLON.equals(currentToken.type())) {
                            currentToken = peekToken();
                            if (TokenType.IDENT.equals(currentToken.type())) {
                                Token parameterType = currentToken;
                                currentToken = peekToken();
                                parameters.add(new Statement.Var(parameterName, parameterType, null));
                            }
                        }
                    }
                }
                if (TokenType.CLOSE_PARENTHESIS.equals(currentToken.type())) {
                    currentToken = peekToken();
                }
            }

            if (TokenType.COLON.equals(currentToken.type())) {
                currentToken = peekToken();
                if (TokenType.IDENT.equals(currentToken.type())) {
                    returnType = currentToken;
                    peekToken();
                }
            }

            if (TokenType.SEMICOLON.equals(currentToken.type())) {
                currentToken = peekToken();
            }

            if (TokenType.VAR.equals(tokenList.get(tokenIndex).type())) {
                readVar(vars, varArrays);
                body.addAll(vars);
                body.addAll(varArrays);
            }

            if (TokenType.SEMICOLON.equals(currentToken.type())) {
                currentToken = tokenList.get(tokenIndex);
            }

            if (!TokenType.IDENT.equals(tokenList.get(tokenIndex).type())) {
                currentToken = peekToken();
            }


            List<Statement.Procedure> subProcedures = new ArrayList<>();
            while (TokenType.PROCEDURE.equals(tokenList.get(tokenIndex).type())) {
                readProcedures(subProcedures);
            }
            body.addAll(subProcedures);

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
            procedures.add(new Statement.Procedure(name, returnType, parameters, body));
        }
    }

    private Statement readReturnStatement() {
        Token currentToken = peekToken();
        if (TokenType.RETURN.equals(currentToken.type())) {
            currentToken = peekToken();
        }

        BasicExpression expression = readExpression(new BasicExpression.Literal(currentToken));

        if (TokenType.SEMICOLON.equals(tokenList.get(tokenIndex).type())) {
            peekToken();
        }

        return new Statement.Return(expression);
    }

    public Statement readMain() {
        List<Statement> body = new ArrayList<>();
        List<Statement.Var> vars = new ArrayList<>();
        List<Statement.VarArray> varArrays = new ArrayList<>();
        Token currentToken = tokenList.get(tokenIndex);
        if (TokenType.VAR.equals(currentToken.type())) {
            readVar(vars, varArrays);
        }

        body.addAll(vars);
        body.addAll(varArrays);

        if (TokenType.BEGIN.equals(tokenList.get(tokenIndex).type())) {
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
            case CASE -> readCaseStatement();
            case FOR -> readForStatement();
            case IDENT -> readAssignmentOrCallStatement();
            case QUESTION_MARK -> readReadStatement();
            case BANG -> readWriteStatement();
            case RETURN -> readReturnStatement();
            case BEGIN -> readMain();
            default -> throw new IllegalTokenException(
                String.format("Illegal token (%s) during Statement reading, line: %d ",
                    token.type(), token.line()));
        };
    }

    private Statement.If readIfStatement() {
        peekToken();
        List<Statement> body = new ArrayList<>();
        List<Statement.Elsif> elsifBranches = new ArrayList<>();
        Statement.ElseBranch elseBranch = null;
        Token currentToken = peekToken();
        BasicExpression condition = readExpression(new BasicExpression.Literal(currentToken));

        while (!TokenType.ELSIF.equals(tokenList.get(tokenIndex).type()) &&
            !TokenType.ELSE.equals(tokenList.get(tokenIndex).type()) &&
            !TokenType.END.equals(tokenList.get(tokenIndex).type())) {
            body.add(readStatement());
        }

        if (TokenType.ELSIF.equals(tokenList.get(tokenIndex).type())) {
            currentToken = peekToken();
            int line = currentToken.line();
            currentToken = peekToken();
            BasicExpression elsifCondition = readExpression(new BasicExpression.Literal(currentToken));
            List<Statement> elsifBody = new ArrayList<>();

            while (!TokenType.ELSIF.equals(tokenList.get(tokenIndex).type()) &&
                !TokenType.ELSE.equals(tokenList.get(tokenIndex).type()) &&
                !TokenType.END.equals(tokenList.get(tokenIndex).type())) {
                elsifBody.add(readStatement());
            }

            elsifBranches.add(new Statement.Elsif(elsifCondition, elsifBody, line));
        }

        if (TokenType.ELSE.equals(tokenList.get(tokenIndex).type())) {
            peekToken();
            List<Statement> elseBody = new ArrayList<>();
            while (!TokenType.END.equals(tokenList.get(tokenIndex).type())) {
                elseBody.add(readStatement());
            }
            elseBranch = new Statement.ElseBranch(elseBody);
        }

        peekToken();
        if (TokenType.SEMICOLON.equals(tokenList.get(tokenIndex).type())) {
            peekToken();
        }
        return new Statement.If(condition, body, elsifBranches, elseBranch, currentToken.line());
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

    public Statement.Case readCaseStatement() {
        List<Statement.CaseBranch> body = new ArrayList<>();
        List<Statement> defaultCase = new ArrayList<>();
        peekToken();
        Token currentToken = peekToken();
        Token ident = currentToken;

        currentToken = peekToken();

        if (!TokenType.OF.equals(currentToken.type())) {
            throw new IllegalTokenException("Invalid CASE condition");
        }

        while (!TokenType.ELSE.equals(tokenList.get(tokenIndex).type()) &&
            !TokenType.END.equals(tokenList.get(tokenIndex).type())) {
            body.add(readCaseBranch());
            if (TokenType.PIPE.equals(tokenList.get(tokenIndex).type())) {
                peekToken();
            }
        }

        if (TokenType.ELSE.equals(tokenList.get(tokenIndex).type())) {
            peekToken();
            while (!TokenType.END.equals(tokenList.get(tokenIndex).type())) {
                defaultCase.add(readStatement());
            }
        }

        if (TokenType.END.equals(tokenList.get(tokenIndex).type())) {
            peekToken();
        }

        if (TokenType.SEMICOLON.equals(tokenList.get(tokenIndex).type())) {
            peekToken();
        }

        return new Statement.Case(ident, body, defaultCase);
    }

    private Statement.CaseBranch readCaseBranch() {
        List<Statement> body = new ArrayList<>();
        Set<Integer> range = readCaseBranchRange();

        while (!TokenType.PIPE.equals(tokenList.get(tokenIndex).type()) &&
            !TokenType.ELSE.equals(tokenList.get(tokenIndex).type())) {
            body.add(readStatement());
        }

        if (TokenType.PIPE.equals(tokenList.get(tokenIndex).type())) {
            peekToken();
        }

        return new Statement.CaseBranch(range, body);
    }

    public Set<Integer> readCaseBranchRange() {
        Set<Integer> range = new HashSet<>();
        Token currentToken = peekToken();
        while (!TokenType.COLON.equals(currentToken.type())) {
            int rangeStart = ((Double) currentToken.literal()).intValue();
            if (TokenType.DOT.equals(tokenList.get(tokenIndex).type())) {
                peekToken();
                currentToken = peekToken();
                if (!TokenType.DOT.equals(currentToken.type())) {
                    Interpreter.error(currentToken.line(), "Invalid CASE condition", "Expected '..' token");
                    System.exit(1);
                }
                int rangeEnd = ((Double) peekToken().literal()).intValue();

                range.addAll(IntStream.range(rangeStart, rangeEnd + 1).boxed().collect(Collectors.toSet()));
            } else {
                range.add(rangeStart);
            }
            currentToken = peekToken();
            if (TokenType.COMMA.equals(currentToken.type())) {
                currentToken = peekToken();
            }
        }
        return range;
    }

    public Statement.For readForStatement() {
        List<Statement> body = new ArrayList<>();
        peekToken();

        Statement index = readAssignmentOrCallStatement();

        Token currentToken = peekToken();

        if (TokenType.TO.equals(currentToken.type())) {
            currentToken = peekToken();
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
        return new Statement.For(index, toExpression, byExpression, body, currentToken.line());
    }

    public Statement readAssignmentOrCallStatement() {
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
            expression = readExpression(new BasicExpression.Literal(currentToken));
            if (TokenType.SEMICOLON.equals(tokenList.get(tokenIndex).type())) {
                peekToken();
            }
            return new Statement.Assignment(new BasicExpression.Literal(ident), expression);
        } else if (TokenType.SEMICOLON.equals(currentToken.type())) {
            return new Statement.Call(ident, Collections.emptyList());
        } else if (TokenType.OPEN_BRACKET.equals(currentToken.type())) {
            List<Token> indices = new ArrayList<>();
            currentToken = peekToken();
            indices.add(currentToken);
            peekToken();
            currentToken = peekToken();
            while (!TokenType.ASSIGNMENT.equals(currentToken.type())) {
                indices.add(peekToken());
                peekToken();
                currentToken = peekToken();
            }
            currentToken = peekToken();
            if (TokenType.SEMICOLON.equals(tokenList.get(tokenIndex).type())) {
                peekToken();
            }
            expression = readExpression(new BasicExpression.Literal(currentToken));
            return new Statement.Assignment(new BasicExpression.ArrayVariable(ident,
                                                                              indices.toArray(new Token[0])),
                                                                              expression);
        } else if (TokenType.OPEN_PARENTHESIS.equals(currentToken.type())) {
            currentToken = peekToken();
            List<BasicExpression> argumentExpressions = new ArrayList<>();
            argumentExpressions.add(readExpression(new BasicExpression.Literal(currentToken)));
            while (TokenType.COMMA.equals(tokenList.get(tokenIndex).type())) {
                peekToken();
                currentToken = peekToken();
                argumentExpressions.add(readExpression(new BasicExpression.Literal(currentToken)));
            }
            if (TokenType.CLOSE_PARENTHESIS.equals(tokenList.get(tokenIndex).type())) {
                peekToken();
            }
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

        if (expression instanceof BasicExpression.Literal &&
            TokenType.NOT.equals(((BasicExpression.Literal) expression).value.type())) {
            Token negatedToken = peekToken();
            if (TokenType.OPEN_PARENTHESIS.equals(negatedToken.type())) {
                expression = new BasicExpression.Negation(readExpression(new BasicExpression.Literal(peekToken())));
            } else {
                expression = new BasicExpression.Negation(new BasicExpression.Literal(negatedToken));
            }
        }

        if (expression instanceof BasicExpression.Literal &&
            TokenType.IDENT.equals(((BasicExpression.Literal) expression).value.type()) &&
            TokenType.OPEN_PARENTHESIS.equals(tokenList.get(tokenIndex).type())) {
            Token ident = ((BasicExpression.Literal) expression).value;
            peekToken();
            List<BasicExpression> argumentExpressions = new ArrayList<>();
            Token currentToken = peekToken();
            argumentExpressions.add(readExpression(new BasicExpression.Literal(currentToken)));
            while (TokenType.COMMA.equals(tokenList.get(tokenIndex).type())) {
                peekToken();
                currentToken = peekToken();
                argumentExpressions.add(readExpression(new BasicExpression.Literal(currentToken)));
            }
            peekToken();
            expression = new BasicExpression.ProcedureCall(ident, argumentExpressions);
        }

        if (expression instanceof BasicExpression.Literal &&
            TokenType.IDENT.equals(((BasicExpression.Literal) expression).value.type()) &&
            TokenType.OPEN_BRACKET.equals(tokenList.get(tokenIndex).type())) {
            Token ident = ((BasicExpression.Literal) expression).value;
            peekToken();
            Token index = peekToken();
            List<Token> indices = new ArrayList<>();
            indices.add(index);
            peekToken();
            while (!TokenType.COMMA.equals(tokenList.get(tokenIndex).type())) {
                peekToken();
                indices.add(peekToken());
                peekToken();
            }
            expression = new BasicExpression.ArrayVariable(ident, indices.toArray(new Token[0]));
        }

        if (expression instanceof BasicExpression.Literal &&
            TokenType.OPEN_PARENTHESIS.equals(((BasicExpression.Literal) expression).value.type()) &&
            TokenType.OPEN_PARENTHESIS.equals(tokenList.get(tokenIndex).type())) {
            expression = readExpression(new BasicExpression.Literal(peekToken()));
            expression = new BasicExpression.Grouping(expression);
        }

        if (expression instanceof BasicExpression.Literal &&
            TokenType.OPEN_PARENTHESIS.equals(((BasicExpression.Literal) expression).value.type())) {
            Token currentToken = peekToken();
            BasicExpression groupExpression = null;
            if (TokenType.MINUS.equals(currentToken.type())) {
                currentToken = peekToken();
                groupExpression = new BasicExpression.Negation(new BasicExpression.Literal(currentToken));
            } else {
                groupExpression = new BasicExpression.Literal(currentToken);
            }
            currentToken = peekToken();
            while (!TokenType.CLOSE_PARENTHESIS.equals(currentToken.type()) && (
                MATH_OPERATION_TYPE_LIST.contains(currentToken.type()) ||
                    BOOLEAN_OPERATION_TYPE_LIST.contains(currentToken.type()))) {
                Token groupedOperator = currentToken;
                BasicExpression groupedRight = null;
                currentToken = peekToken();
                if (TokenType.MINUS.equals(currentToken.type())) {
                    currentToken = peekToken();
                    groupedRight = new BasicExpression.Negation(new BasicExpression.Literal(currentToken));
                } else {
                    groupedRight = new BasicExpression.Literal(currentToken);
                }
                groupExpression = new BasicExpression.Binary(groupExpression, groupedOperator,
                                                             groupedRight, currentToken.line());
                if (MATH_OPERATION_TYPE_LIST.contains(tokenList.get(tokenIndex).type()) ||
                    BOOLEAN_OPERATION_TYPE_LIST.contains(tokenList.get(tokenIndex).type())) {
                    currentToken = peekToken();
                }
            }
            peekToken();
            expression = new BasicExpression.Grouping(groupExpression);
        }

        if (!MATH_OPERATION_TYPE_LIST.contains(tokenList.get(tokenIndex).type()) &&
            !BOOLEAN_OPERATION_TYPE_LIST.contains(tokenList.get(tokenIndex).type())) {
            return expression;
        }

        Token currentToken = peekToken();

        while (MATH_OPERATION_TYPE_LIST.contains(currentToken.type()) ||
            BOOLEAN_OPERATION_TYPE_LIST.contains(currentToken.type())) {
            BasicExpression left = expression;

            if (left instanceof BasicExpression.Literal &&
                TokenType.NOT.equals(((BasicExpression.Literal) left).value.type())) {
                left = new BasicExpression.Negation(readExpression(new BasicExpression.Literal(peekToken())));
            }
            Token operator = currentToken;
            currentToken = peekToken();
            Token right = currentToken;

            if (TokenType.OPEN_PARENTHESIS.equals(currentToken.type())) {
                currentToken = peekToken();
                BasicExpression groupExpression = null;
                if (TokenType.MINUS.equals(currentToken.type())) {
                    currentToken = peekToken();
                    groupExpression = new BasicExpression.Negation(new BasicExpression.Literal(currentToken));
                } else {
                    groupExpression = new BasicExpression.Literal(currentToken);
                }
                currentToken = peekToken();
                while (!TokenType.CLOSE_PARENTHESIS.equals(currentToken.type()) &&
                    (MATH_OPERATION_TYPE_LIST.contains(currentToken.type()) ||
                        BOOLEAN_OPERATION_TYPE_LIST.contains(currentToken.type()))) {
                    Token groupedOperator = currentToken;
                    currentToken = peekToken();
                    if (TokenType.MINUS.equals(currentToken.type())) {
                        currentToken = peekToken();
                        BasicExpression groupedRight = new BasicExpression.Negation(new BasicExpression.Literal(currentToken));
                        groupExpression = new BasicExpression.Binary(groupExpression, groupedOperator, groupedRight,
                                                                     currentToken.line());
                    } else {
                        BasicExpression groupedRight = new BasicExpression.Literal(currentToken);
                        groupExpression = new BasicExpression.Binary(groupExpression, groupedOperator,
                                                                     groupedRight, currentToken.line());
                    }
                    if (MATH_OPERATION_TYPE_LIST.contains(tokenList.get(tokenIndex).type())) {
                        currentToken = peekToken();
                    }
                }
                currentToken = peekToken();
                expression = new BasicExpression.Binary(expression, operator,
                    new BasicExpression.Grouping(groupExpression), currentToken.line());
            } else if (TokenType.IDENT.equals(currentToken.type()) &&
                TokenType.OPEN_PARENTHESIS.equals(tokenList.get(tokenIndex).type())) {
                Token ident = currentToken;
                List<BasicExpression> argumentExpressions = new ArrayList<>();
                peekToken();
                currentToken = peekToken();
                argumentExpressions.add(readExpression(new BasicExpression.Literal(currentToken)));
                while (TokenType.COMMA.equals(tokenList.get(tokenIndex).type())) {
                    peekToken();
                    currentToken = peekToken();
                    argumentExpressions.add(readExpression(new BasicExpression.Literal(currentToken)));
                }
                currentToken = peekToken();
                expression = new BasicExpression.Binary(expression, operator,
                                                        new BasicExpression.ProcedureCall(ident, argumentExpressions),
                                                        currentToken.line());
            } else {
                currentToken = peekToken();
                BasicExpression rightExpression;

                if (TokenType.NOT.equals(right.type())) {
                    rightExpression = new BasicExpression.Negation(readExpression(new BasicExpression.Literal(currentToken)));
                    currentToken = peekToken();
                } else {
                    rightExpression = new BasicExpression.Literal(right);
                }
                expression = new BasicExpression.Binary(left, operator, rightExpression, currentToken.line());
            }

            if (TokenType.CLOSE_PARENTHESIS.equals(currentToken.type())) {
                currentToken = peekToken();
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

    public Statement.Write readWriteStatement() {
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

    private List<Token> getTokenList() {
        return tokenList;
    }

    private int getTokenIndex() {
        return tokenIndex;
    }

}
