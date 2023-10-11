package service.impl;

import entity.BugDetail;
import service.BugService;
import service.HtmlService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gaozijie
 * @date 2023-05-04
 */
public class BugServiceImpl implements BugService {

    private final HtmlService htmlService = new HtmlServiceImpl();

    @Override
    public List<BugDetail> listBugDetail(List<String> htmlTexts) {
        // 正则匹配出bug
        String bugPattern = "<span.*> 解决了</span>\\s*<span.*>Bug</span>\\s*<a.*bugID=(\\d+).*>(.+)</a>";
        List<BugDetail> bugDetails = new ArrayList<>(16);
        BugDetail bugDetail;
        Matcher matcher;
        for (String htmlText : htmlTexts) {
            matcher = Pattern.compile(bugPattern).matcher(htmlText);
            while (matcher.find()) {
                bugDetail = new BugDetail();
                bugDetail.setBugId(matcher.group(1));
                bugDetail.setBugDesc(matcher.group(2));
                bugDetails.add(bugDetail);
            }
        }
        return bugDetails;
    }

    @Override
    public void fillBugDetails(List<BugDetail> bugDetails) {
        for (BugDetail bugDetail : bugDetails) {
            String bugDetailHtmlText = htmlService.getBugDetailHtmlText(bugDetail.getBugId());
            this.fillBugDetail(bugDetailHtmlText, bugDetail);
        }
    }

    /**
     * 补充bug详情数据
     * @param htmlText bug详情html文本
     * @param bugDetail bug详情
     */
    private void fillBugDetail(String htmlText, BugDetail bugDetail) {
        // 查询产品名称
        String productPattern = "<th.*>所属产品</th>\\s*<td>\\s*<a.*>(.+)</a>";
        Matcher matcher = Pattern.compile(productPattern).matcher(htmlText);
        String productName = matcher.find() ? matcher.group(1) : "";
        // 查询bug优先级
        String priorityPattern = "<th.*>优先级</th>\\s*<td>\\s*<span.*>(.*)</span>";
        matcher = Pattern.compile(priorityPattern).matcher(htmlText);
        String priority = matcher.find() ? matcher.group(1) : "";
        // 数据补充
        bugDetail.setProductName(productName);
        bugDetail.setPriority(Integer.valueOf(priority));
    }
}
