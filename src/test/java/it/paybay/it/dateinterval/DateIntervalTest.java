package it.paybay.it.dateinterval;

import junit.framework.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

/**
 * Created by fabio on 04/11/15.
 */
public class DateIntervalTest {

    public static final int START_YEAR = 2013;
    public static final Month START_MONTH = Month.MARCH;
    public static final int START_DAY = 1;

    public static final int END_YEAR = 2014;
    public static final Month END_MONTH = Month.APRIL;
    public static final int END_DAY = 7;

    public static final DateInterval DATE_INTERVAL = DateInterval.create().start(START_YEAR, START_MONTH, START_DAY).end(END_YEAR, END_MONTH, END_DAY);
    public static final DateInterval DATE_INTERVAL_OPEN_LEFT = DateInterval.create().end(END_YEAR, END_MONTH, END_DAY);
    public static final DateInterval DATE_INTERVAL_OPEN_RIGHT = DateInterval.create().start(START_YEAR, START_MONTH, START_DAY);
    public static final DateInterval DATE_INTERVAL_OPEN_LEFT_SHORT = DateInterval.create().end(START_YEAR, START_MONTH, START_DAY);
    public static final DateInterval DATE_INTERVAL_OPEN_RIGHT_SHORT = DateInterval.create().start(END_YEAR, END_MONTH, END_DAY);

    @Test
    public void isValidContainer() {

        LocalDate start = DATE_INTERVAL.getStart();
        Assert.assertNotNull(start);

        LocalDate end = DATE_INTERVAL.getEnd();
        Assert.assertNotNull(end);

        Assert.assertEquals(START_YEAR, start.getYear());
        Assert.assertEquals(START_MONTH, start.getMonth());
        Assert.assertEquals(START_DAY, start.getDayOfMonth());
        Assert.assertEquals(END_YEAR, end.getYear());
        Assert.assertEquals(END_MONTH, end.getMonth());
        Assert.assertEquals(END_DAY, end.getDayOfMonth());

    }

    @Test
    public void equalSameInterval() {

        Assert.assertEquals(DATE_INTERVAL, DATE_INTERVAL);

        DateInterval overlap = DATE_INTERVAL.overlap(DATE_INTERVAL);
        Assert.assertEquals(DATE_INTERVAL, overlap);

        DateInterval otherInterval = DateInterval.create().start(START_YEAR, START_MONTH, START_DAY).end(END_YEAR, END_MONTH, END_DAY);
        Assert.assertEquals(DATE_INTERVAL, otherInterval);

        Assert.assertTrue(DATE_INTERVAL.overlaps(DATE_INTERVAL));
        Assert.assertTrue(DATE_INTERVAL.overlaps(overlap));
        Assert.assertTrue(DATE_INTERVAL.overlaps(otherInterval));

    }

    @Test(expected = IllegalArgumentException.class)
    public void invertedInterval() {

        DateInterval.create().start(END_YEAR, END_MONTH, END_DAY).end(START_YEAR, START_MONTH, START_DAY);

    }

    @Test
    public void overlapTest() {

        DateInterval overlapLeftRight = DATE_INTERVAL_OPEN_LEFT.overlap(DATE_INTERVAL_OPEN_RIGHT);
        Assert.assertEquals(DATE_INTERVAL, overlapLeftRight);

        DateInterval overlapRightLeft = DATE_INTERVAL_OPEN_RIGHT.overlap(DATE_INTERVAL_OPEN_LEFT);
        Assert.assertEquals(DATE_INTERVAL, overlapRightLeft);

    }

    @Test
    public void overlapOneDay() {

        DateInterval overlapRight = DATE_INTERVAL_OPEN_LEFT.overlap(DATE_INTERVAL_OPEN_RIGHT_SHORT);
        Assert.assertNotNull(overlapRight);

        LocalDate rightStart = overlapRight.getStart();
        LocalDate rightEnd = overlapRight.getEnd();

        Assert.assertEquals(rightStart, rightEnd);

        Assert.assertEquals(END_YEAR, rightStart.getYear());
        Assert.assertEquals(END_MONTH, rightStart.getMonth());
        Assert.assertEquals(END_DAY, rightStart.getDayOfMonth());
        Assert.assertEquals(END_YEAR, rightEnd.getYear());
        Assert.assertEquals(END_MONTH, rightEnd.getMonth());
        Assert.assertEquals(END_DAY, rightEnd.getDayOfMonth());

        DateInterval overlapLeft = DATE_INTERVAL_OPEN_RIGHT.overlap(DATE_INTERVAL_OPEN_LEFT_SHORT);
        Assert.assertNotNull(overlapLeft);

        LocalDate leftStart = overlapLeft.getStart();
        LocalDate leftEnd = overlapLeft.getEnd();

        Assert.assertEquals(leftStart, leftEnd);

        Assert.assertEquals(START_YEAR, leftStart.getYear());
        Assert.assertEquals(START_MONTH, leftStart.getMonth());
        Assert.assertEquals(START_DAY, leftStart.getDayOfMonth());
        Assert.assertEquals(START_YEAR, leftEnd.getYear());
        Assert.assertEquals(START_MONTH, leftEnd.getMonth());
        Assert.assertEquals(START_DAY, leftEnd.getDayOfMonth());

    }

    @Test
    public void overlapNone() {

        DateInterval overlap = DATE_INTERVAL_OPEN_LEFT_SHORT.overlap(DATE_INTERVAL_OPEN_RIGHT_SHORT);
        Assert.assertNull(overlap);

        overlap = DATE_INTERVAL_OPEN_RIGHT_SHORT.overlap(DATE_INTERVAL_OPEN_LEFT_SHORT);
        Assert.assertNull(overlap);

    }


}
