import java.util.Scanner;

class Main {

  public static void main(String[] args) {
    // put your code here
    Scanner scanner = new Scanner(System.in);
    String html = scanner.nextLine();
    parseHtml(html);
  }

  private static void parseHtml(String html) {
    if (html.charAt(0) != '<') {
      return;
    }
    String temp = html;
    while (!temp.isEmpty()) {
      StringBuilder stringBuilder = new StringBuilder();
      int index = 0;

      do {
        stringBuilder.append(temp.charAt(index));
      } while (temp.charAt(index++) != '>');

      String closeTag = stringBuilder.insert(1, '/').toString();
      int closeTagIndex = temp.indexOf(closeTag);
      String tag = temp.substring(closeTag.length() - 1, closeTagIndex);

      parseHtml(tag);

      System.out.println(tag);
      temp = temp.substring(closeTagIndex + closeTag.length());
    }
  }
}