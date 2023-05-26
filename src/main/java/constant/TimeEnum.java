package constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author gaozijie
 * @date 2023-05-04
 */
@Getter
@AllArgsConstructor
public enum TimeEnum {
    /**
     * 本日
     */
    TODAY("1","today", "本日"),
    /**
     * 本周
     */
    THIS_WEEK("2", "thisweek", "本周"),
    /**
     * 本月
     */
    THIS_MONTH("3", "thismonth", "本月");

    private final String num;

    private final String value;

    private final String desc;

    public static TimeEnum instance(String num) {
        for (TimeEnum timeEnum : TimeEnum.values()) {
            if (timeEnum.num.equals(num)) {
                return timeEnum;
            }
        }
        return null;
    }
}
