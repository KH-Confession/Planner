package com.kh.model.vo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

  // DB에 사용될 필드

  private String userId;
  private String userPw;
  private String userName;
  private String nickname;
  private String email;
  private String phone;
  private LocalDate enrollDate;

  private String userPwConfirm;

  public static User from(ResultSet resultSet) throws SQLException {
    return User.builder()
        .userId(resultSet.getString("user_id"))
        .userPw(resultSet.getString("user_pw"))
        .userName(resultSet.getString("user_name"))
        .nickname(resultSet.getString("nickname"))
        .email(resultSet.getString("email"))
        .phone(resultSet.getString("phone"))
        .enrollDate(resultSet.getDate("enroll_date").toLocalDate())
        .build();
  }

  public static User requestDto(HttpServletRequest req) throws NullPointerException, IllegalArgumentException {
    String userId = req.getParameter("userId");
    String userPw = req.getParameter("userPw");
    String userPwConfirm = req.getParameter("userPwConfirm");
    String userName = req.getParameter("userName");
    String nickname = req.getParameter("nickname");
    String email = req.getParameter("email");
    String phone = req.getParameter("phone");

    if (!userId.matches("^[A-Za-z][A-Za-z0-9_]{7,16}$")) {
      throw new ValidationException("아이디는 영문자로 시작해야 하며 8~16자의 영문자, 숫자, _를 사용해야합니다.");
    }
    if (!userPw.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()-_=+]).{8,20}$")) {
      throw new ValidationException("비밀번호는 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해야합니다.");
    }
    if (!userPw.equals(userPwConfirm)) {
      throw new ValidationException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    }
    if (!userName.matches("^[가-힣]{2,20}$")) {
      throw new ValidationException("이름은 2~20자의 한글을 사용해야합니다.");
    }
    if (!nickname.matches("^[가-힣a-zA-Z0-9]{3,20}$")) {
      throw new ValidationException("닉네임은 3~20자의 한글, 영문, 숫자를 사용해야합니다.");
    }
    if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
      throw new ValidationException("유효하지 않은 이메일입니다.");
    }
    if (!phone.matches("^01[0-9][0-9]{3,4}[0-9]{4}$")) {
      throw new ValidationException("유효하지 않은 휴대전화번호입니다.");
    }

    return User.builder()
        .userId(req.getParameter("userId"))
        .userPw(req.getParameter("userPw"))
        .userPwConfirm(req.getParameter("userPwConfirm"))
        .userName(req.getParameter("userName"))
        .nickname(req.getParameter("nickname"))
        .email(req.getParameter("email"))
        .phone(req.getParameter("phone"))
        .build();
  }

  public boolean equalsPassword() {
    return this.getUserPw().equals(this.getUserPwConfirm());
  }
}
