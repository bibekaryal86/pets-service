package pets.service.controller;

import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pets.service.model.Status;
import pets.service.model.TransactionFilters;
import pets.service.model.TransactionRequest;
import pets.service.model.TransactionResponse;
import pets.service.service.TransactionService;

@RestController
@RequestMapping("/transactions/{username}")
public class TransactionController {

  private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
  private final TransactionService transactionService;

  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @GetMapping(value = "/id", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<TransactionResponse> getTransactionById(
      @PathVariable("username") String username, @RequestParam("id") String id) {
    logger.info("Get Transaction By Id: {} | {}", username, id);

    if (!hasText(username) && !hasText(id)) {
      return response("Error Retrieving Transaction: Invalid Request! Please Try Again!!!");
    } else {
      return response(transactionService.getTransactionById(username, id, true));
    }
  }

  @PostMapping(value = "/user", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<TransactionResponse> getTransactionsByUser(
      @PathVariable("username") String username,
      @RequestBody(required = false) TransactionFilters transactionFilters) {
    logger.info("Get Transactions By User: {} | {}", username, transactionFilters);

    if (!hasText(username)) {
      return response("Error Retrieving Transactions: Invalid Request! Please Try Again!!!");
    } else {
      return response(transactionService.getTransactionsByUser(username, transactionFilters, true));
    }
  }

  @PostMapping(value = "/transaction", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<TransactionResponse> saveNewTransaction(
      @PathVariable("username") String username,
      @RequestBody TransactionRequest transactionRequest) {
    logger.info("Save New Transaction: {} | {}", username, transactionRequest);

    if (!hasText(username)
        || transactionRequest == null
        || !hasText(transactionRequest.getAccountId())
        || !hasText(transactionRequest.getTypeId())
        || !hasText(transactionRequest.getCategoryId())
        || (!hasText(transactionRequest.getMerchantId())
            && !hasText(transactionRequest.getNewMerchant()))
        || !hasText(transactionRequest.getUsername())
        || !hasText(transactionRequest.getDate())) {
      return response("Error Saving Transaction: Invalid Request! Please Try Again!!!");
    } else {
      return response(transactionService.saveNewTransaction(username, transactionRequest, true));
    }
  }

  @PutMapping(value = "/transaction", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<TransactionResponse> updateTransaction(
      @PathVariable("username") String username,
      @RequestParam("id") String id,
      @RequestBody TransactionRequest transactionRequest) {
    logger.info("Update Transaction: {} | {} | {}", username, id, transactionRequest);

    if (!hasText(username) || !hasText(id) || transactionRequest == null) {
      return response("Error Updating Transaction: Invalid Request! Please Try Again!!!");
    } else {
      return response(transactionService.updateTransaction(username, id, transactionRequest, true));
    }
  }

  @DeleteMapping(value = "/transaction", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<TransactionResponse> deleteTransaction(
      @PathVariable("username") String username, @RequestParam("id") String id) {
    logger.info("Delete Transaction: {} | {}", username, id);

    if (!hasText(username) || !hasText(id)) {
      return response("Error Deleting Transaction: Invalid Request! Please Try Again!!!");
    } else {
      return response(transactionService.deleteTransaction(id));
    }
  }

  private ResponseEntity<TransactionResponse> response(TransactionResponse transactionResponse) {
    if (transactionResponse.getStatus() == null) {
      return new ResponseEntity<>(transactionResponse, OK);
    } else {
      return new ResponseEntity<>(transactionResponse, SERVICE_UNAVAILABLE);
    }
  }

  private ResponseEntity<TransactionResponse> response(String errMsg) {
    return new ResponseEntity<>(
        TransactionResponse.builder()
            .transactions(emptyList())
            .deleteCount(0L)
            .status(Status.builder().errMsg(errMsg).build())
            .build(),
        BAD_REQUEST);
  }
}
