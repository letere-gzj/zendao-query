package constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author gaozijie
 * @date 2023-05-19
 */
@Getter
@AllArgsConstructor
public enum QueryTypeEnum {
    /**
     * 简易
     */
    SIMPLE("1", "简易"),
    /**
     * 详细
     */
    DETAIL("2", "详细");

    private final String num;

    private final String desc;

    public static QueryTypeEnum instance(String num) {
        for (QueryTypeEnum queryTypeEnum : QueryTypeEnum.values()) {
            if (queryTypeEnum.num.equals(num)) {
                return queryTypeEnum;
            }
        }
        return null;
    }
}
