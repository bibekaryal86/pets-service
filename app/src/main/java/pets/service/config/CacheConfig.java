package pets.service.config;

import static java.util.Objects.requireNonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import pets.service.service.AccountTypeService;
import pets.service.service.BankService;
import pets.service.service.CategoryService;
import pets.service.service.TransactionTypeService;

@Configuration
public class CacheConfig {

  private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);

  private final CacheManager cacheManager;
  private final AccountTypeService accountTypeService;
  private final BankService bankService;
  private final CategoryService categoryService;
  private final TransactionTypeService transactionTypeService;

  public CacheConfig(
      CacheManager cacheManager,
      AccountTypeService accountTypeService,
      BankService bankService,
      CategoryService categoryService,
      TransactionTypeService transactionTypeService) {
    this.cacheManager = cacheManager;
    this.accountTypeService = accountTypeService;
    this.bankService = bankService;
    this.categoryService = categoryService;
    this.transactionTypeService = transactionTypeService;
  }

  @Scheduled(cron = "0 0 0 * * *")
  protected void putAllCache() throws InterruptedException {
    logger.info("Firing Cache Evict!!!");
    cacheManager
        .getCacheNames()
        .forEach(cacheName -> requireNonNull(cacheManager.getCache(cacheName)).clear());

    Thread.sleep(5000);

    logger.info("Firing All Cache!!!");
    accountTypeService.getAllAccountTypes();
    bankService.getAllBanks();
    categoryService.getAllCategories(null, null);
    // category type cache is enabled when calling get all categories
    transactionTypeService.getAllTransactionTypes();
  }
}
