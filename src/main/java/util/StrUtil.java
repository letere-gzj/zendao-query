package util;

/**
 * @author gaozijie
 * @date 2023-05-19
 */
public class StrUtil {

    /**
     * 字符串填补空格
     * @param str 字符串
     * @param len 字符串长度
     * @return
     */
    public static String fillWithBlank(String str, int len) {
        if (str == null) {
            str = "";
        }
        if (str.length() >= len) {
            return str;
        }
        // 填充空字符
        return String.format("%-" + len + "s", str);
    }
}
