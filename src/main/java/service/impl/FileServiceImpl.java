package service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileWriter;
import constant.QueryTypeEnum;
import constant.TimeEnum;
import constant.ZendaoConstant;
import entity.BugDetail;
import service.FileService;
import util.DateUtil;
import util.StrUtil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author gaozijie
 * @date 2023-05-19
 */
public class FileServiceImpl implements FileService {

    @Override
    public boolean exportToTxt(List<BugDetail> bugDetails, QueryTypeEnum queryTypeEnum, TimeEnum timeEnum) {
        if (CollectionUtil.isEmpty(bugDetails)) {
            return false;
        }
        // 拼接文件名
        String fileName;
        switch (timeEnum) {
            case TODAY:
                fileName = LocalDate.now().toString();
                break;
            case THIS_WEEK:
                LocalDateTime weekStart = DateUtil.beginOfWeek(LocalDateTime.now()).getLocalDateTime();
                LocalDateTime weekEnd = weekStart.plusDays(7);
                fileName = weekStart.toLocalDate() + " to " + weekEnd.toLocalDate();
                break;
            case THIS_MONTH:
                fileName = LocalDate.now().toString().substring(0, 7);
                break;
            default:
                throw new RuntimeException("time参数错误");
        }
        // 拼接导出的文本内容
        String context;
        if (Objects.equals(queryTypeEnum, QueryTypeEnum.SIMPLE)) {
            fileName += "_简易";
            context = bugDetails.stream().map(BugDetail::getBugDesc)
                    .collect(Collectors.joining("\n"));
        }
        else if (Objects.equals(queryTypeEnum, QueryTypeEnum.DETAIL)) {
            fileName += "_详细";
            context = bugDetails.stream().map(x -> StrUtil.fillWithBlank(x.getProductName(), 5) + '\t' + x.getPriority() + '\t' + x.getBugDesc())
                    .collect(Collectors.joining("\n"));
        }
        else {
            throw new RuntimeException("queryType参数错误");
        }
        // 导出为txt
        FileWriter fileWriter = new FileWriter(ZendaoConstant.FILE_EXPORT_PATH + "/"+ fileName +".txt");
        fileWriter.write(context);
        return true;
    }
}
