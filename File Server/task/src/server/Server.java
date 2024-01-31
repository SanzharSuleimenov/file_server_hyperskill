package server;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server implements Serializable {

  public static final String RESOURCE_PATH;

  static {
    RESOURCE_PATH = System.getProperty("user.dir") +
//        File.separator + "File Server" +
//        File.separator + "task" +
        File.separator + "src" +
        File.separator + "server" +
        File.separator + "data" +
        File.separator;

    File dir = new File(RESOURCE_PATH);
    dir.mkdirs();
  }

  private final FileService fileService = new FileService();

  public static void main(String[] args) throws InterruptedException {
    Server server = new Server();
    server.menu();
    server.fileService.serializeFileServiceData();
  }

  private void menu() throws InterruptedException {
    ExecutorService executor = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors());
    try (ServerSocket serverSocket =
        new ServerSocket(8080, 50, InetAddress.getByName("localhost"))) {
      System.out.println("Server started!");
      while (!serverSocket.isClosed()) {
        Socket socket = serverSocket.accept();
        String method = new DataInputStream(socket.getInputStream()).readUTF();
        switch (method) {
          case "save" -> executor.execute(() -> fileService.saveFile(socket));
          case "get" -> executor.execute(() -> fileService.getFile(socket));
          case "del" -> executor.execute(() -> fileService.deleteFile(socket));
          case "exit" -> {
            try {
              socket.close();
              serverSocket.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      }
    } catch (IOException e) {
      System.err.println("IOException reason: " + e.getMessage());
    } catch (Exception e) {
      System.err.println("Exception reason: " + e.getMessage());
    } finally {
      executor.shutdown();
      executor.awaitTermination(2, TimeUnit.SECONDS);
    }
  }
}
