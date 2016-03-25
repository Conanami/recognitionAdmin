package mybatis.one.mapper;

import com.web.dto.BatchStatisticDto;
import com.web.dto.DtoDBRecogs;
import com.web.dto.MTMDataDto;
import mybatis.one.po.DBRecogs;
import mybatis.one.po.DBTmpPhone;
import mybatis.one.po.DBZNCaseData;
import mybatis.one.po.DBZNContact;
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
    public List<DtoDBRecogs> pickup(@Param("merchid") String merchid);

    // 查询一个批次里面，已经完成识别的 记录, 包括识别结果1,2,3,4， 识别状态 4， 9， 11， 12
    public List<DBRecogs> selectBatchRecogCount(@Param("batchid") String batchid);

    // 查询统计结果
    public List<BatchStatisticDto> selectResultStatistic(@Param("batchid") String batchid);

    /**
     * 批量插入案件 案件详情
     * @param batchid
     * @param list
     */
    public int insertCaseBatch(@Param("batchid") String batchid,
                                @Param("list") List<DBMTMContact> list);

    // 批量插入案件
    public int insertCaseData();

    /**
     * 查询刚刚批量插入案件的 电话号码（过滤重复和空）
     * @param batchid
     * @return
     */
    public List<MTMDataDto> queryPhone(@Param("batchid") String batchid);

    //删除临时表里面 某商户的全部数据
    public int deleteTmpPhoneByMerchId(@Param("merchid") String merchid);
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

    //A：债务人可以联系上
    public int countForStatusA(@Param("caseno") String caseno);
    //B:  债务人联系不上，联系人可以联系上
    public int countForStatusB(@Param("caseno") String caseno);
    // 查询案件下，有没有尚未识别的记录
    public int selectNoRecogContactCase(@Param("caseno") String caseno);

    //C：债务人联系不上，联系人也没有可接通，只有关机
    public int countForStatusC(@Param("caseno") String caseno);
    //D: 债务人联系不上，联系人连关机都没有，全部停机
    public int countForStatusD(@Param("caseno") String caseno);

    //查询可以待写回 兆能资产 的 联系人记录
    public List<DBZNContact> selectZNContactResult();
    // 查询可以待写回 兆能资产 的 案件状态记录
    public List<DBZNCaseData> selectZNCaseDataResult();

    //批量更新 映射 联系人表  zncontact   暂时作废
    public int batchUpdateZNContactStatus(@Param("list") List<DBZNContact> list);
    // 批量更新 映射 案件表  zncaseData  暂时作废
    public int batchUpdateZNCaseDataStatus(@Param("list") List<DBZNCaseData> list);
}
