public class Main {
    public static void main(String[] args) {
        DoublyLinkedCircularBoundedQueue<Integer> queue = new DoublyLinkedCircularBoundedQueue<>(3);

        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        queue.offer(4);
        queue.offer(5);
        System.out.println(queue.isEmpty());

        queue.flush();

        System.out.println(queue.isEmpty());

        queue.offer(6);
        queue.offer(7);
        queue.offer(8);

        System.out.println(queue.isFull());

        while (!queue.isEmpty())
            System.out.println(queue.poll());
    }
}
