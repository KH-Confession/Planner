package com.kh.model.dao;

import com.kh.database.JdbcTemplate;
import com.kh.database.RowMapper;
import com.kh.model.vo.Plan;
import java.util.List;

public class PlanDao {

  public int save(Plan plan) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    String query = "INSERT INTO PLAN(PLAN_ID, WRITER, TITLE, START_DATE, END_DATE, REMIND_ALARM_DATE, COMPLETE)"
        + "VALUES(SEQ_PLAN.NEXTVAL, ?, ?, ?, ?, ?, ?)";
    return jdbcTemplate.executeUpdate(query, plan.getWriter(), plan.getTitle(), plan.getStartDate(),
        plan.getEndDate(), plan.getRemindAlarmDate(), plan.getComplete());
  }

  public int update(Plan update) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    String query = "UPDATE PLAN SET TITLE=?, START_DATE=?, END_DATE=?, REMIND_ALARM_DATE=?, COMPLETE=? "
        + "WHERE (WRITER=? AND PLAN_ID=?)";
    return jdbcTemplate.executeUpdate(query,
        update.getTitle(), update.getStartDate(), update.getEndDate(),
        update.getRemindAlarmDate(), update.getComplete(), update.getWriter(),
        update.getPlanId());
  }

  public int updateCompleteByPlanIdAndWriter(String complete, int planId, String writer) {
    JdbcTemplate jdbctemplate = new JdbcTemplate();
    String query = "UPDATE PLAN SET COMPLETE = ? WHERE PLAN_ID = ? AND WRITER = ?";
    return jdbctemplate.executeUpdate(query, complete, planId, writer);
  }

  public int deleteByPlanIdAndWriter(int planId, String writer) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    String query = "DELETE FROM PLAN WHERE (PLAN_ID=? AND WRITER=?)";
    return jdbcTemplate.executeUpdate(query, planId, writer);
  }

  public Plan findByPlanId(int planId) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    String query = "SELECT * FROM PLAN WHERE PLAN_ID=?";
    RowMapper<Plan> mapper = Plan::from;
    return jdbcTemplate.executeQueryForOne(query, mapper, planId);
  }

  public Plan findByPlanIdAndWriter(int planId, String writer) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    String query = "SELECT * FROM PLAN WHERE PLAN_ID=? AND WRITER=?";
    RowMapper<Plan> mapper = Plan::from;
    return jdbcTemplate.executeQueryForOne(query, mapper, planId, writer);
  }

  public List<Plan> findByWriterOrderByEndDate(String writer) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    String query = "SELECT * FROM PLAN WHERE WRITER = ? ORDER BY END_DATE";
    RowMapper<Plan> mapper = Plan::from;
    return jdbcTemplate.executeQuery(query, mapper, writer);
  }

  public List<Plan> findByWriterAndTitleContaining(String writer, String titleKeyword) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    String query = "SELECT * FROM PLAN WHERE WRITER=? AND TITLE LIKE ?";
    RowMapper<Plan> mapper = Plan::from;
    return jdbcTemplate.executeQuery(query, mapper, writer, "%" + titleKeyword + "%");
  }
}
