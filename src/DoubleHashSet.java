import java.lang.reflect.Array;

public class DoubleHashSet<T> implements ISet<T> {

    private final int size;
    private final int nearestPrimeSize;
    private int keysPresent;
    private T[] array;

    public DoubleHashSet(Class<T> _class, int size) {
        this.size = size;
        nearestPrimeSize = nearestPrimeNumber(size);
        keysPresent = 0;
        array = (T[]) Array.newInstance(_class, size);
        for (int i = array.length; i < size; i++)
            array[i] = null;
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
            throw new ItemIsNotPresentedException("Item is not in Hash Set");

        int index = hashCode(item);
        int offset = hashCode2(item);

        while (array[index] != null) {
            if (array[index] == item) {
                array[index] = null;
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
            else if (array[index] == item)
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
}
