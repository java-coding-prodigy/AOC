package shhj;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntBiFunction;

public class Dijkstras {

    public static void main(String[] args) {

    }

    public static <T> OptionalInt dijsktra(T start, T end,
            Function<? super T, ? extends Iterable<T>> neighbours,
            ToIntBiFunction<? super T, ? super T> getDistance) {
        Queue<T> queue = new LinkedList<>();
        queue.add(start);
        Map<T, Integer> distances = new HashMap<>();
        distances.put(start, 0);
        while (!queue.isEmpty()) {
            T current = queue.poll();
            for (T neighbour : neighbours.apply(current)) {
                int distance = distances.get(current) + getDistance.applyAsInt(current, neighbour);
                if (distances.getOrDefault(neighbour, Integer.MAX_VALUE) > distance) {
                    distances.put(neighbour, distance);
                    queue.add(neighbour);
                }
            }
        }
        Integer result = distances.get(end);
        return result == null ? OptionalInt.empty() : OptionalInt.of(result);
    }

}
