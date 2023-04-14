package pets.service.utils;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;
import pets.service.model.RefCategoryType;
import pets.service.model.RefCategoryTypeResponse;
import pets.service.model.Transaction;

public class CategoryTypeHelper {

  /** for SonarLint */
  private CategoryTypeHelper() {
    throw new IllegalStateException("Utility class");
  }

  public static RefCategoryTypeResponse applyUsedInTransactionsOnlyFilter(
      List<RefCategoryType> refCategoryTypes, List<Transaction> transactions) {
    Set<String> usedCategoryTypes =
        transactions.stream()
            .map(transaction -> transaction.getRefCategory().getRefCategoryType().getId())
            .collect(toSet());

    List<RefCategoryType> filteredRefCategoryTypesList =
        refCategoryTypes.stream()
            .filter(refCategoryType -> usedCategoryTypes.contains(refCategoryType.getId()))
            .collect(toList());

    return RefCategoryTypeResponse.builder().refCategoryTypes(filteredRefCategoryTypesList).build();
  }
}
