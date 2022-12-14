package mybatis.one.mapper;

import java.util.List;
import mybatis.one.po.DBZNCaseData;
import mybatis.one.po.DBZNCaseDataExample;
import org.apache.ibatis.annotations.Param;

public interface DBZNCaseDataMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncasedata
     *
     * @mbggenerated
     */
    int countByExample(DBZNCaseDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncasedata
     *
     * @mbggenerated
     */
    int deleteByExample(DBZNCaseDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncasedata
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String caseno);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncasedata
     *
     * @mbggenerated
     */
    int insert(DBZNCaseData record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncasedata
     *
     * @mbggenerated
     */
    int insertSelective(DBZNCaseData record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncasedata
     *
     * @mbggenerated
     */
    List<DBZNCaseData> selectByExample(DBZNCaseDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncasedata
     *
     * @mbggenerated
     */
    DBZNCaseData selectByPrimaryKey(String caseno);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncasedata
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DBZNCaseData record, @Param("example") DBZNCaseDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncasedata
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DBZNCaseData record, @Param("example") DBZNCaseDataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncasedata
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DBZNCaseData record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncasedata
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DBZNCaseData record);
}