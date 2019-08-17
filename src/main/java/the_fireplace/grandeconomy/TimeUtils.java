package the_fireplace.grandeconomy;

public class TimeUtils {
    public static long getCurrentDay() {
        return millisToDays(getCurrentServerTimeMillis());
    }

    public static long getCurrentServerTimeMillis() {
        return System.currentTimeMillis();
    }

    public static long millisToDays(long time) {
        long day = 1000 * 60 * 24 * 60;
        return time / day;
    }
}
