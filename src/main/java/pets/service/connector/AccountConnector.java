package pets.service.connector;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pets.service.model.AccountRequest;
import pets.service.model.AccountResponse;

import static org.springframework.http.HttpMethod.*;

@Component
public class AccountConnector {

    private final RestTemplate restTemplate;
    private final String getAccountByIdUrl;
    private final String getAccountsByUserUrl;
    private final String saveNewAccountUrl;
    private final String updateAccountPutUrl;
    private final String deleteAccountUrl;

    public AccountConnector(@Qualifier("restTemplate") RestTemplate restTemplate,
                            String getAccountByIdUrl,
                            String getAccountsByUserUrl,
                            String saveNewAccountUrl,
                            String updateAccountPutUrl,
                            String deleteAccountUrl) {
        this.restTemplate = restTemplate;
        this.getAccountByIdUrl = getAccountByIdUrl;
        this.getAccountsByUserUrl = getAccountsByUserUrl;
        this.saveNewAccountUrl = saveNewAccountUrl;
        this.updateAccountPutUrl = updateAccountPutUrl;
        this.deleteAccountUrl = deleteAccountUrl;
    }

    public AccountResponse getAccountById(@NonNull final String id) {
        String url = UriComponentsBuilder
                .fromHttpUrl(getAccountByIdUrl)
                .buildAndExpand(id)
                .toString();

        ResponseEntity<AccountResponse> responseEntity = restTemplate
                .getForEntity(url, AccountResponse.class);

        return responseEntity.getBody();
    }

    public AccountResponse getAccountsByUser(@NonNull final String username) {
        String url = UriComponentsBuilder
                .fromHttpUrl(getAccountsByUserUrl)
                .buildAndExpand(username)
                .toString();

        ResponseEntity<AccountResponse> responseEntity = restTemplate
                .getForEntity(url, AccountResponse.class);

        return responseEntity.getBody();
    }

    public AccountResponse saveNewAccount(@NonNull final AccountRequest accountRequest) {
        ResponseEntity<AccountResponse> responseEntity = restTemplate
                .exchange(saveNewAccountUrl,
                        POST,
                        new HttpEntity<>(accountRequest, null),
                        AccountResponse.class);

        return responseEntity.getBody();
    }

    public AccountResponse updateAccount(@NonNull final String id,
                                         @NonNull final AccountRequest accountRequest) {
        String url = UriComponentsBuilder
                .fromHttpUrl(updateAccountPutUrl)
                .buildAndExpand(id)
                .toString();

        ResponseEntity<AccountResponse> responseEntity = restTemplate
                .exchange(url,
                        PUT,
                        new HttpEntity<>(accountRequest, null),
                        AccountResponse.class);

        return responseEntity.getBody();
    }

    public AccountResponse deleteAccount(@NonNull final String id) {
        String url = UriComponentsBuilder
                .fromHttpUrl(deleteAccountUrl)
                .buildAndExpand(id)
                .toString();

        ResponseEntity<AccountResponse> responseEntity = restTemplate
                .exchange(url,
                        DELETE,
                        new HttpEntity<>(null, null),
                        AccountResponse.class);

        return responseEntity.getBody();
    }
}
