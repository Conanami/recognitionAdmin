import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;

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

    @Test
    public void test2(){
        //读取 excel

        String filepath = "/Users/boshu/Downloads/电话信息97.xls";

        int indexrow = 1;
        int indexcol = 1;
        //表示从 第一行,开始取手机号, 取的是 第一列

        try {
            FileInputStream fis = new FileInputStream(new File(filepath));
            Workbook workbook = WorkbookFactory.create(fis);

            Sheet sheet = workbook.getSheetAt(0);
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum = sheet.getLastRowNum();
            log.info(firstRowNum+"  "+lastRowNum);
            int start = Math.max(firstRowNum, indexrow-1);
            for (int i=start;i<=lastRowNum;i++){
                Cell cell = sheet.getRow(i).getCell(indexcol-1);
                if (cell!=null) cell.setCellType(Cell.CELL_TYPE_STRING);
                log.info(cell.getStringCellValue());
            }

//            int maxrow = sheet.getRows();
//            for (int i=indexrow-1;i<maxrow;i++){
//                Cell cell = sheet.getRow(i)[indexcol-1];
//                log.info(cell.getContents());
//            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
