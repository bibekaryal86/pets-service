package pets.service.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(NON_NULL)
public class ReportCategoryTypes implements Serializable {
  private RefCategoryType refCategoryType;
  private BigDecimal totalRefCategoryType;
  private List<ReportCategories> reportCategories;
}
