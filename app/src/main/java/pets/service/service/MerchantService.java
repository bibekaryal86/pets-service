package pets.service.service;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.springframework.util.CollectionUtils.isEmpty;
import static pets.service.utils.MerchantHelper.*;
import static pets.service.utils.ObjectMapperProvider.objectMapper;

import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import pets.service.connector.MerchantConnector;
import pets.service.model.*;

@Service
public class MerchantService {

  private static final Logger logger = LoggerFactory.getLogger(MerchantService.class);
  private final MerchantConnector merchantConnector;
  private final TransactionService transactionService;
  // instead of Lazy implementation, maybe add a new MerchantHelperService class
  // in order to avoid cyclical dependency

  public MerchantService(
      MerchantConnector merchantConnector, @Lazy TransactionService transactionService) {
    this.merchantConnector = merchantConnector;
    this.transactionService = transactionService;
  }

  public RefMerchantResponse getMerchantById(String id) {
    try {
      RefMerchantResponse refMerchantResponse = merchantConnector.getMerchantById(id);
      setSystemDependentMerchants(refMerchantResponse);
      return refMerchantResponse;
    } catch (Exception ex) {
      logger.error("Exception in Get Merchant for Id: {}", id, ex);
      return response("Merchant Unavailable! Please Try Again!!!", null);
    }
  }

  public RefMerchantResponse getMerchantsByUser(
      String username, RefMerchantFilters refMerchantFilters) {
    RefMerchantResponse refMerchantResponse;

    try {
      refMerchantResponse = merchantConnector.getMerchantsByUser(username);
    } catch (Exception ex) {
      logger.error("Exception in Get Merchant for User: {} | {}", username, refMerchantFilters);
      refMerchantResponse = response("Merchants Unavailable! Please Try Again!!!", ex);
    }

    if (!isEmpty(refMerchantResponse.getRefMerchants())) {
      setSystemDependentMerchants(refMerchantResponse);
      refMerchantResponse.setRefMerchantsFilterList(
          getRefMerchantsFilterListByName(refMerchantResponse));

      TransactionResponse transactionResponse =
          transactionService.getTransactionsByUser(username, null, false);
      refMerchantResponse.setRefMerchantsNotUsedInTransactions(
          getRefMerchantsNotUsedInTransactions(
              refMerchantResponse.getRefMerchants(), transactionResponse.getTransactions()));

      if (refMerchantFilters != null) {
        refMerchantResponse =
            applyFilters(
                refMerchantResponse, refMerchantFilters, transactionResponse.getTransactions());
      }
    }

    return refMerchantResponse;
  }

  public CompletableFuture<RefMerchantResponse> getMerchantsByUserFuture(String username) {
    return CompletableFuture.supplyAsync(() -> getMerchantsByUser(username, null));
  }

  public RefMerchantResponse saveNewMerchant(RefMerchantRequest merchantRequest) {
    try {
      return merchantConnector.saveNewMerchant(merchantRequest);
    } catch (Exception ex) {
      logger.error("Exception in Save New Merchant: {}", merchantRequest, ex);
      return response("Save Merchant Unavailable! Please Try Again!!!", ex);
    }
  }

  public RefMerchantResponse updateMerchant(String id, RefMerchantRequest merchantRequest) {
    try {
      return merchantConnector.updateMerchant(id, merchantRequest);
    } catch (Exception ex) {
      logger.error("Exception in Update Merchant: {} | {}", id, merchantRequest, ex);
      return response("Update Merchant Unavailable! Please Try Again!!!", ex);
    }
  }

  public RefMerchantResponse deleteMerchant(String username, String id) {
    RefMerchantResponse refMerchantResponse;
    TransactionResponse transactionResponse =
        transactionService.getTransactionsByUser(username, null, false);

    if (transactionResponse.getStatus() == null) {
      Transaction usedTransaction =
          transactionResponse.getTransactions().stream()
              .filter(transaction -> transaction.getRefMerchant().getId().equals(id))
              .findFirst()
              .orElse(null);

      if (usedTransaction == null) {
        try {
          refMerchantResponse = merchantConnector.deleteMerchant(id);
        } catch (Exception ex) {
          logger.error("Exception in Delete Merchant: {}", id, ex);
          refMerchantResponse = response("Delete Merchant Unavailable! Please Try Again!!!", ex);
        }
      } else {
        logger.error("Delete Merchant Error, Merchant Used In Transactions: {} | {}", username, id);
        refMerchantResponse =
            response("Delete Merchant Unavailable! Merchant is Used in Transactions!!!", null);
      }
    } else {
      logger.error("Delete Merchant Error, Empty Transactions List: {} | {}", username, id);
      refMerchantResponse =
          response("Delete Merchant Unavailable! Error Retrieving Transactions!!!", null);
    }

    return refMerchantResponse;
  }

  private RefMerchantResponse response(String errMsg, Exception ex) {
    if (ex instanceof HttpStatusCodeException) {
      try {
        if (((HttpStatusCodeException) ex).getStatusCode().is4xxClientError()) {
          return responseException(errMsg, ex);
        }

        return objectMapper()
            .readValue(
                ((HttpStatusCodeException) ex).getResponseBodyAsString(),
                RefMerchantResponse.class);
      } catch (Exception ex1) {
        logger.error("Exception Reading Http Exception: {}", errMsg, ex1);
        return responseException(errMsg, ex1);
      }
    } else {
      return responseException(errMsg, ex);
    }
  }

  private RefMerchantResponse responseException(String errMsg, Exception ex) {
    return RefMerchantResponse.builder()
        .refMerchants(emptyList())
        .deleteCount(0L)
        .refMerchantsFilterList(emptySet())
        .status(Status.builder().errMsg(errMsg).message(ex == null ? null : ex.toString()).build())
        .build();
  }
}
