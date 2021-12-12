package pets.service.connector;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pets.service.model.TransactionRequest;
import pets.service.model.TransactionResponse;

import static org.springframework.http.HttpMethod.*;

@Component
public class TransactionConnector {

    private final RestTemplate restTemplate;
    private final String getTransactionByIdUrl;
    private final String getTransactionsByUserUrl;
    private final String saveNewTransactionUrl;
    private final String updateTransactionPutUrl;
    private final String deleteTransactionUrl;
    private final String deleteTransactionsByAccountUrl;

    public TransactionConnector(@Qualifier("restTemplate") RestTemplate restTemplate,
                                String getTransactionByIdUrl,
                                String getTransactionsByUserUrl,
                                String saveNewTransactionUrl,
                                String updateTransactionPutUrl,
                                String deleteTransactionUrl,
                                String deleteTransactionsByAccountUrl) {
        this.restTemplate = restTemplate;
        this.getTransactionByIdUrl = getTransactionByIdUrl;
        this.getTransactionsByUserUrl = getTransactionsByUserUrl;
        this.saveNewTransactionUrl = saveNewTransactionUrl;
        this.updateTransactionPutUrl = updateTransactionPutUrl;
        this.deleteTransactionUrl = deleteTransactionUrl;
        this.deleteTransactionsByAccountUrl = deleteTransactionsByAccountUrl;
    }

    public TransactionResponse getTransactionById(@NonNull final String id) {
        String url = UriComponentsBuilder
                .fromHttpUrl(getTransactionByIdUrl)
                .buildAndExpand(id)
                .toString();

        ResponseEntity<TransactionResponse> responseEntity = restTemplate
                .getForEntity(url, TransactionResponse.class);

        return responseEntity.getBody();
    }

    public TransactionResponse getTransactionsByUser(@NonNull final String username) {
        String url = UriComponentsBuilder
                .fromHttpUrl(getTransactionsByUserUrl)
                .buildAndExpand(username)
                .toString();

        ResponseEntity<TransactionResponse> responseEntity = restTemplate
                .getForEntity(url, TransactionResponse.class);

        return responseEntity.getBody();
    }

    public TransactionResponse saveNewTransaction(@NonNull final TransactionRequest transactionRequest) {
        ResponseEntity<TransactionResponse> responseEntity = restTemplate
                .exchange(saveNewTransactionUrl,
                        POST,
                        new HttpEntity<>(transactionRequest, null),
                        TransactionResponse.class);

        return responseEntity.getBody();
    }

    public TransactionResponse updateTransaction(@NonNull final String id,
                                                 @NonNull final TransactionRequest transactionRequest) {
        String url = UriComponentsBuilder
                .fromHttpUrl(updateTransactionPutUrl)
                .buildAndExpand(id)
                .toString();

        ResponseEntity<TransactionResponse> responseEntity = restTemplate
                .exchange(url,
                        PUT,
                        new HttpEntity<>(transactionRequest, null),
                        TransactionResponse.class);

        return responseEntity.getBody();
    }

    public TransactionResponse deleteTransaction(@NonNull final String id) {
        String url = UriComponentsBuilder
                .fromHttpUrl(deleteTransactionUrl)
                .buildAndExpand(id)
                .toString();

        ResponseEntity<TransactionResponse> responseEntity = restTemplate
                .exchange(url,
                        DELETE,
                        new HttpEntity<>(null, null),
                        TransactionResponse.class);

        return responseEntity.getBody();
    }

    public TransactionResponse deleteTransactionsByAccount(@NonNull final String accountId) {
        String url = UriComponentsBuilder
                .fromHttpUrl(deleteTransactionsByAccountUrl)
                .buildAndExpand(accountId)
                .toString();

        ResponseEntity<TransactionResponse> responseEntity = restTemplate
                .exchange(url,
                        DELETE,
                        new HttpEntity<>(null, null),
                        TransactionResponse.class);

        return responseEntity.getBody();
    }
}
