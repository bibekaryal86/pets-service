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
import pets.service.model.RefTransactionTypeRequest;
import pets.service.model.RefTransactionTypeResponse;

import static org.springframework.http.HttpMethod.*;

@Component
public class TransactionTypeConnector {

    private final RestTemplate restTemplate;
    private final String getAllTransactionTypesUrl;
    private final String saveNewTransactionTypeUrl;
    private final String updateTransactionTypeUrl;
    private final String deleteTransactionTypeUrl;

    public TransactionTypeConnector(@Qualifier("restTemplate") RestTemplate restTemplate,
                                    String getAllTransactionTypesUrl,
                                    String saveNewTransactionTypeUrl,
                                    String updateTransactionTypeUrl,
                                    String deleteTransactionTypeUrl) {
        this.restTemplate = restTemplate;
        this.getAllTransactionTypesUrl = getAllTransactionTypesUrl;
        this.saveNewTransactionTypeUrl = saveNewTransactionTypeUrl;
        this.updateTransactionTypeUrl = updateTransactionTypeUrl;
        this.deleteTransactionTypeUrl = deleteTransactionTypeUrl;
    }

    @Cacheable(value = "transactionTypes", unless = "#result==null")
    public RefTransactionTypeResponse getAllTransactionTypes() {
        ResponseEntity<RefTransactionTypeResponse> responseEntity = restTemplate
                .getForEntity(getAllTransactionTypesUrl, RefTransactionTypeResponse.class);

        return responseEntity.getBody();
    }

    @CacheEvict(value = "transactionTypes", allEntries = true)
    public RefTransactionTypeResponse saveNewTransactionType(@NonNull final RefTransactionTypeRequest refTransactionTypeRequest) {
        ResponseEntity<RefTransactionTypeResponse> responseEntity = restTemplate
                .exchange(saveNewTransactionTypeUrl,
                        POST,
                        new HttpEntity<>(refTransactionTypeRequest, null),
                        RefTransactionTypeResponse.class);

        return responseEntity.getBody();
    }

    @CacheEvict(value = "transactionTypes", allEntries = true)
    public RefTransactionTypeResponse updateTransactionType(@NonNull final String id, @NonNull final RefTransactionTypeRequest refTransactionTypeRequest) {
        String url = UriComponentsBuilder
                .fromHttpUrl(updateTransactionTypeUrl)
                .buildAndExpand(id)
                .toString();

        ResponseEntity<RefTransactionTypeResponse> responseEntity = restTemplate
                .exchange(url,
                        PUT,
                        new HttpEntity<>(refTransactionTypeRequest, null),
                        RefTransactionTypeResponse.class);

        return responseEntity.getBody();
    }

    @CacheEvict(value = "transactionTypes", allEntries = true)
    public RefTransactionTypeResponse deleteTransactionType(@NonNull final String id) {
        String url = UriComponentsBuilder
                .fromHttpUrl(deleteTransactionTypeUrl)
                .buildAndExpand(id)
                .toString();

        ResponseEntity<RefTransactionTypeResponse> responseEntity = restTemplate
                .exchange(url,
                        DELETE,
                        new HttpEntity<>(null, null),
                        RefTransactionTypeResponse.class);

        return responseEntity.getBody();
    }
}
