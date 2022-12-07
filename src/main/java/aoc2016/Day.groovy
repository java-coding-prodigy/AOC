package aoc2016

import java.nio.file.Files
import java.nio.file.Path

abstract class Day {

    private final int number
    private final Path inputPath
    private final List<String> input

    Day(def number) {
        this.number = number
        this.inputPath  = Path.of("C:\\Users\\Abc\\IdeaProjects\\AOC\\src\\main\\resources\\2016\\Day${number}.txt")
        this.input = Files.readAllLines(inputPath)
        run(input)
    }

    abstract void run(List<String> input);

}
