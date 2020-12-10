package ua.edu.ucu.stream;

import ua.edu.ucu.function.IntBinaryOperator;
import ua.edu.ucu.function.IntConsumer;
import ua.edu.ucu.function.IntPredicate;
import ua.edu.ucu.function.IntToIntStreamFunction;
import ua.edu.ucu.function.IntUnaryOperator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class AsIntStream implements IntStream, Iterable<Integer> {

    private ArrayList<Integer> data;
    private Iterator<Integer> valuesIterator;

    private AsIntStream() {
        valuesIterator = new Iterator<Integer>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < data.size();
            }

            @Override
            public Integer next() {
                if (!hasNext()){
                    throw new NoSuchElementException();
                }

                index += 1;
                return data.get(index - 1);
            }
        };
    }

    public static IntStream of(int... values) {
        AsIntStream stream = new AsIntStream();

        stream.data = new ArrayList<>();
        for (int value : values) {
            stream.data.add(value);
        }
        return stream;
    }

    @Override
    public Double average() {
        if (data.size() == 0) {
            throw new IllegalArgumentException();
        }

        double result = 0;
        int count = 0;
        for (int value : this) {
            result += value;
            count += 1;
        }
        return result / count;
    }

    @Override
    public Integer max() {
        if (data.size() == 0) {
            throw new IllegalArgumentException();
        }

        int result = Integer.MIN_VALUE;
        for (int value : this) {
            if (value > result) {
                result = value;
            }
        }
        return result;
    }

    @Override
    public Integer min() {
        if (data.size() == 0) {
            throw new IllegalArgumentException();
        }

        int result = Integer.MAX_VALUE;
        for (int value : this) {
            if (value < result) {
                result = value;
            }
        }
        return result;
    }

    @Override
    public long count() {
        long result = 0;
        for (int ignored : this) {
            result += 1;
        }

        return result;
    }

    @Override
    public Integer sum() {
        if (data.size() == 0) {
            throw new IllegalArgumentException();
        }

        int result = 0;
        for (int value : this) {
            result += value;
        }
        return result;
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        this.valuesIterator = new Iterator<Integer>() {
            private final Iterator<Integer> prevIterator = valuesIterator;

            @Override
            public boolean hasNext() {
                return prevIterator.hasNext();
            }

            @Override
            public Integer next() {
                int value = prevIterator.next();
                while (!predicate.test(value)) {
                    value = prevIterator.next();
                }
                return value;
            }
        };
        return this;
    }

    @Override
    public void forEach(IntConsumer action) {
        for (int value : this) {
            action.accept(value);
        }
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        this.valuesIterator = new Iterator<Integer>() {
            private final Iterator<Integer> prevIterator = valuesIterator;

            @Override
            public boolean hasNext() {
                return prevIterator.hasNext();
            }

            @Override
            public Integer next() {
                int value = prevIterator.next();
                return mapper.apply(value);
            }
        };
        return this;
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        this.valuesIterator = new Iterator<Integer>() {
            private final Iterator<Integer> prevIterator = valuesIterator;
            private IntStream tempValue;

            @Override
            public boolean hasNext() {
                if (tempValue == null) { return prevIterator.hasNext(); }
                return prevIterator.hasNext() ||
                        ((AsIntStream) tempValue).iterator().hasNext();
            }

            @Override
            public Integer next() {
                if (tempValue == null ||
                        !((AsIntStream) tempValue).iterator().hasNext()) {
                    int value = prevIterator.next();
                    tempValue = func.applyAsIntStream(value);
                }
                return ((AsIntStream) tempValue).iterator().next();
            }
        };
        return this;
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        int result = identity;
        for (int value : this) {
            result = op.apply(result, value);
        }
        return result;
    }

    @Override
    public int[] toArray() {
        ArrayList<Integer> tempResult = new ArrayList<>();
        for (int value : this) {
            tempResult.add(value);
        }

        int[] result = new int[tempResult.size()];
        for (int i = 0; i < tempResult.size(); i++) {
            result[i] = tempResult.get(i);
        }
        return result;
    }

    @Override
    public Iterator<Integer> iterator() {
        return valuesIterator;
    }
}
