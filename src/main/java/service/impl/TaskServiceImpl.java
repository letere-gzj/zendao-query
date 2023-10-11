package service.impl;

import entity.BugDetail;
import entity.TaskDetail;
import service.HtmlService;
import service.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gaozijie
 * @date 2023-10-10
 */
public class TaskServiceImpl implements TaskService {

    private final HtmlService htmlService = new HtmlServiceImpl();

    @Override
    public List<TaskDetail> listTaskDetail(List<String> htmlTexts) {
        String taskPattern = "<span.*> 完成了</span>\\s*<span.*>任务</span>\\s*<a.*taskID=(\\d+).*>(.+)</a>";
        List<TaskDetail> taskDetails = new ArrayList<>(16);
        Matcher matcher;
        for (String htmlText : htmlTexts) {
            matcher = Pattern.compile(taskPattern).matcher(htmlText);
            while (matcher.find()) {
                TaskDetail taskDetail = new TaskDetail();
                taskDetail.setTaskId(matcher.group(1));
                taskDetail.setTaskDesc(matcher.group(2));
                taskDetails.add(taskDetail);
            }
        }
        return taskDetails;
    }

    @Override
    public void fillTaskDetails(List<TaskDetail> taskDetails) {
        String htmlText;
        for (TaskDetail taskDetail : taskDetails) {
            htmlText = htmlService.getTaskDetailHtmlText(taskDetail.getTaskId());
            this.fillTaskDetail(htmlText, taskDetail);
        }
    }

    /**
     * 补充任务详情数据
     * @param htmlText 任务详情html文本
     * @param taskDetail 任务详情
     */
    private void fillTaskDetail(String htmlText, TaskDetail taskDetail) {
        // 查询产品名称
        String productPattern = "<th.*>所属迭代</th>\\s*<td>\\s*<a.*>(.+)</a>";
        Matcher matcher = Pattern.compile(productPattern).matcher(htmlText);
        String productName = matcher.find() ? matcher.group(1) : "";
        // 查询优先级
        String priorityPattern = "<th.*>优先级</th>\\s*<td>\\s*<span.*>(\\d+)</span>";
        matcher = Pattern.compile(priorityPattern).matcher(htmlText);
        String priority = matcher.find() ? matcher.group(1) : "";
        // 数据补充
        taskDetail.setProductName(productName);
        taskDetail.setPriority(Integer.valueOf(priority));
    }
}
