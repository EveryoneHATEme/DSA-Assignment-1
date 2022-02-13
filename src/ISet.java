public interface ISet<T> {
    void add(T item) throws HashTableIsFullException; // add item in the set

    void remove(T item) throws ItemIsNotPresentedException; // remove an item from a set

    boolean contains(T item); // check if an item belongs to a set

    int size(); // number of elements in a set

    boolean isEmpty(); // check if the set is empty
}


class HashTableIsFullException extends Exception {

    public HashTableIsFullException(String exception) {
        super(exception);
    }
}


class ItemIsNotPresentedException extends Exception {

    public ItemIsNotPresentedException(String exception) {
        super(exception);
    }
}