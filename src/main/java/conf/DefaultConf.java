package conf;

import cn.hutool.core.io.IoUtil;
import constant.ZendaoConstant;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gaozijie
 * @date 2023-10-10
 */
public class DefaultConf {

    public static String domainUrl;
    public static String cookie;
    public static String username;
    public static String encodePwd;

    private final static String CONF_DOMAIN_URL = "domainUrl";
    private final static String CONF_COOKIE = "cookie";
    private final static String CONF_USERNAME = "username";
    private final static String CONF_ENCODE_PWD = "encodePwd";

    static {
        Map<String, String> paramMap = loadConfig();
        domainUrl = paramMap.get(CONF_DOMAIN_URL);
        cookie = paramMap.get(CONF_COOKIE);
        username = paramMap.get(CONF_USERNAME);
        encodePwd = paramMap.get(CONF_ENCODE_PWD);
    }

    /**
     * 加载配置文件
     * @return 配置Map
     */
    private static Map<String, String> loadConfig() {
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
