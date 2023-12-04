package structure;

import java.util.HashMap;
import java.util.Map;

public class Array<T> {

    private final int size;
    private final Type type;
    private final Map<Integer, T> indexValueMap;

    public Array(int size, Type type) {
        this.size = size;
        this.type = type;
        this.indexValueMap = new HashMap<>();
    }

    public T getItemOnIndex(int index) {
        return indexValueMap.get(index);
    }

    public void setItemOnIndex(int index, T value) {
        indexValueMap.put(index, value);
    }

    public int getSize() {
        return size;
    }

    public Type getType() {
        return type;
    }
}
