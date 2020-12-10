package ua.edu.ucu;

import org.junit.Before;
import org.junit.Test;
import ua.edu.ucu.stream.AsIntStream;
import ua.edu.ucu.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class StreamTest {
    private IntStream intStream;
    private IntStream emptyIntStream;

    @Before
    public void init() {
        int[] intArr = {-1, 0, 1, 2, 3};
        intStream = AsIntStream.of(intArr);

        int[] emptyIntArr = {};
        emptyIntStream = AsIntStream.of(emptyIntArr);

        intStream = intStream
                .filter(x -> x > 0) // 1, 2, 3
                .map(x -> x * x) // 1, 4, 9
                .flatMap(x -> AsIntStream.of(x - 1, x, x + 1)); // 0, 1, 2, 3, 4, 5, 8, 9, 10

        emptyIntStream = emptyIntStream
                .filter(x -> x > 0)
                .map(x -> x * x)
                .flatMap(x -> AsIntStream.of(x - 1, x, x + 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStreamAverage() {
        double result = 42.0 / 9;
        assertEquals(intStream.average(), result, 0.0000001);
        emptyIntStream.average();
    }

    @Test
    public void testStreamCount() {
        assertEquals(intStream.count(), 9);
        emptyIntStream.count();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStreamMax() {
        assertEquals(intStream.max(), 10, 0.00000001);
        emptyIntStream.max();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStreamMin() {
        assertEquals(intStream.min(), 0, 0.00000001);
        emptyIntStream.min();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStreamSum() {
        assertEquals(intStream.sum(), 42, 0.00000001);
        emptyIntStream.sum();
    }
}
