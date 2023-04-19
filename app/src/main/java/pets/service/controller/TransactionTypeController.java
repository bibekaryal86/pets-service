package pets.service.controller;

import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pets.service.model.RefTransactionTypeRequest;
import pets.service.model.RefTransactionTypeResponse;
import pets.service.model.Status;
import pets.service.service.TransactionTypeService;

@RestController
@RequestMapping("/transactiontypes/{username}")
public class TransactionTypeController {

  private static final Logger logger = LoggerFactory.getLogger(TransactionTypeController.class);
  private final TransactionTypeService transactionTypeService;

  public TransactionTypeController(TransactionTypeService transactionTypeService) {
    this.transactionTypeService = transactionTypeService;
  }

  @GetMapping(value = "/id", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefTransactionTypeResponse> getTransactionTypeById(
      @PathVariable("username") String username, @RequestParam("id") String id) {
    logger.info("Get Transaction Type By Id: {} | {}", username, id);

    if (!hasText(username) && !hasText(id)) {
      return response("Error Retrieving Transaction Type: Invalid Request! Please Try Again!!!");
    } else {
      return response(transactionTypeService.getTransactionTypeById(id));
    }
  }

  @GetMapping(value = "/transactiontype", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefTransactionTypeResponse> getAllTransactionTypes(
      @PathVariable("username") String username) {
    logger.info("Get All Transaction Types: {}", username);

    if (!hasText(username)) {
      return response("Error Retrieving Transaction Types: Invalid Request! Please Try Again!!!");
    } else {
      return response(transactionTypeService.getAllTransactionTypes());
    }
  }

  @Hidden
  @PostMapping(value = "/transactiontype", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefTransactionTypeResponse> saveNewTransactionType(
      @PathVariable("username") String username,
      @RequestBody RefTransactionTypeRequest transactionTypeRequest) {
    logger.info("Save New Transaction Type: {} | {}", username, transactionTypeRequest);

    if (!hasText(username)
        || transactionTypeRequest == null
        || !hasText(transactionTypeRequest.getDescription())) {
      return response("Error Saving Transaction Type: Invalid Request! Please Try Again!!!");
    } else {
      return response(transactionTypeService.saveNewTransactionType(transactionTypeRequest));
    }
  }

  @Hidden
  @PutMapping(value = "/transactiontype", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefTransactionTypeResponse> updateTransactionType(
      @PathVariable("username") String username,
      @RequestParam("id") String id,
      @RequestBody RefTransactionTypeRequest transactionTypeRequest) {
    logger.info("Update Transaction Type: {} | {} | {}", username, id, transactionTypeRequest);

    if (!hasText(username) || !hasText(id) || transactionTypeRequest == null) {
      return response("Error Updating Transaction Type: Invalid Request! Please Try Again!!!");
    } else {
      return response(transactionTypeService.updateTransactionType(id, transactionTypeRequest));
    }
  }

  @Hidden
  @DeleteMapping(value = "/transactiontype", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefTransactionTypeResponse> deleteTransactionType(
      @PathVariable("username") String username, @RequestParam("id") String id) {
    logger.info("Delete Transaction Type: {} | {}", username, id);

    if (!hasText(username) || !hasText(id)) {
      return response("Error Deleting Transaction Type: Invalid Request! Please Try Again!!!");
    } else {
      return response(transactionTypeService.deleteTransactionType(id));
    }
  }

  private ResponseEntity<RefTransactionTypeResponse> response(
      RefTransactionTypeResponse refTransactionTypeResponse) {
    if (refTransactionTypeResponse.getStatus() == null) {
      return new ResponseEntity<>(refTransactionTypeResponse, OK);
    } else {
      return new ResponseEntity<>(refTransactionTypeResponse, SERVICE_UNAVAILABLE);
    }
  }

  private ResponseEntity<RefTransactionTypeResponse> response(String errMsg) {
    return new ResponseEntity<>(
        RefTransactionTypeResponse.builder()
            .refTransactionTypes(emptyList())
            .deleteCount(0L)
            .status(Status.builder().errMsg(errMsg).build())
            .build(),
        BAD_REQUEST);
  }
}
