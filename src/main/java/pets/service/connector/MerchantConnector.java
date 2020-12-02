package pets.service.connector;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pets.service.model.RefMerchantRequest;
import pets.service.model.RefMerchantResponse;

import static org.springframework.http.HttpMethod.*;

@Component
public class MerchantConnector {

    private final RestTemplate restTemplate;
    private final String getMerchantByIdUrl;
    private final String getMerchantsByUserUrl;
    private final String saveNewMerchantUrl;
    private final String updateMerchantUrl;
    private final String deleteMerchantUrl;

    public MerchantConnector(@Qualifier("restTemplate") RestTemplate restTemplate,
                             String getMerchantByIdUrl,
                             String getMerchantsByUserUrl,
                             String saveNewMerchantUrl,
                             String updateMerchantUrl,
                             String deleteMerchantUrl) {
        this.restTemplate = restTemplate;
        this.getMerchantByIdUrl = getMerchantByIdUrl;
        this.getMerchantsByUserUrl = getMerchantsByUserUrl;
        this.saveNewMerchantUrl = saveNewMerchantUrl;
        this.updateMerchantUrl = updateMerchantUrl;
        this.deleteMerchantUrl = deleteMerchantUrl;
    }

    public RefMerchantResponse getMerchantById(@NonNull final String id) {
        String url = UriComponentsBuilder
                .fromHttpUrl(getMerchantByIdUrl)
                .buildAndExpand(id)
                .toString();

        ResponseEntity<RefMerchantResponse> responseEntity = restTemplate
                .getForEntity(url, RefMerchantResponse.class);

        return responseEntity.getBody();
    }

    public RefMerchantResponse getMerchantsByUser(@NonNull final String username) {
        String url = UriComponentsBuilder
                .fromHttpUrl(getMerchantsByUserUrl)
                .buildAndExpand(username)
                .toString();

        ResponseEntity<RefMerchantResponse> responseEntity = restTemplate
                .getForEntity(url, RefMerchantResponse.class);

        return responseEntity.getBody();
    }

    public RefMerchantResponse saveNewMerchant(@NonNull final RefMerchantRequest merchantRequest) {
        ResponseEntity<RefMerchantResponse> responseEntity = restTemplate
                .exchange(saveNewMerchantUrl,
                        POST,
                        new HttpEntity<>(merchantRequest, null),
                        RefMerchantResponse.class);

        return responseEntity.getBody();
    }

    public RefMerchantResponse updateMerchant(@NonNull final String id,
                                              @NonNull final RefMerchantRequest merchantRequest) {
        String url = UriComponentsBuilder
                .fromHttpUrl(updateMerchantUrl)
                .buildAndExpand(id)
                .toString();

        ResponseEntity<RefMerchantResponse> responseEntity = restTemplate
                .exchange(url,
                        PUT,
                        new HttpEntity<>(merchantRequest, null),
                        RefMerchantResponse.class);

        return responseEntity.getBody();
    }

    public RefMerchantResponse deleteMerchant(@NonNull final String id) {
        String url = UriComponentsBuilder
                .fromHttpUrl(deleteMerchantUrl)
                .buildAndExpand(id)
                .toString();

        ResponseEntity<RefMerchantResponse> responseEntity = restTemplate
                .exchange(url,
                        DELETE,
                        new HttpEntity<>(null, null),
                        RefMerchantResponse.class);

        return responseEntity.getBody();
    }
}
