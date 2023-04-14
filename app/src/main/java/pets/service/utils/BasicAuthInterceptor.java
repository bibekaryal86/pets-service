package pets.service.utils;

import static pets.service.utils.Constants.BASIC_AUTH_PWD_PETSDATABASE;
import static pets.service.utils.Constants.BASIC_AUTH_USR_PETSDATABASE;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class BasicAuthInterceptor implements ClientHttpRequestInterceptor {
  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    Map<String, String> authConfig = getAuthConfig();
    request
        .getHeaders()
        .setBasicAuth(
            authConfig.get(BASIC_AUTH_USR_PETSDATABASE),
            authConfig.get(BASIC_AUTH_PWD_PETSDATABASE));
    return execution.execute(request, body);
  }

  private Map<String, String> getAuthConfig() {
    Map<String, String> authConfigMap = new HashMap<>();

    if (System.getProperty(BASIC_AUTH_USR_PETSDATABASE) != null) {
      // for running locally
      authConfigMap.put(
          BASIC_AUTH_USR_PETSDATABASE, System.getProperty(BASIC_AUTH_USR_PETSDATABASE));
      authConfigMap.put(
          BASIC_AUTH_PWD_PETSDATABASE, System.getProperty(BASIC_AUTH_PWD_PETSDATABASE));
    } else if (System.getenv(BASIC_AUTH_USR_PETSDATABASE) != null) {
      // for GCP
      authConfigMap.put(BASIC_AUTH_USR_PETSDATABASE, System.getenv(BASIC_AUTH_USR_PETSDATABASE));
      authConfigMap.put(BASIC_AUTH_PWD_PETSDATABASE, System.getenv(BASIC_AUTH_PWD_PETSDATABASE));
    }

    return authConfigMap;
  }
}
