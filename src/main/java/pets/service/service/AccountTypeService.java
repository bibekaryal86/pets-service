package pets.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import pets.service.connector.AccountTypeConnector;
import pets.service.model.RefAccountType;
import pets.service.model.RefAccountTypeRequest;
import pets.service.model.RefAccountTypeResponse;
import pets.service.model.Status;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static pets.service.utils.ObjectMapperProvider.objectMapper;

@Service
public class AccountTypeService {

    private static final Logger logger = LoggerFactory.getLogger(AccountTypeService.class);

    private final AccountTypeConnector accountTypeConnector;

    public AccountTypeService(AccountTypeConnector accountTypeConnector) {
        this.accountTypeConnector = accountTypeConnector;
    }

    public RefAccountTypeResponse getAllAccountTypes() {
        try {
            return accountTypeConnector.getAllAccountTypes();
        } catch (Exception ex) {
            logger.error("Exception in Get All Account Types", ex);
            return response("Account Types Unavailable! Please Try Again!!!", ex);
        }
    }

    public RefAccountTypeResponse getAccountTypeById(String id) {
        RefAccountTypeResponse refAccountTypeResponse = getAllAccountTypes();
        RefAccountType accountType;

        if (refAccountTypeResponse.getStatus() == null) {
            accountType = refAccountTypeResponse.getRefAccountTypes().stream()
                    .filter(refAccountType -> refAccountType.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (accountType == null) {
                logger.error("Account Type Not Found for Id: {}", id);
                return response("Account Type Unavailable! Please Try Again!!!", null);
            } else {
                return RefAccountTypeResponse.builder()
                        .refAccountTypes(singletonList(accountType))
                        .build();
            }
        } else {
            return refAccountTypeResponse;
        }
    }

    public RefAccountTypeResponse saveNewAccountType(RefAccountTypeRequest refAccountTypeRequest) {
        try {
            return accountTypeConnector.saveNewAccountType(refAccountTypeRequest);
        } catch (Exception ex) {
            logger.error("Exception in Save New Account Type: {}", refAccountTypeRequest, ex);
            return response("Save Account Type Unavailable! Please Try Again!!!", ex);
        }
    }

    public RefAccountTypeResponse updateAccountType(String id, RefAccountTypeRequest refAccountTypeRequest) {
        try {
            return accountTypeConnector.updateAccountType(id, refAccountTypeRequest);
        } catch (Exception ex) {
            logger.error("Exception in Update Account Type for Id: {} | {}", id, refAccountTypeRequest, ex);
            return response("Update Account Type Unavailable! Please Try Again!!!", ex);
        }
    }

    public RefAccountTypeResponse deleteAccountType(String id) {
        try {
            return accountTypeConnector.deleteAccountType(id);
        } catch (Exception ex) {
            logger.error("Exception in Delete Account Type for Id: {}", id, ex);
            return response("Delete Account Type Unavailable! Please Try Again!!!", ex);
        }
    }

    private RefAccountTypeResponse response(String errMsg, Exception ex) {
        if (ex instanceof HttpStatusCodeException) {
            try {
                if (((HttpStatusCodeException) ex).getStatusCode().is4xxClientError()) {
                    return responseException(errMsg, ex);
                }

                return objectMapper().readValue(((HttpStatusCodeException) ex).getResponseBodyAsString(),
                        RefAccountTypeResponse.class);
            } catch (Exception ex1) {
                logger.error("Exception Reading Http Exception: {}", errMsg, ex1);
                return responseException(errMsg, ex1);
            }
        } else {
            return responseException(errMsg, ex);
        }
    }

    private RefAccountTypeResponse responseException(String errMsg, Exception ex) {
        return RefAccountTypeResponse.builder()
                .refAccountTypes(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg(errMsg)
                        .message(ex == null ? null : ex.toString())
                        .build())
                .build();
    }
}
