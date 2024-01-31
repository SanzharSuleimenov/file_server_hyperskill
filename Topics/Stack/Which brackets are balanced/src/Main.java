import java.util.Scanner;
import java.util.Stack;

class Main {

  public static void main(String[] args) {
    // put your code here
    Scanner scanner = new Scanner(System.in);
    String input = scanner.next();
    Stack<Character> stack = new Stack<>();
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      if (c == '(' || c == '[' || c == '{') {
        stack.push(c);
      }
      if (c == '}' || c == ']' || c == ')') {
        if (stack.empty()
            || (stack.peek() != '{' && c == '}')
            || (stack.peek() != '(' && c == ')')
            || (stack.peek() != '[' && c == ']')
        ) {
          System.out.println(false);
          return;
        }
        stack.pop();
      }
    }
    System.out.println(stack.isEmpty());
  }
}