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
            @Param("merchid") String merchid,
            @Param("mobile") String mobile,
			@Param("start") Integer start,
			@Param("pagesize") Integer pagesize);

    public Integer totalRecogs(
            @Param("merchid") String merchid,
            @Param("mobile") String mobile
    );

    /**
     * 领取一个电话号码
     * @param merchid
     * @return
     */
    public DBRecogs pickup(@Param("merchid") String merchid);
}
