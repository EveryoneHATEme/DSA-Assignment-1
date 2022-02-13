public class QueuedBoundedStack<T> implements IBoundedStack<T> {

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
        while (!queue.isEmpty() && newQueue.size() < newQueue.capacity())
            newQueue.offer(queue.poll());

        queue = newQueue;
    }

    @Override
    public T pop() {
        // Time complexity: O(1)
        // DoublyLinkedCircularBoundedQueue.poll() time complexity is O(1)

        return queue.poll();
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
