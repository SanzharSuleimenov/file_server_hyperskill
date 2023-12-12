package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

  private final static String SENT_MESSAGE = "All files were sent!";

  public static void main(String[] args) {
    try (ServerSocket serverSocket = new ServerSocket(9090)) {
      System.out.println("Server started!");
      try (
          Socket socket = serverSocket.accept();
          DataInputStream inputStream = new DataInputStream(socket.getInputStream());
          DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {
        System.out.println("Received: " + inputStream.readUTF());
        outputStream.writeUTF(SENT_MESSAGE);
        System.out.println("Sent: " + SENT_MESSAGE);
      } catch (IOException e) {
        System.err.println("IOException reason: " + e.getMessage());
      }
    } catch (IOException e) {
      System.err.println("IOException reason: " + e.getMessage());
    } catch (Exception e) {
      System.err.println("Exception reason: " + e.getMessage());
    }
  }
}
