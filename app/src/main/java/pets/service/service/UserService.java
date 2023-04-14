package pets.service.service;

import static java.util.Collections.emptyList;
import static pets.service.utils.ObjectMapperProvider.objectMapper;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import pets.service.connector.UserConnector;
import pets.service.model.Status;
import pets.service.model.UserRequest;
import pets.service.model.UserResponse;

@Service
public class UserService {

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  private final UserConnector userConnector;

  public UserService(UserConnector userConnector) {
    this.userConnector = userConnector;
  }

  public UserResponse getUserByUsername(String username) {
    try {
      return userConnector.getUserByUsername(username);
    } catch (Exception ex) {
      logger.error("Exception in Get User by Username: {}", username);
      return response("User Unavailable! Please Try Again!!!", ex);
    }
  }

  public UserResponse getUserByEmail(String email) {
    try {
      return userConnector.getUserByEmail(email);
    } catch (Exception ex) {
      logger.error("Exception in Get User by Email: {}", email);
      return response("User Email Unavailable! Please Try Again!!!", ex);
    }
  }

  public UserResponse getUserByPhone(String phone) {
    try {
      return userConnector.getUserByPhone(phone);
    } catch (Exception ex) {
      logger.error("Exception in Get User by Phone: {}", phone);
      return response("User Phone Unavailable! Please Try Again!!!", ex);
    }
  }

  public UserResponse saveNewUser(UserRequest userRequest) {
    try {
      return userConnector.saveNewUser(userRequest);
    } catch (Exception ex) {
      logger.error("Exception in Save New User: {}", userRequest);
      return response("Save User Unavailable! Please Try Again!!!", ex);
    }
  }

  public UserResponse updateUser(String id, UserRequest userRequest) {
    try {
      return userConnector.updateUser(id, userRequest);
    } catch (Exception ex) {
      logger.error("Exception in Update New User: {} | {}", id, userRequest);
      return response("Update User Unavailable! Please Try Again!!!", ex);
    }
  }

  public UserResponse updateUser(String id, Map<String, Object> newValues) {
    try {
      return userConnector.updateUser(id, newValues);
    } catch (Exception ex) {
      logger.error("Exception in Update New User: {} | {}", id, newValues);
      return response("Update User Unavailable! Please Try Again!!!", ex);
    }
  }

  private UserResponse response(String errMsg, Exception ex) {
    if (ex instanceof HttpStatusCodeException) {
      try {
        if (((HttpStatusCodeException) ex).getStatusCode().is4xxClientError()) {
          return responseException(errMsg, ex);
        }

        return objectMapper()
            .readValue(
                ((HttpStatusCodeException) ex).getResponseBodyAsString(), UserResponse.class);
      } catch (Exception ex1) {
        logger.error("Exception Reading Http Exception: {}", errMsg, ex1);
        return responseException(errMsg, ex1);
      }
    } else {
      return responseException(errMsg, ex);
    }
  }

  private UserResponse responseException(String errMsg, Exception ex) {
    return UserResponse.builder()
        .users(emptyList())
        .deleteCount(0L)
        .status(Status.builder().errMsg(errMsg).message(ex == null ? null : ex.toString()).build())
        .build();
  }
}
