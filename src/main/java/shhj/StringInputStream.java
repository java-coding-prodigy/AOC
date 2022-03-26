package shhj;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class StringInputStream extends InputStream implements Serializable {

    private String string;
    private int pos;
    private int size;

    public StringInputStream(String string) {
        this.string = string;
        pos = 0;
        size = string.length();
    }

    public StringInputStream(String str, int offset, int length) {
        this.string = str;
        this.pos = offset;
        this.size = length;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(System.in.getClass());
        String str = "{(ghrgh)(hfghjhg)}";
        InputStream mine =
                new StringInputStream(str);
        Scanner sc; //= new Scanner(mine);

        sc = new Scanner(new ObjectInputStream(mine));
        while(sc.hasNext()) {
            System.out.println(sc.next());
        }
    }

    @Override public int read() {
        return pos < size ? string.charAt(pos++) : -1;
    }

    @Override public int read(byte[] b, int off, int len) {
        Objects.checkFromIndexSize(off, len, b.length);

        if (pos > size) {
            return -1;
        }

        int avail = Math.min(size - pos, len);
        System.arraycopy(string.getBytes(), pos, b, off, avail);
        pos += avail;

        return avail;
    }

    @Override public byte[] readAllBytes() {
        return string.getBytes();
    }

    @Override public void close() {

    }

    @Override public int read(byte[] b) {
        return read(b, 0, b.length);
    }

}
