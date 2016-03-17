package mybatis.one.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBZNContactExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    public DBZNContactExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andSeqidIsNull() {
            addCriterion("seqid is null");
            return (Criteria) this;
        }

        public Criteria andSeqidIsNotNull() {
            addCriterion("seqid is not null");
            return (Criteria) this;
        }

        public Criteria andSeqidEqualTo(Integer value) {
            addCriterion("seqid =", value, "seqid");
            return (Criteria) this;
        }

        public Criteria andSeqidNotEqualTo(Integer value) {
            addCriterion("seqid <>", value, "seqid");
            return (Criteria) this;
        }

        public Criteria andSeqidGreaterThan(Integer value) {
            addCriterion("seqid >", value, "seqid");
            return (Criteria) this;
        }

        public Criteria andSeqidGreaterThanOrEqualTo(Integer value) {
            addCriterion("seqid >=", value, "seqid");
            return (Criteria) this;
        }

        public Criteria andSeqidLessThan(Integer value) {
            addCriterion("seqid <", value, "seqid");
            return (Criteria) this;
        }

        public Criteria andSeqidLessThanOrEqualTo(Integer value) {
            addCriterion("seqid <=", value, "seqid");
            return (Criteria) this;
        }

        public Criteria andSeqidIn(List<Integer> values) {
            addCriterion("seqid in", values, "seqid");
            return (Criteria) this;
        }

        public Criteria andSeqidNotIn(List<Integer> values) {
            addCriterion("seqid not in", values, "seqid");
            return (Criteria) this;
        }

        public Criteria andSeqidBetween(Integer value1, Integer value2) {
            addCriterion("seqid between", value1, value2, "seqid");
            return (Criteria) this;
        }

        public Criteria andSeqidNotBetween(Integer value1, Integer value2) {
            addCriterion("seqid not between", value1, value2, "seqid");
            return (Criteria) this;
        }

        public Criteria andImportbatchidIsNull() {
            addCriterion("importbatchid is null");
            return (Criteria) this;
        }

        public Criteria andImportbatchidIsNotNull() {
            addCriterion("importbatchid is not null");
            return (Criteria) this;
        }

        public Criteria andImportbatchidEqualTo(String value) {
            addCriterion("importbatchid =", value, "importbatchid");
            return (Criteria) this;
        }

        public Criteria andImportbatchidNotEqualTo(String value) {
            addCriterion("importbatchid <>", value, "importbatchid");
            return (Criteria) this;
        }

        public Criteria andImportbatchidGreaterThan(String value) {
            addCriterion("importbatchid >", value, "importbatchid");
            return (Criteria) this;
        }

        public Criteria andImportbatchidGreaterThanOrEqualTo(String value) {
            addCriterion("importbatchid >=", value, "importbatchid");
            return (Criteria) this;
        }

        public Criteria andImportbatchidLessThan(String value) {
            addCriterion("importbatchid <", value, "importbatchid");
            return (Criteria) this;
        }

        public Criteria andImportbatchidLessThanOrEqualTo(String value) {
            addCriterion("importbatchid <=", value, "importbatchid");
            return (Criteria) this;
        }

        public Criteria andImportbatchidLike(String value) {
            addCriterion("importbatchid like", value, "importbatchid");
            return (Criteria) this;
        }

        public Criteria andImportbatchidNotLike(String value) {
            addCriterion("importbatchid not like", value, "importbatchid");
            return (Criteria) this;
        }

        public Criteria andImportbatchidIn(List<String> values) {
            addCriterion("importbatchid in", values, "importbatchid");
            return (Criteria) this;
        }

        public Criteria andImportbatchidNotIn(List<String> values) {
            addCriterion("importbatchid not in", values, "importbatchid");
            return (Criteria) this;
        }

        public Criteria andImportbatchidBetween(String value1, String value2) {
            addCriterion("importbatchid between", value1, value2, "importbatchid");
            return (Criteria) this;
        }

        public Criteria andImportbatchidNotBetween(String value1, String value2) {
            addCriterion("importbatchid not between", value1, value2, "importbatchid");
            return (Criteria) this;
        }

        public Criteria andCasenoIsNull() {
            addCriterion("caseno is null");
            return (Criteria) this;
        }

        public Criteria andCasenoIsNotNull() {
            addCriterion("caseno is not null");
            return (Criteria) this;
        }

        public Criteria andCasenoEqualTo(String value) {
            addCriterion("caseno =", value, "caseno");
            return (Criteria) this;
        }

        public Criteria andCasenoNotEqualTo(String value) {
            addCriterion("caseno <>", value, "caseno");
            return (Criteria) this;
        }

        public Criteria andCasenoGreaterThan(String value) {
            addCriterion("caseno >", value, "caseno");
            return (Criteria) this;
        }

        public Criteria andCasenoGreaterThanOrEqualTo(String value) {
            addCriterion("caseno >=", value, "caseno");
            return (Criteria) this;
        }

        public Criteria andCasenoLessThan(String value) {
            addCriterion("caseno <", value, "caseno");
            return (Criteria) this;
        }

        public Criteria andCasenoLessThanOrEqualTo(String value) {
            addCriterion("caseno <=", value, "caseno");
            return (Criteria) this;
        }

        public Criteria andCasenoLike(String value) {
            addCriterion("caseno like", value, "caseno");
            return (Criteria) this;
        }

        public Criteria andCasenoNotLike(String value) {
            addCriterion("caseno not like", value, "caseno");
            return (Criteria) this;
        }

        public Criteria andCasenoIn(List<String> values) {
            addCriterion("caseno in", values, "caseno");
            return (Criteria) this;
        }

        public Criteria andCasenoNotIn(List<String> values) {
            addCriterion("caseno not in", values, "caseno");
            return (Criteria) this;
        }

        public Criteria andCasenoBetween(String value1, String value2) {
            addCriterion("caseno between", value1, value2, "caseno");
            return (Criteria) this;
        }

        public Criteria andCasenoNotBetween(String value1, String value2) {
            addCriterion("caseno not between", value1, value2, "caseno");
            return (Criteria) this;
        }

        public Criteria andSerinoIsNull() {
            addCriterion("serino is null");
            return (Criteria) this;
        }

        public Criteria andSerinoIsNotNull() {
            addCriterion("serino is not null");
            return (Criteria) this;
        }

        public Criteria andSerinoEqualTo(String value) {
            addCriterion("serino =", value, "serino");
            return (Criteria) this;
        }

        public Criteria andSerinoNotEqualTo(String value) {
            addCriterion("serino <>", value, "serino");
            return (Criteria) this;
        }

        public Criteria andSerinoGreaterThan(String value) {
            addCriterion("serino >", value, "serino");
            return (Criteria) this;
        }

        public Criteria andSerinoGreaterThanOrEqualTo(String value) {
            addCriterion("serino >=", value, "serino");
            return (Criteria) this;
        }

        public Criteria andSerinoLessThan(String value) {
            addCriterion("serino <", value, "serino");
            return (Criteria) this;
        }

        public Criteria andSerinoLessThanOrEqualTo(String value) {
            addCriterion("serino <=", value, "serino");
            return (Criteria) this;
        }

        public Criteria andSerinoLike(String value) {
            addCriterion("serino like", value, "serino");
            return (Criteria) this;
        }

        public Criteria andSerinoNotLike(String value) {
            addCriterion("serino not like", value, "serino");
            return (Criteria) this;
        }

        public Criteria andSerinoIn(List<String> values) {
            addCriterion("serino in", values, "serino");
            return (Criteria) this;
        }

        public Criteria andSerinoNotIn(List<String> values) {
            addCriterion("serino not in", values, "serino");
            return (Criteria) this;
        }

        public Criteria andSerinoBetween(String value1, String value2) {
            addCriterion("serino between", value1, value2, "serino");
            return (Criteria) this;
        }

        public Criteria andSerinoNotBetween(String value1, String value2) {
            addCriterion("serino not between", value1, value2, "serino");
            return (Criteria) this;
        }

        public Criteria andPrelationIsNull() {
            addCriterion("prelation is null");
            return (Criteria) this;
        }

        public Criteria andPrelationIsNotNull() {
            addCriterion("prelation is not null");
            return (Criteria) this;
        }

        public Criteria andPrelationEqualTo(String value) {
            addCriterion("prelation =", value, "prelation");
            return (Criteria) this;
        }

        public Criteria andPrelationNotEqualTo(String value) {
            addCriterion("prelation <>", value, "prelation");
            return (Criteria) this;
        }

        public Criteria andPrelationGreaterThan(String value) {
            addCriterion("prelation >", value, "prelation");
            return (Criteria) this;
        }

        public Criteria andPrelationGreaterThanOrEqualTo(String value) {
            addCriterion("prelation >=", value, "prelation");
            return (Criteria) this;
        }

        public Criteria andPrelationLessThan(String value) {
            addCriterion("prelation <", value, "prelation");
            return (Criteria) this;
        }

        public Criteria andPrelationLessThanOrEqualTo(String value) {
            addCriterion("prelation <=", value, "prelation");
            return (Criteria) this;
        }

        public Criteria andPrelationLike(String value) {
            addCriterion("prelation like", value, "prelation");
            return (Criteria) this;
        }

        public Criteria andPrelationNotLike(String value) {
            addCriterion("prelation not like", value, "prelation");
            return (Criteria) this;
        }

        public Criteria andPrelationIn(List<String> values) {
            addCriterion("prelation in", values, "prelation");
            return (Criteria) this;
        }

        public Criteria andPrelationNotIn(List<String> values) {
            addCriterion("prelation not in", values, "prelation");
            return (Criteria) this;
        }

        public Criteria andPrelationBetween(String value1, String value2) {
            addCriterion("prelation between", value1, value2, "prelation");
            return (Criteria) this;
        }

        public Criteria andPrelationNotBetween(String value1, String value2) {
            addCriterion("prelation not between", value1, value2, "prelation");
            return (Criteria) this;
        }

        public Criteria andPnameIsNull() {
            addCriterion("pname is null");
            return (Criteria) this;
        }

        public Criteria andPnameIsNotNull() {
            addCriterion("pname is not null");
            return (Criteria) this;
        }

        public Criteria andPnameEqualTo(String value) {
            addCriterion("pname =", value, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameNotEqualTo(String value) {
            addCriterion("pname <>", value, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameGreaterThan(String value) {
            addCriterion("pname >", value, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameGreaterThanOrEqualTo(String value) {
            addCriterion("pname >=", value, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameLessThan(String value) {
            addCriterion("pname <", value, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameLessThanOrEqualTo(String value) {
            addCriterion("pname <=", value, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameLike(String value) {
            addCriterion("pname like", value, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameNotLike(String value) {
            addCriterion("pname not like", value, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameIn(List<String> values) {
            addCriterion("pname in", values, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameNotIn(List<String> values) {
            addCriterion("pname not in", values, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameBetween(String value1, String value2) {
            addCriterion("pname between", value1, value2, "pname");
            return (Criteria) this;
        }

        public Criteria andPnameNotBetween(String value1, String value2) {
            addCriterion("pname not between", value1, value2, "pname");
            return (Criteria) this;
        }

        public Criteria andPtelIsNull() {
            addCriterion("ptel is null");
            return (Criteria) this;
        }

        public Criteria andPtelIsNotNull() {
            addCriterion("ptel is not null");
            return (Criteria) this;
        }

        public Criteria andPtelEqualTo(String value) {
            addCriterion("ptel =", value, "ptel");
            return (Criteria) this;
        }

        public Criteria andPtelNotEqualTo(String value) {
            addCriterion("ptel <>", value, "ptel");
            return (Criteria) this;
        }

        public Criteria andPtelGreaterThan(String value) {
            addCriterion("ptel >", value, "ptel");
            return (Criteria) this;
        }

        public Criteria andPtelGreaterThanOrEqualTo(String value) {
            addCriterion("ptel >=", value, "ptel");
            return (Criteria) this;
        }

        public Criteria andPtelLessThan(String value) {
            addCriterion("ptel <", value, "ptel");
            return (Criteria) this;
        }

        public Criteria andPtelLessThanOrEqualTo(String value) {
            addCriterion("ptel <=", value, "ptel");
            return (Criteria) this;
        }

        public Criteria andPtelLike(String value) {
            addCriterion("ptel like", value, "ptel");
            return (Criteria) this;
        }

        public Criteria andPtelNotLike(String value) {
            addCriterion("ptel not like", value, "ptel");
            return (Criteria) this;
        }

        public Criteria andPtelIn(List<String> values) {
            addCriterion("ptel in", values, "ptel");
            return (Criteria) this;
        }

        public Criteria andPtelNotIn(List<String> values) {
            addCriterion("ptel not in", values, "ptel");
            return (Criteria) this;
        }

        public Criteria andPtelBetween(String value1, String value2) {
            addCriterion("ptel between", value1, value2, "ptel");
            return (Criteria) this;
        }

        public Criteria andPtelNotBetween(String value1, String value2) {
            addCriterion("ptel not between", value1, value2, "ptel");
            return (Criteria) this;
        }

        public Criteria andTelckIsNull() {
            addCriterion("telck is null");
            return (Criteria) this;
        }

        public Criteria andTelckIsNotNull() {
            addCriterion("telck is not null");
            return (Criteria) this;
        }

        public Criteria andTelckEqualTo(String value) {
            addCriterion("telck =", value, "telck");
            return (Criteria) this;
        }

        public Criteria andTelckNotEqualTo(String value) {
            addCriterion("telck <>", value, "telck");
            return (Criteria) this;
        }

        public Criteria andTelckGreaterThan(String value) {
            addCriterion("telck >", value, "telck");
            return (Criteria) this;
        }

        public Criteria andTelckGreaterThanOrEqualTo(String value) {
            addCriterion("telck >=", value, "telck");
            return (Criteria) this;
        }

        public Criteria andTelckLessThan(String value) {
            addCriterion("telck <", value, "telck");
            return (Criteria) this;
        }

        public Criteria andTelckLessThanOrEqualTo(String value) {
            addCriterion("telck <=", value, "telck");
            return (Criteria) this;
        }

        public Criteria andTelckLike(String value) {
            addCriterion("telck like", value, "telck");
            return (Criteria) this;
        }

        public Criteria andTelckNotLike(String value) {
            addCriterion("telck not like", value, "telck");
            return (Criteria) this;
        }

        public Criteria andTelckIn(List<String> values) {
            addCriterion("telck in", values, "telck");
            return (Criteria) this;
        }

        public Criteria andTelckNotIn(List<String> values) {
            addCriterion("telck not in", values, "telck");
            return (Criteria) this;
        }

        public Criteria andTelckBetween(String value1, String value2) {
            addCriterion("telck between", value1, value2, "telck");
            return (Criteria) this;
        }

        public Criteria andTelckNotBetween(String value1, String value2) {
            addCriterion("telck not between", value1, value2, "telck");
            return (Criteria) this;
        }

        public Criteria andPtel1IsNull() {
            addCriterion("ptel1 is null");
            return (Criteria) this;
        }

        public Criteria andPtel1IsNotNull() {
            addCriterion("ptel1 is not null");
            return (Criteria) this;
        }

        public Criteria andPtel1EqualTo(String value) {
            addCriterion("ptel1 =", value, "ptel1");
            return (Criteria) this;
        }

        public Criteria andPtel1NotEqualTo(String value) {
            addCriterion("ptel1 <>", value, "ptel1");
            return (Criteria) this;
        }

        public Criteria andPtel1GreaterThan(String value) {
            addCriterion("ptel1 >", value, "ptel1");
            return (Criteria) this;
        }

        public Criteria andPtel1GreaterThanOrEqualTo(String value) {
            addCriterion("ptel1 >=", value, "ptel1");
            return (Criteria) this;
        }

        public Criteria andPtel1LessThan(String value) {
            addCriterion("ptel1 <", value, "ptel1");
            return (Criteria) this;
        }

        public Criteria andPtel1LessThanOrEqualTo(String value) {
            addCriterion("ptel1 <=", value, "ptel1");
            return (Criteria) this;
        }

        public Criteria andPtel1Like(String value) {
            addCriterion("ptel1 like", value, "ptel1");
            return (Criteria) this;
        }

        public Criteria andPtel1NotLike(String value) {
            addCriterion("ptel1 not like", value, "ptel1");
            return (Criteria) this;
        }

        public Criteria andPtel1In(List<String> values) {
            addCriterion("ptel1 in", values, "ptel1");
            return (Criteria) this;
        }

        public Criteria andPtel1NotIn(List<String> values) {
            addCriterion("ptel1 not in", values, "ptel1");
            return (Criteria) this;
        }

        public Criteria andPtel1Between(String value1, String value2) {
            addCriterion("ptel1 between", value1, value2, "ptel1");
            return (Criteria) this;
        }

        public Criteria andPtel1NotBetween(String value1, String value2) {
            addCriterion("ptel1 not between", value1, value2, "ptel1");
            return (Criteria) this;
        }

        public Criteria andTel1ckIsNull() {
            addCriterion("tel1ck is null");
            return (Criteria) this;
        }

        public Criteria andTel1ckIsNotNull() {
            addCriterion("tel1ck is not null");
            return (Criteria) this;
        }

        public Criteria andTel1ckEqualTo(String value) {
            addCriterion("tel1ck =", value, "tel1ck");
            return (Criteria) this;
        }

        public Criteria andTel1ckNotEqualTo(String value) {
            addCriterion("tel1ck <>", value, "tel1ck");
            return (Criteria) this;
        }

        public Criteria andTel1ckGreaterThan(String value) {
            addCriterion("tel1ck >", value, "tel1ck");
            return (Criteria) this;
        }

        public Criteria andTel1ckGreaterThanOrEqualTo(String value) {
            addCriterion("tel1ck >=", value, "tel1ck");
            return (Criteria) this;
        }

        public Criteria andTel1ckLessThan(String value) {
            addCriterion("tel1ck <", value, "tel1ck");
            return (Criteria) this;
        }

        public Criteria andTel1ckLessThanOrEqualTo(String value) {
            addCriterion("tel1ck <=", value, "tel1ck");
            return (Criteria) this;
        }

        public Criteria andTel1ckLike(String value) {
            addCriterion("tel1ck like", value, "tel1ck");
            return (Criteria) this;
        }

        public Criteria andTel1ckNotLike(String value) {
            addCriterion("tel1ck not like", value, "tel1ck");
            return (Criteria) this;
        }

        public Criteria andTel1ckIn(List<String> values) {
            addCriterion("tel1ck in", values, "tel1ck");
            return (Criteria) this;
        }

        public Criteria andTel1ckNotIn(List<String> values) {
            addCriterion("tel1ck not in", values, "tel1ck");
            return (Criteria) this;
        }

        public Criteria andTel1ckBetween(String value1, String value2) {
            addCriterion("tel1ck between", value1, value2, "tel1ck");
            return (Criteria) this;
        }

        public Criteria andTel1ckNotBetween(String value1, String value2) {
            addCriterion("tel1ck not between", value1, value2, "tel1ck");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNull() {
            addCriterion("createtime is null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNotNull() {
            addCriterion("createtime is not null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeEqualTo(Date value) {
            addCriterion("createtime =", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotEqualTo(Date value) {
            addCriterion("createtime <>", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThan(Date value) {
            addCriterion("createtime >", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("createtime >=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThan(Date value) {
            addCriterion("createtime <", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThanOrEqualTo(Date value) {
            addCriterion("createtime <=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIn(List<Date> values) {
            addCriterion("createtime in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotIn(List<Date> values) {
            addCriterion("createtime not in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeBetween(Date value1, Date value2) {
            addCriterion("createtime between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotBetween(Date value1, Date value2) {
            addCriterion("createtime not between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andSeqstatusIsNull() {
            addCriterion("seqstatus is null");
            return (Criteria) this;
        }

        public Criteria andSeqstatusIsNotNull() {
            addCriterion("seqstatus is not null");
            return (Criteria) this;
        }

        public Criteria andSeqstatusEqualTo(String value) {
            addCriterion("seqstatus =", value, "seqstatus");
            return (Criteria) this;
        }

        public Criteria andSeqstatusNotEqualTo(String value) {
            addCriterion("seqstatus <>", value, "seqstatus");
            return (Criteria) this;
        }

        public Criteria andSeqstatusGreaterThan(String value) {
            addCriterion("seqstatus >", value, "seqstatus");
            return (Criteria) this;
        }

        public Criteria andSeqstatusGreaterThanOrEqualTo(String value) {
            addCriterion("seqstatus >=", value, "seqstatus");
            return (Criteria) this;
        }

        public Criteria andSeqstatusLessThan(String value) {
            addCriterion("seqstatus <", value, "seqstatus");
            return (Criteria) this;
        }

        public Criteria andSeqstatusLessThanOrEqualTo(String value) {
            addCriterion("seqstatus <=", value, "seqstatus");
            return (Criteria) this;
        }

        public Criteria andSeqstatusLike(String value) {
            addCriterion("seqstatus like", value, "seqstatus");
            return (Criteria) this;
        }

        public Criteria andSeqstatusNotLike(String value) {
            addCriterion("seqstatus not like", value, "seqstatus");
            return (Criteria) this;
        }

        public Criteria andSeqstatusIn(List<String> values) {
            addCriterion("seqstatus in", values, "seqstatus");
            return (Criteria) this;
        }

        public Criteria andSeqstatusNotIn(List<String> values) {
            addCriterion("seqstatus not in", values, "seqstatus");
            return (Criteria) this;
        }

        public Criteria andSeqstatusBetween(String value1, String value2) {
            addCriterion("seqstatus between", value1, value2, "seqstatus");
            return (Criteria) this;
        }

        public Criteria andSeqstatusNotBetween(String value1, String value2) {
            addCriterion("seqstatus not between", value1, value2, "seqstatus");
            return (Criteria) this;
        }

        public Criteria andImportbatchidLikeInsensitive(String value) {
            addCriterion("upper(importbatchid) like", value.toUpperCase(), "importbatchid");
            return (Criteria) this;
        }

        public Criteria andCasenoLikeInsensitive(String value) {
            addCriterion("upper(caseno) like", value.toUpperCase(), "caseno");
            return (Criteria) this;
        }

        public Criteria andSerinoLikeInsensitive(String value) {
            addCriterion("upper(serino) like", value.toUpperCase(), "serino");
            return (Criteria) this;
        }

        public Criteria andPrelationLikeInsensitive(String value) {
            addCriterion("upper(prelation) like", value.toUpperCase(), "prelation");
            return (Criteria) this;
        }

        public Criteria andPnameLikeInsensitive(String value) {
            addCriterion("upper(pname) like", value.toUpperCase(), "pname");
            return (Criteria) this;
        }

        public Criteria andPtelLikeInsensitive(String value) {
            addCriterion("upper(ptel) like", value.toUpperCase(), "ptel");
            return (Criteria) this;
        }

        public Criteria andTelckLikeInsensitive(String value) {
            addCriterion("upper(telck) like", value.toUpperCase(), "telck");
            return (Criteria) this;
        }

        public Criteria andPtel1LikeInsensitive(String value) {
            addCriterion("upper(ptel1) like", value.toUpperCase(), "ptel1");
            return (Criteria) this;
        }

        public Criteria andTel1ckLikeInsensitive(String value) {
            addCriterion("upper(tel1ck) like", value.toUpperCase(), "tel1ck");
            return (Criteria) this;
        }

        public Criteria andSeqstatusLikeInsensitive(String value) {
            addCriterion("upper(seqstatus) like", value.toUpperCase(), "seqstatus");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table zncontact
     *
     * @mbggenerated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table zncontact
     *
     * @mbggenerated
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}