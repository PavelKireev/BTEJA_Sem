package structure;

import java.util.*;

public class Array {

    private String name;
    private Map<Integer, Set<Integer>> range;
    private String type;
    private Map<CompositeKey, Object> indexValueMap;

    public Array(String name,
                 Map<Integer, Set<Integer>> range,
                 String type,
                 Map<CompositeKey, Object> indexValueMap) {
        this.name = name;
        this.range = range;
        this.type = type;
        this.indexValueMap = indexValueMap;
    }

    public String getName() {
        return name;
    }

    public Array setName(String name) {
        this.name = name;
        return this;
    }

    public Map<Integer, Set<Integer>> getRange() {
        return range;
    }

    public Array setRange(Map<Integer, Set<Integer>> range) {
        this.range = range;
        return this;
    }

    public String getType() {
        return type;
    }

    public Array setType(String type) {
        this.type = type;
        return this;
    }

    public Map<CompositeKey, Object> getIndexValueMap() {
        return indexValueMap;
    }

    public Array setIndexValueMap(Map<CompositeKey, Object> indexValueMap) {
        this.indexValueMap = indexValueMap;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Array array = (Array) o;
        return Objects.equals(getName(), array.getName())
            && Objects.equals(getRange(), array.getRange())
            && Objects.equals(getType(), array.getType())
            && Objects.equals(getIndexValueMap(), array.getIndexValueMap());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getRange(), getType(), getIndexValueMap());
    }

    public static class CompositeKey {
        private final Integer[] index;

        public CompositeKey(Integer... index) {
            this.index = index;
        }

        public Integer[] getIndex() {
            return index;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CompositeKey that = (CompositeKey) o;
            return Arrays.equals(getIndex(), that.getIndex());
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(getIndex());
        }
    }
}
