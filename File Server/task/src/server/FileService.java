package server;

import static server.Server.RESOURCE_PATH;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import server.service.DeleteFile;
import server.service.GetFile;
import server.service.SaveFile;

public class FileService implements GetFile, DeleteFile, SaveFile, Serializable {

  private static final long serialVersionUID = 1l;

  private final FileIdTable fileIdTable = new FileIdTable();

  @Override
  public void deleteFile(Socket socket) {
    try (
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
      String option = dis.readUTF();
      String optionValue = dis.readUTF();
      if (option.equalsIgnoreCase("id")) {
        String result = fileIdTable.getFilenameById(Integer.valueOf(optionValue));
        if (result == null) {
          System.err.printf("File with ID %s not found", optionValue);
          dos.writeInt(404);
          return;
        }
        optionValue = result;
      }
      boolean isDeleted = Files.deleteIfExists(Path.of(RESOURCE_PATH + optionValue));
      if (!isDeleted) {
        System.err.printf("File %s doesn't exist on a server", optionValue);
        dos.writeInt(404);
        return;
      }
      dos.writeInt(200);
    } catch (Exception e) {
      System.err.println("Caught file delete exception: " + e.getMessage());
    } finally {
      try {
        socket.close();
      } catch (IOException e) {
        System.err.printf("Couldn't close socket for reason: %s", e.getMessage());
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void getFile(Socket socket) {
    try (
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
      String option = dis.readUTF();
      String optionValue = dis.readUTF();
      if (option.equalsIgnoreCase("id")) {
        String result = fileIdTable.getFilenameById(Integer.valueOf(optionValue));
        if (result == null) {
          System.err.printf("File with ID %s not found", optionValue);
          dos.writeInt(404);
          return;
        }
        optionValue = result;
      }
      Path filePath = Path.of(RESOURCE_PATH + optionValue);
      if (!Files.exists(filePath)) {
        System.err.printf("File %s doesn't exist on a server", optionValue);
        dos.writeInt(404);
        return;
      }
      dos.writeInt(200);
      byte[] content = Files.readAllBytes(filePath);
      dos.writeInt(content.length);
      dos.write(content);
    } catch (Exception e) {
      System.err.println("Caught get file exception: " + e.getMessage());
    } finally {
      try {
        socket.close();
      } catch (IOException e) {
        System.err.printf("Couldn't close socket for reason: %s", e.getMessage());
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void saveFile(Socket socket) {
    try (DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {
      int length = inputStream.readInt();
      byte[] content = inputStream.readNBytes(length);
      String filename = inputStream.readUTF();
      Files.write(Path.of(Server.RESOURCE_PATH + filename), content);
      int id = fileIdTable.generateFileId(filename);
      outputStream.writeInt(id);
    } catch (IOException e) {
      System.out.println("Exception: " + e.getMessage());
      throw new RuntimeException(e);
    } finally {
      try {
        socket.close();
      } catch (IOException e) {
        System.err.printf("Couldn't close socket for reason: %s", e.getMessage());
        throw new RuntimeException(e);
      }
    }
  }

  private void serializeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
  }

  private void deserializeObject(ObjectInputStream ois)
      throws IOException, ClassNotFoundException {
    ois.defaultReadObject();
    System.out.println("File ID table" + fileIdTable);
  }
}
