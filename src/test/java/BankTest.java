import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by boshu on 2016/1/2.
 */
public class BankTest {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Test
    public void test() {
        try {
            File file = new File("c:\\tools\\201601271532341483.txt");
            String page = IOUtils.toString(new FileInputStream(file));
            Document document = Jsoup.parse(page);
            Element realForm = document.select("div.info-show ").first();
            if (realForm != null) {
                String name = realForm.select(".item").get(1).html().replace("<label>账户户名：</label>", "");
                String cardno = realForm.select(".item").get(2).html().replace("<label>账户账号：</label>", "");
                log.info("name:"+name +"\n cardno:"+cardno);
//                Element userinfo = document.getElementsContainingOwnText("尊敬的").first();
//                String userName = "";
//                if (userinfo != null) {
//                    String text = userinfo.html();
//                    userName = text.substring(text.indexOf("\">") + 2, text.indexOf("</span>"));
//                    log.info("userName:" + userName);
//                }

                //登录成功，保存相关信息
                //context.addTempParam(ParamKey.CCB_ACCOUNT, ccbAccountTemp);

            }

//            String tmp = URLDecoder.decode("%0D%0A%0D%0A%0D%0AE0000000000010%7C0101%7C%E4%BA%BA%E6%B0%91%E5%B8%81%7C0%7C0", "utf-8");
//            log.info("tmp:" + tmp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
