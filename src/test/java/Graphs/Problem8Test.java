package Graphs;


import AOC2021Practice.Problem8;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Problem8Test {
    @Test
    void test(){
        List<Problem8.Instruction> instructions = Arrays.stream("""
                nop +0
                acc +1
                jmp +4
                acc +3
                jmp -3
                acc -99
                acc +1
                jmp -4
                acc +6""".split("\n")).map(Problem8.Instruction::parseLine).toList();

        long accumulator = 0;
        List<Integer> changeableIndices = IntStream.range(0, instructions.size())
                .filter(i -> instructions.get(i).type().equals("jmp") || instructions.get(i).type().equals("nop") )
                .boxed()
                .toList();
        //System.out.println(changeableIndices);
        //Set<Integer> indicesChanged = new HashSet<>();
        for (int currentIndex : changeableIndices) {
            List<Problem8.Instruction> currentInstructions = instructions.stream().map(
                    Problem8.Instruction::sameObj).toList();
            //System.out.print(currentInstructions.get(currentIndex) + " vs ");
            //indicesChanged.add(currentIndex);
            currentInstructions.get(currentIndex).swapType();
            //System.out.println(currentInstructions.get(currentIndex));
            System.out.println("Current Instructions are:" + currentInstructions.stream().map(
                    Problem8.Instruction::toString).collect(
                    Collectors.joining("\n")));
            int maxIndex = -1;
            Set<Integer> instructionsExecuted = new HashSet<>();
            for (int i = 0; i < currentInstructions.size(); ) {
                Problem8.Instruction instruction = currentInstructions.get(i);
                /*try {
                    instruction = currentInstructions.get(i);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println(currentIndex);
                    return accumulator;
                }*/
                //System.out.println(instruction);
                maxIndex = Math.max(i, maxIndex);
                //System.out.println(instruction);
                if (instructionsExecuted.contains(i)) {
                    System.out.println("Endless loop when tried to change " + currentIndex
                            + " First repeat instruction was " + i + " Max index reached was "
                            + maxIndex);
                    accumulator = 0;
                    break;
                }
                instructionsExecuted.add(i);
                switch (instruction.type()) {
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
        assertEquals(accumulator,8);
    }
}
