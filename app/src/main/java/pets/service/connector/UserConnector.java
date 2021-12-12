package pets.service.connector;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pets.service.model.UserRequest;
import pets.service.model.UserResponse;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class UserConnector {

    private final RestTemplate restTemplate;
    private final String getUserByUsernameUrl;
    private final String getUserByEmailUrl;
    private final String getUserByPhoneUrl;
    private final String saveNewUserUrl;
    private final String updateUserPutUrl;
    private final String updateUserPatchUrl;

    public UserConnector(@Qualifier("restTemplate") RestTemplate restTemplate,
                         String getUserByUsernameUrl,
                         String getUserByEmailUrl,
                         String getUserByPhoneUrl,
                         String saveNewUserUrl,
                         String updateUserPutUrl,
                         String updateUserPatchUrl) {
        this.restTemplate = restTemplate;
        this.getUserByUsernameUrl = getUserByUsernameUrl;
        this.getUserByEmailUrl = getUserByEmailUrl;
        this.getUserByPhoneUrl = getUserByPhoneUrl;
        this.saveNewUserUrl = saveNewUserUrl;
        this.updateUserPutUrl = updateUserPutUrl;
        this.updateUserPatchUrl = updateUserPatchUrl;
    }

    public UserResponse getUserByUsername(@NonNull final String username) {
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("username", username);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(singletonList(APPLICATION_JSON));
        httpHeaders.setContentType(APPLICATION_JSON);
        httpHeaders.add("user-header", username);

        String url = UriComponentsBuilder
                .fromHttpUrl(getUserByUsernameUrl)
                .buildAndExpand(pathVariables)
                .toString();

        ResponseEntity<UserResponse> responseEntity = restTemplate
                .exchange(url,
                        GET,
                        new HttpEntity<>(null, httpHeaders),
                        UserResponse.class);

        return responseEntity.getBody();
    }

    public UserResponse getUserByEmail(@NonNull final String email) {
        String url = UriComponentsBuilder
                .fromHttpUrl(getUserByEmailUrl)
                .queryParam("email", email)
                .toString();

        ResponseEntity<UserResponse> responseEntity = restTemplate
                .exchange(url,
                        GET,
                        new HttpEntity<>(null, null),
                        UserResponse.class);

        return responseEntity.getBody();
    }

    public UserResponse getUserByPhone(@NonNull final String phone) {
        String url = UriComponentsBuilder
                .fromHttpUrl(getUserByPhoneUrl)
                .queryParam("phone", phone)
                .toString();

        ResponseEntity<UserResponse> responseEntity = restTemplate
                .getForEntity(url, UserResponse.class);

        return responseEntity.getBody();
    }

    public UserResponse saveNewUser(@NonNull final UserRequest userRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(singletonList(APPLICATION_JSON));
        httpHeaders.setContentType(APPLICATION_JSON);

        ResponseEntity<UserResponse> responseEntity = restTemplate
                .exchange(saveNewUserUrl,
                        POST,
                        new HttpEntity<>(userRequest, httpHeaders),
                        UserResponse.class);

        return responseEntity.getBody();
    }

    public UserResponse updateUser(@NonNull final String id, @NonNull final UserRequest userRequest) {
        String url = UriComponentsBuilder
                .fromHttpUrl(updateUserPutUrl)
                .buildAndExpand(id)
                .toString();

        ResponseEntity<UserResponse> responseEntity = restTemplate
                .exchange(url,
                        PUT,
                        new HttpEntity<>(userRequest, null),
                        UserResponse.class);

        return responseEntity.getBody();
    }

    public UserResponse updateUser(@NonNull final String id, @NonNull final Map<String, Object> newValues) {
        String url = UriComponentsBuilder
                .fromHttpUrl(updateUserPatchUrl)
                .buildAndExpand(id)
                .toString();

        ResponseEntity<UserResponse> responseEntity = restTemplate
                .exchange(url,
                        PATCH,
                        new HttpEntity<>(newValues, null),
                        UserResponse.class);

        return responseEntity.getBody();
    }
}
