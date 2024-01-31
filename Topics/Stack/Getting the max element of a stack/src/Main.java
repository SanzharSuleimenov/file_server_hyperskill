import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

class Main {

  public static void main(String[] args) {
    // put your code here
    Deque<Integer> stck = new ArrayDeque<>();
    Scanner s = new Scanner(System.in);
    int cnt = s.nextInt();
    while (cnt-- > 0) {
      String command = s.next();
      switch (command) {
        case "push":
          int value = s.nextInt();
          stck.push(stck.isEmpty() || value >= stck.peek() ? value : stck.peek());
          break;
        case "pop":
          stck.pop();
          break;
        default:
          System.out.println(stck.peek());
      }
    }
  }
}
