package com.mblub.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Stream;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

public class WebPageReader {

  private URL webPage;

  public URL getWebPage() {
    return webPage;
  }

  public void setWebPage(URL webPage) {
    this.webPage = webPage;
  }

  public WebPageReader withWebPage(URL webPage) {
    setWebPage(webPage);
    return this;
  }

  public Stream<String> getWebPageContent() {
    SSLContext sslContext;
    try {
      sslContext = new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true).build();
    } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
      String errorMessage = "Could not build SSLContext for sceneWebPage " + webPage;
      System.err.println(errorMessage);
      throw new UncheckedIOException(new IOException(errorMessage, e));
    }

    CloseableHttpClient client = HttpClients.custom().setSSLContext(sslContext)
            .setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
    HttpGet httpGet = new HttpGet(webPage.toString());
    httpGet.setHeader("Accept", "application/xml");

    HttpResponse response;
    try {
      response = client.execute(httpGet);
    } catch (IOException e) {
      String errorMessage = "Could not get HTTP response for web page " + webPage;
      System.err.println(errorMessage);
      throw new UncheckedIOException(new IOException(errorMessage, e));
    }
    int statusCode = response.getStatusLine().getStatusCode();
    if (statusCode != 200) {
      throw new UncheckedIOException(new IOException("Received status code " + statusCode
              + " attempting to get HTTP response for target url  for sceneWebPage " + webPage));
    }

    InputStream contentStream;
    try {
      contentStream = response.getEntity().getContent();
    } catch (UnsupportedOperationException | IOException e) {
      String errorMessage = "Could not get HTTP response content for target sceneWebPage " + webPage;
      System.err.println(errorMessage);
      throw new UncheckedIOException(new IOException(errorMessage, e));
    }

    return new BufferedReader(new InputStreamReader(contentStream, Charset.forName("UTF-8"))).lines();
  }
}
