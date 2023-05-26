package service;

import entity.BugDetail;

import java.util.List;

/**
 * @author gaozijie
 * @date 2023-05-04
 */
public interface BugService {
    /**
     * 获取cookie
     * @return
     */
    String getCookie();

    /**
     * 查询bug列表
     * @param cookie
     * @param time
     * @return
     */
    List<BugDetail> getBugDetails(String cookie, String time);

    /**
     * 填充bug详情
     *
     * @param cookie
     * @param bugDetails
     */
    void fillBugDetails(String cookie, List<BugDetail> bugDetails);
}
