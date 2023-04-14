package pets.service.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class Account implements Serializable {
  private String id;
  private RefAccountType refAccountType;
  private RefBank refBank;
  private String description;
  private User user;
  private BigDecimal openingBalance;
  private BigDecimal currentBalance;
  private String status;
  private String creationDate;
  private String lastModified;
}
