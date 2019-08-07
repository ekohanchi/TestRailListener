package com.project.test.testrailintegration.api.client.method;

import static com.project.test.testrailintegration.config.Config.API_KEY;
import static com.project.test.testrailintegration.config.Config.API_PATH;
import static com.project.test.testrailintegration.config.Config.HOST;
import static com.project.test.testrailintegration.config.Config.PATH;
import static com.project.test.testrailintegration.config.Config.PORT;
import static com.project.test.testrailintegration.config.Config.PROTOCOL;
import static com.project.test.testrailintegration.config.Config.USER;
import static com.project.test.testrailintegration.core.LogUtilities.logInfoMessage;
import static java.lang.String.format;

import com.project.test.testrailintegration.exception.ApiException;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;

public abstract class AbstractRailRequest {

  protected String executeGet(String command) {
    try {
      HttpGet request = new HttpGet(getUri(command));
      return execute(request);
    } catch (AuthenticationException | URISyntaxException | IOException e) {
      throw new ApiException(e);
    }
  }

  protected String executePost(String command, Object body) {
    try {
      HttpPost request = new HttpPost(getUri(command));
      String json = new Gson().toJson(body);
      request.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
      logInfoMessage(format("Send %s, with json=%s", request, json));
      return execute(request);
    } catch (AuthenticationException | URISyntaxException | IOException e) {
      throw new ApiException(e);
    }
  }

  private String execute(HttpUriRequest request)
      throws IOException, AuthenticationException {
    request.addHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
    addAuthentication(request);
    CloseableHttpClient client = HttpClients.createDefault();
    return client.execute(request, new BasicResponseHandler());
  }

  private void addAuthentication(HttpUriRequest request) throws AuthenticationException {
    request.addHeader(
        new BasicScheme().authenticate(new UsernamePasswordCredentials(USER, API_KEY), request,
            new BasicHttpContext()));
  }

  private URI getUri(String command) throws URISyntaxException {
    String query = API_PATH + command;
    return new URIBuilder().setScheme(PROTOCOL).setPort(PORT).setHost(HOST).setPath(PATH)
        .setCustomQuery(query)
        .build();
  }
}