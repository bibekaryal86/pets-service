package pets.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pets.service.model.RefAccountTypeRequest;
import pets.service.model.RefAccountTypeResponse;
import pets.service.model.Status;
import pets.service.service.AccountTypeService;
import springfox.documentation.annotations.ApiIgnore;

import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

@RestController
@RequestMapping("/accounttypes/{username}")
public class AccountTypeController {

    private static final Logger logger = LoggerFactory.getLogger(AccountTypeController.class);
    private final AccountTypeService accountTypeService;

    public AccountTypeController(AccountTypeService accountTypeService) {
        this.accountTypeService = accountTypeService;
    }

    @GetMapping(value = "/id", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RefAccountTypeResponse> getAccountTypeById(@PathVariable("username") String username,
                                                                     @RequestParam("id") String id) {
        logger.info("Get Account Type By Id: {} | {}", username, id);

        if (!hasText(username) && !hasText(id)) {
            return response("Error Retrieving Account Type: Invalid Request! Please Try Again!!!");
        } else {
            return response(accountTypeService.getAccountTypeById(id));
        }
    }

    @GetMapping(value = "/accounttype", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RefAccountTypeResponse> getAllAccountTypes(@PathVariable("username") String username) {
        logger.info("Get All Account Types: {}", username);

        if (!hasText(username)) {
            return response("Error Retrieving Account Types: Invalid Request! Please Try Again!!!");
        } else {
            return response(accountTypeService.getAllAccountTypes());
        }
    }

    @ApiIgnore
    @PostMapping(value = "/accounttype", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RefAccountTypeResponse> saveNewAccountType(@PathVariable("username") String username,
                                                                     @RequestBody RefAccountTypeRequest accountTypeRequest) {
        logger.info("Save New Account Type: {} | {}", username, accountTypeRequest);

        if (!hasText(username) ||
                accountTypeRequest == null ||
                !hasText(accountTypeRequest.getDescription())
        ) {
            return response("Error Saving Account Type: Invalid Request! Please Try Again!!!");
        } else {
            return response(accountTypeService.saveNewAccountType(accountTypeRequest));
        }
    }

    @ApiIgnore
    @PutMapping(value = "/accounttype", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RefAccountTypeResponse> updateAccountType(@PathVariable("username") String username,
                                                                    @RequestParam("id") String id,
                                                                    @RequestBody RefAccountTypeRequest accountTypeRequest) {
        logger.info("Update Account Type: {} | {} | {}", username, id, accountTypeRequest);

        if (!hasText(username) ||
                !hasText(id) ||
                accountTypeRequest == null
        ) {
            return response("Error Updating Account Type: Invalid Request! Please Try Again!!!");
        } else {
            return response(accountTypeService.updateAccountType(id, accountTypeRequest));
        }
    }

    @ApiIgnore
    @DeleteMapping(value = "/accounttype", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RefAccountTypeResponse> deleteAccountType(@PathVariable("username") String username,
                                                                    @RequestParam("id") String id) {
        logger.info("Delete Account Type: {} | {}", username, id);

        if (!hasText(username) || !hasText(id)) {
            return response("Error Deleting Account Type: Invalid Request! Please Try Again!!!");
        } else {
            return response(accountTypeService.deleteAccountType(id));
        }
    }

    private ResponseEntity<RefAccountTypeResponse> response(RefAccountTypeResponse refAccountTypeResponse) {
        if (refAccountTypeResponse.getStatus() == null) {
            return new ResponseEntity<>(refAccountTypeResponse, OK);
        } else {
            return new ResponseEntity<>(refAccountTypeResponse, SERVICE_UNAVAILABLE);
        }
    }

    private ResponseEntity<RefAccountTypeResponse> response(String errMsg) {
        return new ResponseEntity<>(RefAccountTypeResponse.builder()
                .refAccountTypes(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg(errMsg)
                        .build())
                .build(),
                BAD_REQUEST);
    }
}
