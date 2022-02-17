public class DoublyLinkedCircularBoundedQueue<T> implements ICircularBoundedQueue<T> {
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