import java.util.HashMap;
import java.util.Map;

public class AnalyticsExpirationDateManagerBuilder {

    protected static final long ONE_HOUR = 1000 * 60 * 60;

    protected Parameter minTimeoutParam;
    private AnalyticsExpirationDateManager<Long> manager;
    private Map<Parameter, Long> analyticsToTimeouts = new HashMap<Parameter, Long>();
    protected long defaultTimeout = 0;
    private Long expirationTimeout;
    private Long minimunTimeout;

    private AnalyticsExpirationDateManagerBuilder() {
        this.minTimeoutParam = new Parameter("minTimeout", "type");
    }

    public static AnalyticsExpirationDateManagerBuilder aExpirationDateManager() {
        return new AnalyticsExpirationDateManagerBuilder();
    }

    public static long hours(int quantity) {
        return quantity * ONE_HOUR;
    }

    public AnalyticsExpirationDateManagerBuilder withDefaultTimeout(long milliseconds) {
        this.defaultTimeout = milliseconds;
        return this;
    }

    public AnalyticsExpirationDateManagerBuilder withExpirationTimeout(long milliseconds) {
        this.expirationTimeout = new Long(milliseconds);
        return this;
    }

    public AnalyticsExpirationDateManagerBuilder withMinimunTimeout(long milliseconds) {
        this.minimunTimeout = new Long(milliseconds);
        return this;
    }

    public AnalyticsExpirationDateManager<Long> build() {
        if (this.minimunTimeout != null) {
            analyticsToTimeouts.put(minTimeoutParam, minimunTimeout);
        }
        this.manager = new AnalyticsExpirationDateManager<Long>(analyticsToTimeouts, defaultTimeout);
        if (this.expirationTimeout != null) {
            this.manager.setExpirationTimeout(minTimeoutParam.getName(), expirationTimeout);
        }
        return this.manager;
    }

}

