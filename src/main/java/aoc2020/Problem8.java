package AOC2021Practice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Problem8 implements AoC2020Puzzle {
    @Override public Object part1() throws IOException {
        List<String> instructions =
                Files.readAllLines(Path.of("src/main/resources/Instructions.txt"));
        Set<Integer> instructionsExecuted = new HashSet<>();
        int accumulator = 0;
        for (int i = 0; true; ) {
            String line;
            try {
                line = instructions.get(i);
            } catch (IndexOutOfBoundsException e) {
                return accumulator;
            }
            System.out.println(line);
            Instruction instruction = Instruction.parseLine(line);
            if (instructionsExecuted.contains(i))
                return accumulator;
            instructionsExecuted.add(i);
            switch (instruction.type) {
                case "acc" -> accumulator += instruction.number();
                case "jmp" -> i += instruction.number() - 1;
                default -> {
                }
            }
            i++;
        }
    }


    @Override public Object part2() throws IOException {
        List<Instruction> instructions =
                Files.readAllLines(Path.of("src/main/resources/Instructions.txt"))
                        .stream()
                        .map(Instruction::parseLine)
                        .toList();

        long accumulator = 0;
        List<Integer> changeableIndices = IntStream.range(0, instructions.size())
                .filter(i -> instructions.get(i).type().equals("jmp") || instructions.get(i).type().equals("nop") )
                .boxed()
                .toList();
        //System.out.println(changeableIndices);
        //Set<Integer> indicesChanged = new HashSet<>();
        for (int currentIndex : changeableIndices) {
            List<Instruction> currentInstructions = instructions.stream().map(Instruction::sameObj).toList();
            System.out.print(currentInstructions.get(currentIndex) + " vs ");
            //indicesChanged.add(currentIndex);
            currentInstructions.get(currentIndex).swapType();
            //System.out.println(currentInstructions.get(currentIndex));
            int maxIndex = -1;
            Set<Integer> instructionsExecuted = new HashSet<>();
            for (int i = 0; i < currentInstructions.size(); ) {
                Instruction instruction = currentInstructions.get(i);
                /*try {
                    instruction = currentInstructions.get(i);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println(currentIndex);
                    return accumulator;
                }*/
                //System.out.println(instruction);
                maxIndex = Math.max(i, maxIndex);
                if (instructionsExecuted.contains(i)) {
                    System.out.println("Endless loop when tried to change " + currentIndex
                            + " First repeat instruction was " + i + " Max index reached was "
                            + maxIndex);
                    accumulator = 0;
                    break;
                }
                instructionsExecuted.add(i);
                switch (instruction.type) {
                    case "acc" -> accumulator += instruction.number();
                    case "jmp" -> i += instruction.number() - 1;
                    default -> {
                    }
                }
                i++;
                if (i == currentInstructions.size())
                    System.out.println(
                            "Executing last instruction, current value of accumulator is "
                                    + accumulator + " Index changed was " + currentIndex);
            }
        }
        return accumulator;
    }


    public static class Instruction {
        public static final Pattern pattern = Pattern.compile("([ajn][cmo][cp])\\s([+-]\\d+)");
        String type;
        int number;

        public Instruction(String type, int number) {
            this.type = type;
            this.number = number;
        }

        public static Instruction parseLine(String line) {
            final Matcher matcher = pattern.matcher(line);
            if (!matcher.matches())
                System.out.println("No match found for " + line);
            return new Instruction(matcher.group(1), Integer.parseInt(matcher.group(2)));
        }

        @Override public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Instruction that))
                return false;
            return number == that.number && type.equals(that.type);
        }

        @Override public int hashCode() {
            return Objects.hash(type, number);
        }

        public String type() {
            return type;
        }

        public int number() {
            return number;
        }

        public void swapType() {
            if (type.equals("jmp")) {
                type = "nop";
                return;
            }
            if (type.equals("nop"))
                type = "jmp";
        }

        @Override
        public String toString(){
            return String.format("%s\s%+d",type,number);
        }

        public Instruction sameObj() {
            return new Instruction(type,number);
        }
    }
}
