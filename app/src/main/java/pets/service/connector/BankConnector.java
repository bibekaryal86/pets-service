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
import pets.service.model.RefBankRequest;
import pets.service.model.RefBankResponse;

import static org.springframework.http.HttpMethod.*;

@Component
public class BankConnector {

    private final RestTemplate restTemplate;
    private final String getAllBanksUrl;
    private final String saveNewBankUrl;
    private final String updateBankUrl;
    private final String deleteBankUrl;

    public BankConnector(@Qualifier("restTemplate") RestTemplate restTemplate,
                         String getAllBanksUrl,
                         String saveNewBankUrl,
                         String updateBankUrl,
                         String deleteBankUrl) {
        this.restTemplate = restTemplate;
        this.getAllBanksUrl = getAllBanksUrl;
        this.saveNewBankUrl = saveNewBankUrl;
        this.updateBankUrl = updateBankUrl;
        this.deleteBankUrl = deleteBankUrl;
    }

    @Cacheable(value = "banks", unless = "#result==null")
    public RefBankResponse getAllBanks() {
        ResponseEntity<RefBankResponse> responseEntity = restTemplate
                .getForEntity(getAllBanksUrl, RefBankResponse.class);

        return responseEntity.getBody();
    }

    @CacheEvict(value = "banks", allEntries = true, beforeInvocation = true)
    public RefBankResponse saveNewBank(@NonNull final RefBankRequest refBankRequest) {
        ResponseEntity<RefBankResponse> responseEntity = restTemplate
                .exchange(saveNewBankUrl,
                        POST,
                        new HttpEntity<>(refBankRequest, null),
                        RefBankResponse.class);

        return responseEntity.getBody();
    }

    @CacheEvict(value = "banks", allEntries = true, beforeInvocation = true)
    public RefBankResponse updateBank(@NonNull final String id, @NonNull final RefBankRequest refBankRequest) {
        String url = UriComponentsBuilder
                .fromHttpUrl(updateBankUrl)
                .buildAndExpand(id)
                .toString();

        ResponseEntity<RefBankResponse> responseEntity = restTemplate
                .exchange(url,
                        PUT,
                        new HttpEntity<>(refBankRequest, null),
                        RefBankResponse.class);

        return responseEntity.getBody();
    }

    @CacheEvict(value = "banks", allEntries = true, beforeInvocation = true)
    public RefBankResponse deleteBank(@NonNull final String id) {
        String url = UriComponentsBuilder
                .fromHttpUrl(deleteBankUrl)
                .buildAndExpand(id)
                .toString();

        ResponseEntity<RefBankResponse> responseEntity = restTemplate
                .exchange(url,
                        DELETE,
                        new HttpEntity<>(null, null),
                        RefBankResponse.class);

        return responseEntity.getBody();
    }
}
