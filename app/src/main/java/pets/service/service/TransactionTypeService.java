package pets.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import pets.service.connector.TransactionTypeConnector;
import pets.service.model.RefTransactionType;
import pets.service.model.RefTransactionTypeRequest;
import pets.service.model.RefTransactionTypeResponse;
import pets.service.model.Status;

import java.util.concurrent.CompletableFuture;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static pets.service.utils.ObjectMapperProvider.objectMapper;

@Service
public class TransactionTypeService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionTypeService.class);

    private final TransactionTypeConnector transactionTypeConnector;

    public TransactionTypeService(TransactionTypeConnector transactionTypeConnector) {
        this.transactionTypeConnector = transactionTypeConnector;
    }

    public RefTransactionTypeResponse getAllTransactionTypes() {
        try {
            return transactionTypeConnector.getAllTransactionTypes();
        } catch (Exception ex) {
            logger.error("Exception in Get All Transaction Types", ex);
            return response("Transaction Types Unavailable! Please Try Again!!!", ex);
        }
    }

    public CompletableFuture<RefTransactionTypeResponse> getAllTransactionTypesFuture() {
        return CompletableFuture.supplyAsync(this::getAllTransactionTypes);
    }

    public RefTransactionTypeResponse getTransactionTypeById(String id) {
        RefTransactionTypeResponse refTransactionTypesResponse = getAllTransactionTypes();
        RefTransactionType transactionType;

        if (refTransactionTypesResponse.getStatus() == null) {
            transactionType = refTransactionTypesResponse.getRefTransactionTypes().stream()
                    .filter(refTransactionType -> refTransactionType.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (transactionType == null) {
                logger.error("Transaction Type Not Found for Id: {}", id);
                return response("Transaction Type Unavailable! Please Try Again!!!", null);
            } else {
                return RefTransactionTypeResponse.builder()
                        .refTransactionTypes(singletonList(transactionType))
                        .build();
            }
        } else {
            return refTransactionTypesResponse;
        }
    }

    public RefTransactionTypeResponse saveNewTransactionType(RefTransactionTypeRequest refTransactionTypeRequest) {
        try {
            return transactionTypeConnector.saveNewTransactionType(refTransactionTypeRequest);
        } catch (Exception ex) {
            logger.error("Exception in Save New Transaction Type: {}", refTransactionTypeRequest, ex);
            return response("Save Transaction Type Unavailable! Please Try Again!!!", ex);
        }
    }

    public RefTransactionTypeResponse updateTransactionType(String id, RefTransactionTypeRequest refTransactionTypeRequest) {
        try {
            return transactionTypeConnector.updateTransactionType(id, refTransactionTypeRequest);
        } catch (Exception ex) {
            logger.error("Exception in Update Transaction Type: {} | {}", id, refTransactionTypeRequest, ex);
            return response("Update Transaction Type Unavailable! Please Try Again!!!", ex);
        }
    }

    public RefTransactionTypeResponse deleteTransactionType(String id) {
        try {
            return transactionTypeConnector.deleteTransactionType(id);
        } catch (Exception ex) {
            logger.error("Exception in Delete Transaction Type: {}", id);
            return response("Delete Transaction Type Unavailable! Please Try Again!!!", ex);
        }
    }

    private RefTransactionTypeResponse response(String errMsg, Exception ex) {
        if (ex instanceof HttpStatusCodeException) {
            try {
                if (((HttpStatusCodeException) ex).getStatusCode().is4xxClientError()) {
                    return responseException(errMsg, ex);
                }

                return objectMapper().readValue(((HttpStatusCodeException) ex).getResponseBodyAsString(),
                        RefTransactionTypeResponse.class);
            } catch (Exception ex1) {
                logger.error("Exception Reading Http Exception: {}", errMsg, ex1);
                return responseException(errMsg, ex1);
            }
        } else {
            return responseException(errMsg, ex);
        }
    }

    private RefTransactionTypeResponse responseException(String errMsg, Exception ex) {
        return RefTransactionTypeResponse.builder()
                .refTransactionTypes(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg(errMsg)
                        .message(ex == null ? null : ex.toString())
                        .build())
                .build();
    }
}
