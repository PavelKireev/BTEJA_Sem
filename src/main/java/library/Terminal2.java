package library;

import java.util.List;

public class Terminal2 {

    public static final List<String> supportedProcedures = List.of("WriteString", "WriteInt", "WriteLn",
                                                                   "WriteChar", "WriteCard", "WriteReal");

    public static void WriteString(String text) {
        System.out.print(text);
    }

    public static void WriteInt(int number) {
        System.out.print(number);
    }

    public static void WriteLn() {
        System.out.println();
    }

    public static void WriteChar(char character) {
        System.out.print(character);
    }

    public static void WriteCard(int number) {
        if (number < 0) {
            number = Integer.MAX_VALUE + number + 1;
        }
        System.out.print(number);
    }

    public static void WriteReal(double number) {
        System.out.print(number);
    }
}
