package hr.fer.zemris.java.collections;

public interface List extends Collection {
    Object get(int index);
    void insert(Object value, int position);
    int indexOf(Object value);
    void remove(int index);

}
