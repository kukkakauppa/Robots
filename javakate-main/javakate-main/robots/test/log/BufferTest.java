package log;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

public class BufferTest {
    @DisplayName("Добавление элементов в буфер")
    @Test
    public void testAddAndGet() {
        Buffer<Integer> b = new Buffer<>(2);
        assertEquals(0, b.size());
        b.add(1);
        assertEquals(1, b.get(0));
        assertEquals(1, b.size());
        b.add(2);
        assertEquals(2, b.size());
        assertEquals(1, b.get(0));
        assertEquals(2, b.get(1));
        b.add(3);
        assertEquals(2, b.size());
        assertEquals(2, b.get(0));
        assertEquals(3, b.get(1));
    }

    @DisplayName("Получение элементов через итератор")
    @Test
    public void testIterator() {
        Buffer<Integer> buffer = new Buffer<>(3);
        buffer.add(1);
        buffer.add(2);
        buffer.add(3);

        Iterator<Integer> iterator = buffer.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(3, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testConcurrentAccess() throws InterruptedException {
        final Buffer<Integer> buffer = new Buffer<>(10);
        ExecutorService service = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        // Writer thread
        service.submit(() -> {
            for (int i = 0; i < 5; i++) {
                buffer.add(i);
                try {
                    Thread.sleep(10); // Simulate some delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            latch.countDown();
        });

        // Reader thread
        service.submit(() -> {
            for (int i = 0; i < 5; i++) {
                if (buffer.size() > i) {
                    assertNotNull(buffer.get(i));
                }
                try {
                    Thread.sleep(15); // Simulate some delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            latch.countDown();
        });

        latch.await(); // Wait until both threads are finished
        service.shutdown();

        assertEquals(5, buffer.size()); // Ensure all elements were added
    }

    @DisplayName("Обращение к элементу за границами буфера")
    @Test
    public void testOutOfBounds() {
        Buffer<Integer> buffer = new Buffer<>(2);
        buffer.add(1);
        buffer.add(2);
        assertThrows(IndexOutOfBoundsException.class, () -> buffer.get(2));
    }

    @Test
    public void testNoSuchElementExceptionFromIterator() {
        Buffer<Integer> buffer = new Buffer<>(2);
        buffer.add(1);

        Iterator<Integer> iterator = buffer.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testSubListWithValidRange() {
        Buffer<Integer> l = new Buffer<>(100);
        for (int i = 1; i <= 50; i++) {
            l.add(i); // Добавляем элемент в список
            assertEquals(i, l.size()); // Проверяем размер списка
            assertEquals(i, l.get(i - 1)); // Проверяем последний элемент в списке
        }
        assertIterableEquals(asList(2, 3, 4), l.subList(1, 4));
        assertIterableEquals(asList(33, 34), l.subList(32, 34));
    }

    @Test
    void testSubListWithEmptyResult() {
        Buffer<Integer> list = new Buffer<>(10);
        list.add(1);
        list.add(2);

        Iterable<Integer> subList = list.subList(1, 1);
        List<Integer> expected = Collections.emptyList();
        assertIterableEquals(expected, subList, "The subList should be empty.");
    }

    @Test
    void testSubListWithFullRange() {
        Buffer<Integer> list = new Buffer<>(10);
        list.add(1);
        list.add(2);
        list.add(3);

        Iterable<Integer> subList = list.subList(0, 3);
        List<Integer> expected = asList(1, 2, 3);
        assertIterableEquals(expected, subList, "The subList should contain all elements.");
    }

    @Test
    void testSubListWithInvalidRange() {
        Buffer<Integer> list = new Buffer<>(10);
        list.add(1);
        list.add(2);
        list.add(3);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.subList(-1, 4);
        }, "subList should throw IndexOutOfBoundsException for invalid range");
    }
}
