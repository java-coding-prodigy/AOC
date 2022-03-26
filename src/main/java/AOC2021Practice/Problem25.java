package AOC2021Practice;

public class Problem25 {

    public static void main(String[] args) {
        int subjectNo = 7;
        final int divisor = 20201227;
        final int cardKey = 13233401;
        final int doorKey = 6552760;
        int cardLoop = -1;
        int doorLoop = -1;
        long current = 1;
        for(int i = 1;cardLoop == -1 || doorLoop == -1; i++){
            current = current * subjectNo % divisor;
            if(cardKey == current){
                cardLoop = i;
            }
            if(doorKey == current){
                doorLoop = i;
            }
        }
        current = 1;
        subjectNo = doorKey;
        for(int i = 1; i <= cardLoop; i++){
            current = current * subjectNo % divisor;
        }
        System.out.println(current);
    }

}
