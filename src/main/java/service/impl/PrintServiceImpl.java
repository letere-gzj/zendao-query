package service.impl;

import cn.hutool.core.collection.CollectionUtil;
import constant.BooleanEnum;
import constant.QueryTypeEnum;
import constant.TimeEnum;
import constant.ZendaoConstant;
import entity.BugDetail;
import entity.TaskDetail;
import service.*;
import util.StrUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * @author gaozijie
 * @date 2023-05-10
 */
public class PrintServiceImpl implements PrintService {

    private final BugService bugService = new BugServiceImpl();
    private final FileService fileService = new FileServiceImpl();
    private final HtmlService htmlService = new HtmlServiceImpl();
    private final TaskService taskService = new TaskServiceImpl();

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
        // 爬虫查询bug和任务
        List<String> dynamicHtmlText = htmlService.getDynamicHtmlText(time);
        List<BugDetail> bugDetails = bugService.listBugDetail(dynamicHtmlText);
        List<TaskDetail> taskDetails = taskService.listTaskDetail(dynamicHtmlText);
        // 打印bug信息
        if (Objects.equals(queryTypeEnum, QueryTypeEnum.SIMPLE)) {
            this.simplePrint(bugDetails, taskDetails, time);
        } else {
            bugService.fillBugDetails(bugDetails);
            taskService.fillTaskDetails(taskDetails);
            this.detailedPrint(bugDetails, taskDetails, time);
        }
        // 文件导出
        if (CollectionUtil.isEmpty(bugDetails)) {
            return;
        }
        System.out.print("是否导出为txt文件(y:是, n:否)：");
        boolean isExportFile = Objects.equals(scanner.nextLine(), BooleanEnum.YES.getValue());
        if (isExportFile) {
            this.printFileExport(bugDetails, queryTypeEnum, time);
        }
    }

    /**
     * 简易打印bug
     * @param bugDetails bug详情
     */
    private void simplePrint(List<BugDetail> bugDetails, List<TaskDetail> taskDetails, TimeEnum time) {
        System.out.println("\n-----------------------------------");
        System.out.printf("%s解决的bug个数：%d | 完成的任务个数: %d\n", time.getDesc(), bugDetails.size(), taskDetails.size());
        // 打印Bug
        int index;
        if (!CollectionUtil.isEmpty(bugDetails)) {
            System.out.printf("[%s解决的bug详情如下：]\n", time.getDesc());
            index = 1;
            for (BugDetail bugDetail : bugDetails) {
                System.out.printf("%d.%s%n", index++, bugDetail.getBugDesc());
            }
        }
        // 打印任务
        if (!CollectionUtil.isEmpty(taskDetails)) {
            System.out.printf("[%s完成的任务详情如下：]\n", time.getDesc());
            index = 1;
            for (TaskDetail taskDetail : taskDetails) {
                System.out.printf("%d.%s%n", index++, taskDetail.getTaskDesc());
            }
        }
        System.out.println("-----------------------------------\n");
    }

    /**
     * 详细打印bug
     * @param bugDetails bug详情
     */
    private void detailedPrint(List<BugDetail> bugDetails, List<TaskDetail> taskDetails, TimeEnum time) {
        System.out.println("\n-----------------------------------");
        System.out.printf("%s解决的bug个数：%d | 完成的任务个数: %d\n", time.getDesc(), bugDetails.size(), taskDetails.size());
        int index;
        // 打印bug信息
        if (!CollectionUtil.isEmpty(bugDetails)) {
            System.out.printf("[%s解决的bug详情如下：]\n", time.getDesc());
            index = 1;
            bugDetails.sort(((o1, o2) -> {
                if (Objects.equals(o1.getProductName(), o2.getProductName())) {
                    return o1.getPriority().compareTo(o2.getPriority());
                } else {
                    return o1.getProductName().compareTo(o2.getProductName());
                }
            }));
            for (BugDetail bugDetail : bugDetails) {
                System.out.printf("%d. %s\t%d\t%s%n", index++, StrUtil.fillWithBlank(bugDetail.getProductName(), 5), bugDetail.getPriority(), bugDetail.getBugDesc());
            }
        }
        // 打印任务信息
        if (!CollectionUtil.isEmpty(taskDetails)) {
            System.out.printf("[%s完成的任务详情如下：]\n", time.getDesc());
            index = 1;
            taskDetails.sort(((o1, o2) -> {
                if (Objects.equals(o1.getProductName(), o2.getProductName())) {
                    return o1.getPriority().compareTo(o2.getPriority());
                } else {
                    return o1.getProductName().compareTo(o2.getProductName());
                }
            }));
            for (TaskDetail taskDetail : taskDetails) {
                System.out.printf("%d. %s\t%d\t%s%n", index++, StrUtil.fillWithBlank(taskDetail.getProductName(), 5), taskDetail.getPriority(), taskDetail.getTaskDesc());
            }
        }
        System.out.println("-----------------------------------\n");
    }

    /**
     * 打印文件导出信息
     * @param bugDetails bug集合
     * @param queryTypeEnum 查询方式
     */
    private void printFileExport(List<BugDetail> bugDetails, QueryTypeEnum queryTypeEnum, TimeEnum timeEnum) {
        if (fileService.exportToTxt(bugDetails, queryTypeEnum, timeEnum)) {
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
