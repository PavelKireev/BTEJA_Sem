package analyzer;

import ast.Statement;
import context.ProcedureContext;
import interpreter.Interpreter;
import library.Terminal2;
import structure.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportStatementAnalyzer implements StatementAnalyzer<Statement.Import> {

    private static Map<String, List<String>> libraryMap = new HashMap<>();

    static {
        libraryMap.put("Terminal2", Terminal2.supportedProcedures);
    }

    @Override
    public void analyze(Statement.Import statement, Block block) {
        int line = statement.getModule().line();
        String libraryName = statement.getModule().lexeme();
        List<String> procedureName = statement.getImports();
        List<String> supportedProcedures = libraryMap.get(libraryName);
        procedureName.forEach(name -> {
            if (!supportedProcedures.contains(name)) {
                Interpreter.error(line,
                                  "Import declaration",
                                  "Procedure " + name + " is not supported in library " + libraryName);
                System.exit(1);
            }
        });
    }
}
