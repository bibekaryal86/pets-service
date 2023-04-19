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
import pets.service.model.RefCategoryTypeRequest;
import pets.service.model.RefCategoryTypeResponse;
import pets.service.model.Status;
import pets.service.service.CategoryTypeService;

@RestController
@RequestMapping("/categorytypes/{username}")
public class CategoryTypeController {

  private static final Logger logger = LoggerFactory.getLogger(CategoryTypeController.class);
  private final CategoryTypeService categoryTypeService;

  public CategoryTypeController(CategoryTypeService categoryTypeService) {
    this.categoryTypeService = categoryTypeService;
  }

  @GetMapping(value = "/id", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefCategoryTypeResponse> getCategoryTypeById(
      @PathVariable("username") String username, @RequestParam("id") String id) {
    logger.info("Get Category Type By Id: {} | {}", username, id);

    if (!hasText(username) && !hasText(id)) {
      return response("Error Retrieving Category Type: Invalid Request! Please Try Again!!!");
    } else {
      return response(categoryTypeService.getCategoryTypeById(id));
    }
  }

  @GetMapping(value = "/categorytype", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefCategoryTypeResponse> getAllCategoryTypes(
      @PathVariable("username") String username,
      @RequestParam(name = "usedInTxnsOnly", required = false, defaultValue = "false")
          boolean usedInTxnsOnly) {
    logger.info("Get All Category Types: {}", username);

    if (!hasText(username)) {
      return response("Error Retrieving Category Types: Invalid Request! Please Try Again!!!");
    } else {
      return response(categoryTypeService.getAllCategoryTypes(username, usedInTxnsOnly));
    }
  }

  @Hidden
  @PostMapping(value = "/categorytype", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefCategoryTypeResponse> saveNewCategoryType(
      @PathVariable("username") String username,
      @RequestBody RefCategoryTypeRequest categoryTypeRequest) {
    logger.info("Save New Category Type: {} | {}", username, categoryTypeRequest);

    if (!hasText(username)
        || categoryTypeRequest == null
        || !hasText(categoryTypeRequest.getDescription())) {
      return response("Error Saving Category Type: Invalid Request! Please Try Again!!!");
    } else {
      return response(categoryTypeService.saveNewCategoryType(categoryTypeRequest));
    }
  }

  @Hidden
  @PutMapping(value = "/categorytype", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefCategoryTypeResponse> updateCategoryType(
      @PathVariable("username") String username,
      @RequestParam("id") String id,
      @RequestBody RefCategoryTypeRequest categoryTypeRequest) {
    logger.info("Update Category Type: {} | {} | {}", username, id, categoryTypeRequest);

    if (!hasText(username) || !hasText(id) || categoryTypeRequest == null) {
      return response("Error Updating Category Type: Invalid Request! Please Try Again!!!");
    } else {
      return response(categoryTypeService.updateCategoryType(id, categoryTypeRequest));
    }
  }

  @Hidden
  @DeleteMapping(value = "/categorytype", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefCategoryTypeResponse> deleteCategoryType(
      @PathVariable("username") String username, @RequestParam("id") String id) {
    logger.info("Delete Category Type: {} | {}", username, id);

    if (!hasText(username) || !hasText(id)) {
      return response("Error Deleting Category Type: Invalid Request! Please Try Again!!!");
    } else {
      return response(categoryTypeService.deleteCategoryType(id));
    }
  }

  private ResponseEntity<RefCategoryTypeResponse> response(
      RefCategoryTypeResponse refCategoryTypeResponse) {
    if (refCategoryTypeResponse.getStatus() == null) {
      return new ResponseEntity<>(refCategoryTypeResponse, OK);
    } else {
      return new ResponseEntity<>(refCategoryTypeResponse, SERVICE_UNAVAILABLE);
    }
  }

  private ResponseEntity<RefCategoryTypeResponse> response(String errMsg) {
    return new ResponseEntity<>(
        RefCategoryTypeResponse.builder()
            .refCategoryTypes(emptyList())
            .deleteCount(0L)
            .status(Status.builder().errMsg(errMsg).build())
            .build(),
        BAD_REQUEST);
  }
}
