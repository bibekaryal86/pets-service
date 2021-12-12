package pets.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class RefCategoryRequest implements Serializable {
    @NonNull
    private String description;
    @NonNull
    private String categoryTypeId;
}
