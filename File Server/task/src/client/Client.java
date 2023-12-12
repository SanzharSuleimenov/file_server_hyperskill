package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

  private final static String SENT_MESSAGE = "Give me everything you have!";
  public static void main(String[] args) {
    try (
        Socket socket = new Socket("127.0.0.1", 9090);
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {
      System.out.println("Client started!");
      outputStream.writeUTF(SENT_MESSAGE);
      System.out.println("Sent: " + SENT_MESSAGE);
      System.out.println("Received: " + inputStream.readUTF());
    } catch (IOException e) {
      System.err.println("IOException reason: " + e.getMessage());
    } catch (Exception e) {
      System.err.println("Exception reason: " + e.getMessage());
    }
  }
}
