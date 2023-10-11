package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaozijie
 * @date 2023-10-10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDetail {
    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务描述
     */
    private String taskDesc;

    /**
     * 项目类型
     */
    private String productName;

    /**
     * 优先级
     */
    private Integer priority;
}
