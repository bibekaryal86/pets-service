package pets.service.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@JsonInclude(NON_NULL)
public class RefMerchantRequest implements Serializable {
  @NonNull private String description;
  @NonNull private String username;
}
