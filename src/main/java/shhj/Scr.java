package shhj;


//import org.openjdk.jmh.annotations.Benchmark;

import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Set;


public class Scr {
    public static void main(String[] args) throws Exception {
        String str1 = "foo";
        String str2 = new String("foo");
        String unicode = new String(new char[]{'a',',',' ','‘','a'});
        System.out.println(str1);
        System.out.println(str2);
        //System.out.println(solution("abcabcabcabc"));
        //System.out.println(solution("abccbaabccba"));
        //org.openjdk.jmh.Main.main(args);
    }

    //@Benchmark
    public void init(){
        solution("abcabcabcabc");
    }

    public static int solution(String x) {
        if (x.length() > 200) {
            throw new InputMismatchException("The strings length is more than 200");
        }
        for (int i = 1; i <= x.length() / 2; i++) {
            Set<String> set = new HashSet<>();
            for (int j = 0; j < x.length() - i; j += i) {
                set.add(x.substring(j, j + i));
            }
            //System.out.println(i);
            if (set.size() == 1) {
                return x.length() / i;
            }
        }
        return -1;
    }
}
