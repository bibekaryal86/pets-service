package pets.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@JsonInclude(NON_NULL)
public class ReportCategoryTypes implements Serializable {
    private RefCategoryType refCategoryType;
    private BigDecimal totalRefCategoryType;
    private List<ReportCategories> reportCategories;
}
