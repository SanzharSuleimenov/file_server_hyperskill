package server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FileIdTable implements Serializable {

  private static final long serialVersionUID = 1l;

  private final Map<Integer, String> table = new HashMap<>();
  private transient final Random random = new Random();

  public synchronized int generateFileId(String filename) {
    int fileId = random.nextInt(Integer.MAX_VALUE);
    table.put(fileId, filename);
    return fileId;
  }

  public String getFilenameById(Integer id) {
    return table.get(id);
  }

  @Override
  public String toString() {
    return "FileIdTable{" +
        "table=" + table +
        ", random=" + random +
        '}';
  }
}
