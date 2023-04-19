package pets.service.service;

import static java.util.Collections.emptyList;
import static pets.service.utils.AccountHelper.*;

import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Service;
import pets.service.connector.AccountConnector;
import pets.service.model.AccountResponse;
import pets.service.model.RefAccountTypeResponse;
import pets.service.model.RefBankResponse;
import pets.service.model.Status;
import pets.service.model.TransactionResponse;

@Service
public class TransactionServiceHelperService {
  // created to avoid circular dependence between account & transaction service

  private final AccountConnector accountConnector;
  private final AccountTypeService accountTypeService;
  private final BankService bankService;
  private final AccountServiceHelperService accountServiceHelperService;

  public TransactionServiceHelperService(
      AccountConnector accountConnector,
      AccountTypeService accountTypeService,
      BankService bankService,
      AccountServiceHelperService accountServiceHelperService) {
    this.accountConnector = accountConnector;
    this.accountTypeService = accountTypeService;
    this.bankService = bankService;
    this.accountServiceHelperService = accountServiceHelperService;
  }

  public CompletableFuture<AccountResponse> getAccountsByUserFuture(String username) {
    return CompletableFuture.supplyAsync(() -> getAccountsByUser(username));
  }

  private AccountResponse getAccountsByUser(String username) {
    try {
      AccountResponse accountResponse = accountConnector.getAccountsByUser(username);
      RefAccountTypeResponse refAccountTypeResponse = accountTypeService.getAllAccountTypes();
      RefBankResponse refBankResponse = bankService.getAllBanks();
      TransactionResponse transactionResponse =
          accountServiceHelperService.getTransactionsByUser(username);
      applyAllDetailsStatic(
          accountResponse,
          refAccountTypeResponse.getRefAccountTypes(),
          refBankResponse.getRefBanks());
      calculateCurrentBalanceStatic(accountResponse, transactionResponse.getTransactions());
      return accountResponse;
    } catch (Exception ex) {
      return AccountResponse.builder()
          .accounts(emptyList())
          .deleteCount(0L)
          .status(
              Status.builder()
                  .errMsg("Get Accounts By User Unavailable! Please Try Again!!!")
                  .message(ex.getMessage())
                  .build())
          .build();
    }
  }
}
