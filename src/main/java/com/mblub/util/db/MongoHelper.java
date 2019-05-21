package com.mblub.util.db;

import static java.util.Arrays.asList;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientSettings.Builder;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoIterable;

import fr.javatic.mongo.jacksonCodec.JacksonCodecProvider;
import fr.javatic.mongo.jacksonCodec.ObjectMapperFactory;

public class MongoHelper {
  protected MongoClientSettings clientSettings;
  protected ServerAddress serverAddress;
  protected String serverHost;
  protected Integer serverPort;
  protected String connectionString;

  protected Supplier<ObjectMapper> objectMapperSupplier = ObjectMapperFactory::createObjectMapper;
  protected Function<MongoClientSettings, MongoClient> clientSettingsToClient = MongoClients::create;

  /**
   * @deprecated use {@link #getConnectionString()} instead
   */
  @Deprecated
  public Integer getServerPort() {
    return serverPort;
  }

  /**
   * @deprecated use {@link #setConnectionString(String)} instead
   */
  @Deprecated
  public void setServerPort(Integer serverPort) {
    this.serverPort = serverPort;
    serverAddress = null;
  }

  /**
   * @deprecated use {@link #withConnectionString(String)} instead
   */
  @Deprecated
  public MongoHelper withServerPort(Integer serverPort) {
    setServerPort(serverPort);
    return this;
  }

  /**
   * @deprecated use {@link #getConnectionString()} instead
   */
  @Deprecated
  public String getServerHost() {
    return serverHost;
  }

  /**
   * @deprecated use {@link #setConnectionString(String)} instead
   */
  @Deprecated
  public void setServerHost(String serverHost) {
    this.serverHost = serverHost;
  }

  /**
   * @deprecated use {@link #withConnectionString(String)} instead
   */
  @Deprecated
  public MongoHelper withServerHost(String serverHost) {
    setServerHost(serverHost);
    return this;
  }

  public String getConnectionString() {
    return connectionString;
  }

  public void setConnectionString(String connectionString) {
    this.connectionString = connectionString;
  }

  public MongoHelper withConnectionString(String connectionString) {
    setConnectionString(connectionString);
    return this;
  }

  public Supplier<ObjectMapper> getObjectMapperSupplier() {
    return objectMapperSupplier;
  }

  public void setObjectMapperSupplier(Supplier<ObjectMapper> objectMapperSupplier) {
    this.objectMapperSupplier = objectMapperSupplier;
  }

  public MongoHelper withObjectMapperSupplier(Supplier<ObjectMapper> objectMapperSupplier) {
    setObjectMapperSupplier(objectMapperSupplier);
    return this;
  }

  protected MongoClientSettings getClientSettings() {
    if (clientSettings == null) {
      CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
              CodecRegistries.fromProviders(new JacksonCodecProvider(objectMapperSupplier.get())));

      Builder builder = MongoClientSettings.builder().codecRegistry(codecRegistry);
      if (StringUtils.isEmpty(connectionString)) {
        builder = builder.applyToClusterSettings(b -> b.hosts(asList(getServerAddress())));
      } else {
        builder = builder.applyConnectionString(new ConnectionString(connectionString));
      }
      clientSettings = builder.build();
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

  public <TResult> Stream<TResult> sequentialStream(MongoIterable<TResult> findResult) {
    return stream(findResult, false);
  }

  public <TResult> Stream<TResult> parallelStream(MongoIterable<TResult> findResult) {
    return stream(findResult, false);
  }

  public <TResult> Stream<TResult> stream(MongoIterable<TResult> findResult, boolean isParallel) {
    return StreamSupport.stream(findResult.spliterator(), isParallel);
  }
}
