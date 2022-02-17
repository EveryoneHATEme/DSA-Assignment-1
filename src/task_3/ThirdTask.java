package task_3;

import java.util.Scanner;


public class ThirdTask {

    public static void main(String[] args) {
        new Run();
    }
}


class Run {
    private final IBoundedStack<DoubleHashSet<String>> historyStack;
    final private int hashSetSize;

    public Run() {
        Scanner scanner = new Scanner(System.in);

        hashSetSize = scanner.nextInt();
        int k = scanner.nextInt();
        scanner.nextLine();

        historyStack = new QueuedBoundedStack<>(k);
        historyStack.push(new DoubleHashSet<>(hashSetSize));

        for (int i = 0; i < hashSetSize; i++)
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
                case "UNDO":
                    undo(splitted);
                    break;
                default:
                    break;
            }
        } catch (CannotExcecuteException exception) {
            System.out.println("ERROR: cannot execute " + line);
        }
    }

    private void createNew(String name) throws CannotExcecuteException {
        DoubleHashSet<String> last = historyStack.top().clone();

        String nameFile = isDir(name) ? name.substring(0, name.length() - 1) : name;
        String nameFolder = isDir(name) ? name : name + '/';

        if (!(last.contains(nameFile) || last.contains(nameFolder))) {
            try {
                last.add(name);
            } catch (HashTableIsFullException ignored) {}
        }
        else
            throw new CannotExcecuteException(name + " is already in set");

        historyStack.push(last);
    }

    private void remove(String name) throws CannotExcecuteException {
        DoubleHashSet<String> last = historyStack.top().clone();

        try {
            last.remove(name);
        } catch (ItemIsNotPresentedException exception) {
            throw new CannotExcecuteException(name + " is not presented in set");
        }

        historyStack.push(last);
    }

    private void list() {
        System.out.println(historyStack.top().toString());
    }

    private void undo(String[] command) throws CannotExcecuteException {
        if (command.length == 1)
            undo(1);
        else
            undo(Integer.parseInt(command[1]));
    }

    private void undo(int count) throws CannotExcecuteException {
        if (historyStack.size() <= count)
            throw new CannotExcecuteException("History stack size: " + historyStack.size() + " undo count: " + count);

        try {
            for (int i = 0; i < count; i++)
                historyStack.pop();
        } catch (StackIsEmptyException ignored) {}
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
    String toString(); // method for accessing the data
}


class DoubleHashSet<T> implements ISet<T>, Cloneable {

    private final int size;
    private int keysPresent;
    private final int nearestPrimeSize;
    private Object[] array;
    private final EmptyObject removed;

    public DoubleHashSet(int size) {
        this.size = size;
        nearestPrimeSize = nearestPrimeNumber(size);
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
        return nearestPrimeSize - (x.hashCode() % nearestPrimeSize);
    }

    private int nearestPrimeNumber(int n) {
        if (n % 2 == 1)
            n -= 2;
        else
            n--;

        int j;
        for (int i = n; i >= 2; i -= 2) {
            if (i % 2 == 0)
                continue;
            for (j = 3; j < Math.sqrt(i); j += 2) {
                if (i % j == 0)
                    break;
            }
            if (j > Math.sqrt(i))
                return i;
        }

        return 2;
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

    public String toString() {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < size; i++) {
            if (array[i] != null && array[i] != removed) {
                result.append(array[i]);
                if (i != size - 1)
                    result.append(" ");
            }
        }

        return result.toString();
    }

    @Override
    public DoubleHashSet<T> clone() {
        try {
            DoubleHashSet<T> clone = (DoubleHashSet<T>) super.clone();
            clone.array = array.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
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


interface ICircularBoundedQueue<T> {
    void offer(T value);                        // insert an element to the rear of the queue
    // overwrite the oldest elements
    // when the queue is full
    T poll() throws QueueIsEmptyException;      // remove an element from the front of the queue
    T peek();                                   // look at the element at the front of the queue
    // (without removing it)
    void flush();                               // remove all elements from the queue
    boolean isEmpty();                          // is the queue empty?
    boolean isFull();                           // is the queue full?
    int size();                                 // number of elements
    int capacity();                             // maximum capacity
}


class QueueIsEmptyException extends Exception {

    public QueueIsEmptyException(String exception) {
        super(exception);
    }
}


class DoublyLinkedCircularBoundedQueue<T> implements ICircularBoundedQueue<T> {
    private Entry<T> head;
    private Entry<T> tail;
    private int size;
    final private int capacity;


    public DoublyLinkedCircularBoundedQueue(int capacity) {
        this.capacity = capacity;
        size = 0;
        head = null;
        tail = null;
    }

    @Override
    public void offer(T value) {
        // Time complexity: O(1)
        // the function does not contain loops or parts of code that depend on the number of elements in the queue

        Entry<T> entry; // Queue entry that will be added in queue

        if (size == 0) {    // If size is zero, then both head and tail are null, so we set them as an entry
            entry = new Entry<T>(value);
            head = entry;
        } else {    // Else we just add it in the end of queue
            entry = new Entry<T>(value, tail);
            tail.setNext(entry);
        }

        // if size exceeds the capacity, then we pop first element from queue
        try {
            if (size >= capacity)
                poll();
        } catch (QueueIsEmptyException ignored) {
        }

        tail = entry;
        size++;
    }

    @Override
    public T poll() throws QueueIsEmptyException {
        // Time complexity: O(1)
        // the function does not contain loops or parts of code that depend on the number of elements in the queue

        if (size <= 0)
            throw new QueueIsEmptyException("Cannot poll element: queue is empty");

        T value = head.getValue();
        head = head.getNext();  // Garbage collector will remove head that we had before
        size--;
        return value;
    }

    @Override
    public T peek() {
        // Time complexity: O(1)
        // the function does not contain loops or parts of code that depend on the number of elements in the queue

        return head.getValue(); // Function should not erase head element
    }

    @Override
    public void flush() {
        // Time complexity: O(n) in any case
        // Despite the absence of obvious loops in function, the java garbage collector will remove each item
        // and spend constant time on each

        head = null;    // Garbage collector will do all work
        tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        // Time complexity: O(1)
        // Function evaluates just one statement

        // We update queue size after every update of queue, so we can check if queue is empty using size
        return size == 0;
    }

    @Override
    public boolean isFull() {
        // Time complexity: O(1)
        // Function evaluates just one statement

        return size == capacity;
    }

    @Override
    public int size() {
        // Time complexity: O(1)
        // We already know the value that we should return

        return size;
    }

    @Override
    public int capacity() {
        // Time complexity: O(1)
        // We already know the value that we should return

        return capacity;
    }
}


class Entry<T> {
    // All functions in this class have time complexity O(1), because they don't use loops, they just return or save values that already known

    final private T value; // value stored in this entry
    private Entry<T> previous;  // reference to the previous entry
    private Entry<T> next;  // reference to the next entry

    public Entry(T value, Entry<T> previous, Entry<T> next) {
        this.value = value;
        this.previous = previous;
        this.next = next;
    }

    public Entry(T value, Entry<T> previous) {
        this(value, previous, null);
    }

    public Entry(T value) {
        this(value, null, null);
    }

    public T getValue() {
        return value;
    }

    public void setPrevious(Entry<T> previous) {
        this.previous = previous;
    }

    public Entry<T> getPrevious() {
        return previous;
    }

    public Entry<T> getNext() {
        return next;
    }

    public void setNext(Entry<T> next) {
        this.next = next;
    }
}


interface IBoundedStack<T> {
    void push(T value);                         // push an element onto the stack
    // remove the oldest element
    // when if stack is full
    T pop() throws StackIsEmptyException;       // remove an element from the top of the stack
    T top();                                    // look at the element at the top of the stack
    // (without removing it)
    void flush();                               // remove all elements from the stack
    boolean isEmpty();                          // is the stack empty?
    boolean isFull();                           // is the stack full?
    int size();                                 // number of elements
    int capacity();                             // maximum capacity
}


class StackIsEmptyException extends Exception {

    public StackIsEmptyException(String exception) {
        super(exception);
    }
}


class QueuedBoundedStack<T> implements IBoundedStack<T> {

    // to implement Queued Bounded Stack I used DoublyLinkedCircularBoundedQueue that I implemented before
    // the queue stores values of stack

    private DoublyLinkedCircularBoundedQueue<T> queue;

    public QueuedBoundedStack(int capacity) {
        queue = new DoublyLinkedCircularBoundedQueue<>(capacity);
    }

    @Override
    public void push(T value) {
        // Time complexity: O(n)
        // This code puts new value as a first element of new queue and then pushes all elements of previous queue in new

        // creating new queue with first element - given value
        DoublyLinkedCircularBoundedQueue<T> newQueue = new DoublyLinkedCircularBoundedQueue<>(queue.capacity());
        newQueue.offer(value);

        // pushing all elements from previous queue
        try {
            while (!queue.isEmpty() && newQueue.size() < newQueue.capacity())
                newQueue.offer(queue.poll());
        } catch (QueueIsEmptyException ignored) {

        }

        queue = newQueue;
    }

    @Override
    public T pop() throws StackIsEmptyException {
        // Time complexity: O(1)
        // DoublyLinkedCircularBoundedQueue.poll() time complexity is O(1)
        try {
            return queue.poll();
        } catch (QueueIsEmptyException exception) {
            throw new StackIsEmptyException(exception.getMessage());
        }
    }

    @Override
    public T top() {
        // Time complexity: O(1)
        // DoublyLinkedCircularBoundedQueue.peek() time complexity is O(1)

        return queue.peek();
    }

    @Override
    public void flush() {
        // Time complexity: O(n)
        // DoublyLinkedCircularBoundedQueue.flush() time complexity is O(n)

        queue.flush();
    }

    @Override
    public boolean isEmpty() {
        // Time complexity: O(1)
        // DoublyLinkedCircularBoundedQueue.isEmpty() time complexity is O(1)

        return queue.isEmpty();
    }

    @Override
    public boolean isFull() {
        // Time complexity: O(1)
        // DoublyLinkedCircularBoundedQueue.isFull() time complexity is O(1)

        return queue.isFull();
    }

    @Override
    public int size() {
        // Time complexity: O(1)
        // DoublyLinkedCircularBoundedQueue.size() time complexity is O(1)

        return queue.size();
    }

    @Override
    public int capacity() {
        // Time complexity: O(1)
        // DoublyLinkedCircularBoundedQueue.capacity() time complexity is O(1)

        return queue.capacity();
    }
}
