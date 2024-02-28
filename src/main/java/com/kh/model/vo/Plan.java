package com.kh.model.vo;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {

  private int planId;
  private String writer;
  private String title;
  private Date startDate;
  private Date endDate;
  private Date remindAlarmDate;
  private boolean complete;
  private Date createDate;

  public static Plan from(ResultSet resultSet) throws SQLException {
    return Plan.builder()
        .planId(resultSet.getInt("PLAN_ID"))
        .writer(resultSet.getString("WRITER"))
        .title(resultSet.getString("TITLE"))
        .startDate(resultSet.getDate("START_DATE"))
        .endDate(resultSet.getDate("END_DATE"))
        .createDate(resultSet.getDate("CREATE_DATE"))
        .remindAlarmDate(resultSet.getDate("REMIND_ALARM_DATE"))
        .complete(resultSet.getBoolean("COMPLETE"))
        .build();
  }

}