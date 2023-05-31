package service;

import entity.BugDetail;

import java.util.List;

/**
 * @author gaozijie
 * @date 2023-05-04
 */
public interface BugService {
    /**
     * 查询bug列表
     * @param time
     * @return
     */
    List<BugDetail> getBugDetails(String time);

    /**
     * 填充bug详情
     *
     * @param bugDetails
     */
    void fillBugDetails(List<BugDetail> bugDetails);
}
