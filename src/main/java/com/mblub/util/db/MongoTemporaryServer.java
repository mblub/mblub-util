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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MongoTemporaryServer {
  private static final Logger LOG = LogManager.getLogger(MongoTemporaryServer.class);
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
  protected Path logRootPath;
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

  public Path getLogRootPath() {
    return logRootPath;
  }

  public void setLogRootPath(Path logRootPath) {
    this.logRootPath = logRootPath;
  }

  public MongoTemporaryServer withLogRootPath(Path logPath) {
    setLogRootPath(logPath);
    return this;
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
    LOG.debug("Creating temporary directory for mongo database");
    databasePath = Files.createTempDirectory("mongoTempDb");
    LOG.debug("Created temporary directory: " + databasePath);
    ProcessBuilder pb = new ProcessBuilder();
    Path logPath = logRootPath.resolve("mongodb-" + databasePort + ".log");
    Path sysoutPath = logRootPath.resolve("mongodb-sysout.log");
    pb.command(getMongoDaemon().toString(), "--port", databasePort.toString(), "--dbpath", databasePath.toString(),
            "--logpath", logPath.toString());
    pb.redirectOutput(sysoutPath.toFile());
    pb.redirectError(sysoutPath.toFile());
    mongoProcess = pb.start();
    LOG.debug("OS process for running temporary mongo database " + mongoProcess);
    if (mongoProcess.getClass().getName().equals("java.lang.UNIXProcess")) {
      /* get the PID on unix/linux systems */
      try {
        Field f = mongoProcess.getClass().getDeclaredField("pid");
        f.setAccessible(true);
        pid = Integer.toString(f.getInt(mongoProcess));
        LOG.debug("pid of temporary mongo: " + pid);
      } catch (Throwable e) {
      }
    }
  }

  public void stopServer() throws IOException, InterruptedException {
    LOG.debug("Killing mongo temporary database at pid " + pid + "...");
    ProcessBuilder pb = new ProcessBuilder("kill", pid);
    Process killProcess = pb.start();
    pb.redirectOutput(Redirect.INHERIT);
    killProcess.waitFor();
    LOG.debug("Kill signal delivered");
    mongoProcess.waitFor();
    LOG.debug("Finished waiting for mongoProcess (it has been killed)");
    deleteDatabaseFiles();
    LOG.debug("Deleted temporary database files");
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
