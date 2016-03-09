package mybatis.one.mapper;

import java.util.List;
import mybatis.one.po.DBTmpPhone;
import mybatis.one.po.DBTmpPhoneExample;
import org.apache.ibatis.annotations.Param;

public interface DBTmpPhoneMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tmpPhone
     *
     * @mbggenerated
     */
    int countByExample(DBTmpPhoneExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tmpPhone
     *
     * @mbggenerated
     */
    int deleteByExample(DBTmpPhoneExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tmpPhone
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer seqid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tmpPhone
     *
     * @mbggenerated
     */
    int insert(DBTmpPhone record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tmpPhone
     *
     * @mbggenerated
     */
    int insertSelective(DBTmpPhone record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tmpPhone
     *
     * @mbggenerated
     */
    List<DBTmpPhone> selectByExample(DBTmpPhoneExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tmpPhone
     *
     * @mbggenerated
     */
    DBTmpPhone selectByPrimaryKey(Integer seqid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tmpPhone
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DBTmpPhone record, @Param("example") DBTmpPhoneExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tmpPhone
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DBTmpPhone record, @Param("example") DBTmpPhoneExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tmpPhone
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DBTmpPhone record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tmpPhone
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DBTmpPhone record);
}