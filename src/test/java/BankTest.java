import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by boshu on 2016/1/2.
 */
public class BankTest {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Test
    public void test() {
        int count = (int) Math.ceil(1*1.0f / 2000.0f);
        log.info("count: "+count);
    }
}
