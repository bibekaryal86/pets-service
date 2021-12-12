package pets.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import pets.service.connector.CategoryTypeConnector;
import pets.service.model.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static pets.service.utils.CategoryTypeHelper.applyUsedInTransactionsOnlyFilter;
import static pets.service.utils.ObjectMapperProvider.objectMapper;

@Service
public class CategoryTypeService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryTypeService.class);

    private final CategoryTypeConnector categoryTypeConnector;
    private final TransactionService transactionService;

    public CategoryTypeService(CategoryTypeConnector categoryTypeConnector,
                               @Lazy TransactionService transactionService) {
        this.categoryTypeConnector = categoryTypeConnector;
        this.transactionService = transactionService;
    }

    public RefCategoryTypeResponse getAllCategoryTypes(String username, boolean usedInTxnsOnly) {
        RefCategoryTypeResponse refCategoryTypeResponse;
        try {
            refCategoryTypeResponse = categoryTypeConnector.getAllCategoryTypes();
        } catch (Exception ex) {
            logger.error("Exception in Get All Category Types", ex);
            return response("Category Types Unavailable! Please Try Again!!!", ex);
        }

        if (usedInTxnsOnly) {
            TransactionResponse transactionResponse = transactionService.getTransactionsByUser(username, null, true);
            refCategoryTypeResponse = applyUsedInTransactionsOnlyFilter(refCategoryTypeResponse.getRefCategoryTypes(), transactionResponse.getTransactions());
        }

        return refCategoryTypeResponse;
    }

    public RefCategoryTypeResponse getCategoryTypeById(String id) {
        RefCategoryTypeResponse refCategoryTypesResponse = getAllCategoryTypes(null, false);
        RefCategoryType categoryType;

        if (refCategoryTypesResponse.getStatus() == null) {
            categoryType = refCategoryTypesResponse.getRefCategoryTypes().stream()
                    .filter(refCategoryType -> refCategoryType.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (categoryType == null) {
                logger.error("Category Type Not Found for Id: {}", id);
                return response("Category Type Unavailable! Please Try Again!!!", null);
            } else {
                return RefCategoryTypeResponse.builder()
                        .refCategoryTypes(singletonList(categoryType))
                        .build();
            }
        } else {
            return refCategoryTypesResponse;
        }
    }

    public RefCategoryTypeResponse saveNewCategoryType(RefCategoryTypeRequest refCategoryTypeRequest) {
        try {
            return categoryTypeConnector.saveNewCategoryType(refCategoryTypeRequest);
        } catch (Exception ex) {
            logger.error("Exception in Save New Category Type: {}", refCategoryTypeRequest, ex);
            return response("Save Category Type Unavailable! Please Try Again!!!", ex);
        }
    }

    public RefCategoryTypeResponse updateCategoryType(String id, RefCategoryTypeRequest refCategoryTypeRequest) {
        try {
            return categoryTypeConnector.updateCategoryType(id, refCategoryTypeRequest);
        } catch (Exception ex) {
            logger.error("Exception in Update Category Type: {} | {}", id, refCategoryTypeRequest, ex);
            return response("Update Category Type Unavailable! Please Try Again!!!", ex);
        }
    }

    public RefCategoryTypeResponse deleteCategoryType(String id) {
        try {
            return categoryTypeConnector.deleteCategoryType(id);
        } catch (Exception ex) {
            logger.error("Exception in Delete Category Type: {}", id, ex);
            return response("Delete Category Type Unavailable! Please Try Again!!!", ex);
        }
    }

    private RefCategoryTypeResponse response(String errMsg, Exception ex) {
        if (ex instanceof HttpStatusCodeException) {
            try {
                if (((HttpStatusCodeException) ex).getStatusCode().is4xxClientError()) {
                    return responseException(errMsg, ex);
                }

                return objectMapper().readValue(((HttpStatusCodeException) ex).getResponseBodyAsString(),
                        RefCategoryTypeResponse.class);
            } catch (Exception ex1) {
                logger.error("Exception Reading Http Exception: {}", errMsg, ex1);
                return responseException(errMsg, ex1);
            }
        } else {
            return responseException(errMsg, ex);
        }
    }

    private RefCategoryTypeResponse responseException(String errMsg, Exception ex) {
        return RefCategoryTypeResponse.builder()
                .refCategoryTypes(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg(errMsg)
                        .message(ex == null ? null : ex.toString())
                        .build())
                .build();
    }
}
