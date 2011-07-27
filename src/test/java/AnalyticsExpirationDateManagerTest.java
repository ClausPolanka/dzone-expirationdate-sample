import java.util.Date;

import static AnalyticsExpirationDateManagerBuilder.aExpirationDateManager;
import static AnalyticsExpirationDateManagerBuilder.hours;

public class AnalyticsExpirationDateManagerTest extends BaseTestForAnalyticsExperationDateManager {

    public void testExpirationTimeWithJustDefaultValues() {
        manager = aExpirationDateManager().build();
        Date cacheExpiration = manager.getExpirationDate(cacheKey, 0L);
        assertThat(dateOf(cacheExpiration).is(0).hoursAhead().from(systemDate));
    }

    public void testExpirationTimeWithMinimunTimeoutOfOneHour() {
        addMinimunTimeoutToCache();
        manager = aExpirationDateManager()
                    .withMinimunTimeout(hours(1))
                    .build();
        Date cacheExpiration = manager.getExpirationDate(cacheKey, 0L);
        assertThat(dateOf(cacheExpiration).is(1).hoursAhead().from(systemDate));
    }

    public void testExpirationTimeWhenCacheKeyIsNullAndDefaultTimeoutIsOneHour() {
        manager = aExpirationDateManager()
                    .withDefaultTimeout(hours(1))
                    .build();
        Date cacheExpiration = manager.getExpirationDate(NULL_CACHEKEY, 0L);
        // When cache key is null, system date and time is returned and default timeout is not used.
        assertThat(dateOf(cacheExpiration).is(0).hoursAhead().from(systemDate));
    }

    public void testExpirationTimeWithDefaultTimeout() {
        doNotIncludeMinimunTimeoutInCache();
        manager = aExpirationDateManager()
                    .withDefaultTimeout(hours(1))
                    .build();
        Date cacheExpiration = manager.getExpirationDate(cacheKey, 0L);
        assertThat(dateOf(cacheExpiration).is(1).hoursAhead().from(systemDate));
    }

    public void testExpirationTimeWhenExpirationTimeoutIsSet() {
        manager = aExpirationDateManager()
                    .withDefaultTimeout(hours(1))
                    .withExpirationTimeout(hours(2))
                    .build();
        Date cacheExpiration = manager.getExpirationDate(cacheKey, 0L);
        // Expiration timeout has precedence over default timeout.
        assertThat(dateOf(cacheExpiration).is(2).hoursAhead().from(systemDate));
    }

}
