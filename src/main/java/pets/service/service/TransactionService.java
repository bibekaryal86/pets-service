package pets.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import pets.service.connector.TransactionConnector;
import pets.service.model.*;

import java.util.concurrent.CompletableFuture;

import static java.util.Collections.emptyList;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;
import static pets.service.utils.ObjectMapperProvider.objectMapper;
import static pets.service.utils.TransactionHelper.applyAllDetailsStatic;
import static pets.service.utils.TransactionHelper.applyFilters;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionConnector transactionConnector;
    private AccountService accountService;
    private final CategoryService categoryService;
    private final MerchantService merchantService;
    private final TransactionTypeService transactionTypeService;

    public TransactionService(TransactionConnector transactionConnector,
                              CategoryService categoryService,
                              MerchantService merchantService,
                              TransactionTypeService transactionTypeService) {
        this.transactionConnector = transactionConnector;
        this.categoryService = categoryService;
        this.merchantService = merchantService;
        this.transactionTypeService = transactionTypeService;
    }

    // to avoid circular dependency
    // maybe add a new service like TransactionHelperService to avoid it
    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public TransactionResponse getTransactionById(String username, String id, boolean applyAllDetails) {
        TransactionResponse transactionResponse;

        try {
            transactionResponse = transactionConnector.getTransactionById(id);
        } catch (Exception ex) {
            logger.error("Exception in Get Transaction by Id: {} | {} | {}", username, id, applyAllDetails);
            transactionResponse = response("Transaction Unavailable! Please Try Again!!!", ex);
        }

        if (!isEmpty(transactionResponse.getTransactions()) &&
                applyAllDetails) {
            applyAllDetails(username, transactionResponse);
        }

        return transactionResponse;
    }

    public TransactionResponse getTransactionsByUser(String username,
                                                     TransactionFilters transactionFilters,
                                                     boolean applyAllDetails) {
        TransactionResponse transactionResponse;

        try {
            transactionResponse = transactionConnector.getTransactionsByUser(username);
        } catch (Exception ex) {
            logger.error("Exception in Get Transactions By User: {} | {} | {}", username, transactionFilters, applyAllDetails);
            transactionResponse = response("Transactions Unavailable! Please Try Again!!!", ex);
        }

        if (!isEmpty(transactionResponse.getTransactions())) {
            if (applyAllDetails) {
                applyAllDetails(username, transactionResponse);
            }

            if (transactionFilters != null) {
                transactionResponse = applyFilters(transactionResponse, transactionFilters);
            }
        }

        return transactionResponse;
    }

    public TransactionResponse saveNewTransaction(String username,
                                                  TransactionRequest transactionRequest,
                                                  boolean applyAllDetails) {
        TransactionResponse transactionResponse;

        // check if user selected to enter a new merchant
        if (hasText(transactionRequest.getNewMerchant())) {
            // save merchant first and set it as merchant id in transaction request
            RefMerchantResponse refMerchantResponse = merchantService.saveNewMerchant(RefMerchantRequest.builder()
                    .username(username)
                    .description(transactionRequest.getNewMerchant())
                    .build());

            if (refMerchantResponse.getStatus() == null &&
                    !isEmpty(refMerchantResponse.getRefMerchants())) {
                String newMerchantId = refMerchantResponse.getRefMerchants().get(0).getId();
                logger.info("Save New Transaction, New Merchant Id: {} | {}", username, newMerchantId);
                transactionRequest.setMerchantId(newMerchantId);
            } else {
                logger.error("Error Saving New Merchant for Transaction: {} | {}", username, transactionRequest.getNewMerchant());
                return response("Save Transaction Unavailable! New Merchant Not Saved!!!", null);
            }
        }

        try {
            transactionResponse = transactionConnector.saveNewTransaction(transactionRequest);
        } catch (Exception ex) {
            logger.error("Exception in Save New Transaction: {} | {} | {}", username, transactionRequest, applyAllDetails);
            transactionResponse = response("Save Transaction Unavailable! Please Try Again!!!", ex);
        }

        if (!isEmpty(transactionResponse.getTransactions()) &&
                applyAllDetails) {
            applyAllDetails(username, transactionResponse);
        }

        return transactionResponse;
    }

    public TransactionResponse updateTransaction(String username, String id,
                                                 TransactionRequest transactionRequest,
                                                 boolean applyAllDetails) {
        TransactionResponse transactionResponse;

        // check if user selected to enter a new merchant
        if (hasText(transactionRequest.getNewMerchant())) {
            // save merchant first and set it as merchant id in transaction request
            RefMerchantResponse merchantResponse = merchantService.saveNewMerchant(RefMerchantRequest.builder()
                    .username(username)
                    .description(transactionRequest.getNewMerchant())
                    .build());

            if (merchantResponse.getStatus() == null &&
                    !isEmpty(merchantResponse.getRefMerchants())) {
                String newMerchantId = merchantResponse.getRefMerchants().get(0).getId();
                logger.info("Update Transaction, New Merchant Id: {} | {}", username, newMerchantId);
                transactionRequest.setMerchantId(newMerchantId);
            } else {
                logger.error("Error Saving New Merchant for Transaction: {} | {}", username, transactionRequest.getNewMerchant());
                return response("Update Transaction Unavailable! New Merchant Not Saved!!!", null);
            }
        }

        try {
            transactionResponse = transactionConnector.updateTransaction(id, transactionRequest);
        } catch (Exception ex) {
            logger.error("Exception in Update Transaction: {} | {} | {} | {}", username, id, transactionRequest, applyAllDetails);
            transactionResponse = response("Update Transaction Unavailable! Please Try Again!!!", null);
        }

        if (!isEmpty(transactionResponse.getTransactions()) &&
                applyAllDetails) {
            applyAllDetails(username, transactionResponse);
        }

        return transactionResponse;
    }

    public TransactionResponse deleteTransaction(String id) {
        try {
            return transactionConnector.deleteTransaction(id);
        } catch (Exception ex) {
            logger.error("Exception in Delete Transaction: {}", id);
            return response("Delete Transaction Unavailable! Please Try Again!!!", ex);
        }
    }

    public TransactionResponse deleteTransactionsByAccount(String accountId) {
        try {
            return transactionConnector.deleteTransactionsByAccount(accountId);
        } catch (Exception ex) {
            logger.error("Exception in Delete Transactions by Account: {}", accountId);
            return response("Delete Transaction by Account Unavailable! Please Try Again!!!", ex);
        }
    }

    private void applyAllDetails(String username, TransactionResponse transactionResponse) {
        CompletableFuture<AccountResponse> accountResponseCompletableFuture = accountService.getAccountsByUserFuture(username);
        CompletableFuture<RefCategoryResponse> refCategoryResponseCompletableFuture = categoryService.getAllCategoriesFuture();
        CompletableFuture<RefMerchantResponse> refMerchantResponseCompletableFuture = merchantService.getMerchantsByUserFuture(username);
        CompletableFuture<RefTransactionTypeResponse> refTransactionTypeResponseCompletableFuture = transactionTypeService.getAllTransactionTypesFuture();

        applyAllDetailsStatic(transactionResponse, accountResponseCompletableFuture, refCategoryResponseCompletableFuture,
                refMerchantResponseCompletableFuture, refTransactionTypeResponseCompletableFuture);
    }

    private TransactionResponse response(String errMsg, Exception ex) {
        if (ex instanceof HttpStatusCodeException) {
            try {
                if (((HttpStatusCodeException) ex).getStatusCode().is4xxClientError()) {
                    return responseException(errMsg, ex);
                }

                return objectMapper().readValue(((HttpStatusCodeException) ex).getResponseBodyAsString(),
                        TransactionResponse.class);
            } catch (Exception ex1) {
                logger.error("Exception Reading Http Exception: {}", errMsg, ex1);
                return responseException(errMsg, ex1);
            }
        } else {
            return responseException(errMsg, ex);
        }
    }

    private TransactionResponse responseException(String errMsg, Exception ex) {
        return TransactionResponse.builder()
                .transactions(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg(errMsg)
                        .message(ex == null ? null : ex.toString())
                        .build())
                .build();
    }
}
