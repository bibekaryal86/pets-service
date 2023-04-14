package pets.service.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.Data;

@Data
@JsonInclude(NON_NULL)
public class RefCategoryFilters implements Serializable {
  private String categoryTypeId;
  private boolean usedInTxnsOnly;
}
