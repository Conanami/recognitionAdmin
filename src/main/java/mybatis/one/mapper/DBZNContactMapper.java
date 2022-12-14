package mybatis.one.mapper;

import java.util.List;
import mybatis.one.po.DBZNContact;
import mybatis.one.po.DBZNContactExample;
import org.apache.ibatis.annotations.Param;

public interface DBZNContactMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    int countByExample(DBZNContactExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    int deleteByExample(DBZNContactExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer seqid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    int insert(DBZNContact record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    int insertSelective(DBZNContact record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    List<DBZNContact> selectByExample(DBZNContactExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    DBZNContact selectByPrimaryKey(Integer seqid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DBZNContact record, @Param("example") DBZNContactExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DBZNContact record, @Param("example") DBZNContactExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DBZNContact record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DBZNContact record);
}