package com.mblub.util.db;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.lang.reflect.Field;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Pattern;

public class MongoTemporaryServer {
  protected static final Pattern PID_LOG_PATTERN = Pattern.compile("MongoDB starting : pid=([0-9]*) ");

  // TODO: create an integration test and remove this
  public static void main(String[] args) throws IOException, InterruptedException {
    new MongoTemporaryServer().withMongoHome(Paths.get("/Applications/mongodb/mongodb-osx-x86_64-4.0.4"))
            .withDatabasePort(27018).run();
  }

  // TODO: create an integration test and remove this
  protected void run() throws IOException, InterruptedException {
    startServer();
    Thread.sleep(22000L);
    stopServer();
  }

  protected Path mongoHome;
  protected Integer databasePort;
  protected String pid;
  protected Path databasePath;
  protected Process mongoProcess;
  protected Path mongoDaemon;

  public Path getMongoHome() {
    return mongoHome;
  }

  public void setMongoHome(Path mongoHome) {
    this.mongoHome = mongoHome;
    this.mongoDaemon = null;
  }

  public MongoTemporaryServer withMongoHome(Path mongoHome) {
    setMongoHome(mongoHome);
    return this;
  }

  public Integer getDatabasePort() {
    return databasePort;
  }

  public void setDatabasePort(Integer databasePort) {
    this.databasePort = databasePort;
  }

  public MongoTemporaryServer withDatabasePort(Integer databasePort) {
    setDatabasePort(databasePort);
    return this;
  }

  public String getPid() {
    return pid;
  }

  public Path getDatabasePath() {
    return databasePath;
  }

  public Process getMongoProcess() {
    return mongoProcess;
  }

  public Path getMongoDaemon() {
    if (mongoDaemon == null) {
      mongoDaemon = mongoHome.resolve("bin").resolve("mongod");
    }
    return mongoDaemon;
  }

  public void startServer() throws IOException {
    System.out.println("in startServer");
    databasePath = Files.createTempDirectory("mongoTempDb");
    System.out.println("temp dbPath: " + databasePath);
    ProcessBuilder pb = new ProcessBuilder();
    pb.command(getMongoDaemon().toString(), "--port", databasePort.toString(), "--dbpath", databasePath.toString());
    pb.redirectOutput(Redirect.INHERIT);
    mongoProcess = pb.start();
    System.out.println("started process " + mongoProcess);
    if (mongoProcess.getClass().getName().equals("java.lang.UNIXProcess")) {
      /* get the PID on unix/linux systems */
      try {
        Field f = mongoProcess.getClass().getDeclaredField("pid");
        f.setAccessible(true);
        pid = Integer.toString(f.getInt(mongoProcess));
      } catch (Throwable e) {
      }
    }
  }

  public void stopServer() throws IOException, InterruptedException {
    System.out.println("killing " + pid + "...");
    ProcessBuilder pb = new ProcessBuilder("kill", pid);
    Process killProcess = pb.start();
    pb.redirectOutput(Redirect.INHERIT);
    killProcess.waitFor();
    System.out.println("after kill");
    mongoProcess.waitFor();
    System.out.println("after mongoProcess waitFor");
    deleteDatabaseFiles();
    System.out.println("after deleting database files");
  }

  protected void deleteDatabaseFiles() {
    try {
      Files.walkFileTree(databasePath, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          Files.delete(file);
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
          if (e == null) {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
          }
          // directory iteration failed
          throw e;
        }
      });
    } catch (IOException e) {
      throw new RuntimeException("Failed to delete database files at " + databasePath, e);
    }
  }
}
