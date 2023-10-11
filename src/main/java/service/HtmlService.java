package service;

import constant.TimeEnum;

import java.util.List;

/**
 * @author gaozijie
 * @date 2023-10-10
 */
public interface HtmlService {

    /**
     * 获取'动态'页面html文本<p>
     * （有可能有多个页面，所以用list进行返回）
     * @param time 时间
     * @return html文本数组
     */
    List<String> getDynamicHtmlText(TimeEnum time);

    /**
     * 获取bug详情页面的html文本
     * @param bugId bugId
     * @return bug详情html文本
     */
    String getBugDetailHtmlText(String bugId);

    /**
     * 获取任务详情页面的html文本
     * @param taskId 任务id
     * @return 任务详情html文本
     */
    String getTaskDetailHtmlText(String taskId);
}
