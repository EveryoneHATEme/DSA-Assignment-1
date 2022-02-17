public class Main {
    public static void main(String[] args) {
        DoubleHashSet<String> hashSet = new DoubleHashSet<String>(String.class, 30);

        try {
            hashSet.add("absfbsb");
            hashSet.add("absfzvzcvbsb");
            hashSet.add("absfgbsb");
            hashSet.add("abfdsfbsb");
        } catch (HashTableIsFullException ignored) {

        }

        System.out.println(hashSet.size());

        System.out.println(hashSet.contains("absfzvzcvbsb"));

        try {
            hashSet.remove("absfzvzcvbsb");
            hashSet.remove("abfdsfbsb");
        } catch (ItemIsNotPresentedException ignored) {

        }

        System.out.println(hashSet.contains("abfdsfbsb"));

    }

    public static void checkQueue() {
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

        try {
            while (!queue.isEmpty())
                System.out.println(queue.poll());
        } catch (QueueIsEmptyException ignored) {

        }
    }

    public static void checkStack() {
        QueuedBoundedStack<Integer> stack = new QueuedBoundedStack<>(3);

        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);

        try {
            while (!stack.isEmpty())
                System.out.println(stack.pop());
        } catch (StackIsEmptyException ignored) {

        }
    }
}
