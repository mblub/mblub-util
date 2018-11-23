package com.mblub.util.db;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

import fr.javatic.mongo.jacksonCodec.JacksonCodecProvider;
import fr.javatic.mongo.jacksonCodec.ObjectMapperFactory;

public class MongoHelper {
  protected MongoClientOptions clientOptions;
  protected ServerAddress serverAddress;
  protected String serverHost;
  protected Integer serverPort;

  public Integer getServerPort() {
    return serverPort;
  }

  public void setServerPort(Integer serverPort) {
    this.serverPort = serverPort;
    serverAddress = null;
  }

  public MongoHelper withServerPort(Integer serverPort) {
    setServerPort(serverPort);
    return this;
  }

  public String getServerHost() {
    return serverHost;
  }

  public void setServerHost(String serverHost) {
    this.serverHost = serverHost;
  }

  public MongoHelper withServerHost(String serverHost) {
    setServerHost(serverHost);
    return this;
  }

  protected MongoClientOptions getClientOptions() {
    if (clientOptions == null) {
      CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
              CodecRegistries.fromProviders(new JacksonCodecProvider(ObjectMapperFactory.createObjectMapper())));

      clientOptions = MongoClientOptions.builder().codecRegistry(codecRegistry).build();
    }
    return clientOptions;
  }

  // TODO: do not assume localhost
  protected ServerAddress getServerAddress() {
    if (serverAddress == null) {
      serverAddress = new ServerAddress(serverHost == null ? ServerAddress.defaultHost() : serverHost,
              serverPort == null ? ServerAddress.defaultPort() : serverPort);
    }
    return serverAddress;
  }

  public MongoClient getClient() {
    return new MongoClient(getServerAddress(), getClientOptions());
  }
}
