package pets.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pets.service.model.AccountFilters;
import pets.service.model.AccountRequest;
import pets.service.model.AccountResponse;
import pets.service.model.Status;
import pets.service.service.AccountService;
import springfox.documentation.annotations.ApiIgnore;

import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

@RestController
@RequestMapping("/accounts/{username}")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = "/id", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable("username") String username,
                                                          @RequestParam("id") String id) {
        logger.info("Get Account By Id: {} | {}", username, id);

        if (!hasText(username) && !hasText(id)) {
            return response("Error Retrieving Account: Invalid Request! Please Try Again!!!");
        } else {
            return response(accountService.getAccountById(username, id, true));
        }
    }

    @PostMapping(value = "/user", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> getAccountsByUser(@PathVariable("username") String username,
                                                             @RequestBody(required = false) AccountFilters accountFilters) {
        logger.info("Get Accounts By User: {} | {}", username, accountFilters);

        if (!hasText(username)) {
            return response("Error Retrieving Accounts: Invalid Request! Please Try Again!!!");
        } else {
            return response(accountService.getAccountsByUser(username, accountFilters, true));
        }
    }

    @ApiIgnore
    @PostMapping(value = "/account", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> saveNewAccount(@PathVariable("username") String username,
                                                          @RequestBody AccountRequest accountRequest) {
        logger.info("Save New Account: {} | {}", username, accountRequest);

        if (!hasText(username) ||
                accountRequest == null ||
                !hasText(accountRequest.getTypeId()) ||
                !hasText(accountRequest.getBankId()) ||
                !hasText(accountRequest.getDescription()) ||
                !hasText(accountRequest.getStatus()) ||
                !hasText(accountRequest.getUsername())
        ) {
            return response("Error Saving Account: Invalid Request! Please Try Again!!!");
        } else {
            return response(accountService.saveNewAccount(username, accountRequest, true));
        }
    }

    @ApiIgnore
    @PutMapping(value = "/account", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable("username") String username,
                                                         @RequestParam("id") String id,
                                                         @RequestBody AccountRequest accountRequest) {
        logger.info("Update Account: {} | {} | {}", username, id, accountRequest);

        if (!hasText(username) ||
                !hasText(id) ||
                accountRequest == null
        ) {
            return response("Error Updating Account: Invalid Request! Please Try Again!!!");
        } else {
            return response(accountService.updateAccount(username, id, accountRequest, true));
        }
    }

    @ApiIgnore
    @DeleteMapping(value = "/account", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> deleteAccount(@PathVariable("username") String username,
                                                         @RequestParam("id") String id) {
        logger.info("Delete Account: {} | {}", username, id);

        if (!hasText(username) || !hasText(id)) {
            return response("Error Deleting Account: Invalid Request! Please Try Again!!!");
        } else {
            return response(accountService.deleteAccount(username, id));
        }
    }

    private ResponseEntity<AccountResponse> response(AccountResponse accountResponse) {
        if (accountResponse.getStatus() == null) {
            return new ResponseEntity<>(accountResponse, OK);
        } else {
            return new ResponseEntity<>(accountResponse, SERVICE_UNAVAILABLE);
        }
    }

    private ResponseEntity<AccountResponse> response(String errMsg) {
        return new ResponseEntity<>(AccountResponse.builder()
                .accounts(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg(errMsg)
                        .build())
                .build(),
                BAD_REQUEST);
    }
}
