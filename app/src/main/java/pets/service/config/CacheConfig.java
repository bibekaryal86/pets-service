package pets.service.config;

import static java.util.Objects.requireNonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired private CacheManager cacheManager;
  @Autowired private AccountTypeService accountTypeService;
  @Autowired private BankService bankService;
  @Autowired private CategoryService categoryService;
  @Autowired private TransactionTypeService transactionTypeService;

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
