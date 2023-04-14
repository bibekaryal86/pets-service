package pets.service.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(NON_NULL)
public class ReportsResponse implements Serializable {
  private List<ReportCurrentBalances> reportCurrentBalances;
  private List<ReportCashFlows> reportCashFlows;
  private List<ReportCategoryTypes> reportCategoryTypes;
  private Status status;
}
