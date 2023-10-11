package service;

import entity.BugDetail;
import entity.TaskDetail;

import java.util.List;

/**
 * @author gaozijie
 * @date 2023-10-10
 */
public interface TaskService {

    /**
     * 查询bug列表
     * @param htmlTexts html文本集合
     * @return bug集合
     */
    List<TaskDetail> listTaskDetail(List<String> htmlTexts);

    /**
     * 填充任务详情
     * @param taskDetails 任务
     */
    void fillTaskDetails(List<TaskDetail> taskDetails);
}
