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
public class ReportCashFlows implements Serializable {
  private String month;
  private int monthToSort;
  private String monthBeginDate;
  private String monthEndDate;
  private BigDecimal totalIncomes;
  private BigDecimal totalExpenses;
  private BigDecimal netSavings;
}
