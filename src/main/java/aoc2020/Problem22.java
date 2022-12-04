package aoc2020;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Problem22 implements AoC2020Puzzle {


    List<String> input = Files.readAllLines(
            Path.of("C:\\Users\\Abc\\IdeaProjects\\AOC2021\\src\\main\\resources\\2020\\Decks.txt"));

    public Problem22() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new Problem22().part2());
    }

    @Override public Object part1() throws IOException {

        Deque<Integer> deck1 = new ArrayDeque<>();
        Deque<Integer> deck2 = new ArrayDeque<>();


        int i;

        for (i = 1; ; i++) {
            if (input.get(i).isEmpty())
                break;
            deck1.add(Integer.parseInt(input.get(i)));
        }

        for (i += 2; i < input.size(); i++) {
            deck2.add(Integer.parseInt(input.get(i)));
        }

        while (!deck1.isEmpty() && !deck2.isEmpty()) {
            int num1 = deck1.pop();
            int num2 = deck2.pop();
            if (num1 > num2) {
                deck1.add(num1);
                deck1.add(num2);
            } else if (num1 < num2) {
                deck2.add(num2);
                deck2.add(num1);
            } else {
                throw new IllegalStateException();
            }
        }

        return computeScore(deck1.isEmpty() ? deck2 : deck1);
    }

    private int computeScore(Deque<Integer> deck) {
        Iterable<Integer> iterable = deck::descendingIterator;
        int result = 0;
        int i = 0;
        for (int num : iterable) {
            result += num * ++i;
        }
        return result;
    }

    @Override public Object part2() throws IOException {
        Deque<Integer> deck1 = new ArrayDeque<>();
        Deque<Integer> deck2 = new ArrayDeque<>();


        int i;

        for (i = 1; ; i++) {
            if (input.get(i).isEmpty())
                break;
            deck1.add(Integer.parseInt(input.get(i)));
        }

        for (i += 2; i < input.size(); i++) {
            deck2.add(Integer.parseInt(input.get(i)));
        }


        Set<String> states = new HashSet<>();

        return computeScore(game(deck1, deck2, states) ? deck1 : deck2);
    }

    private boolean game(Deque<Integer> deck1, Deque<Integer> deck2, Set<String> states) {
        states.add(String.valueOf(deck1) + deck2);
        while (true) {
            int num1 = deck1.pop();
            int num2 = deck2.pop();
            String currentState = String.valueOf(deck1) + deck2;
            if (states.contains(currentState)) {
                System.out.println(computeScore(deck1));
                return true;
            }
            states.add(currentState);
            if (num1 <= deck1.size() && num2 <= deck2.size()) {
                if (game(new ArrayDeque<>(deck1.stream().limit(num1).toList()),
                        new ArrayDeque<>(deck2.stream().limit(num2).toList()), new HashSet<>())) {
                    deck1.add(num1);
                    deck1.add(num2);
                } else {
                    deck2.add(num2);
                    deck2.add(num1);
                }
            } else {
                if (num1 > num2) {
                    deck1.add(num1);
                    deck1.add(num2);
                } else {
                    deck2.add(num2);
                    deck2.add(num1);
                }
            }
            if (deck1.isEmpty()) {
                return false;
            }
            if (deck2.isEmpty()) {
                return true;
            }
        }
    }
}
