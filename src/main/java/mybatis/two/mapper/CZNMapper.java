package mybatis.two.mapper;

import com.web.dto.MTMDataDto;
import mybatis.two.po.DBMTMCaseData;
import mybatis.two.po.DBMTMContact;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by boshu on 2016/3/7.
 */
public interface CZNMapper {
    // 查询 案件号以 casenostart 打头的
    public List<MTMDataDto> queryTel(@Param("casenostart") String casenostart );
    //查询 案件号以 casenostart 打头的
    public List<DBMTMContact> queryCase(@Param("casenostart") String casenostart );

    public List<Map<String, String>> queryContact(@Param("phonelikestring") String phonelikestring);
    public List<Map<String, String>> queryContact1(@Param("phonelikestring") String phonelikestring);

    // 根据案件号查询合约
    public Map<String, String> queryContactByCaseNo(@Param("case_no") String case_no,
                                                          @Param("serino") String serino);

    public void updateContact(@Param("result") String result,
                              @Param("phonelikestring") String phonelikestring);
}
