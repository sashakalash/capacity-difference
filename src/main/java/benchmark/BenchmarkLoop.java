package benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})

public class BenchmarkLoop {
    private static final int ITEMS_QUANTITY = 100;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchmarkLoop.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput) @OutputTimeUnit(TimeUnit.MINUTES)
    public static void randomReadAndWriteSynchronizedMap() {
        Map<String, Integer> map = Collections.synchronizedMap(new HashMap<>());
        performReadAndWriteTest(map);
    }

    @Benchmark @BenchmarkMode(Mode.Throughput) @OutputTimeUnit(TimeUnit.MINUTES)
    public static void randomReadAndWriteConcurrentHashMap() {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        performReadAndWriteTest(map);
    }

    private static void performReadAndWriteTest(final Map<String, Integer> map) {
        for (int i = 0; i < ITEMS_QUANTITY; i++) {
            Integer randNumber = (int) Math.ceil(Math.random() * ITEMS_QUANTITY);
            map.get(String.valueOf(randNumber));
            map.put(String.valueOf(randNumber), randNumber);
        }
    }
}