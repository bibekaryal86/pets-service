package pets.service.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.NonNull;

@Data
@JsonInclude(NON_NULL)
public class RefCategoryTypeRequest implements Serializable {
  @NonNull private String description;

  @JsonCreator
  public RefCategoryTypeRequest(@JsonProperty("description") String description) {
    this.description = description;
  }
}
