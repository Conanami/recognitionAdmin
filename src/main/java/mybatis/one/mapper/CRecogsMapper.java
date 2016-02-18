package mybatis.one.mapper;

import mybatis.one.po.DBRecogs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    public DBRecogs pickup(@Param("merchid") String merchid);
}
