import junit.framework.TestCase;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.Mockito.when;

public class AnalyticsExpirationDateManagerOriginalTest extends TestCase {

    private static final long ONE_HOUR_TIMEOUT = 1000 * 60 * 60;
    private static final long TWO_HOUR_TIMEOUT = ONE_HOUR_TIMEOUT * 2;

    private Map<Parameter, Long> analyticsToTimeout;
    private long defaultTimeout;

    private Parameter minTimeoutParam;
    @Mock
    private CacheKeyImpl<Parameter> cacheKey;

    protected void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        this.minTimeoutParam = new Parameter("minTimeout", "type");

        when(cacheKey.getFirstKey()).thenReturn(minTimeoutParam);

        this.analyticsToTimeout = new HashMap<Parameter, Long>();
        this.defaultTimeout = 0;
    }

    public void testGetExpirationDateWhenAnalyticsToTimeoutsAndCacheKeyAreEmpty() {
        AnalyticsExpirationDateManager<Long> manager = new AnalyticsExpirationDateManager<Long>(analyticsToTimeout, defaultTimeout);
        Date date = manager.getExpirationDate(cacheKey, 0L);
        assertNotNull(date);
    }

    public void testGetExpirationDateWithMinimumTimeoutOfOneHour() {
        this.analyticsToTimeout.put(this.minTimeoutParam, ONE_HOUR_TIMEOUT);
        Collection<Parameter> cacheKeysWithMinTimeoutParam = new ArrayList<Parameter>();
        cacheKeysWithMinTimeoutParam.add(this.minTimeoutParam);
        when(this.cacheKey.getKeys()).thenReturn(cacheKeysWithMinTimeoutParam);

        AnalyticsExpirationDateManager<Long> manager = new AnalyticsExpirationDateManager<Long>(analyticsToTimeout, defaultTimeout);
        Date date = manager.getExpirationDate(cacheKey, 0L);

        assertNotNull(date);
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.setTime(date);

        Calendar currentDate = Calendar.getInstance();

        // Check if expiration date is one hour ahead current date.
        int expirationDateHour = expirationDate.get(Calendar.HOUR_OF_DAY);
        int currentDateHour = currentDate.get(Calendar.HOUR_OF_DAY);
        assertTrue(expirationDateHour - currentDateHour == 1);
    }

    public void testGetExpirationDateWhenCacheKeyIsNullAndDefaultTimeoutIsOneHour() {
        CacheKeyImpl<Parameter> NULL_CACHEKEY = null;
        AnalyticsExpirationDateManager<Long> manager = new AnalyticsExpirationDateManager<Long>(analyticsToTimeout, ONE_HOUR_TIMEOUT);
        Date date = manager.getExpirationDate(NULL_CACHEKEY, 0L);

        assertNotNull(date);
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.setTime(date);

        Calendar currentDate = Calendar.getInstance();

        // Check if expiration date hour is the same of current date hour.
        // When cache key is null, system date and time is returned and default timeout is not used.
        int expirationDateHour = expirationDate.get(Calendar.HOUR_OF_DAY);
        int currentDateHour = currentDate.get(Calendar.HOUR_OF_DAY);
        assertTrue(expirationDateHour - currentDateHour == 0);
    }

    public void testGetExpirationDateWithDefaultTimeout() {
        // Default timeout is used when no time out is specified.
        Collection<Parameter> cacheKeysWithoutTimeoutParam = new ArrayList<Parameter>();
        cacheKeysWithoutTimeoutParam.add(new Parameter("name", "type"));
        when(this.cacheKey.getKeys()).thenReturn(cacheKeysWithoutTimeoutParam);

        AnalyticsExpirationDateManager<Long> manager = new AnalyticsExpirationDateManager<Long>(analyticsToTimeout, ONE_HOUR_TIMEOUT);
        Date date = manager.getExpirationDate(cacheKey, 0L);

        assertNotNull(date);
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.setTime(date);

        Calendar currentDate = Calendar.getInstance();

        // Check if expiration date is one hour ahead current date.
        int expirationDateHour = expirationDate.get(Calendar.HOUR_OF_DAY);
        int currentDateHour = currentDate.get(Calendar.HOUR_OF_DAY);
        assertTrue(expirationDateHour - currentDateHour == 1);
    }

    public void testGetExpirationDateWhenMinTimeoutIsSetAfterCreation() {
        AnalyticsExpirationDateManager<Long> manager = new AnalyticsExpirationDateManager<Long>(analyticsToTimeout, ONE_HOUR_TIMEOUT);
        manager.setExpirationTimeout(this.minTimeoutParam.getName(), TWO_HOUR_TIMEOUT);

        Date date = manager.getExpirationDate(cacheKey, 0L);

        assertNotNull(date);
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.setTime(date);

        Calendar currentDate = Calendar.getInstance();

        // Check if expiration date is two hour ahead current date.
        int expirationDateHour = expirationDate.get(Calendar.HOUR_OF_DAY);
        int currentDateHour = currentDate.get(Calendar.HOUR_OF_DAY);
        assertTrue("Error", expirationDateHour - currentDateHour == 2);
    }

}
