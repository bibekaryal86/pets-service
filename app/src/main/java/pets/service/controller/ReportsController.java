package pets.service.controller;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pets.service.model.ReportsResponse;
import pets.service.model.Status;
import pets.service.service.ReportsService;

@RestController
@RequestMapping("/reports/{username}")
public class ReportsController {

  private static final Logger logger = LoggerFactory.getLogger(ReportsController.class);
  private final ReportsService reportsService;

  public ReportsController(ReportsService reportsService) {
    this.reportsService = reportsService;
  }

  @GetMapping(value = "/currentbalances", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<ReportsResponse> getCurrentBalancesReport(
      @PathVariable("username") String username) {
    logger.info("Get Current Balances Report: {}", username);

    if (!hasText(username)) {
      return response(
          "Error Retrieving Current Balances Report! Invalid Request!! Please Try Again!!!");
    } else {
      return response(reportsService.getCurrentBalancesReport(username));
    }
  }

  @GetMapping(value = "/cashflows", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<ReportsResponse> getCashFlowsReport(
      @PathVariable("username") String username,
      @RequestParam("selectedyear") String selectedYear) {
    logger.info("Get Cash Flows Report: {} | {}", username, selectedYear);

    if (!hasText(username) || !hasText(selectedYear)) {
      return response("Error Retrieving Cash Flows Report! Invalid Request!! Please Try Again!!!");
    } else {
      return response(reportsService.getCashFlowsReport(username, selectedYear));
    }
  }

  @GetMapping(value = "/categories", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<ReportsResponse> getCategoriesReport(
      @PathVariable("username") String username,
      @RequestParam("selectedyear") String selectedYear) {
    logger.info("Get Categories Report: {} | {}", username, selectedYear);

    if (!hasText(username) || !hasText(selectedYear)) {
      return response("Error Retrieving Categories Report! Invalid Request!! Please Try Again!!!");
    } else {
      return response(reportsService.getCategoriesReport(username, selectedYear));
    }
  }

  private ResponseEntity<ReportsResponse> response(ReportsResponse reportResponse) {
    if (reportResponse.getStatus() == null) {
      return new ResponseEntity<>(reportResponse, OK);
    } else {
      return new ResponseEntity<>(reportResponse, SERVICE_UNAVAILABLE);
    }
  }

  private ResponseEntity<ReportsResponse> response(String errMsg) {
    return new ResponseEntity<>(
        ReportsResponse.builder().status(Status.builder().errMsg(errMsg).build()).build(),
        BAD_REQUEST);
  }
}
