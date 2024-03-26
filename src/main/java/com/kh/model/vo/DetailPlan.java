package com.kh.model.vo;

import com.kh.model.dao.PlanDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.json.JSONObject;

@Data
@AllArgsConstructor
@Builder
public class DetailPlan {

  private static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  private static String DATE_FORMAT = "yyyy-MM-dd";
  private static String TIME_FORMAT = "HH:mm";

  private int detailPlanId;
  private int planId;
  private String writer;
  private String contents;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private LocalDateTime remindAlarmTime;
  private String complete;
  private LocalDateTime createDate;

  public static DetailPlan from(ResultSet resultSet) throws SQLException {
    return DetailPlan.builder()
        .detailPlanId(resultSet.getInt("DETAIL_PLAN_ID"))
        .planId(resultSet.getInt("PLAN_ID"))
        .writer(resultSet.getString("WRITER"))
        .contents(resultSet.getString("CONTENTS"))
        .startTime(parse(resultSet.getString("START_TIME")))
        .endTime(parse(resultSet.getString("END_TIME")))
        .remindAlarmTime(parse(resultSet.getString("REMIND_ALARM_TIME")))
        .createDate(parse(resultSet.getString("CREATE_DATE")))
        .complete(resultSet.getString("COMPLETE"))
        .build();
  }

  private static LocalDateTime parse(String sqlDate) {
    return sqlDate == null ? null :
        LocalDateTime.parse(sqlDate, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
  }

  public static DetailPlan dto(HttpServletRequest req, Object user) {
    String contents = req.getParameter("detailContents");
    String planId = req.getParameter("planIdForDetail");
    String startDate = req.getParameter("detailStartDate");
    String startTime = req.getParameter("detailStartTime");
    String endTime = req.getParameter("detailEndTime");

    return DetailPlan.builder()
        .planId(Integer.parseInt(planId))
        .writer(String.valueOf(user))
        .contents(contents)
        .startTime(LocalDateTime.parse(startDate + startTime,
            DateTimeFormatter.ofPattern(DATE_FORMAT + TIME_FORMAT)))
        .endTime(LocalDateTime.parse(startDate + endTime,
            DateTimeFormatter.ofPattern(DATE_FORMAT + TIME_FORMAT)))
        .build();
  }

  public void validate() {
    Plan parent = new PlanDao().findByPlanId(this.planId);
    LocalDateTime startDate = parent.getStartDate().toLocalDate().atStartOfDay();
    LocalDateTime endDate = parent.getEndDate().toLocalDate().atStartOfDay();

    if (this.getStartTime().isBefore(startDate) || this.getStartTime().isAfter(endDate)) {
      throw new ValidationException("invalid start date");
    }

    if (this.getStartTime().isAfter(this.getEndTime())) {
      throw new ValidationException("invalid start/end time");
    }
  }

  public String getStartDateString() {
    return this.getStartTime().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
  }

  public String getStartTimeString() {
    return this.getStartTime().format(DateTimeFormatter.ofPattern(TIME_FORMAT));
  }

  public String getEndTimeString() {
    return this.getEndTime().format(DateTimeFormatter.ofPattern(TIME_FORMAT));
  }

  public void setFrom(JSONObject requestBody) {
    String updateContents = requestBody.getString("updateContents");
    String updateStartDate = requestBody.getString("updateStartDate");
    String updateStartTime = requestBody.getString("updateStartTime");
    String updateEndTime = requestBody.getString("updateEndTime");
    String updateAlarmTime = requestBody.getString("updateRemindAlarmTime");

    this.setContents(updateContents);
    this.setStartTime(LocalDateTime.parse(updateStartDate + updateStartTime,
        DateTimeFormatter.ofPattern(DATE_FORMAT + TIME_FORMAT)));
    this.setEndTime(LocalDateTime.parse(updateStartDate + updateEndTime,
        DateTimeFormatter.ofPattern(DATE_FORMAT + TIME_FORMAT)));
    this.setRemindAlarmTime(LocalDateTime.parse(updateAlarmTime));
  }
}
