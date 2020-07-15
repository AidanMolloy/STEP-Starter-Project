// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;


/**
 * The auth servlet is responsible for authenticating users.
 */
@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Set response header
    response.setContentType("application/json;");

    // Reference to UserService
    UserService userService = UserServiceFactory.getUserService();
    Map<String, String> authResponse = new HashMap<String, String>();


    // Check if user is logged
    if (userService.isUserLoggedIn()) {
      String userEmail = userService.getCurrentUser().getEmail();
      String urlToRedirectToAfterUserLogsOut = "/index.html?page=comments";
      String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);

      authResponse.put("email", userEmail);
      authResponse.put("logoutUrl", logoutUrl);

      // Convert into JSON and write to /auth
      String json = convertToJson(authResponse);
      response.getWriter().println(json);
    } else {
      String urlToRedirectToAfterUserLogsIn = "/index.html?page=comments";
      String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);

      authResponse.put("email", "");
      authResponse.put("loginUrl", loginUrl);

      // Convert into JSON and write to /auth
      String json = convertToJson(authResponse);
      response.getWriter().println(json);
    }
  }
  // Convert into a JSON string using the Gson library.
  private String convertToJson(Map<String, String> authResponse) {
    Gson gson = new Gson();
    String json = gson.toJson(authResponse);
    return json;
  }
}
