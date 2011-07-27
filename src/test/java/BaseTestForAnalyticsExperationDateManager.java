import junit.framework.TestCase;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static org.mockito.Mockito.when;

public abstract class BaseTestForAnalyticsExperationDateManager extends TestCase {

    protected Parameter minTimeoutParam;
    @Mock
    protected CacheKeyImpl<Parameter> cacheKey;
    protected Date systemDate;
    protected CacheKeyImpl<Parameter> NULL_CACHEKEY = null;
    protected AnalyticsExpirationDateManager<Long> manager;

    protected void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.minTimeoutParam = new Parameter("minTimeout", "type");
        when(cacheKey.getFirstKey()).thenReturn(minTimeoutParam);
        this.systemDate = new Date();
    }

    protected void assertThat(boolean condition) {
        assertTrue(condition);
    }

    protected void addMinimunTimeoutToCache() {
        this.configureCacheResponse(this.minTimeoutParam);
    }

    protected void doNotIncludeMinimunTimeoutInCache() {
        this.configureCacheResponse(new Parameter("name", "type"));
    }

    private void configureCacheResponse(Parameter parameter) {
        Collection<Parameter> cacheKeysWithMinTimeoutParam = new ArrayList<Parameter>();
        cacheKeysWithMinTimeoutParam.add(parameter);
        when(this.cacheKey.getKeys()).thenReturn(cacheKeysWithMinTimeoutParam);
    }
}
