package com.kh.servlet;

import com.kh.servlet.user.DeleteUserController;
import com.kh.servlet.user.GetUserInfoController;
import com.kh.servlet.user.NicknameDuplicateController;
import com.kh.servlet.user.SignInController;
import com.kh.servlet.user.SignUpController;
import com.kh.servlet.user.UserIdDuplicateController;
import java.util.HashMap;
import java.util.Map;

public class RequestMapping {

  Map<String, RestController> map = new HashMap<>();

  public void initMapping() {
    map.put("/api/user/duplicate/userid", new UserIdDuplicateController());
    map.put("/api/user/duplicate/nickname", new NicknameDuplicateController());
    map.put("/api/user/signup", new SignUpController());
    map.put("/api/user/signin", new SignInController());
    map.put("/api/user/info", new GetUserInfoController());
    map.put("/api/user/delete", new DeleteUserController());
  }

  public RestController findController(String url) {
    return this.map.get(url);
  }
}
