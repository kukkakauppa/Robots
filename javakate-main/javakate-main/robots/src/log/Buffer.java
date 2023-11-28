package log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.lang.Math.min;

// Кольцевой буфер
public class Buffer<T> implements Iterable<T> {
    private final T[] d; // Буфер для данных фиксированного размера
    private int count = 0; // Индекс для записи в буфер (количество записей за всё время)

    public Buffer(int capacity) {
        d = (T[]) new Object[capacity];
    }

    synchronized public void add(T x) { // Добавляем элемент в список
        d[count % d.length] = x; // Записываем в позицию по модулю размера буфера
        count++; // Увеличиваем количество записей в буфере
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            // Индекс текущего элемента
            // Если буфер ещё неполный (меньше записей было чем размер буфера)
            // То начинаем с 0-ого элемента, если буфер уже полон, то отступаем размер буфера от конца
            private int idx = count < d.length ? 0 : count - d.length;

            // Если индекс меньше общего количества элементов, то итератор может идти вперёд
            @Override
            public boolean hasNext() {
                return idx < count;
            }

            @Override
            public T next() { // Если просят следующий элемент, то
                // Если дошли до конца => исключение
                if (idx == count) throw new NoSuchElementException();
                // Иначе возращаем элемент и увеличиваем индекс
                return d[idx++ % d.length];
            }
        };
    }

    // Требуется подсписок idxFrom..idxTo
    // Получаем элементы через get и добавляем в результирующий список
    public synchronized Iterable<T> subList(int idxFrom, int idxTo) {
        ArrayList<T> r = new ArrayList<>();
        for (int i = idxFrom; i < idxTo; i++) r.add(get(i));
        return r;
    }

    // Получение элемента по индексу
    synchronized public T get(int i) {
        if (i < 0 || i >= size())
            throw new IndexOutOfBoundsException("Index " + i + " is out of range (0.." + (size() - 1) + ")");
        // Если буфер ещё неполный (меньше записей было чем размер буфера)
        return count < d.length ? d[i] : d[(count + i) % d.length];
    }

    public int size() {
        return min(count, d.length);
    }

    public void clear() {
        count = 0;
    }
}
