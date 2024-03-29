package com.kh.servlet.plan;

import com.kh.model.dao.PlanDao;
import com.kh.model.dao.UserDao;
import com.kh.model.vo.Plan;
import com.kh.model.vo.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class SearchTitleController
 */
@WebServlet("/search.pl")
public class SearchTitleServlet extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");
    String keyWord = request.getParameter("search");
    HttpSession session = request.getSession();
    String userId = (String)session.getAttribute("userId");
    List<Plan> planList = new PlanDao().findByWriterAndTitleContaining(userId, keyWord);
    User user = new UserDao().findByUserId(userId);
    JSONObject responseBody = new JSONObject();
    JSONArray planJsonList = new JSONArray();

    for (Plan plan : planList) {
      JSONObject planJson = new JSONObject();

      planJson.put("title", plan.getTitle());
      planJson.put("startDate", plan.getStartDate());
      planJson.put("endDate", plan.getEndDate());
      planJson.put("remindAlarmDate", plan.getRemindAlarmDate());
      planJson.put("planId", plan.getPlanId());
      planJson.put("complete", plan.getComplete());
      planJsonList.put(planJson);
    }
    responseBody.put("planList", planJsonList);
    responseBody.put("nickname", user.getNickname());

    response.setStatus(200);
    response.getWriter().print(responseBody.toString());
    response.getWriter().close();


  }


}
