package pets.service.service;

import static java.util.Collections.emptyList;

import org.springframework.stereotype.Service;
import pets.service.connector.TransactionConnector;
import pets.service.model.Status;
import pets.service.model.TransactionResponse;

@Service
public class AccountServiceHelperService {
  // this class is to break circular dependency in transaction & account service

  private final TransactionConnector transactionConnector;

  public AccountServiceHelperService(TransactionConnector transactionConnector) {
    this.transactionConnector = transactionConnector;
  }

  public TransactionResponse deleteTransactionsByAccount(String accountId) {
    try {
      return transactionConnector.deleteTransactionsByAccount(accountId);
    } catch (Exception ex) {
      return TransactionResponse.builder()
          .transactions(emptyList())
          .deleteCount(0L)
          .status(
              Status.builder()
                  .errMsg("Delete Transactions by Account Unavailable! Please Try Again!!!")
                  .message(ex.getMessage())
                  .build())
          .build();
    }
  }

  public TransactionResponse getTransactionsByUser(String username) {
    try {
      return transactionConnector.getTransactionsByUser(username);
    } catch (Exception ex) {
      return TransactionResponse.builder()
          .transactions(emptyList())
          .deleteCount(0L)
          .status(
              Status.builder()
                  .errMsg("Get Transactions By User Unavailable! Please Try Again!!!")
                  .message(ex.getMessage())
                  .build())
          .build();
    }
  }
}
