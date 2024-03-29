package pets.service.utils;

import static pets.service.utils.LogMasker.maskDetails;

import java.io.IOException;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

@NoArgsConstructor
public class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {
  private final Logger requestLogger =
      LoggerFactory.getLogger("spring.web.client.MessageTracing.sent");
  private final Logger responseLogger =
      LoggerFactory.getLogger("spring.web.client.MessageTracing.received");

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    this.logRequest(request, body);
    long startTime = System.currentTimeMillis();

    ClientHttpResponse clientHttpResponse = execution.execute(request, body);

    long endTime = System.currentTimeMillis();
    this.logResponse(request, clientHttpResponse, endTime - startTime);

    return clientHttpResponse;
  }

  private void logRequest(HttpRequest request, byte[] body) {
    String maskedUri = maskDetails(request.getURI().toString());
    StringBuilder stringBuilder =
        new StringBuilder("Sending [")
            .append(request.getMethod())
            .append("] Request [")
            .append(maskedUri)
            .append("]");

    if (this.hasTextBody(request.getHeaders())) {
      stringBuilder.append(" [Headers] ").append(request.getHeaders());
    }

    if (body.length > 0) {
      stringBuilder.append(" [Body] ").append("[EXCLUDED]");
    }

    String requestLog = stringBuilder.toString();
    this.requestLogger.info(requestLog);
  }

  private void logResponse(
      HttpRequest request, ClientHttpResponse clientHttpResponse, long durationInMs) {
    String maskedUri = maskDetails(request.getURI().toString());

    try {
      StringBuilder stringBuilder =
          new StringBuilder("Received [")
              .append(clientHttpResponse.getStatusCode().value())
              .append("] Response [")
              .append(maskedUri)
              .append("]");

      HttpHeaders httpHeaders = clientHttpResponse.getHeaders();
      long contentLength = httpHeaders.getContentLength();

      if (contentLength != 0L) {
        if (this.hasTextBody(httpHeaders)) {
          stringBuilder.append(" [Headers] ").append(httpHeaders);
          stringBuilder.append(" [Body] ").append("[EXCLUDED]");
        } else {
          stringBuilder
              .append(" [Content Type] [ ")
              .append(httpHeaders.getContentType())
              .append(" ]");
          stringBuilder.append(" [Content Length] [ ").append(contentLength).append(" ]");
        }
      }

      stringBuilder.append(" [After] [ ").append(durationInMs).append(" ms]");
      String responseLog = stringBuilder.toString();
      this.responseLogger.info(responseLog);
    } catch (IOException ex) {
      this.responseLogger.error(
          "Failed to Log Response for {} Request to {}", request.getMethod(), maskedUri, ex);
    }
  }

  private boolean hasTextBody(HttpHeaders httpHeaders) {
    MediaType mediaType = httpHeaders.getContentType();
    if (mediaType == null) {
      return false;
    } else {
      return "text".equals(mediaType.getType())
          || "xml".equals(mediaType.getSubtype())
          || "json".equals(mediaType.getSubtype());
    }
  }
}
