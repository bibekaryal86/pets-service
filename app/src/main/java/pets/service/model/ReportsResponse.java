package pets.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@JsonInclude(NON_NULL)
public class ReportsResponse implements Serializable {
    private List<ReportCurrentBalances> reportCurrentBalances;
    private List<ReportCashFlows> reportCashFlows;
    private List<ReportCategoryTypes> reportCategoryTypes;
    private Status status;
}
