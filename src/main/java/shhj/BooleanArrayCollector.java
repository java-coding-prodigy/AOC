package shhj;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class BooleanArrayCollector implements Collector<Boolean, boolean[], boolean[]>
{

    int length;
    int index = 0;


    public static void main(String[] args) {

    }

    public BooleanArrayCollector(int length)
    {
        this.length = length;
    }

    @Override
    public Supplier<boolean[]> supplier()
    {
        return () -> new boolean[length];
    }

    @Override
    public BiConsumer<boolean[], Boolean> accumulator()
    {
        return (array, bool) -> array[index++] = bool;
    }

    @Override
    public BinaryOperator<boolean[]> combiner()
    {
        return (arr1, arr2) ->
        {
            for (int i = 0; index < length; i++, index++)
            {
                arr1[index] = arr2[i];
            }
            return arr1;
        };
    }

    @Override
    public Function<boolean[], boolean[]> finisher()
    {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics()
    {
        return Set.of(Characteristics.IDENTITY_FINISH);
    }
}
