package library;

import java.util.List;

public class Native {

    public static final List<String> supportedProcedures = List.of("CHR", "FLOAT", "TRUNC", "ORD",
                                                                   "CAP", "VAL", "INC", "DEC", "MIN", "MAX");

    public static float FLOAT(Integer number) {
        return number.floatValue();
    }

    public static int TRUNC(Float number) {
        return number.intValue();
    }

    public static <C, T extends Number> C VAL(C clazz, T text) {
        clazz.getClass().cast(String.class);
        return (C) text;
    }

    public static Integer INC(Integer number) {
        return ++number;
    }

    public static Integer INC(Integer number, Integer increment) {
        number = number + increment;
        return number;
    }

    public static Integer DEC(Integer number) {
        return --number;
    }

    public static Integer DEC(Integer number, Integer decrement) {
        number = number - decrement;
        return number;
    }

    public static int intMin() {
        return Integer.MIN_VALUE;
    }

    public static int intMax() {
        return Integer.MAX_VALUE;
    }

    public static float realMin() {
        return Float.MIN_VALUE;
    }

    public static float realMax() {
        return Float.MAX_VALUE;
    }

    public static char CHR(Integer ordinal) {
        return (char) ordinal.intValue();
    }
}
