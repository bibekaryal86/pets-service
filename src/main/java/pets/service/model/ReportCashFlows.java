package pets.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

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
