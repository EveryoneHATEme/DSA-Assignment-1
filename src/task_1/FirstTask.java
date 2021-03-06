package task_1;

import java.util.Scanner;


public class FirstTask {
    public static void main(String[] args) {
        run();
    }

    public static void run() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int k = scanner.nextInt();

        DoublyLinkedCircularBoundedQueue<String> queue = new DoublyLinkedCircularBoundedQueue<>(k);
        scanner.nextLine();

        String buffer;
        for (int i = 0; i < n; i++) {
            buffer = scanner.nextLine();
            queue.offer(buffer);
        }

        scanner.close();

        while (!queue.isEmpty())
            System.out.println(queue.poll());
    }
}


interface ICircularBoundedQueue<T> {
    void offer(T value);    // insert an element to the rear of the queue
    // overwrite the oldest elements
    // when the queue is full
    T poll();               // remove an element from the front of the queue
    T peek();               // look at the element at the front of the queue
    // (without removing it)
    void flush();           // remove all elements from the queue
    boolean isEmpty();      // is the queue empty?
    boolean isFull();       // is the queue full?
    int size();             // number of elements
    int capacity();         // maximum capacity
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
        if (size >= capacity)
            poll();

        tail = entry;
        size++;
    }

    @Override
    public T poll() {
        // Time complexity: O(1)
        // the function does not contain loops or parts of code that depend on the number of elements in the queue

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