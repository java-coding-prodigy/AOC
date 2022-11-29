package aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problem2 {
    private static final Pattern pattern =
            Pattern.compile("(\\d{1,2})-(\\d{1,2})\\s*([a-z]):\\s*([a-z]*)");

    public static void main(String[] args) {

        try {
            System.out.println(Files.readAllLines(Path.of("src/main/resources/Passwords.txt"))
                    .stream()
                    .filter(Problem2::matchesPolicy)
                    .count());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean matchesPolicy(String str) {
        Matcher matcher = pattern.matcher(str);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Your pattern is wrong");
        }
        int min = Integer.parseInt(matcher.group(1));
        int max = Integer.parseInt(matcher.group(2));
        char ltr = matcher.group(3).charAt(0);
        return matcher.group(4).charAt(min - 1) == ltr ^ matcher.group(4).charAt(max - 1) == ltr;
    }
}
