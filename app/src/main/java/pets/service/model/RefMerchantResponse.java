package pets.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class RefMerchantResponse implements Serializable {
    private List<RefMerchant> refMerchants;
    private Long deleteCount;
    private Set<String> refMerchantsFilterList;
    private List<RefMerchant> refMerchantsNotUsedInTransactions;
    private Status status;
}
