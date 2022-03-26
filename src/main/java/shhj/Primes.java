package shhj;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Primes {
    public static void main(String[] args) throws IOException {
        List<Long> primes = new LinkedList<>();
        primes.add(2L);
        primes.add(3L);
        OutputStream stream = Files.newOutputStream(Path.of("C:\\Users\\Abc\\IdeaProjects\\AOC2021\\src\\main\\java\\shhj\\Primes.txt"));
        stream.write('\f');
        stream.write("2\n3\n".getBytes());
        long num = 5;
        do {
            long sqrt = (long) Math.sqrt(num);
            boolean b = false;
            for (long l : primes) {
                if (l > sqrt) {
                    b = true;
                    break;
                }
                if (num % l == 0) {
                    break;
                }
            }
            if (b) {
                primes.add(num);
                stream.write((num + "\n").getBytes());
            }
            num++;
        } while (true);
    }
}
