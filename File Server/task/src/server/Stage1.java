package server;

import java.util.HashSet;
import java.util.Set;

public class Stage1 {

  private final Set<String> files;
  private final Set<String> allowedFiles;

  public Stage1() {
    this.files = new HashSet<>();
    this.allowedFiles = new HashSet<>();
    for (int i = 1; i <= 10; i++) {
      this.allowedFiles.add("file" + i);
    }
  }

  public void add(String filename) {
    if (!allowedFiles.contains(filename) || files.contains(filename)) {
      System.out.println("Cannot add the file " + filename);
    } else {
      files.add(filename);
      System.out.println("The file " + filename + " added successfully");
    }
  }

  public void get(String filename) {
    if (!files.contains(filename)) {
      System.out.println("The file " + filename + " not found");
    } else {
      System.out.println("The file " + filename + " was sent");
    }
  }

  public void delete(String filename) {
    if (!files.contains(filename)) {
      System.out.println("The file " + filename + " not found");
    } else {
      System.out.println("The file " + filename + " was deleted");
      files.remove(filename);
    }
  }
}
