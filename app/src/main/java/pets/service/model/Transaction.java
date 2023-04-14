package pets.service.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class Transaction implements Serializable {
  private String id;
  private String description;
  private Account account;
  private Account trfAccount;
  private RefTransactionType refTransactionType;
  private RefCategory refCategory;
  private RefMerchant refMerchant;
  private User user;
  private String date;
  private BigDecimal amount;
  private Boolean regular;
  private Boolean necessary;
  private String creationDate;
  private String lastModified;
}
