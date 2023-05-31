package service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileWriter;
import constant.QueryTypeEnum;
import constant.ZendaoConstant;
import entity.BugDetail;
import service.FileService;
import util.StrUtil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
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
    public boolean exportToTxt(List<BugDetail> bugDetails, QueryTypeEnum queryTypeEnum) {
        if (CollectionUtil.isEmpty(bugDetails)) {
            return false;
        }
        // 拼接导出的文本内容
        String context;
        if (Objects.equals(queryTypeEnum, QueryTypeEnum.SIMPLE)) {
            context = bugDetails.stream().map(BugDetail::getBugDesc)
                    .collect(Collectors.joining("\n"));
        }
        else if (Objects.equals(queryTypeEnum, QueryTypeEnum.DETAIL)) {
            context = bugDetails.stream().map(x -> StrUtil.fillWithBlank(x.getProductName(), 5) + '\t' + x.getPriority() + '\t' + x.getBugDesc())
                    .collect(Collectors.joining("\n"));
        }
        else {
            throw new RuntimeException("queryType参数错误");
        }
        // 导出为txt
        FileWriter fileWriter = new FileWriter(ZendaoConstant.FILE_EXPORT_PATH + "/"+ LocalDate.now() +".txt");
        fileWriter.write(context);
        return true;
    }

    @Override
    public Map<String, String> loadConfFile() {
        String conf;
        try {
            conf = IoUtil.read(new FileReader(ZendaoConstant.CONF_PATH));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String[] paramStrs = conf.split("\r\n");
        Map<String, String> paramMap = new HashMap<>();
        for (String paramStr : paramStrs) {
            String[] param = paramStr.split(": ");
            paramMap.put(param[0], param[1]);
        }
        return paramMap;
    }
}
