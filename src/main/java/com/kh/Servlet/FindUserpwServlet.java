package com.kh.Servlet;

import java.io.IOException;

import com.kh.model.dao.UserDao;
import com.kh.model.vo.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/user/findpw")
public class FindUserpwServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            String userId = req.getParameter("userId");

            User user = new UserDao().findByUserId(userId);
            
            if (user != null) {
                resp.setStatus(HttpServletResponse.SC_OK);
                user.getUserPw();
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    
}