package service;

import constant.QueryTypeEnum;
import constant.TimeEnum;
import entity.BugDetail;

import java.util.List;
import java.util.Map;

/**
 * @author gaozijie
 * @date 2023-05-19
 */
public interface FileService {

    /**
     * 导出为txt
     *
     * @param bugDetails    bug集合
     * @param queryTypeEnum 查询类型(1：简易, 2:详细)
     * @param timeEnum
     * @return
     */
    boolean exportToTxt(List<BugDetail> bugDetails,
                        QueryTypeEnum queryTypeEnum,
                        TimeEnum timeEnum);

    /**
     * 加载配置文件
     * @return
     */
    Map<String, String> loadConfFile();
}
