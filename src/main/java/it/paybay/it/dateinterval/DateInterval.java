package it.paybay.it.dateinterval;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

/**
 * Created by fabio on 04/11/15.
 */
public class DateInterval implements Serializable {

    protected LocalDate start;
    protected LocalDate end;

    public DateInterval() {
    }

    public DateInterval(LocalDate start) {
    }

    public DateInterval(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("illegal date interval :: the end can not precede the start");
        }
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public boolean overlaps(LocalDateTime moment) {

        return overlaps(moment.toLocalDate());

    }

    // builder
    public static DateInterval create() {
        return new DateInterval();
    }

    public DateInterval start(int year, Month month, int dayOfMonth) {
        this.start = LocalDate.of(year, month, dayOfMonth);

        if (end != null && end.isBefore(start)) {
            throw new IllegalArgumentException("illegal date interval :: the end can not precede the start");
        }

        return this;
    }

    public DateInterval end(int year, Month month, int dayOfMonth) {
        this.end = LocalDate.of(year, month, dayOfMonth);

        if (start != null && end.isBefore(start)) {
            throw new IllegalArgumentException("illegal date interval :: the end can not precede the start");
        }

        return this;
    }

    public boolean overlaps(LocalDate day) {

        if (start == null && end == null) {
            return true;
        }

        if (start == null) {
            return !end.isBefore(day);
        }

        if (end == null) {
            return !start.isAfter(day);
        }

        return !end.isBefore(day) && !start.isAfter(day);

    }

    public boolean overlaps(DateInterval interval) {

        LocalDate otherStart = interval.getStart();
        LocalDate otherEnd = interval.getEnd();

        // if one of the interval is always --> always overlaps
        if ((start == null && end == null) || (otherStart == null && otherEnd == null)) {
            return true;
        }

        // if both are endless on the left or on the right side --> overlaps
        if ((start == null && otherStart == null) || (end == null && otherEnd == null)) {
            return true;
        }

        if (start == null || otherEnd == null) {
            return !otherStart.isAfter(end);
        }

        if (otherStart == null || end == null) {
            return !start.isAfter(otherEnd);
        }

        return !start.isAfter(otherEnd) && !otherStart.isAfter(end);

    }

    private static LocalDate maxStart(LocalDate a, LocalDate b) {

        // a is -infinity
        if (a == null) {
            return b;
        }

        // b is -infinity
        if (b == null) {
            return a;
        }

        return (a.isAfter(b)) ? a : b;

    }

    private static LocalDate minEnd(LocalDate a, LocalDate b) {

        // a is +infinity --> b is less of equal than a
        if (a == null) {
            return b;
        }

        // b is +infinity --> a is less of equal than b
        if (b == null) {
            return a;
        }

        return (a.isBefore(b)) ? a : b;

    }

    public DateInterval overlap(DateInterval interval) {

        if (overlaps(interval) == false) {
            return null;
        }

        LocalDate start = DateInterval.maxStart(this.start, interval.getStart());
        LocalDate end = DateInterval.minEnd(this.end, interval.getEnd());
        return new DateInterval(start, end);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DateInterval that = (DateInterval) o;

        if (start != null ? !start.equals(that.start) : that.start != null) return false;
        return !(end != null ? !end.equals(that.end) : that.end != null);

    }

    @Override
    public int hashCode() {
        int result = start != null ? start.hashCode() : 0;
        result = 31 * result + (end != null ? end.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DateInterval{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

}
