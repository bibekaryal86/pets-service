package pets.service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pets.service.model.Status;
import pets.service.model.UserRequest;
import pets.service.model.UserResponse;
import pets.service.service.UserService;

import java.util.Map;

import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

@RestController
@RequestMapping("/users/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/username/{username}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable("username") String username) {
        if (!hasText(username)) {
            return response("Error Retrieving User: Invalid Request! Please Try Again!!!");
        } else {
            return response(userService.getUserByUsername(username));
        }
    }

    @GetMapping(value = "/email/{email}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable("email") String email) {
        if (!hasText(email)) {
            return response("Error Retrieving User by Email: Invalid Request! Please Try Again!!!");
        } else {
            return response(userService.getUserByEmail(email));
        }
    }

    @GetMapping(value = "/phone/{phone}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> getUserByPhone(@PathVariable("phone") String phone) {
        if (!hasText(phone)) {
            return response("Error Retrieving User by Phone: Invalid Request! Please Try Again!!!");
        } else {
            return response(userService.getUserByPhone(phone));
        }
    }

    @PostMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> saveNewUser(@RequestBody UserRequest userRequest) {
        if (userRequest == null ||
                !hasText(userRequest.getUsername()) ||
                !hasText(userRequest.getPassword()) ||
                !hasText(userRequest.getFirstName()) ||
                !hasText(userRequest.getLastName()) ||
                !hasText(userRequest.getEmail()) ||
                !hasText(userRequest.getPhone()) ||
                !hasText(userRequest.getStatus())
        ) {
            return response("Error Saving User: Invalid Request! Please Try Again!!!");
        } else {
            return response(userService.saveNewUser(userRequest));
        }
    }

    @PutMapping(value = "/{username}/id", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> updateUser(@PathVariable("username") String username,
                                                   @RequestParam("id") String id,
                                                   @RequestBody UserRequest userRequest) {
        if (!hasText(username) ||
                !hasText(id) ||
                userRequest == null
        ) {
            return response("Error Updating User: Invalid Request! Please Try Again!!!");
        } else {
            return response(userService.updateUser(id, userRequest));
        }
    }

    @PatchMapping(value = "/{username}/id", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> updateUser(@PathVariable("username") String username,
                                                   @RequestParam("id") String id,
                                                   @RequestBody Map<String, Object> newValues) {
        if (!hasText(username) ||
                !hasText(id) ||
                newValues == null
        ) {
            return response("Error Updating User: Invalid Request! Please Try Again!!!");
        } else {
            return response(userService.updateUser(id, newValues));
        }
    }

    private ResponseEntity<UserResponse> response(UserResponse userResponse) {
        if (userResponse.getStatus() == null) {
            return new ResponseEntity<>(userResponse, OK);
        } else {
            return new ResponseEntity<>(userResponse, SERVICE_UNAVAILABLE);
        }
    }

    private ResponseEntity<UserResponse> response(String errMsg) {
        return new ResponseEntity<>(UserResponse.builder()
                .users(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg(errMsg)
                        .build())
                .build(),
                BAD_REQUEST);
    }
}
