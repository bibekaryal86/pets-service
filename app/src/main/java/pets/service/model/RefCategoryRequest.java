package pets.service.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.Data;
import lombok.NonNull;

@Data
@JsonInclude(NON_NULL)
public class RefCategoryRequest implements Serializable {
  @NonNull private String description;
  @NonNull private String categoryTypeId;
}
