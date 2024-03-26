package com.kh.servlet.plan;

import com.kh.model.dao.PlanDao;
import com.kh.model.vo.Plan;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.json.JSONObject;

@WebServlet("/plans")
public class PlanPostServlet extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Object user = req.getSession().getAttribute("userId");
    JSONObject responseBody = new JSONObject();

    try {
      Plan newPlan = Plan.postRequestDto(req, user);
      new PlanDao().save(newPlan);
      resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    } catch (Exception e) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      responseBody.put("message", e.getLocalizedMessage());
    }
    resp.getWriter().write(responseBody.toString());
    resp.getWriter().close();
  }
}
