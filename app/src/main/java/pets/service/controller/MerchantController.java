package pets.service.controller;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pets.service.model.RefMerchantFilters;
import pets.service.model.RefMerchantRequest;
import pets.service.model.RefMerchantResponse;
import pets.service.model.Status;
import pets.service.service.MerchantService;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/merchants/{username}")
public class MerchantController {

  private static final Logger logger = LoggerFactory.getLogger(MerchantController.class);
  private final MerchantService merchantService;

  public MerchantController(MerchantService merchantService) {
    this.merchantService = merchantService;
  }

  @GetMapping(value = "/id", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefMerchantResponse> getMerchantById(
      @PathVariable("username") String username, @RequestParam("id") String id) {
    logger.info("Get Merchant By Id: {} | {}", username, id);

    if (!hasText(username) && !hasText(id)) {
      return response("Error Retrieving Merchant: Invalid Request! Please Try Again!!!");
    } else {
      return response(merchantService.getMerchantById(id));
    }
  }

  @PostMapping(value = "/merchants", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefMerchantResponse> getMerchantsByUser(
      @PathVariable("username") String username,
      @RequestBody(required = false) RefMerchantFilters refMerchantFilters) {
    logger.info("Get Merchants By User: {} | {}", username, refMerchantFilters);

    if (!hasText(username)) {
      return response("Error Retrieving Merchants: Invalid Request! Please Try Again!!!");
    } else {
      return response(merchantService.getMerchantsByUser(username, refMerchantFilters));
    }
  }

  @ApiIgnore
  @PostMapping(value = "/merchant", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefMerchantResponse> saveNewMerchant(
      @PathVariable("username") String username, @RequestBody RefMerchantRequest merchantRequest) {
    logger.info("Save New Merchant: {} | {}", username, merchantRequest);

    if (!hasText(username)
        || merchantRequest == null
        || !hasText(merchantRequest.getDescription())) {
      return response("Error Saving Merchant: Invalid Request! Please Try Again!!!");
    } else {
      return response(merchantService.saveNewMerchant(merchantRequest));
    }
  }

  @ApiIgnore
  @PutMapping(value = "/merchant", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefMerchantResponse> updateMerchant(
      @PathVariable("username") String username,
      @RequestParam("id") String id,
      @RequestBody RefMerchantRequest merchantRequest) {
    logger.info("Update Merchant: {} | {} | {}", username, id, merchantRequest);

    if (!hasText(username) || !hasText(id) || merchantRequest == null) {
      return response("Error Updating Merchant: Invalid Request! Please Try Again!!!");
    } else {
      return response(merchantService.updateMerchant(id, merchantRequest));
    }
  }

  @ApiIgnore
  @DeleteMapping(value = "/merchant", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefMerchantResponse> deleteMerchant(
      @PathVariable("username") String username, @RequestParam("id") String id) {
    logger.info("Delete Merchant: {} | {}", username, id);

    if (!hasText(username) || !hasText(id)) {
      return response("Error Deleting Merchant: Invalid Request! Please Try Again!!!");
    } else {
      return response(merchantService.deleteMerchant(username, id));
    }
  }

  private ResponseEntity<RefMerchantResponse> response(RefMerchantResponse refMerchantResponse) {
    if (refMerchantResponse.getStatus() == null) {
      return new ResponseEntity<>(refMerchantResponse, OK);
    } else {
      return new ResponseEntity<>(refMerchantResponse, SERVICE_UNAVAILABLE);
    }
  }

  private ResponseEntity<RefMerchantResponse> response(String errMsg) {
    return new ResponseEntity<>(
        RefMerchantResponse.builder()
            .refMerchants(emptyList())
            .refMerchantsFilterList(emptySet())
            .deleteCount(0L)
            .status(Status.builder().errMsg(errMsg).build())
            .build(),
        BAD_REQUEST);
  }
}
