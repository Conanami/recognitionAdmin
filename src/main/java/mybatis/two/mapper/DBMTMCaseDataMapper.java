package mybatis.two.mapper;

import java.util.List;
import mybatis.two.po.DBMTMCaseData;
import mybatis.two.po.DBMTMCaseDataExample;
import org.apache.ibatis.annotations.Param;

public interface DBMTMCaseDataMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table MTMCaseData
     *
     * @mbggenerated
     */
    int countByExample(DBMTMCaseDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table MTMCaseData
     *
     * @mbggenerated
     */
    int deleteByExample(DBMTMCaseDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table MTMCaseData
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String caseNo);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table MTMCaseData
     *
     * @mbggenerated
     */
    int insert(DBMTMCaseData record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table MTMCaseData
     *
     * @mbggenerated
     */
    int insertSelective(DBMTMCaseData record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table MTMCaseData
     *
     * @mbggenerated
     */
    List<DBMTMCaseData> selectByExample(DBMTMCaseDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table MTMCaseData
     *
     * @mbggenerated
     */
    DBMTMCaseData selectByPrimaryKey(String caseNo);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table MTMCaseData
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DBMTMCaseData record, @Param("example") DBMTMCaseDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table MTMCaseData
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DBMTMCaseData record, @Param("example") DBMTMCaseDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table MTMCaseData
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DBMTMCaseData record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table MTMCaseData
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DBMTMCaseData record);
}