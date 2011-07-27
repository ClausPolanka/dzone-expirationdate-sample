import java.util.Date;

public class DateComparator {

    private Date origin;
    private Date target;
    private long milliseconds;
    private long unitsOfTime;

    private DateComparator(Date origin) {
        this.origin = origin;
    }

    public static DateComparator date(Date origin) {
        return new DateComparator(origin);
    }

    public DateComparator is(long unitsOfTime) {
        this.unitsOfTime = unitsOfTime;
        return this;
    }

    public DateComparator hoursAhead() {
        this.milliseconds = unitsOfTime * 60 * 60 * 1000;
        return this;
    }

    public static long hours(int hours) {
        return hoursInMillis(hours);
    }

    private static long hoursInMillis(int hours) {
        return hours * 60 * 60 * 1000;
    }

    public boolean from(Date date) {
        this.target = date;
        return this.checkDifference();
    }

    private boolean checkDifference() {
        return (origin.getTime() - target.getTime() >= this.milliseconds);
    }
}
