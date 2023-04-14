package pets.service.service;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static pets.service.utils.ObjectMapperProvider.objectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import pets.service.connector.BankConnector;
import pets.service.model.RefBank;
import pets.service.model.RefBankRequest;
import pets.service.model.RefBankResponse;
import pets.service.model.Status;

@Service
public class BankService {

  private static final Logger logger = LoggerFactory.getLogger(BankService.class);

  private final BankConnector bankConnector;

  public BankService(BankConnector bankConnector) {
    this.bankConnector = bankConnector;
  }

  public RefBankResponse getAllBanks() {
    try {
      return bankConnector.getAllBanks();
    } catch (Exception ex) {
      logger.error("Exception in Get All Banks", ex);
      return response("Banks Unavailable! Please Try Again!!!", ex);
    }
  }

  public RefBankResponse getBankById(String id) {
    RefBankResponse refBankResponse = getAllBanks();
    RefBank refBank;

    if (refBankResponse.getStatus() == null) {
      refBank =
          refBankResponse.getRefBanks().stream()
              .filter(refBank1 -> refBank1.getId().equals(id))
              .findFirst()
              .orElse(null);

      if (refBank == null) {
        logger.error("Bank Not Found for Id: {}", id);
        return response("Bank Unavailable! Please Try Again!!!", null);
      } else {
        return RefBankResponse.builder().refBanks(singletonList(refBank)).build();
      }
    } else {
      return refBankResponse;
    }
  }

  public RefBankResponse saveNewBank(RefBankRequest refBankRequest) {
    try {
      return bankConnector.saveNewBank(refBankRequest);
    } catch (Exception ex) {
      logger.error("Exception in Save New Bank: {}", refBankRequest, ex);
      return response("Save Bank Unavailable! Please Try Again!!!", ex);
    }
  }

  public RefBankResponse updateBank(String id, RefBankRequest refBankRequest) {
    try {
      return bankConnector.updateBank(id, refBankRequest);
    } catch (Exception ex) {
      logger.error("Exception in Update Bank: {} | {}", id, refBankRequest, ex);
      return response("Update Bank Unavailable! Please Try Again!!!", ex);
    }
  }

  public RefBankResponse deleteBank(String id) {
    try {
      return bankConnector.deleteBank(id);
    } catch (Exception ex) {
      logger.error("Exception in Delete Bank: {}", id, ex);
      return response("Delete Bank Unavailable! Please Try Again!!!", ex);
    }
  }

  private RefBankResponse response(String errMsg, Exception ex) {
    if (ex instanceof HttpStatusCodeException) {
      try {
        if (((HttpStatusCodeException) ex).getStatusCode().is4xxClientError()) {
          return responseException(errMsg, ex);
        }

        return objectMapper()
            .readValue(
                ((HttpStatusCodeException) ex).getResponseBodyAsString(), RefBankResponse.class);
      } catch (Exception ex1) {
        logger.error("Exception Reading Http Exception: {}", errMsg, ex1);
        return responseException(errMsg, ex1);
      }
    } else {
      return responseException(errMsg, ex);
    }
  }

  private RefBankResponse responseException(String errMsg, Exception ex) {
    return RefBankResponse.builder()
        .refBanks(emptyList())
        .deleteCount(0L)
        .status(Status.builder().errMsg(errMsg).message(ex == null ? null : ex.toString()).build())
        .build();
  }
}
