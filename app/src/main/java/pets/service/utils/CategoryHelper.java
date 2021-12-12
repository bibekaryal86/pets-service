package pets.service.utils;

import lombok.NonNull;
import pets.service.model.*;

import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;
import static org.springframework.util.StringUtils.hasText;

public class CategoryHelper {

    /**
     * for SonarLint
     */
    private CategoryHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static RefCategoryResponse applyFilters(RefCategoryResponse refCategoryResponse,
                                                   RefCategoryFilters refCategoryFilters,
                                                   List<Transaction> transactions) {
        List<RefCategory> refCategories = refCategoryResponse.getRefCategories();

        if (hasText(refCategoryFilters.getCategoryTypeId())) {
            refCategories = applyCategoryTypeFilter(refCategoryResponse.getRefCategories(), refCategoryFilters.getCategoryTypeId());
        }

        if (refCategoryFilters.isUsedInTxnsOnly()) {
            refCategories = applyUsedInTransactionsOnlyFilter(refCategories, transactions);
        }

        return RefCategoryResponse.builder()
                .refCategories(refCategories)
                .build();
    }

    private static List<RefCategory> applyCategoryTypeFilter(List<RefCategory> refCategories, String refCategoryTypeId) {
        return refCategories.stream()
                .filter(refCategory -> refCategory.getRefCategoryType().getId().equals(refCategoryTypeId))
                .collect(toList());
    }

    private static List<RefCategory> applyUsedInTransactionsOnlyFilter(List<RefCategory> refCategories, List<Transaction> transactions) {
        Set<String> usedCategories = transactions.stream()
                .map(transaction -> transaction.getRefCategory().getId())
                .collect(toSet());

        return refCategories.stream()
                .filter(refCategory -> usedCategories.contains(refCategory.getId()))
                .collect(toList());
    }

    public static void applyAllDetailsStatic(RefCategoryResponse categoryResponse, @NonNull List<RefCategoryType> refCategoryTypes) {
        applyRefCategoryTypeDetails(categoryResponse, refCategoryTypes);
    }

    private static void applyRefCategoryTypeDetails(RefCategoryResponse categoryResponse, List<RefCategoryType> refCategoryTypes) {
        refCategoryTypes.forEach(refCategoryType -> categoryResponse.getRefCategories().stream()
                .filter(refCategory -> refCategory.getRefCategoryType() != null &&
                        hasText(refCategory.getRefCategoryType().getId()) &&
                        hasText(refCategoryType.getId()))
                .filter(refCategory -> refCategory.getRefCategoryType().getId().equals(refCategoryType.getId()))
                .forEach(refCategory -> refCategory.getRefCategoryType().setDescription(refCategoryType.getDescription())));
    }

    /**
     * The category list comes back sorted by category type description from service database
     * But within a category type, it is not sorted by category description
     * (eg: DMV FEES comes after PUBLIC TRANSPORTATION in AUTO & TRANSPORT)
     * This method fixes that
     */
    public static RefCategoryResponse sortWithinRefCategoryType(RefCategoryResponse categoryResponse) {
        // final list of sorted ref categories
        List<RefCategory> finalSortedList = new LinkedList<>();
        // since input is a list, put them in a map by refCategoryTypeId
        Map<String, List<RefCategory>> unsortedMap = categoryResponse.getRefCategories().stream()
                .collect(groupingBy(refCategory -> refCategory.getRefCategoryType().getDescription()));
        // sort this map by key first
        Map<String, List<RefCategory>> sortedMap = unsortedMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        // now sort within each map
        for (Map.Entry<String, List<RefCategory>> entry : sortedMap.entrySet()) {
            finalSortedList.addAll(sortWithinRefCategoryType(entry.getValue()));
        }
        // now return the sorted list in the response object
        return RefCategoryResponse.builder()
                .refCategories(finalSortedList)
                .build();
    }

    public static List<RefCategory> sortWithinRefCategoryType(List<RefCategory> unsortedList) {
        return unsortedList.stream()
                .sorted(comparing(RefCategory::getDescription))
                .collect(toList());
    }
}
