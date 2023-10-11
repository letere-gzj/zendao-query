package constant;

import java.io.File;

/**
 * @author gaozijie
 * @date 2023-05-04
 */
public interface ZendaoConstant {
    /**
     * 禅道登录地址
     */
    String LOGIN_URL = "/index.php?m=user&f=login&t=html";

    /**
     * 禅道动态查询地址
     */
    String DYNAMIC_QUERY_URL = "/index.php?m=my&f=dynamic&type=%s";

    /**
     * 禅道bug详情查询地址
     */
    String BUG_QUERY_URL = "/index.php?m=bug&f=view&bugID=%s";

    /**
     * 禅道任务详情查询地址
     */
    String TASK_QUERY_URL = "/index.php?m=task&f=view&taskID=%s";

    /**
     * 配置文件路径
     */
    String CONF_PATH = "./conf/bug.conf";

    /**
     * 文件导出路径
     */
    String FILE_EXPORT_PATH = new File("./file").getAbsolutePath();
}
