package service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import conf.DefaultConf;
import constant.TimeEnum;
import constant.ZendaoConstant;
import service.HtmlService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gaozijie
 * @date 2023-10-10
 */
public class HtmlServiceImpl implements HtmlService {

    @Override
    public List<String> getDynamicHtmlText(TimeEnum time) {
        List<String> htmlTexts = new ArrayList<>();
        String url = String.format(DefaultConf.domainUrl + ZendaoConstant.DYNAMIC_QUERY_URL, time.getValue());
        String text;
        do {
            text = HttpUtil.createPost(url).header("cookie", DefaultConf.cookie).execute().body();
            htmlTexts.add(text);
        } while (!ObjectUtil.isEmpty(url = this.getPrevPageUrl(text)));
        return htmlTexts;
    }

    @Override
    public String getBugDetailHtmlText(String bugId) {
        String url = String.format(DefaultConf.domainUrl + ZendaoConstant.BUG_QUERY_URL, bugId);
        return HttpUtil.createPost(url).header("cookie", DefaultConf.cookie).execute().body();
    }

    @Override
    public String getTaskDetailHtmlText(String taskId) {
        String url = String.format(DefaultConf.domainUrl + ZendaoConstant.TASK_QUERY_URL, taskId);
        return HttpUtil.createPost(url).header("cookie", DefaultConf.cookie).execute().body();
    }

    /**
     * 获取前一页地址数据
     * @param text html文本
     * @return 前一页地址
     */
    private String getPrevPageUrl(String text) {
        String prePagePattern = "<a id=\"prevPage\" class=\"btn btn-info\".*?>";
        Matcher matcher = Pattern.compile(prePagePattern).matcher(text);
        return matcher.find() ? DefaultConf.domainUrl + matcher.group(0).replaceAll(".*href=\"(.*)\".*", "$1") : "";
    }
}
