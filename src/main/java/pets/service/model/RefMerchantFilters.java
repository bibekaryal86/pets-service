package pets.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class RefMerchantFilters implements Serializable {
    private String firstChar;
    private boolean notUsedInTransactionsOnly;
}
