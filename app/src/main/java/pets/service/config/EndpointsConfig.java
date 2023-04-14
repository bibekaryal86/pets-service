package pets.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EndpointsConfig {

  @Bean
  public String getAccountByIdUrl(@Value("${account_get_by_id}") String getAccountByIdUrl) {
    return getAccountByIdUrl;
  }

  @Bean
  public String getAccountsByUserUrl(
      @Value("${accounts_get_by_user}") String getAccountsByUserUrl) {
    return getAccountsByUserUrl;
  }

  @Bean
  public String saveNewAccountUrl(@Value("${account_save_new}") String saveNewAccountUrl) {
    return saveNewAccountUrl;
  }

  @Bean
  public String updateAccountPutUrl(@Value("${account_update_put}") String updateAccountPutUrl) {
    return updateAccountPutUrl;
  }

  @Bean
  public String deleteAccountUrl(@Value("${account_delete}") String deleteAccountUrl) {
    return deleteAccountUrl;
  }

  @Bean
  public String getAllAccountTypesUrl(
      @Value("${account_types_get_all}") String getAllAccountTypesUrl) {
    return getAllAccountTypesUrl;
  }

  @Bean
  public String saveNewAccountTypeUrl(
      @Value("${account_type_save_new}") String saveNewAccountTypeUrl) {
    return saveNewAccountTypeUrl;
  }

  @Bean
  public String updateAccountTypeUrl(@Value("${account_type_update}") String updateAccountTypeUrl) {
    return updateAccountTypeUrl;
  }

  @Bean
  public String deleteAccountTypeUrl(@Value("${account_type_delete}") String deleteAccountTypeUrl) {
    return deleteAccountTypeUrl;
  }

  @Bean
  public String getAllBanksUrl(@Value("${banks_get_all}") String getAllBanksUrl) {
    return getAllBanksUrl;
  }

  @Bean
  public String saveNewBankUrl(@Value("${bank_save_new}") String saveNewBankUrl) {
    return saveNewBankUrl;
  }

  @Bean
  public String updateBankUrl(@Value("${bank_update}") String updateBankUrl) {
    return updateBankUrl;
  }

  @Bean
  public String deleteBankUrl(@Value("${bank_delete}") String deleteBankUrl) {
    return deleteBankUrl;
  }

  @Bean
  public String getAllCategoriesUrl(@Value("${categories_get_all}") String getAllCategoriesUrl) {
    return getAllCategoriesUrl;
  }

  @Bean
  public String saveNewCategoryUrl(@Value("${category_save_new}") String saveNewCategoryUrl) {
    return saveNewCategoryUrl;
  }

  @Bean
  public String updateCategoryUrl(@Value("${category_update}") String updateCategoryUrl) {
    return updateCategoryUrl;
  }

  @Bean
  public String deleteCategoryUrl(@Value("${category_delete}") String deleteCategoryUrl) {
    return deleteCategoryUrl;
  }

  @Bean
  public String getAllCategoryTypesUrl(
      @Value("${category_types_get_all}") String getAllCategoryTypesUrl) {
    return getAllCategoryTypesUrl;
  }

  @Bean
  public String saveNewCategoryTypeUrl(
      @Value("${category_type_save_new}") String saveNewCategoryTypeUrl) {
    return saveNewCategoryTypeUrl;
  }

  @Bean
  public String updateCategoryTypeUrl(
      @Value("${category_type_update}") String updateCategoryTypeUrl) {
    return updateCategoryTypeUrl;
  }

  @Bean
  public String deleteCategoryTypeUrl(
      @Value("${category_type_delete}") String deleteCategoryTypeUrl) {
    return deleteCategoryTypeUrl;
  }

  @Bean
  public String getMerchantByIdUrl(@Value("${merchant_get_by_id}") String getMerchantByIdUrl) {
    return getMerchantByIdUrl;
  }

  @Bean
  public String getMerchantsByUserUrl(
      @Value("${merchants_get_by_user}") String getMerchantsByUserUrl) {
    return getMerchantsByUserUrl;
  }

  @Bean
  public String saveNewMerchantUrl(@Value("${merchant_save_new}") String saveNewMerchantUrl) {
    return saveNewMerchantUrl;
  }

  @Bean
  public String updateMerchantUrl(@Value("${merchant_update}") String updateMerchantUrl) {
    return updateMerchantUrl;
  }

  @Bean
  public String deleteMerchantUrl(@Value("${merchant_delete}") String deleteMerchantUrl) {
    return deleteMerchantUrl;
  }

  @Bean
  public String getTransactionByIdUrl(
      @Value("${transaction_get_by_id}") String getTransactionByIdUrl) {
    return getTransactionByIdUrl;
  }

  @Bean
  public String getTransactionsByUserUrl(
      @Value("${transactions_get_by_user}") String getTransactionsByUserUrl) {
    return getTransactionsByUserUrl;
  }

  @Bean
  public String saveNewTransactionUrl(
      @Value("${transaction_save_new}") String saveNewTransactionUrl) {
    return saveNewTransactionUrl;
  }

  @Bean
  public String updateTransactionPutUrl(
      @Value("${transaction_update_put}") String updateTransactionPutUrl) {
    return updateTransactionPutUrl;
  }

  @Bean
  public String deleteTransactionUrl(@Value("${transaction_delete}") String deleteTransactionUrl) {
    return deleteTransactionUrl;
  }

  @Bean
  public String deleteTransactionsByAccountUrl(
      @Value("${transactions_delete_by_account}") String deleteTransactionsByAccountUrl) {
    return deleteTransactionsByAccountUrl;
  }

  @Bean
  public String getAllTransactionTypesUrl(
      @Value("${transaction_types_get_all}") String getAllTransactionTypesUrl) {
    return getAllTransactionTypesUrl;
  }

  @Bean
  public String saveNewTransactionTypeUrl(
      @Value("${transaction_type_save_new}") String saveNewTransactionTypeUrl) {
    return saveNewTransactionTypeUrl;
  }

  @Bean
  public String updateTransactionTypeUrl(
      @Value("${transaction_type_update}") String updateTransactionTypeUrl) {
    return updateTransactionTypeUrl;
  }

  @Bean
  public String deleteTransactionTypeUrl(
      @Value("${transaction_type_delete}") String deleteTransactionTypeUrl) {
    return deleteTransactionTypeUrl;
  }

  @Bean
  public String getUserByUsernameUrl(
      @Value("${user_get_by_username}") String getUserByUsernameUrl) {
    return getUserByUsernameUrl;
  }

  @Bean
  public String getUserByEmailUrl(@Value("${user_get_by_email}") String getUserByEmailUrl) {
    return getUserByEmailUrl;
  }

  @Bean
  public String getUserByPhoneUrl(@Value("${user_get_by_phone}") String getUserByPhoneUrl) {
    return getUserByPhoneUrl;
  }

  @Bean
  public String saveNewUserUrl(@Value("${user_save_new}") String saveNewUserUrl) {
    return saveNewUserUrl;
  }

  @Bean
  public String updateUserPutUrl(@Value("${user_update_put}") String updateUserPutUrl) {
    return updateUserPutUrl;
  }

  @Bean
  public String updateUserPatchUrl(@Value("${user_update_patch}") String updateUserPatchUrl) {
    return updateUserPatchUrl;
  }
}
