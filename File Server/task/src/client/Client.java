package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

  private static final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    try (
        Socket socket = new Socket(InetAddress.getByName("localhost"), 8080);
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {

      System.out.print("Enter action (1 - get a file, 2 - create a file, 3 - delete a file): ");
      String option = scanner.nextLine().trim().toLowerCase();
      switch (option) {
        case "1" -> getFile(inputStream, outputStream);
        case "2" -> createFile(inputStream, outputStream);
        case "3" -> deleteFile(inputStream, outputStream);
        case "exit" -> outputStream.writeUTF("exit");
        default -> System.err.println("Incorrect option.");
      }

    } catch (IOException e) {
      System.err.println("IOException reason: " + e.getMessage());
    } catch (Exception e) {
      System.err.println("Exception reason: " + e.getMessage());
    }
  }

  private static void createFile(DataInputStream inputStream, DataOutputStream outputStream)
      throws IOException {
    outputStream.writeUTF("PUT");
    System.out.print("Enter filename: ");
    String filename = scanner.nextLine();
    System.out.print("Enter file content: ");
    String content = scanner.nextLine();
    outputStream.writeUTF(filename);
    outputStream.writeUTF(content);
    int statusCode = inputStream.readInt();
    if (statusCode == 403) {
      System.out.println("The response says that creating the file was forbidden!");
      return;
    }
    System.out.println("The response says that file was created!");
  }

  private static void getFile(DataInputStream inputStream, DataOutputStream outputStream)
      throws IOException {
    outputStream.writeUTF("GET");
    System.out.print("Enter filename: ");
    String filename = scanner.nextLine();
    outputStream.writeUTF(filename);
    System.out.println("The request was sent.");
    int statusCode = inputStream.readInt();
    if (statusCode == 404) {
      System.out.println("The response says that the file was not found!");
      return;
    }
    String content = inputStream.readUTF();
    System.out.println("The content of the file is: " + content);
  }

  private static void deleteFile(DataInputStream inputStream, DataOutputStream outputStream)
      throws IOException {
    outputStream.writeUTF("DELETE");
    System.out.print("Enter filename: ");
    String filename = scanner.nextLine();
    outputStream.writeUTF(filename);
    System.out.println("The request was sent.");
    int statusCode = inputStream.readInt();
    if (statusCode == 404) {
      System.out.println("The response says that the file was not found!");
      return;
    }
    System.out.println("The response says that the file was successfully deleted!");
  }
}
