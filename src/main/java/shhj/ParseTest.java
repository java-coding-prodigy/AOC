package shhj;


import java.util.stream.IntStream;

public class ParseTest {

    public static void main(String[] args) {
        System.out.println(parse("-12240",16));
        System.out.println(Integer.parseInt("-12240",16));

    }



    public static int parse(String number, int base){
        IntStream chars = number.chars();
        boolean bool = number.contains("-");
        if(bool){
            chars = chars.skip(1);
        }
        int res = chars.reduce( 0, (a,b) -> base * a + b - '0');
        if(bool){
            res *= -1;
        }
        return res;
    }


}
