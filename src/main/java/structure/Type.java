package structure;

import java.util.HashMap;
import java.util.Map;

public enum Type {
    STRING(String.class),
    INTEGER(Integer.class),
    CARDINAL(Double.class),
    CHAR(Character.class);

    private final Class typeClass;

    Type(Class typeClass) {
        this.typeClass = typeClass;
    }

    public Class getTypeClass() {
        return typeClass;
    }

    private static final Map<Class, Type> map;
    static {
        map = new HashMap<>();
        for (Type v : Type.values()) {
            map.put(v.typeClass, v);
        }
    }
    public static Type findByKey(Class typeClass) {
        return map.get(typeClass);
    }

}
