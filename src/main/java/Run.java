
import service.PrintService;
import service.impl.PrintServiceImpl;

/**
 * @author gaozijie
 * @date 2023-04-28
 */
public class Run {
    /*
    login sample: account=15625049572&password=9a67d7cb3117e974da116a061e3357af&passwordStrength=1&referer=%2F&verifyRand=759922586&keepLogin=1
     */

    public static void main(String[] args) {
        try {
            PrintService printService = new PrintServiceImpl();
            printService.printQueryBug();
        } catch (Exception e) {
            System.out.println("程序异常：" + e.getMessage());
        }
//        System.out.print("按回车键退出...");
//        scanner.nextLine();
    }
}
