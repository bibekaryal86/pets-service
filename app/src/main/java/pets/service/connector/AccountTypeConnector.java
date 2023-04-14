package pets.service.connector;

import static org.springframework.http.HttpMethod.*;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pets.service.model.RefAccountTypeRequest;
import pets.service.model.RefAccountTypeResponse;

@Component
public class AccountTypeConnector {

  private final RestTemplate restTemplate;
  private final String getAllAccountTypesUrl;
  private final String saveNewAccountTypeUrl;
  private final String updateAccountTypeUrl;
  private final String deleteAccountTypeUrl;

  public AccountTypeConnector(
      @Qualifier("restTemplate") RestTemplate restTemplate,
      String getAllAccountTypesUrl,
      String saveNewAccountTypeUrl,
      String updateAccountTypeUrl,
      String deleteAccountTypeUrl) {
    this.restTemplate = restTemplate;
    this.getAllAccountTypesUrl = getAllAccountTypesUrl;
    this.saveNewAccountTypeUrl = saveNewAccountTypeUrl;
    this.updateAccountTypeUrl = updateAccountTypeUrl;
    this.deleteAccountTypeUrl = deleteAccountTypeUrl;
  }

  @Cacheable(value = "accountTypes", unless = "#result==null")
  public RefAccountTypeResponse getAllAccountTypes() {
    ResponseEntity<RefAccountTypeResponse> responseEntity =
        restTemplate.getForEntity(getAllAccountTypesUrl, RefAccountTypeResponse.class);
    return responseEntity.getBody();
  }

  @CacheEvict(value = "accountTypes", allEntries = true, beforeInvocation = true)
  public RefAccountTypeResponse saveNewAccountType(
      @NonNull final RefAccountTypeRequest refAccountTypeRequest) {
    ResponseEntity<RefAccountTypeResponse> responseEntity =
        restTemplate.exchange(
            saveNewAccountTypeUrl,
            POST,
            new HttpEntity<>(refAccountTypeRequest, null),
            RefAccountTypeResponse.class);

    return responseEntity.getBody();
  }

  @CacheEvict(value = "accountTypes", allEntries = true, beforeInvocation = true)
  public RefAccountTypeResponse updateAccountType(
      @NonNull final String id, @NonNull final RefAccountTypeRequest refAccountTypeRequest) {
    String url =
        UriComponentsBuilder.fromHttpUrl(updateAccountTypeUrl).buildAndExpand(id).toString();

    ResponseEntity<RefAccountTypeResponse> responseEntity =
        restTemplate.exchange(
            url, PUT, new HttpEntity<>(refAccountTypeRequest, null), RefAccountTypeResponse.class);

    return responseEntity.getBody();
  }

  @CacheEvict(value = "accountTypes", allEntries = true, beforeInvocation = true)
  public RefAccountTypeResponse deleteAccountType(@NonNull final String id) {
    String url =
        UriComponentsBuilder.fromHttpUrl(deleteAccountTypeUrl).buildAndExpand(id).toString();

    ResponseEntity<RefAccountTypeResponse> responseEntity =
        restTemplate.exchange(
            url, DELETE, new HttpEntity<>(null, null), RefAccountTypeResponse.class);

    return responseEntity.getBody();
  }
}
