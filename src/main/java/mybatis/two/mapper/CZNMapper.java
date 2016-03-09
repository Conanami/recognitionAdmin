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
    public List<MTMDataDto> queryTel(@Param("casenostart") String casenostart );

    public List<Map<String, String>> queryContact(@Param("phonelikestring") String phonelikestring);
    public List<Map<String, String>> queryContact1(@Param("phonelikestring") String phonelikestring);

    public void updateContact(@Param("result") String result,
                              @Param("phonelikestring") String phonelikestring);
}
