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
     * @param htmlTexts html文本集合
     * @return bug集合
     */
    List<BugDetail> listBugDetail(List<String> htmlTexts);

    /**
     * 填充bug详情
     * @param bugDetails bug详情集合
     */
    void fillBugDetails(List<BugDetail> bugDetails);
}
