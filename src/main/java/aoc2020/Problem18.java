package AOC2021Practice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Problem18 implements AoC2020Puzzle {

    List<String> input = Files.readAllLines(
            Path.of("C:\\Users\\Abc\\IdeaProjects\\AOC2021\\src\\main\\resources\\2020\\Expressions.txt"));

    public Problem18() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        //new Problem18().test();
       System.out.println(new Problem18().part2());
    }

    private static long evaluateExpression(String expression) {
        int idx = 0;
        long prev = -1;
        char op = '\u0000';
        for (; idx < expression.length(); idx++) {
            char current = expression.charAt(idx);
            if (current == '(') {
                int closingBracket = getClosingBracket(idx, expression);
                prev = evaluate(prev, op,
                        evaluateExpression(expression.substring(idx + 1, closingBracket)));
                idx = closingBracket;
            } else if (Character.isDigit(current)) {
                prev = evaluate(prev, op, current - '0');
            } else if (current == '*' || current == '+') {
                op = current;
            } else if (current != ' ') {

                throw new IllegalStateException();

            }
        }
        return prev;
    }

    private static int getClosingBracket(int bracketIndex, String original) {
        int count = 0;
        for (int idx = bracketIndex; idx < original.length(); idx++) {
            switch (original.charAt(idx)) {
                case '(' -> count++;
                case ')' -> count--;
            }
            if (count == 0) {
                return idx;
            }
        }
        throw new IllegalStateException("expression does not have closing parenthesis");
    }

    private static long evaluate(long prev, char op, long current) {
        return prev == -1 ? current : switch (op) {
            case '*' -> prev * current;
            case '+' -> prev + current;
            default -> throw new IllegalArgumentException(
                    "'*' or '+' was expected but '" + op + "' was given");
        };
    }

    private static String insertBrackets(String s) {
        StringBuilder sb = new StringBuilder(s.replaceAll(" ", ""));
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '+') {
                int prevIdx = prevIdx(sb,i);
                int afterIdx = afterIdx(sb,i);
                sb.insert(prevIdx,'(').insert(afterIdx + 1,')');
                i += 2;
            }
        }
        return sb.toString();
    }

    private static int afterIdx(StringBuilder sb, int i) {
        int count = 0;
        for(int idx = i + 1; idx < sb.length(); idx++){
            switch(sb.charAt(idx)){
                case '(' -> count++;
                case ')' -> count--;
            }
            if(count == 0){
                return idx+1;
            }
        }
        throw new IllegalStateException();
    }

    private static int prevIdx(StringBuilder sb, int i) {
        int count = 0;
        for(int idx = i - 1; idx >= 0; idx--){
            switch(sb.charAt(idx)){
                case ')' -> count++;
                case '(' -> count--;
            }
            if(count == 0){
                return idx/*-1*/;
            }
        }
        throw new IllegalStateException();
    }

    @Override public Object part1() throws IOException {
        return input.stream().mapToLong(Problem18::evaluateExpression).sum();
    }

    @Override public Object part2() throws IOException {
        return input.stream()
                .map(Problem18::insertBrackets)
                .mapToLong(Problem18::evaluateExpression)
                .sum();
    }

}
