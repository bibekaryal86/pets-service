package pets.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import pets.service.connector.AccountConnector;
import pets.service.model.*;

import java.util.concurrent.CompletableFuture;

import static java.util.Collections.emptyList;
import static org.springframework.util.CollectionUtils.isEmpty;
import static pets.service.utils.AccountHelper.*;
import static pets.service.utils.ObjectMapperProvider.objectMapper;

@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private final AccountConnector accountConnector;
    private final AccountTypeService accountTypeService;
    private final BankService bankService;
    private TransactionService transactionService;

    public AccountService(AccountConnector accountConnector,
                          AccountTypeService accountTypeService,
                          BankService bankService) {
        this.accountConnector = accountConnector;
        this.accountTypeService = accountTypeService;
        this.bankService = bankService;
    }

    // to avoid circular dependency
    // maybe add a new class like AccountHelperService to avoid it
    @Autowired
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public AccountResponse getAccountById(String username, String id, boolean applyAllDetails) {
        AccountResponse accountResponse;

        try {
            accountResponse = accountConnector.getAccountById(id);
        } catch (Exception ex) {
            logger.error("Exception in Get Account by Id: {} | {} | {}", username, id, applyAllDetails);
            accountResponse = response("Account Unavailable! Please Try Again!!!", ex);
        }

        if (!isEmpty(accountResponse.getAccounts())) {
            if (applyAllDetails) {
                applyAllDetails(accountResponse);
            }

            calculateCurrentBalance(username, accountResponse);
        }

        return accountResponse;
    }

    public AccountResponse getAccountsByUser(String username,
                                             AccountFilters accountFilters,
                                             boolean applyAllDetails) {
        AccountResponse accountResponse;

        try {
            accountResponse = accountConnector.getAccountsByUser(username);
        } catch (Exception ex) {
            logger.error("Exception in Get Accounts By User: {} | {} | {}", username, accountFilters, applyAllDetails);
            accountResponse = response("Accounts Unavailable! Please Try Again!!!", ex);
        }

        if (!isEmpty(accountResponse.getAccounts())) {
            if (accountFilters != null) {
                accountResponse = applyFilters(accountResponse, accountFilters);
            }

            if (applyAllDetails) {
                applyAllDetails(accountResponse);
            }

            calculateCurrentBalance(username, accountResponse);
        }

        return accountResponse;
    }

    public CompletableFuture<AccountResponse> getAccountsByUserFuture(String username) {
        return CompletableFuture.supplyAsync(() -> getAccountsByUser(username, null, true));
    }

    public AccountResponse saveNewAccount(String username,
                                          AccountRequest accountRequest,
                                          boolean applyAllDetails) {
        AccountResponse accountResponse;

        try {
            accountResponse = accountConnector.saveNewAccount(accountRequest);
        } catch (Exception ex) {
            logger.error("Exception in Save New Account: {} | {} | {}", username, accountRequest, applyAllDetails);
            accountResponse = response("Save Account Unavailable! Please Try Again!!!", ex);
        }

        if (!isEmpty(accountResponse.getAccounts())) {
            if (applyAllDetails) {
                applyAllDetails(accountResponse);
            }

            calculateCurrentBalance(username, accountResponse);
        }

        return accountResponse;
    }

    public AccountResponse updateAccount(String username, String id,
                                         AccountRequest accountRequest, boolean applyAllDetails) {
        AccountResponse accountResponse;

        try {
            accountResponse = accountConnector.updateAccount(id, accountRequest);
        } catch (Exception ex) {
            logger.error("Exception in Update Account: {} | {} | {} | {}", username, id, accountRequest, applyAllDetails);
            accountResponse = response("Update Account Unavailable! Please Try Again!!!", ex);
        }

        if (!isEmpty(accountResponse.getAccounts())) {
            if (applyAllDetails) {
                applyAllDetails(accountResponse);
            }

            calculateCurrentBalance(username, accountResponse);
        }

        return accountResponse;
    }

    public AccountResponse deleteAccount(String username, String id) {
        AccountResponse accountResponse;

        try {
            accountResponse = accountConnector.deleteAccount(id);

            if (accountResponse.getDeleteCount().intValue() > 0) {
                TransactionResponse transactionResponse = transactionService.deleteTransactionsByAccount(id);

                if (transactionResponse.getStatus() != null) {
                    accountResponse = responseException("Account Deleted! But Error Deleting Transactions!! Please Try Again!!!", null);
                }
            }
        } catch (Exception ex) {
            logger.error("Exception in Delete Account: {} | {}", username, id);
            accountResponse = response("Delete Account Unavailable! Please Try Again!!!", ex);
        }

        return accountResponse;
    }

    private void applyAllDetails(AccountResponse accountResponse) {
        RefAccountTypeResponse refAccountTypeResponse = accountTypeService.getAllAccountTypes();
        RefBankResponse refBankResponse = bankService.getAllBanks();
        applyAllDetailsStatic(accountResponse, refAccountTypeResponse.getRefAccountTypes(),
                refBankResponse.getRefBanks());
    }

    private void calculateCurrentBalance(String username, AccountResponse accountResponse) {
        TransactionResponse transactionResponse = transactionService.getTransactionsByUser(username, null, false);
        calculateCurrentBalanceStatic(accountResponse, transactionResponse.getTransactions());
    }

    private AccountResponse response(String errMsg, Exception ex) {
        if (ex instanceof HttpStatusCodeException) {
            try {
                if (((HttpStatusCodeException) ex).getStatusCode().is4xxClientError()) {
                    return responseException(errMsg, ex);
                }

                return objectMapper().readValue(((HttpStatusCodeException) ex).getResponseBodyAsString(),
                        AccountResponse.class);
            } catch (Exception ex1) {
                logger.error("Exception Reading Http Exception: {}", errMsg, ex1);
                return responseException(errMsg, ex1);
            }
        } else {
            return responseException(errMsg, ex);
        }
    }

    private AccountResponse responseException(String errMsg, Exception ex) {
        return AccountResponse.builder()
                .accounts(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg(errMsg)
                        .message(ex == null ? null : ex.toString())
                        .build())
                .build();
    }
}
