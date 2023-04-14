package pets.service.controller;

import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pets.service.model.RefBankRequest;
import pets.service.model.RefBankResponse;
import pets.service.model.Status;
import pets.service.service.BankService;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/banks/{username}")
public class BankController {

  private static final Logger logger = LoggerFactory.getLogger(BankController.class);
  private final BankService bankService;

  public BankController(BankService bankService) {
    this.bankService = bankService;
  }

  @GetMapping(value = "/id", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefBankResponse> getBankById(
      @PathVariable("username") String username, @RequestParam("id") String id) {
    logger.info("Get Bank By Id: {} | {}", username, id);

    if (!hasText(username) && !hasText(id)) {
      return response("Error Retrieving Bank: Invalid Request! Please Try Again!!!");
    } else {
      return response(bankService.getBankById(id));
    }
  }

  @GetMapping(value = "/bank", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefBankResponse> getAllBanks(@PathVariable("username") String username) {
    logger.info("Get All Banks: {}", username);

    if (!hasText(username)) {
      return response("Error Retrieving Banks: Invalid Request! Please Try Again!!!");
    } else {
      return response(bankService.getAllBanks());
    }
  }

  @ApiIgnore
  @PostMapping(value = "/bank", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefBankResponse> saveNewBank(
      @PathVariable("username") String username, @RequestBody RefBankRequest bankRequest) {
    logger.info("Save New Bank: {} | {}", username, bankRequest);

    if (!hasText(username) || bankRequest == null || !hasText(bankRequest.getDescription())) {
      return response("Error Saving Bank: Invalid Request! Please Try Again!!!");
    } else {
      return response(bankService.saveNewBank(bankRequest));
    }
  }

  @ApiIgnore
  @PutMapping(value = "/bank", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefBankResponse> updateBank(
      @PathVariable("username") String username,
      @RequestParam("id") String id,
      @RequestBody RefBankRequest bankRequest) {
    logger.info("Update Bank: {} | {} | {}", username, id, bankRequest);

    if (!hasText(username) || !hasText(id) || bankRequest == null) {
      return response("Error Updating Bank: Invalid Request! Please Try Again!!!");
    } else {
      return response(bankService.updateBank(id, bankRequest));
    }
  }

  @ApiIgnore
  @DeleteMapping(value = "/bank", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefBankResponse> deleteBank(
      @PathVariable("username") String username, @RequestParam("id") String id) {
    logger.info("Delete Bank: {} | {}", username, id);

    if (!hasText(username) || !hasText(id)) {
      return response("Error Deleting Bank: Invalid Request! Please Try Again!!!");
    } else {
      return response(bankService.deleteBank(id));
    }
  }

  private ResponseEntity<RefBankResponse> response(RefBankResponse refBankResponse) {
    if (refBankResponse.getStatus() == null) {
      return new ResponseEntity<>(refBankResponse, OK);
    } else {
      return new ResponseEntity<>(refBankResponse, SERVICE_UNAVAILABLE);
    }
  }

  private ResponseEntity<RefBankResponse> response(String errMsg) {
    return new ResponseEntity<>(
        RefBankResponse.builder()
            .refBanks(emptyList())
            .deleteCount(0L)
            .status(Status.builder().errMsg(errMsg).build())
            .build(),
        BAD_REQUEST);
  }
}
