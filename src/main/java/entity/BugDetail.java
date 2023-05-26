package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaozijie
 * @date 2023-05-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BugDetail {
    /**
     * bugId
     */
    private String bugId;
    /**
     * bug描述
     */
    private String bugDesc;
    /**
     * 项目类型
     */
    private String productName;
    /**
     * 优先级
     */
    private Integer priority;
}
