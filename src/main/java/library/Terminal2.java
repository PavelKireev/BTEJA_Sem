package library;

import java.util.List;
import java.util.Map;

public class Terminal2 {

    public static final List<String> supportedProcedures = List.of("WriteString", "WriteInt", "WriteLn",
                                                                   "WriteChar", "WriteCard", "WriteReal");

    public static final Map<String, List<String>> ARGS_MAP = Map.of(
        "WriteString", List.of("STRING"),
        "WriteInt", List.of("NUMERIC", "NUMERIC"),
        "WriteLn", List.of(),
        "WriteChar", List.of("CHAR"),
        "WriteCard", List.of("NUMERIC", "NUMERIC"),
        "WriteReal", List.of("NUMERIC", "NUMERIC")
    );

    public static void WriteString(String text) {
        System.out.print(text.replace("\"", ""));
    }

    public static void WriteInt(int number, int spaces) {
        System.out.print(number + " ".repeat(spaces));
    }

    public static void WriteLn() {
        System.out.println();
    }

    public static void WriteChar(char character) {
        System.out.print(character);
    }

    public static void WriteCard(int number, int spaces) {
        if (number < 0) {
            number = Integer.MAX_VALUE + number + 1;
        }
        System.out.print(number + " ".repeat(spaces));
    }

    public static void WriteReal(double number, int spaces) {
        System.out.print(number + " ".repeat(spaces));
    }
}
