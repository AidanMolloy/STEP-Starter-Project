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
import java.io.*; 
import java.lang.*; 
import java.util.*; 
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;  

/** Servlet that returns comment data. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  // Create comments Array
  public String[][] comments = { {"Aidan Molloy", "Love the website", "12:00 - 01/01/2020"} };

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    String userName = getParameter(request, "user-name", "");
    String comment = getParameter(request, "comment", "");

    // Get current date.
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy");
    LocalDateTime now = LocalDateTime.now();

    // Add the new comment to Array.
    String[] newComment = {userName, comment, dtf.format(now)};
    comments = addComment(comments.length, comments, newComment);

    // Redirect user once comment added TODO: Redirect to Comments tab.
    response.sendRedirect("/index.html#comments");
  }

  /**
  * Creates duplicate Array with one extra index holding the new comment data.
  * ArrayLists causing issues with Gson.
  * TODO: Use ArrayLists instead of creating new Array for each comment.
  */
  public static String[][] addComment(int arrayLength, String arr[][], String commentToBeAdded[])
  {
    int i;

    // create a new array of size arrayLength+1
    String newarr[][] = new String[arrayLength + 1][3];

    // insert the elements from the old array into the new array
    // insert all elements till arrayLength then insert the newComment at arrayLength+1
    for (i = 0; i < arrayLength; i++)
        newarr[i] = arr[i];

    newarr[arrayLength] = commentToBeAdded;

    return newarr;
  } 

  // Get data from the form
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  // Print the comments data to /data
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String json = convertToJson(comments);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  
  // Converts comments into a JSON string using the Gson library.
  private String convertToJson(String[][] comments) {
    Gson gson = new Gson();
    String json = gson.toJson(comments);
    return json;
  }
}
