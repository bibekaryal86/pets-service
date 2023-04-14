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
import pets.service.model.RefCategoryTypeRequest;
import pets.service.model.RefCategoryTypeResponse;

@Component
public class CategoryTypeConnector {

  private final RestTemplate restTemplate;
  private final String getAllCategoryTypesUrl;
  private final String saveNewCategoryTypeUrl;
  private final String updateCategoryTypeUrl;
  private final String deleteCategoryTypeUrl;

  public CategoryTypeConnector(
      @Qualifier("restTemplate") RestTemplate restTemplate,
      String getAllCategoryTypesUrl,
      String saveNewCategoryTypeUrl,
      String updateCategoryTypeUrl,
      String deleteCategoryTypeUrl) {
    this.restTemplate = restTemplate;
    this.getAllCategoryTypesUrl = getAllCategoryTypesUrl;
    this.saveNewCategoryTypeUrl = saveNewCategoryTypeUrl;
    this.updateCategoryTypeUrl = updateCategoryTypeUrl;
    this.deleteCategoryTypeUrl = deleteCategoryTypeUrl;
  }

  @Cacheable(value = "categoryTypes", unless = "#result==null")
  public RefCategoryTypeResponse getAllCategoryTypes() {
    ResponseEntity<RefCategoryTypeResponse> responseEntity =
        restTemplate.getForEntity(getAllCategoryTypesUrl, RefCategoryTypeResponse.class);

    return responseEntity.getBody();
  }

  @CacheEvict(value = "categoryTypes", allEntries = true, beforeInvocation = true)
  public RefCategoryTypeResponse saveNewCategoryType(
      @NonNull final RefCategoryTypeRequest refCategoryTypeRequest) {
    ResponseEntity<RefCategoryTypeResponse> responseEntity =
        restTemplate.exchange(
            saveNewCategoryTypeUrl,
            POST,
            new HttpEntity<>(refCategoryTypeRequest, null),
            RefCategoryTypeResponse.class);

    return responseEntity.getBody();
  }

  @CacheEvict(value = "categoryTypes", allEntries = true, beforeInvocation = true)
  public RefCategoryTypeResponse updateCategoryType(
      @NonNull final String id, @NonNull final RefCategoryTypeRequest refCategoryTypeRequest) {
    String url =
        UriComponentsBuilder.fromHttpUrl(updateCategoryTypeUrl).buildAndExpand(id).toString();

    ResponseEntity<RefCategoryTypeResponse> responseEntity =
        restTemplate.exchange(
            url,
            PUT,
            new HttpEntity<>(refCategoryTypeRequest, null),
            RefCategoryTypeResponse.class);

    return responseEntity.getBody();
  }

  @CacheEvict(value = "categoryTypes", allEntries = true, beforeInvocation = true)
  public RefCategoryTypeResponse deleteCategoryType(@NonNull final String id) {

    String url =
        UriComponentsBuilder.fromHttpUrl(deleteCategoryTypeUrl).buildAndExpand(id).toString();

    ResponseEntity<RefCategoryTypeResponse> responseEntity =
        restTemplate.exchange(
            url, DELETE, new HttpEntity<>(null, null), RefCategoryTypeResponse.class);

    return responseEntity.getBody();
  }
}
