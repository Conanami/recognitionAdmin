import com.common.util.SpringContextUtil;
import com.web.task.BatchLogService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by boshu on 2016/3/17.
 */
public class SpringTest {

    BatchLogService batchLogService;

    @Test
    public void read() {
        String[] configLocations = new String[]{
                "spring/applicationContext-dao.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configLocations
                , false
        );
        context.refresh();

        new SpringContextUtil().setApplicationContext(context);

        batchLogService = context.getBean(BatchLogService.class);

        batchLogService.run();
    }
}
