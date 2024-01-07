package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.Scanner;

public class Client {

  private static final Scanner scanner = new Scanner(System.in);
  private static final Random random = new Random();
  private static final String RESOURCE_PATH;

  static {
    RESOURCE_PATH = System.getProperty("user.dir") +
        File.separator + "File Server" +
        File.separator + "task" +
        File.separator + "src" +
        File.separator + "client" +
        File.separator + "data" +
        File.separator;

    File dir = new File(RESOURCE_PATH);
    dir.mkdirs();
  }

  public static void main(String[] args) {
    menu();
  }

  private static void menu() {
    try (
        Socket socket = new Socket(InetAddress.getByName("localhost"), 8080);
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {

      System.out.print("Enter action (1 - get a file, 2 - save a file, 3 - delete a file): ");
      String option = scanner.nextLine().trim().toLowerCase();
      switch (option) {
        case "1" -> getFile(inputStream, outputStream);
        case "2" -> saveFile(inputStream, outputStream);
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

  private static void getFile(DataInputStream inputStream, DataOutputStream outputStream)
      throws IOException {
    System.out.print("Do you want to get the file by name or by id (1 - name, 2 - id): ");
    String option = scanner.nextLine();
    String optionName;
    switch (option) {
      case "1" -> {
        optionName = "name";
        System.out.print("Enter name: ");
      }
      case "2" -> {
        optionName = "id";
        System.out.print("Enter id: ");
      }
      default -> throw new RuntimeException("Incorrect option value");
    }
    String optionValue = scanner.nextLine();
    outputStream.writeUTF("get");
    outputStream.writeUTF(optionName);
    outputStream.writeUTF(optionValue);
    System.out.println("The request was sent.");
    int statusCode = inputStream.readInt();
    if (statusCode == 404) {
      System.out.println("The response says that this file is not found!");
      return;
    }
    int length = inputStream.readInt();
    byte[] content = inputStream.readNBytes(length);
    System.out.print("The file was downloaded! Specify a name for it: ");
    String filename = scanner.nextLine();
    Files.write(Path.of(RESOURCE_PATH + filename), content);
    System.out.println("File save on the hard drive!");
  }

  private static void deleteFile(DataInputStream inputStream, DataOutputStream outputStream)
      throws IOException {
    System.out.print("Do you want to delete the file by name or by id (1 - name, 2 - id): ");
    String option = scanner.nextLine();
    String optionName;
    switch (option) {
      case "1" -> optionName = "name";
      case "2" -> optionName = "id";
      default -> throw new RuntimeException("Incorrect option value");
    }
    String optionValue = scanner.nextLine();
    outputStream.writeUTF("del");
    outputStream.writeUTF(optionName);
    outputStream.writeUTF(optionValue);
    System.out.println("The request was sent.");
    int statusCode = inputStream.readInt();
    if (statusCode == 404) {
      System.out.println("The response says that this file is not found!");
      return;
    }
    System.out.println("The response says that this file was deleted successfully!");
  }

  private static void saveFile(DataInputStream inputStream, DataOutputStream outputStream)
      throws IOException {
    System.out.print("Enter name of the file: ");
    String filename = scanner.nextLine();
    File file = new File(RESOURCE_PATH + filename);
    if (!file.exists()) {
      System.out.printf("File %s doesn't exist", filename);
      return;
    }
    System.out.print("Enter name of the file to be saved on server: ");
    String saveFilename = scanner.nextLine();
    if (saveFilename.isBlank()) {
      saveFilename = "custom_" + random.nextInt(1000) + "_" + System.currentTimeMillis();
    }
    outputStream.writeUTF("save"); // represents save action
    outputStream.writeInt((int) file.length());
    outputStream.write(Files.readAllBytes(file.toPath()));
    outputStream.writeUTF(saveFilename);
    System.out.println("The request was sent.");
    int fileId = inputStream.readInt();
    System.out.println("Response says that file is saved! ID = " + fileId);
  }
}
