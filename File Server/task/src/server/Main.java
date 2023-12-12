package server;

import java.util.Scanner;

public class Main {

  public static void options() {
    Stage1 stage1 = new Stage1();
    Scanner scanner = new Scanner(System.in);
    String command, filename;
    do {
      command = scanner.next();

      switch (command) {
        case "add":
          filename = scanner.next();
          stage1.add(filename);
          break;
        case "get":
          filename = scanner.next();
          stage1.get(filename);
          break;
        case "delete":
          filename = scanner.next();
          stage1.delete(filename);
          break;
      }
    } while (!"exit".equalsIgnoreCase(command));
  }
}