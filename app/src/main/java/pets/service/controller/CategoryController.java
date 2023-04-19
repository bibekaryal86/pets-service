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
import pets.service.model.RefCategoryFilters;
import pets.service.model.RefCategoryRequest;
import pets.service.model.RefCategoryResponse;
import pets.service.model.Status;
import pets.service.service.CategoryService;

@RestController
@RequestMapping("/categories/{username}")
public class CategoryController {

  private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping(value = "/id", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefCategoryResponse> getCategoryById(
      @PathVariable("username") String username, @RequestParam("id") String id) {
    logger.info("Get Category By Id: {} | {}", username, id);

    if (!hasText(username) && !hasText(id)) {
      return response("Error Retrieving Category: Invalid Request! Please Try Again!!!");
    } else {
      return response(categoryService.getCategoryById(id));
    }
  }

  @PostMapping(value = "/categories", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefCategoryResponse> getAllCategories(
      @PathVariable("username") String username,
      @RequestBody(required = false) RefCategoryFilters refCategoryFilters) {
    logger.info("Get All Categories: {} | {}", username, refCategoryFilters);

    if (!hasText(username)) {
      return response("Error Retrieving Categories: Invalid Request! Please Try Again!!!");
    } else {
      return response(categoryService.getAllCategories(username, refCategoryFilters));
    }
  }

  @Hidden
  @PostMapping(value = "/category", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefCategoryResponse> saveNewCategory(
      @PathVariable("username") String username, @RequestBody RefCategoryRequest categoryRequest) {
    logger.info("Save New Category: {} | {}", username, categoryRequest);

    if (!hasText(username)
        || categoryRequest == null
        || !hasText(categoryRequest.getDescription())) {
      return response("Error Saving Category: Invalid Request! Please Try Again!!!");
    } else {
      return response(categoryService.saveNewCategory(categoryRequest));
    }
  }

  @Hidden
  @PutMapping(value = "/category", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefCategoryResponse> updateCategory(
      @PathVariable("username") String username,
      @RequestParam("id") String id,
      @RequestBody RefCategoryRequest categoryRequest) {
    logger.info("Update Category: {} | {} | {}", username, id, categoryRequest);

    if (!hasText(username) || !hasText(id) || categoryRequest == null) {
      return response("Error Updating Category: Invalid Request! Please Try Again!!!");
    } else {
      return response(categoryService.updateCategory(id, categoryRequest));
    }
  }

  @Hidden
  @DeleteMapping(value = "/category", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefCategoryResponse> deleteCategory(
      @PathVariable("username") String username, @RequestParam("id") String id) {
    logger.info("Delete Category: {} | {}", username, id);

    if (!hasText(username) || !hasText(id)) {
      return response("Error Deleting Category: Invalid Request! Please Try Again!!!");
    } else {
      return response(categoryService.deleteCategory(id));
    }
  }

  private ResponseEntity<RefCategoryResponse> response(RefCategoryResponse refCategoryResponse) {
    if (refCategoryResponse.getStatus() == null) {
      return new ResponseEntity<>(refCategoryResponse, OK);
    } else {
      return new ResponseEntity<>(refCategoryResponse, SERVICE_UNAVAILABLE);
    }
  }

  private ResponseEntity<RefCategoryResponse> response(String errMsg) {
    return new ResponseEntity<>(
        RefCategoryResponse.builder()
            .refCategories(emptyList())
            .deleteCount(0L)
            .status(Status.builder().errMsg(errMsg).build())
            .build(),
        BAD_REQUEST);
  }
}
