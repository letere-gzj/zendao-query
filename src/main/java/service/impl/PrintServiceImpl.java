package service.impl;

import cn.hutool.core.collection.CollectionUtil;
import constant.BooleanEnum;
import constant.QueryTypeEnum;
import constant.TimeEnum;
import constant.ZendaoConstant;
import entity.BugDetail;
import service.BugService;
import service.FileService;
import service.PrintService;
import util.StrUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gaozijie
 * @date 2023-05-10
 */
public class PrintServiceImpl implements PrintService {

    private final BugService bugService = new BugServiceImpl();

    private final FileService fileService = new FileServiceImpl();

    @Override
    public void printQueryBug() {
        Scanner scanner = new Scanner(System.in);
        // 获取查询方式
        System.out.print("请输入查询方式(1:简易查询, 2:详细查询)：");
        QueryTypeEnum queryTypeEnum;
        while (true) {
            queryTypeEnum = QueryTypeEnum.instance(scanner.nextLine());
            if (queryTypeEnum != null) {
                break;
            } else {
                System.out.print("输入有误，请重新输入：");
            }
        }
        // 获取查询时间
        System.out.print("请输入查询时间(1:本日, 2:本周, 3:本月)：");
        TimeEnum time;
        while (true) {
            String timeNo = scanner.nextLine();
            time = TimeEnum.instance(timeNo);
            if (time != null) {
                break;
            } else {
                System.out.print("输入有误，请重新输入：");
            }
        }
        // 爬虫查询bug
        String cookie = bugService.getCookie();
        List<BugDetail> bugDetails = bugService.getBugDetails(cookie, time.getValue());
        // 打印bug信息
        if (Objects.equals(queryTypeEnum, QueryTypeEnum.SIMPLE)) {
            this.simplePrint(bugDetails, time);
        } else {
            bugService.fillBugDetails(cookie, bugDetails);
            this.detailedPrint(bugDetails, time);
        }
        // 文件导出
        if (CollectionUtil.isEmpty(bugDetails)) {
            return;
        }
        System.out.print("是否导出为txt文件(y:是, n:否)：");
        boolean isExportFile = Objects.equals(scanner.nextLine(), BooleanEnum.YES.getValue());
        if (isExportFile) {
            this.printFileExport(bugDetails, queryTypeEnum);
        }
    }

    /**
     * 简易打印bug
     * @param bugDetails bug详情
     */
    private void simplePrint(List<BugDetail> bugDetails, TimeEnum time) {
        System.out.println("\n-----------------------------------");
        System.out.println(time.getDesc() + "解决的bug个数：" + bugDetails.size());
        if (!CollectionUtil.isEmpty(bugDetails)) {
            System.out.println(time.getDesc() + "解决的bug详情如下：");
            int index = 1;
            for (BugDetail bugDetail : bugDetails) {
                System.out.printf("%d.%s%n", index++, bugDetail.getBugDesc());
            }
        }
        System.out.println("-----------------------------------\n");
    }

    /**
     * 详细打印bug
     * @param bugDetails bug详情
     */
    private void detailedPrint(List<BugDetail> bugDetails, TimeEnum time) {
        Map<String, List<BugDetail>> productBugMap = new HashMap<>(16);
        Map<Integer, List<BugDetail>> priorityBugMap = new TreeMap<>();
        // bug分组
        List<BugDetail> tempBugDetails;
        for (BugDetail bugDetail : bugDetails) {
            // 按产品分组
            tempBugDetails = productBugMap.getOrDefault(bugDetail.getProductName(), CollectionUtil.newArrayList());
            tempBugDetails.add(bugDetail);
            productBugMap.put(bugDetail.getProductName(), tempBugDetails);
            // 按优先级分组
            tempBugDetails = priorityBugMap.getOrDefault(bugDetail.getPriority(), CollectionUtil.newArrayList());
            tempBugDetails.add(bugDetail);
            priorityBugMap.put(bugDetail.getPriority(), tempBugDetails);
        }
        // 打印bug信息
        System.out.println("\n-----------------------------------");
        System.out.println(time.getDesc() + "解决的bug个数：" + bugDetails.size());
        System.out.println('(' + priorityBugMap.entrySet().stream().map(x -> x.getKey() + "级：" + x.getValue().size()).collect(Collectors.joining(", ")) + ')');
        System.out.println('(' + productBugMap.entrySet().stream().map(x -> x.getKey() + "：" + x.getValue().size()).collect(Collectors.joining(", ")) + ')');
        if (!CollectionUtil.isEmpty(bugDetails)) {
            System.out.println("\n" + time.getDesc() + "解决的bug详情如下：");
            int index = 1;
            bugDetails.sort(Comparator.comparing(BugDetail::getProductName));
            for (BugDetail bugDetail : bugDetails) {
                System.out.printf("%d. %s\t%d\t%s%n", index++, StrUtil.fillWithBlank(bugDetail.getProductName(), 5), bugDetail.getPriority(), bugDetail.getBugDesc());
            }
        }
        System.out.println("-----------------------------------\n");
    }

    /**
     * 打印文件导出信息
     * @param bugDetails bug集合
     * @param queryTypeEnum 查询方式
     */
    private void printFileExport(List<BugDetail> bugDetails, QueryTypeEnum queryTypeEnum) {
        if (fileService.exportToTxt(bugDetails, queryTypeEnum)) {
            System.out.println("导出成功");
            try {
                Desktop.getDesktop().open(new File(ZendaoConstant.FILE_EXPORT_PATH));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("导出失败");
        }
    }
}
