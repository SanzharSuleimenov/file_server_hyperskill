package server;

import static server.Server.RESOURCE_PATH;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FileIdTable implements Serializable {

  private static final long serialVersionUID = 1l;
  private final String SERIALIZED_FILENAME = "serialized-id-table.ser";

  private Map<String, String> table;
  private transient final Random random = new Random();

  public FileIdTable() {
    if (Files.exists(Path.of(RESOURCE_PATH + SERIALIZED_FILENAME))) {
      try (
          FileInputStream fis = new FileInputStream(RESOURCE_PATH + SERIALIZED_FILENAME);
          ObjectInputStream ois = new ObjectInputStream(fis)
      ) {
        this.table = (Map<String, String>) ois.readObject();
      } catch (IOException | ClassNotFoundException e) {
        System.err.println("ID Table deserialization exception: " + e.getMessage());
        throw new RuntimeException(e.getMessage());
      }
    } else {
      this.table = new HashMap<>();
    }
  }

  public synchronized String generateFileId(String filename) {
    String fileId = String.valueOf(random.nextLong(Long.MAX_VALUE));
    var result = table.put(fileId, filename);
    System.out.printf("filename: %s\n", filename);
    System.out.printf("Added k:v -> %s:%s\n", fileId, result);
    return fileId;
  }

  public String getFilenameById(String id) {
    return table.get(id);
  }

  public void serialize() {
    try (
        FileOutputStream fos = new FileOutputStream(RESOURCE_PATH + SERIALIZED_FILENAME);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos)) {
      oos.writeObject(table);
    } catch (IOException e) {
      System.err.println("ID Table serialization exception: " + e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }

  public void deserialize() {
    try (
        FileInputStream fis = new FileInputStream(RESOURCE_PATH + SERIALIZED_FILENAME);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis)
    ) {
      table = (Map<String, String>) ois.readObject();
    } catch (IOException e) {
      System.err.println("Couldn't deserialize ID Table: " + e.getMessage());
      throw new RuntimeException(e.getMessage());
    } catch (ClassNotFoundException e) {
      System.err.println("Class not found when deserializing: " + e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return "FileIdTable{" +
        "table=" + table +
        ", random=" + random +
        '}';
  }
}