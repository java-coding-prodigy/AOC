package aoc2019;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import org.apache.commons.math3.util.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.*;

public class IntCode {

    public final Queue<Integer> inputs = new LinkedList<>();
    final AtomicLong output;
    final AtomicInteger relativeBase;
    final int setting;
    private final Queue<int[]> outputs = new LinkedList<>();
    long[] memory;
    long i;

    public IntCode(int setting, int[] array) {
        this(setting, Arrays.stream(array).asLongStream().toArray());
    }

    public IntCode(IntCode prev, String line) {
        this.memory = prev.memory.clone();
        this.i = prev.i;
        this.output = new AtomicLong(prev.output.get());
        this.relativeBase = new AtomicInteger(prev.relativeBase.get());
        this.inputs.addAll(prev.inputs);
        line.chars().forEach(inputs::add);
        this.setting = prev.setting;
    }

    public IntCode(long[] array) {
        this(-1, array);
    }

    public IntCode(int setting, long[] array) {
        this.memory = Arrays.copyOf(array, array.length);
        this.setting = setting;
        this.output = new AtomicLong();
        this.i = 0;
        this.relativeBase = new AtomicInteger();
    }

    public static Map<Integer, Operation> operations(Integer[] inputs, IntConsumer onOutput) {
        return operations(new ArrayIterator<>(inputs), onOutput, new AtomicInteger());
    }

    public static Map<Integer, Operation> operations(Iterator<Integer> inputs, IntConsumer onOutput,
            AtomicInteger relativeBase) {
        return Map.of(1, new Operation(3, (array, params) -> {
                    array[params[3]] = params[1] + params[2];
                    return params[0] + 4;
                }, OpType.WRITE), 2, new Operation(3, (array, params) -> {
                    array[params[3]] = params[1] * params[2];
                    return params[0] + 4;
                }, OpType.WRITE), 3, new Operation(1, (array, params) -> {
                    array[params[1]] = inputs.next();
                    return params[0] + 2;
                }, OpType.WRITE), 4, new Operation(1, (array, params) -> {
                    if (params[1] != 0)
                        onOutput.accept(params[1]);
                    return params[0] + 2;
                }, OpType.READ), 5,
                new Operation(2, (array, params) -> params[1] != 0 ? params[2] : params[0] + 3,
                        OpType.JUMP), 6,
                new Operation(2, (array, params) -> params[1] == 0 ? params[2] : params[0] + 3,
                        OpType.JUMP), 7, new Operation(3, (array, params) -> {
                    array[params[3]] = params[1] < params[2] ? 1 : 0;
                    return params[0] + 4;
                }, OpType.WRITE), 8, new Operation(3, (array, params) -> {
                    array[params[3]] = params[1] == params[2] ? 1 : 0;
                    return params[0] + 4;
                }, OpType.WRITE), 9, new Operation(1, (array, params) -> {
                    relativeBase.addAndGet(params[1]);
                    return params[0] + 2;
                }, OpType.JUMP), 99, new Operation(0, (array, params) -> {
                    throw new UnsupportedOperationException();
                }, OpType.JUMP));
    }

    public static int run(int[] array, Integer... inputs) {
        return run(Arrays.stream(array).asLongStream().toArray(), new ArrayIterator<>(inputs));
    }

    private static Map<Integer, LongOp> longOps(Iterator<Integer> inputs, AtomicLong output,
            AtomicInteger relativeBase) {
        return longOps(inputs::next, output::set, relativeBase);
    }

    private static Map<Integer, LongOp> longOps(Supplier<Integer> inputs, LongConsumer onOutput,
            AtomicInteger relativeBase) {
        return Map.of(1, new LongOp(3,
                        (long[] params, long[] array) -> array[(int) params[3]] = params[1] + params[2],
                        true), 2, new LongOp(3,
                        (long[] params, long[] array) -> array[(int) params[3]] = params[1] * params[2],
                        true), 3, new LongOp(1,
                        (long[] params, long[] array) -> array[(int) params[1]] = inputs.get(), true), 4,
                new LongOp(1, (long[] params, long[] array) -> onOutput.accept(params[1]), false),
                5, new LongOp(2,
                        (params, array) -> params[1] != 0 ? (int) params[2] : (int) params[0] + 3,
                        false), 6, new LongOp(2,
                        (params, array) -> params[1] == 0 ? (int) params[2] : (int) params[0] + 3,
                        false), 7, new LongOp(3,
                        (long[] params, long[] array) -> array[(int) params[3]] =
                                params[1] < params[2] ? 1 : 0, true), 8, new LongOp(3,
                        (long[] params, long[] array) -> array[(int) params[3]] =
                                params[1] == params[2] ? 1 : 0, true), 9,
                new LongOp(1, (long[] params, long[] array) -> {
                    relativeBase.addAndGet((int) params[1]);
                }, false), 99, new LongOp(0, (long[] params, long[] array) -> -1, false));
    }


    public static Pair<long[], Integer> run(long[] previous, int dir) {
        AtomicInteger relativeBase = new AtomicInteger();
        AtomicLong result = new AtomicLong(-1);
        long[] memory = previous.clone();
        Map<Integer, LongOp> ops =
                longOps(List.of(dir).iterator()::next, result::set, relativeBase);
        int i = 0;
        while (memory[i] % 100 != 99 && result.get() == -1) {
            Ans res = ops.get((int) memory[i] % 100).apply(memory, i, relativeBase.get());
            if (res.success) {
                i = res.idx;
            } else {
                memory = Arrays.copyOf(memory, res.idx + 1);
            }
        }
        return new Pair<>(memory, (int) result.get());
    }

    public static long run(long[] memory, String line) {
        PrimitiveIterator.OfInt inputs = line.chars().iterator();
        AtomicLong result = new AtomicLong();
        run(memory, inputs::next, ch -> {
            if (ch < '\u00FF')
                System.out.print((char) ch);
            else
                result.set(ch);
        });
        return result.get();
    }

    public static void run(long[] memory, Supplier<Integer> input, LongConsumer onOutput) {
        AtomicInteger relativeBase = new AtomicInteger();
        long[] copy = Arrays.copyOf(memory, memory.length);
        Map<Integer, LongOp> ops = longOps(input, onOutput, relativeBase);
        int i = 0;
        while (copy[i] % 100 != 99) {
            Ans res = ops.get((int) copy[i] % 100).apply(copy, i, relativeBase.get());
            if (res.success) {
                i = res.idx;
            } else {
                copy = Arrays.copyOf(copy, res.idx + 1);
            }
        }
    }

    public static int run(long[] instructions, Iterator<Integer> inputs) {
        return run(instructions, inputs::next);
    }

    public static int run(long[] instructions, Supplier<Integer> supplier) {
        AtomicInteger relativeBase = new AtomicInteger();
        AtomicLong result = new AtomicLong();
        long[] copy = Arrays.copyOf(instructions, instructions.length);
        Map<Integer, LongOp> ops = longOps(supplier, result::set, relativeBase);
        int i = 0;
        while (copy[i] % 100 != 99) {
            Ans res = ops.get((int) copy[i] % 100).apply(copy, i, relativeBase.get());
            if (res.success) {
                i = res.idx;
            } else {
                copy = Arrays.copyOf(copy, res.idx + 1);
            }
        }
        return (int) result.get();
    }

    public static int run(int[] array, Iterator<Integer> iterator) {
        return run(Arrays.stream(array).asLongStream().toArray(), iterator);
    }

    public static int runTIllOutput(long[] memory, int input) {
        return run(memory, List.of(input).iterator());
    }


    public CharSequence runTillInput(String command) {
        command.chars().forEach(inputs::add);
        StringBuilder sb = new StringBuilder();
        Map<Integer, LongOp> ops = longOps(inputs::poll, ch -> sb.append((char) ch), relativeBase);
        while (memory[(int) i] % 100 != 99) {
            try {
                Ans res = ops.get((int) memory[(int) i] % 100)
                        .apply(memory, (int) i, relativeBase.get());
                if (res.success) {
                    i = res.idx;
                } else {
                    memory = Arrays.copyOf(memory, res.idx + 1);
                }
            } catch (NullPointerException e) {
                return sb;
            }
        }

        return sb;
    }

    public void addInput(int input) {
        inputs.add(input);
    }

    public boolean runTill3() {
        int[] outputs = new int[3];
        AtomicInteger outputIdx = new AtomicInteger();
        Map<Integer, LongOp> ops = longOps(inputs::poll,
                (output -> outputs[outputIdx.getAndIncrement()] = (int) output), relativeBase);
        while (memory[(int) i] % 100 != 99) {

            try {
                Ans res = ops.get((int) memory[(int) i] % 100)
                        .apply(memory, (int) i, relativeBase.get());
                if (res.success) {
                    i = res.idx;
                } else {
                    memory = Arrays.copyOf(memory, res.idx + 1);
                }
            } catch (NullPointerException e) {
                // if (outputIdx.get() == 3) {
                //System.out.println(outputIdx);
                return true;
                // }
                //addInput(-1);
            }
            if (outputIdx.get() == 3) {
                this.outputs.add(Arrays.copyOf(outputs, 3));
                outputIdx.set(0);
            }

        }
        return false;
    }

    public int[] getOutput() {
        return outputs.poll();
    }

    public int[] runTill3Output() {
        int[] outputs = new int[3];
        AtomicInteger outputIdx = new AtomicInteger();
        Map<Integer, LongOp> ops =
                longOps(() -> -1, (output -> outputs[outputIdx.getAndIncrement()] = (int) output),
                        relativeBase);
        while (memory[(int) i] % 100 != 99 && outputIdx.get() < 3) {

            Ans res =
                    ops.get((int) memory[(int) i] % 100).apply(memory, (int) i, relativeBase.get());
            if (res.success) {
                i = res.idx;
            } else {
                memory = Arrays.copyOf(memory, res.idx + 1);
            }
        }
        if (outputIdx.get() < 3) {
            return null;
        }
        return outputs;
    }


    public long runTillEnd() {
        Map<Integer, LongOp> ops = longOps(inputs::poll, output::set, relativeBase);
        while (memory[(int) i] % 100 != 99) {
            Ans res =
                    ops.get((int) memory[(int) i] % 100).apply(memory, (int) i, relativeBase.get());
            if (res.success) {
                i = res.idx;
            } else {
                memory = Arrays.copyOf(memory, res.idx + 1);
            }

        }
        return output.get();
    }

    public List<Integer> runTill2Output(Iterator<Integer> input) {
        List<Integer> outputs = new ArrayList<>(2);
        output.set(-1);
        Map<Integer, LongOp> ops = longOps(input, output, relativeBase);
        while (memory[(int) i] % 100 != 99 && outputs.size() < 2) {
            Ans res =
                    ops.get((int) memory[(int) i] % 100).apply(memory, (int) i, relativeBase.get());
            if (res.success) {
                i = res.idx;
            } else {
                memory = Arrays.copyOf(memory, res.idx + 1);
            }
            if (output.get() != -1) {
                outputs.add((int) output.get());
                output.set(-1);

            }
        }
        return outputs;
    }

    public int runTillOutput(int input) {
        return runTillOutput(input, false);
    }

    public int runTillOutput(int input, boolean bool) {
        output.set(-1);
        List<Integer> list = new ArrayList<>();
        if (bool) {
            list.add(setting);
        }
        list.add(input);
        Map<Integer, LongOp> operations = longOps(list.iterator(), output, relativeBase);
        while (memory[(int) i] % 100 != 99 && output.get() == -1) {
            i = operations.get((int) memory[(int) i] % 100)
                    .apply(memory, (int) i, relativeBase.get()).idx;

        }
        return (int) output.get();
    }

    private enum OpType {
        WRITE, READ, JUMP
    }


    private record LongOp(int params, ToIntBiFunction<long[], long[]> op, boolean write) {

        LongOp(int params, BiConsumer<long[], long[]> op, boolean write) {
            this(params, (p, arr) -> {
                op.accept(p, arr);
                return (int) p[0] + params + 1;
            }, write);
        }

        Ans apply(long[] memory, int i, int relativeBase) {
            long[] params = new long[this.params + 1];
            params[0] = i;
            int code = (int) memory[i] / 100;
            for (int idx = 1; idx <= this.params; idx++) {
                int mode = code % 10;
                long curr = memory[i + idx];
                if (mode == 2) {
                    curr += relativeBase;
                }
                if (mode != 1 && (!write || idx != this.params)) {
                    if (curr >= memory.length) {
                        return new Ans((int) curr, false);
                    }
                    curr = memory[(int) curr];
                }
                code /= 10;
                params[idx] = curr;
            }
            if (write && params[this.params] >= memory.length) {
                return new Ans((int) params[this.params], false);
            }
            int res = op.applyAsInt(params, memory);
            return new Ans(res, res < memory.length);
        }

    }


    private record Ans(int idx, boolean success) {
    }


    public record Operation(int params, ToIntBiFunction<int[], int[]> op, OpType type) {

        int apply(int[] array, int i, int relativeBase) {
            int[] params = new int[this.params + 1];
            params[0] = i;
            int code = array[i] / 100;
            for (int idx = 1; idx < params.length; idx++) {
                int curr = array[i + idx];
                int mode = code % 10;
                if ((type != OpType.WRITE || idx != params.length - 1)) {
                    curr = switch (mode) {
                        case 0 -> array[curr];
                        case 1 -> curr;
                        case 2 -> array[curr + relativeBase];
                        default -> throw new IllegalStateException();
                    };
                }
                code /= 10;
                params[idx] = curr;
            }
            return op.applyAsInt(array, params);
        }

    }
}
