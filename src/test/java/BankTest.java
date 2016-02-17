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
            File file = new File("C:\\Users\\boshu\\Downloads\\13061603533_201602071728204338.wav");

            log.info(""+file.length());

//            String page = IOUtils.toString(new FileInputStream(file));
//            Document document = Jsoup.parse(page);
//            Element realForm = document.select("div.info-show ").first();
//            if (realForm != null) {
//                String name = realForm.select(".item").get(1).html().replace("<label>账户户名：</label>", "");
//                String cardno = realForm.select(".item").get(2).html().replace("<label>账户账号：</label>", "");
//                log.info("name:"+name +"\n cardno:"+cardno);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
