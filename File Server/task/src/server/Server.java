package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

  private final static String RESOURCE_PATH;

  static {
    RESOURCE_PATH = System.getProperty("user.dir") +
        File.separator + "src" +
        File.separator + "server" +
        File.separator + "data" +
        File.separator;
  }

  public static void main(String[] args) {
    try (ServerSocket serverSocket = new ServerSocket(8080, 50,
        InetAddress.getByName("localhost"))) {
      File dir = new File(RESOURCE_PATH);
      dir.mkdirs();
      System.out.println("Server started!");
      boolean isActive = true;
      while (isActive) {
        try (
            Socket socket = serverSocket.accept();
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {

          String option = inputStream.readUTF().toLowerCase();
          switch (option) {
            case "put" -> createFile(inputStream, outputStream);
            case "get" -> getFile(inputStream, outputStream);
            case "delete" -> deleteFile(inputStream, outputStream);
            case "exit" -> isActive = false;
          }
        } catch (IOException e) {
          System.err.println("IOException reason: " + e.getMessage());
        }
      }
    } catch (IOException e) {
      System.err.println("IOException reason: " + e.getMessage());
    } catch (Exception e) {
      System.err.println("Exception reason: " + e.getMessage());
    }
  }

  private static void createFile(DataInputStream inputStream, DataOutputStream outputStream)
      throws IOException {
    String filename = inputStream.readUTF();
    String content = inputStream.readUTF();
    File file = new File(RESOURCE_PATH + filename);
    boolean createdNew = file.createNewFile();
    if (createdNew) {
      try (FileWriter fileWriter = new FileWriter(file)) {
        fileWriter.write(content);
        outputStream.writeInt(200);
      } catch (Exception e) {
        outputStream.writeInt(403);
        System.err.println("Exception: " + e.getMessage());
      }
    } else {
      outputStream.writeInt(403);
    }
  }

  private static void getFile(DataInputStream inputStream, DataOutputStream outputStream)
      throws IOException {
    String filename = inputStream.readUTF();
    File file = new File(RESOURCE_PATH + filename);
    if (file.canRead()) {
      try (Scanner scanner = new Scanner(file)) {
        String content = scanner.nextLine();
        outputStream.writeInt(200);
        outputStream.writeUTF(content);
      } catch (Exception e) {
        System.err.println("Exception: " + e.getMessage());
        outputStream.writeInt(404);
      }
    } else {
      outputStream.writeInt(404);
    }
  }

  private static void deleteFile(DataInputStream inputStream, DataOutputStream outputStream)
      throws IOException {
    String filename = inputStream.readUTF();
    File file = new File(RESOURCE_PATH + filename);
    if (file.isFile()) {
      try {
        boolean isDeleted = file.delete();
        outputStream.writeInt(isDeleted ? 200 : 404);
      } catch (Exception e) {
        System.err.println("File delete exception: " + e.getMessage());
        outputStream.writeInt(404);
      }
    } else {
      outputStream.writeInt(404);
    }

  }
}
