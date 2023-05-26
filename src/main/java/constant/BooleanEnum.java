package constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author gaozijie
 * @date 2023-05-24
 */
@Getter
@AllArgsConstructor
public enum BooleanEnum {
    /**
     * 是
     */
    YES("y"),
    /**
     * 否
     */
    NO("n");

    private final String value;
}
