import java.io.BufferedReader;
import java.io.InputStreamReader;

class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // start coding here
        int character = reader.read();
        char prev = 'x';
        int count = (char) character != ' ' ? 1 : 0;
        while (character != -1) {
            char ch = (char) character;
            if (prev == ' ' && ch != ' ') {
                count++;
            }
            prev = ch;
            character = reader.read();
        }
        System.out.println(count);
        reader.close();
    }
}
