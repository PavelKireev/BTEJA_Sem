package structure;

public class Variable {

    private String name;
    private Object value;
    private Type type;

    public Variable(String name, Object value, Type type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public Variable setName(String name) {
        this.name = name;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public Variable setValue(Object value) {
        this.value = value;
        return this;
    }

    public Type getType() {
        return type;
    }

    public Variable setType(Type type) {
        this.type = type;
        return this;
    }
}
