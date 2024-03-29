package pets.service.actuator;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "clearCaches")
public class ClearCachesEndpoint {

  private static final Logger logger = LoggerFactory.getLogger(ClearCachesEndpoint.class);

  private final CacheManager cacheManager;

  public ClearCachesEndpoint(CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  @ReadOperation
  public String clearCaches() {
    logger.info("Firing Clear Caches Actuator!!!");
    cacheManager
        .getCacheNames()
        .forEach(cacheName -> requireNonNull(cacheManager.getCache(cacheName)).clear());
    return "Finished Clear Caches Actuator!!!";
  }

  @ReadOperation
  public String clearCache(@Selector String cacheNameSelector) {
    logger.info("Firing Clear Cache Actuator: {} !!!", cacheNameSelector);
    requireNonNull(cacheManager.getCache(cacheNameSelector)).clear();
    return format("Finished Clear Cache Actuator: %s !!!", cacheNameSelector);
  }
}
