package task_2;

import java.util.Scanner;


public class SecondTask {

    public static void main(String[] args) {
        new Run();
    }
}


class Run {

    ISet<String> fileSet;

    public Run() {
        fileSet = new DoubleHashSet<>(1000000);

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < n; i++)
            processLine(scanner.nextLine());

    }

    private void processLine(String line) {
        String[] splitted = line.split(" ");

        try {
            switch (splitted[0]) {
                case "NEW":
                    createNew(splitted[1]);
                    break;
                case "REMOVE":
                    remove(splitted[1]);
                    break;
                case "LIST":
                    list();
                    break;
                default:
                    System.out.println("ERROR: cannot execute " + line);
                    break;
            }
        } catch (CannotExcecuteException | HashTableIsFullException | ItemIsNotPresentedException exception) {
            System.out.println("ERROR: cannot execute " + line);
        }
    }

    private void createNew(String name) throws CannotExcecuteException, HashTableIsFullException {
        if (isDir(name) && (fileSet.contains(name) || fileSet.contains(name.substring(0, name.length() - 1))))
            throw new CannotExcecuteException("");
        else if (fileSet.contains(name) || fileSet.contains(name + '/'))
            throw new CannotExcecuteException("");
        fileSet.add(name);
    }

    private void remove(String name) throws ItemIsNotPresentedException {
        fileSet.remove(name);
    }

    private void list() {
        Object[] array = fileSet.getArray();

        StringBuilder res = new StringBuilder();

        for (int i = 0; i < array.length; i++) {
            if (array[i] == null || array[i] == fileSet.getRemoved())
                continue;
            if (i != array.length - 1)
                res.append(array[i]).append(' ');
            else
                res.append(array[i]);
        }

        System.out.println(res);
    }

    private boolean isDir(String name) {
        return name.charAt(name.length() - 1) == '/';
    }
}


interface ISet<T> {

    void add(T item) throws HashTableIsFullException; // add item in the set

    void remove(T item) throws ItemIsNotPresentedException; // remove an item from a set

    boolean contains(T item); // check if an item belongs to a set

    int size(); // number of elements in a set

    boolean isEmpty(); // check if the set is empty

    T[] getArray(); // method for accessing the data

    EmptyObject getRemoved();
}


class DoubleHashSet<T> implements ISet<T> {

    private final int size;
    //private final int nearestPrimeSize;
    private int keysPresent;
    private Object[] array;
    private final EmptyObject removed;

    public DoubleHashSet(int size) {
        this.size = size;
        //nearestPrimeSize = nearestPrimeNumber(size);
        keysPresent = 0;
        array = new Object[size];
        for (int i = 0; i < size; i++)
            array[i] = null;
        removed = new EmptyObject();
    }

    private int hashCode(T x) {
        int result = x.hashCode() % size;
        if (result < 0)
            result += size;
        return result;
    }

    private int hashCode2(T x) {
        return 7919 - (x.hashCode() % 7919);
    }

    @Override
    public void add(T item) throws HashTableIsFullException {

        int index = hashCode(item);
        int offset = hashCode2(item);

        if (keysPresent == size)
            throw new HashTableIsFullException("Hash table is full");

        while (array[index] != null)
            index = (index + offset) % size;

        array[index] = item;
        keysPresent++;
    }

    @Override
    public void remove(T item) throws ItemIsNotPresentedException {

        if (!contains(item))
            throw new ItemIsNotPresentedException("Item" + item.toString() + "is not in Hash Set");

        int index = hashCode(item);
        int offset = hashCode2(item);

        while (array[index] != null) {
            if (array[index].equals(item)) {
                array[index] = removed;
                keysPresent--;
                return;
            }
            index = (index + offset) % size;
        }
    }

    @Override
    public boolean contains(T item) {
        int index = hashCode(item);
        int offset = hashCode2(item);
        int initialPos = index;
        boolean firstTime = true;

        while (true) {

            if (array[index] == null)
                return false;
            else if (array[index].equals(item))
                return true;
            else if (index == initialPos && !firstTime)
                return false;
            else
                index = (index + offset) % size;

            firstTime = false;
        }
    }

    @Override
    public int size() {
        return keysPresent;
    }

    @Override
    public boolean isEmpty() {
        return keysPresent == 0;
    }

    public T[] getArray() {
        return (T[]) array;
    }

    public EmptyObject getRemoved() {
        return removed;
    }
}


class EmptyObject {

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


class CannotExcecuteException extends Exception {

    public CannotExcecuteException(String exception) {
        super(exception);
    }
}