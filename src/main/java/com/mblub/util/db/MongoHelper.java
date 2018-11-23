package com.mblub.util.db;

import static java.util.Arrays.asList;

import java.util.function.Function;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import fr.javatic.mongo.jacksonCodec.JacksonCodecProvider;
import fr.javatic.mongo.jacksonCodec.ObjectMapperFactory;

public class MongoHelper {
  protected MongoClientSettings clientSettings;
  protected ServerAddress serverAddress;
  protected String serverHost;
  protected Integer serverPort;

  protected Function<MongoClientSettings, MongoClient> clientSettingsToClient = MongoClients::create;

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

  protected MongoClientSettings getClientSettings() {
    if (clientSettings == null) {
      CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
              CodecRegistries.fromProviders(new JacksonCodecProvider(ObjectMapperFactory.createObjectMapper())));

      clientSettings = MongoClientSettings.builder().codecRegistry(codecRegistry)
              .applyToClusterSettings(b -> b.hosts(asList(getServerAddress()))).build();
    }
    return clientSettings;
  }

  protected ServerAddress getServerAddress() {
    if (serverAddress == null) {
      serverAddress = new ServerAddress(serverHost == null ? ServerAddress.defaultHost() : serverHost,
              serverPort == null ? ServerAddress.defaultPort() : serverPort);
    }
    return serverAddress;
  }

  public MongoClient getClient() {
    return clientSettingsToClient.apply(getClientSettings());
  }
}
