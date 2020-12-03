package pets.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import pets.service.connector.CategoryConnector;
import pets.service.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.springframework.util.CollectionUtils.isEmpty;
import static pets.service.utils.CategoryHelper.*;
import static pets.service.utils.ObjectMapperProvider.objectMapper;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryConnector categoryConnector;
    private final CategoryTypeService categoryTypeService;
    private final TransactionService transactionService;

    public CategoryService(CategoryConnector categoryConnector,
                           CategoryTypeService categoryTypeService,
                           @Lazy TransactionService transactionService) {
        this.categoryConnector = categoryConnector;
        this.categoryTypeService = categoryTypeService;
        this.transactionService = transactionService;
    }

    public RefCategoryResponse getAllCategories(String username, RefCategoryFilters refCategoryFilters) {
        RefCategoryResponse categoryResponse;
        try {
            categoryResponse = categoryConnector.getAllCategories();
        } catch (Exception ex) {
            logger.error("Exception in Get All Categories", ex);
            categoryResponse = response("Categories Unavailable! Please Try Again!!!", ex);
        }

        if (refCategoryFilters != null) {
            List<Transaction> transactions = new ArrayList<>();
            if (refCategoryFilters.isUsedInTxnsOnly()) {
                transactions = transactionService.getTransactionsByUser(username, null, false)
                        .getTransactions();
            }

            categoryResponse = applyFilters(categoryResponse, refCategoryFilters, transactions);
        }

        if (!isEmpty(categoryResponse.getRefCategories())) {
            applyAllDetails(categoryResponse);
            categoryResponse = sortWithinRefCategoryType(categoryResponse);
        }

        return categoryResponse;
    }

    public CompletableFuture<RefCategoryResponse> getAllCategoriesFuture() {
        return CompletableFuture.supplyAsync(() -> getAllCategories(null, null));
    }

    public RefCategoryResponse getCategoryById(String id) {
        RefCategoryResponse categoryResponse = getAllCategories(null, null);
        RefCategory category;

        if (categoryResponse.getStatus() == null) {
            category = categoryResponse.getRefCategories().stream()
                    .filter(refCategory -> refCategory.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (category == null) {
                logger.error("Category Not Found for Id: {}", id);
                return response("Category Unavailable! Please Try Again!!!", null);
            } else {
                return RefCategoryResponse.builder()
                        .refCategories(singletonList(category))
                        .build();
            }
        } else {
            return categoryResponse;
        }
    }

    public RefCategoryResponse saveNewCategory(RefCategoryRequest refCategoryRequest) {
        RefCategoryResponse categoryResponse;

        try {
            categoryResponse = categoryConnector.saveNewCategory(refCategoryRequest);
        } catch (Exception ex) {
            logger.error("Exception in Save New Category: {}", refCategoryRequest, ex);
            categoryResponse = response("Save Bank Unavailable! Please Try Again!!!", ex);
        }

        if (!isEmpty(categoryResponse.getRefCategories())) {
            applyAllDetails(categoryResponse);
        }

        return categoryResponse;
    }

    public RefCategoryResponse updateCategory(String id, RefCategoryRequest refCategoryRequest) {
        RefCategoryResponse categoryResponse;

        try {
            categoryResponse = categoryConnector.updateCategory(id, refCategoryRequest);
        } catch (Exception ex) {
            logger.error("Exception in Update Category: {} | {}", id, refCategoryRequest, ex);
            categoryResponse = response("Update Category Unavailable! Please Try Again!!!", ex);
        }

        if (!isEmpty(categoryResponse.getRefCategories())) {
            applyAllDetails(categoryResponse);
        }

        return categoryResponse;
    }

    public RefCategoryResponse deleteCategory(String id) {
        try {
            return categoryConnector.deleteCategory(id);
        } catch (Exception ex) {
            logger.error("Exception in Delete Category: {}", id, ex);
            return response("Delete Category Unavailable! Please Try Again!!!", ex);
        }
    }

    private void applyAllDetails(RefCategoryResponse categoryResponse) {
        RefCategoryTypeResponse categoryTypeResponse = categoryTypeService.getAllCategoryTypes(null, false);
        applyAllDetailsStatic(categoryResponse, categoryTypeResponse.getRefCategoryTypes());
    }

    private RefCategoryResponse response(String errMsg, Exception ex) {
        if (ex instanceof HttpStatusCodeException) {
            try {
                if (((HttpStatusCodeException) ex).getStatusCode().is4xxClientError()) {
                    return responseException(errMsg, ex);
                }

                return objectMapper().readValue(((HttpStatusCodeException) ex).getResponseBodyAsString(),
                        RefCategoryResponse.class);
            } catch (Exception ex1) {
                logger.error("Exception Reading Http Exception: {}", errMsg, ex1);
                return responseException(errMsg, ex1);
            }
        } else {
            return responseException(errMsg, ex);
        }
    }

    private RefCategoryResponse responseException(String errMsg, Exception ex) {
        return RefCategoryResponse.builder()
                .refCategories(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg(errMsg)
                        .message(ex == null ? null : ex.toString())
                        .build())
                .build();
    }
}
