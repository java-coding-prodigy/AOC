package shhj;

import aoc2020.Problem23;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
public class CollectorsCompare {

    public static void main(String[] args) throws RunnerException {
        new Runner(new OptionsBuilder()
                .include(CollectorsCompare.class.getSimpleName())
                .forks(1)
                .build()).run();
    }

    int maxSize;
    Collector<Boolean, boolean[], boolean[]> arrayCollector;
    Collector<Boolean, ?, List<Boolean>> listCollector;


    @Setup
    public void setup(){
        maxSize = 10_000;
        arrayCollector = new BooleanArrayCollector(maxSize);
        listCollector = Collectors.toList();

    }

    public Stream<Boolean> generateStream(int maxSize){
        return Stream.generate(ThreadLocalRandom.current()::nextBoolean).limit(maxSize);
    }
    
    @Benchmark
    public void array(){
        generateStream(maxSize).collect(arrayCollector);
    }

    @Benchmark
    public void list(){
        generateStream(maxSize).collect(listCollector);
    }
}
