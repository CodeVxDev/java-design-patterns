package dev.codevx.patterns.decorator.jdk;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

// Run without a build: java src/main/java/dev/codevx/patterns/decorator/jdk/Demo.java
public class Demo {
  public static void main(String[] args) throws IOException {
    File f = File.createTempFile("decorator", ".gz");
    f.deleteOnExit();
    try (var out = new GZIPOutputStream(new FileOutputStream(f))) {
      out.write("read through three stacked streams".getBytes());
    }

    // Each one is an InputStream wrapping another InputStream: buffering, then unzipping, around the file.
    try (InputStream in =
        new GZIPInputStream(
            new BufferedInputStream(
                new FileInputStream(f)))) {
      System.out.println("streams: " + new String(in.readAllBytes()));
    }

    // A decorator outside java.io: the same List, with a lock taken around every call.
    List<String> list = Collections.synchronizedList(new ArrayList<>());
    list.add("order-1");
    System.out.println("synchronizedList adds and reads like any list: " + list);

    // Looks the same, but it refuses writes: it controls access, so it is a proxy, not a decorator.
    List<String> readOnly = Collections.unmodifiableList(list);
    try {
      readOnly.add("order-2");
    } catch (UnsupportedOperationException e) {
      System.out.println("unmodifiableList refused the write: a proxy, not a decorator");
    }
  }
}
