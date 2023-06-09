package service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import constant.ConfParamConstant;
import constant.ZendaoConstant;
import entity.BugDetail;
import service.BugService;
import service.FileService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gaozijie
 * @date 2023-05-04
 */
public class BugServiceImpl implements BugService {

    FileService fileService = new FileServiceImpl();

    private final Map<String, String> confParams = fileService.loadConfFile();

    @Override
    public List<BugDetail> getBugDetails(String time) {
        // 正则匹配出bug
        String htmlText = this.getDynamicHtmlText(confParams.get(ConfParamConstant.COOKIE), time).toString();
        String bugPattern = "<span class='label-action'> 解决了</span>\\s+<span class=\"text-muted\">Bug</span>\\s+<a href='/index.php\\?m=bug&f=view&bugID=\\d+' >.+</a>";
        Matcher matcher = Pattern.compile(bugPattern).matcher(htmlText);
        List<BugDetail> bugDetails = new ArrayList<>();
        String bugText;
        while (matcher.find()) {
            bugText = matcher.group();
            bugDetails.add(this.buildBugDetail(bugText));
        }
        return bugDetails;
    }

    @Override
    public void fillBugDetails(List<BugDetail> bugDetails) {
        for (BugDetail bugDetail : bugDetails) {
            String bugDetailHtmlText = this.getBugDetailHtmlText(confParams.get(ConfParamConstant.COOKIE), bugDetail.getBugId());
            this.fillBugDetail(bugDetailHtmlText, bugDetail);
        }
    }

    /**
     * 构建bug详情类
     * @param bugText
     * @return
     */
    private BugDetail buildBugDetail(String bugText) {
        String budIdPattern = "bugID=\\d+";
        String bugDescPattern = ">.+</a>";
        Matcher matcher;
        // 查询bugId
        matcher = Pattern.compile(budIdPattern).matcher(bugText);
        String bugId = matcher.find() ? matcher.group(0).substring(6) : "";
        // 查询bug描述
        matcher = Pattern.compile(bugDescPattern).matcher(bugText);
        String bugName = matcher.find() ? matcher.group(0).substring(1, matcher.group(0).length() - 4) : "";
        // 封装返回
        return new BugDetail(bugId, bugName, null, null);
    }

    /**
     * 获取动态查询页面的html文本
     * @param cookie
     * @param time
     * @return
     */
    private List<String> getDynamicHtmlText(String cookie, String time) {
        List<String> htmlTexts = new ArrayList<>();
        String url = String.format(confParams.get(ConfParamConstant.DOMAIN_URL) + ZendaoConstant.DYNAMIC_QUERY_URL, time);
        String text;
        do {
            text = HttpUtil.createPost(url).header("cookie", cookie).execute().body();
            htmlTexts.add(text);
        } while (!ObjectUtil.isEmpty(url = this.getPrevPageUrl(text)));
        return htmlTexts;
    }

    /**
     * 获取前一页地址数据
     * @param text
     * @return
     */
    private String getPrevPageUrl(String text) {
        String prePagePattern = "<a id=\"prevPage\" class=\"btn btn-info\".*?>";
        Matcher matcher = Pattern.compile(prePagePattern).matcher(text);
        return matcher.find() ? confParams.get(ConfParamConstant.DOMAIN_URL) + matcher.group(0).replaceAll(".*href=\"(.*)\".*", "$1") : "";
    }

    /**
     * 获取bug详情页面的html文本
     * @param cookie
     * @param bugId
     * @return
     */
    private String getBugDetailHtmlText(String cookie, String bugId) {
        String url = String.format(confParams.get(ConfParamConstant.DOMAIN_URL) + ZendaoConstant.BUG_QUERY_URL, bugId);
        return HttpUtil.createPost(url).header("cookie", cookie).execute().body();
    }

    /**
     * 补充bug详情数据
     * @param htmlText
     * @param bugDetail
     */
    private void fillBugDetail(String htmlText, BugDetail bugDetail) {
        // 查询产品名称
        String productPattern = "<th class='thWidth'>所属产品</th>\\s.*";
        Matcher matcher = Pattern.compile(productPattern).matcher(htmlText);
        String productName = matcher.find() ? matcher.group(0).replaceAll(".*\\s.*>(.*)</a>", "$1") : "";
        // 查询bug优先级
        String priorityPattern = "<span class='label-pri label-pri-\\d' title='\\d'>\\d</span>";
        matcher = Pattern.compile(priorityPattern).matcher(htmlText);
        String priority = matcher.find() ? matcher.group(0).replaceAll(".*>(\\d)<.*", "$1") : "";
        // 数据补充
        bugDetail.setProductName(productName);
        bugDetail.setPriority(Integer.valueOf(priority));
    }
}
