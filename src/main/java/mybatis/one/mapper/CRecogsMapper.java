package mybatis.one.mapper;

import com.web.dto.DtoDBRecogs;
import com.web.dto.MTMDataDto;
import mybatis.one.po.DBRecogs;
import mybatis.one.po.DBTmpPhone;
import mybatis.two.po.DBMTMContact;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CRecogsMapper {
	
	/**
	 * 查询 识别日志
	 * @return
	 */
    public List<DBRecogs> selectRecogs(
            @Param("batchid") String batchid,
            @Param("status") Integer status,
            @Param("result") Integer result,
            @Param("manualresult") Integer manualresult,
            @Param("mobile") String mobile,
			@Param("start") Integer start,
			@Param("pagesize") Integer pagesize);

    public Integer totalRecogs(
            @Param("batchid") String batchid,
            @Param("status") Integer status,
            @Param("result") Integer result,
            @Param("manualresult") Integer manualresult,
            @Param("mobile") String mobile
    );

    /**
     * 查询  导入批次记录
     * @return
     */
    public List<DBRecogs> selectBatchLogs(
            @Param("merchid") String merchid,
            @Param("batchid") String batchid,
            @Param("start") Integer start,
            @Param("pagesize") Integer pagesize);

    public Integer totalBatchLogs(
            @Param("merchid") String merchid,
            @Param("batchid") String batchid
    );

    /**
     * 领取一个电话号码
     * @param merchid
     * @return
     */
    public DtoDBRecogs pickup(@Param("merchid") String merchid);

    /**
     * 批量插入案件
     * @param batchid
     * @param list
     */
    public void insertCaseBatch(@Param("batchid") String batchid,
                                @Param("list") List<DBMTMContact> list);

    /**
     * 查询刚刚批量插入案件的 电话号码（过滤重复和空）
     * @param batchid
     * @return
     */
    public List<MTMDataDto> queryPhone(@Param("batchid") String batchid);

    //将电话号码批量插入临时表
    public void insertTmpPhoneBatch(@Param("merchid") String merchid
            , @Param("list") List<String> listPhone);

    /**
     * 查询 兆能资产 需要写回的数据
     * @return
     */
    public List<DtoDBRecogs> queryRecogResult();

    /**
     * 查询临时电话号码表的数据， 过滤和 recogs 里面尚未拨打的电话号码重复的。
     * @param merchid
     * @return
     */
    public List<String> selectTmpPhone(@Param("merchid") String merchid);

    //将临时表的电话号码 插入 recogs表
    public void insertTmpPhoneToRecogsBatch(@Param("merchid") String merchid
            , @Param("batchid") String batchid
            , @Param("list") List<String> list
            , @Param("createtime") String createtime);
}
