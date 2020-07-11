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

import com.google.sps.data.Comments;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.*;

/** Servlet that handles comment data. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  // Gets data from form and puts it in the datastore
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    String username = getParameter(request, "username", "");
    String comment = getParameter(request, "comment", "");
    Date currentDate = new Date();

    // Create Entity for the comment data
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("username", username);
    commentEntity.setProperty("comment", comment);
    commentEntity.setProperty("date", currentDate);

    // Enter the comment into the datastore
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    // Redirect user once comment added TODO: Redirect to Comments tab.
    response.sendRedirect("/index.html?page=comments");
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
    // How many results does the user want
    String commentResults = getParameter(request, "results", "5");
    int numResults = Integer.parseInt(commentResults.trim());

    // Create query for Comments sorted by newest first
    Query query = new Query("Comment").addSort("date", SortDirection.DESCENDING);

    // Get all entities from datastore with that query
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    // Convert data into List of Comments
    List<Comments> comments = new ArrayList<>();
    int counter = 1;
    for (Entity entity : results.asIterable()) {
      // Send back amount of results requested
      if (counter > numResults) {
        break;
      }
      long id = entity.getKey().getId();
      String username = (String) entity.getProperty("username");
      String comment = (String) entity.getProperty("comment");
      Date date = (Date) entity.getProperty("date");

      Comments commentResult = new Comments(id, username, comment, date);
      comments.add(commentResult);
      counter++;
    }

    // Convert Comments into JSON and write to /data
    String json = convertToJson(comments);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  
  // Converts comments into a JSON string using the Gson library.
  private String convertToJson(List<Comments> comments) {
    Gson gson = new Gson();
    String json = gson.toJson(comments);
    return json;
  }
}
