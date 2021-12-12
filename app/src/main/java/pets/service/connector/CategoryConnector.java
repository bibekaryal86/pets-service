package pets.service.connector;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pets.service.model.RefCategoryRequest;
import pets.service.model.RefCategoryResponse;

import static org.springframework.http.HttpMethod.*;

@Component
public class CategoryConnector {

    private final RestTemplate restTemplate;
    private final String getAllCategoriesUrl;
    private final String saveNewCategoryUrl;
    private final String updateCategoryUrl;
    private final String deleteCategoryUrl;

    public CategoryConnector(@Qualifier("restTemplate") RestTemplate restTemplate,
                             String getAllCategoriesUrl,
                             String saveNewCategoryUrl,
                             String updateCategoryUrl,
                             String deleteCategoryUrl) {
        this.restTemplate = restTemplate;
        this.getAllCategoriesUrl = getAllCategoriesUrl;
        this.saveNewCategoryUrl = saveNewCategoryUrl;
        this.updateCategoryUrl = updateCategoryUrl;
        this.deleteCategoryUrl = deleteCategoryUrl;
    }

    @Cacheable(value = "categories", unless = "#result==null")
    public RefCategoryResponse getAllCategories() {
        ResponseEntity<RefCategoryResponse> responseEntity = restTemplate
                .getForEntity(getAllCategoriesUrl, RefCategoryResponse.class);

        return responseEntity.getBody();
    }

    @CacheEvict(value = "categories", allEntries = true, beforeInvocation = true)
    public RefCategoryResponse saveNewCategory(@NonNull final RefCategoryRequest refCategoryRequest) {
        ResponseEntity<RefCategoryResponse> responseEntity = restTemplate
                .exchange(saveNewCategoryUrl,
                        POST,
                        new HttpEntity<>(refCategoryRequest, null),
                        RefCategoryResponse.class);

        return responseEntity.getBody();
    }

    @CacheEvict(value = "categories", allEntries = true, beforeInvocation = true)
    public RefCategoryResponse updateCategory(@NonNull final String id,
                                              @NonNull final RefCategoryRequest refCategoryRequest) {
        String url = UriComponentsBuilder
                .fromHttpUrl(updateCategoryUrl)
                .buildAndExpand(id)
                .toString();

        ResponseEntity<RefCategoryResponse> responseEntity = restTemplate
                .exchange(url,
                        PUT,
                        new HttpEntity<>(refCategoryRequest, null),
                        RefCategoryResponse.class);

        return responseEntity.getBody();
    }

    @CacheEvict(value = "categories", allEntries = true, beforeInvocation = true)
    public RefCategoryResponse deleteCategory(@NonNull final String id) {
        String url = UriComponentsBuilder
                .fromHttpUrl(deleteCategoryUrl)
                .buildAndExpand(id)
                .toString();

        ResponseEntity<RefCategoryResponse> responseEntity = restTemplate
                .exchange(url,
                        DELETE,
                        new HttpEntity<>(null, null),
                        RefCategoryResponse.class);

        return responseEntity.getBody();
    }
}
