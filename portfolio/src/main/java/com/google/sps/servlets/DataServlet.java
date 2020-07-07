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

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import java.util.*;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private ArrayList<String> comments = new ArrayList<String>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    addComments(comments);
    String json = convertToJson(comments);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  private void addComments(ArrayList comments) {
    comments.add("Aidan M");
    comments.add("Wow!");
    comments.add("John D");
    comments.add("Amazing!");
    comments.add("Grace F");
    comments.add("Interesting!");
  }

  private String convertToJson(ArrayList comments) {
    String json = "{";
    int j = 0;
    for (int i = 0; i < comments.size(); i++) {
      json += "\"author" + j + "\": ";
      json += "\"" + comments.get(i) + "\"";
      json += ", ";
      i++;
      json += "\"comment" + j + "\": ";
      json += "\"" + comments.get(i) + "\"";
      json += ", ";
      j++;
    }
    json = json.substring(0, json.length()-2);
    json += "}";

    return json;
  }
}
