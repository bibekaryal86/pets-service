package pets.service.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(NON_NULL)
public class TransactionFilters implements Serializable {
  private String accountId;
  private String accountTypeId;
  private BigDecimal amountFrom;
  private BigDecimal amountTo;
  private String bankId;
  private String categoryId;
  private String categoryTypeId;
  private String dateFrom;
  private String dateTo;
  private String merchantId;
  private Boolean necessary;
  private Boolean regular;
  private String transactionTypeId;
}
