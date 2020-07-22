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
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * When the user submits the form, Blobstore processes the file upload and then forwards the request
 * to this servlet. This servlet can then process the request using the blob key we get from
 * Blobstore.
 */
@WebServlet("/CreateComment")
public class CreateCommentServlet extends HttpServlet {

  // Create BlobstoreService to getUpload
  private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
  /**
   * The doPost function gets data from comment form. Creates Entity from data.
   * Stores the entity into datastore.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Get the username
    UserService userService = UserServiceFactory.getUserService();
    String username = getUsername(userService.getCurrentUser().getUserId());

    // Get the comment
    String comment = getParameter(request, "comment", "");

    // Get the uploaded image
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
    List<BlobKey> blobKeys = blobs.get("image");

    String imageUrl = "";
    if (blobKeys == null || blobKeys.isEmpty()) {
      imageUrl = "";
    } else {
      imageUrl = "/serve?blob-key=" + blobKeys.get(0).getKeyString();
    }

    // Get current date
    Date currentDate = new Date();

    // Create Entity for the comment
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("username", username);
    commentEntity.setProperty("comment", comment);
    commentEntity.setProperty("image", imageUrl);
    commentEntity.setProperty("date", currentDate);
    commentEntity.setProperty("id", commentEntity.getKey().getId());

    // Enter the comment into the datastore
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    // Redirect user once comment added
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

  /** 
   * Returns the username of the user with id, or null if the user has not set a nickname. 
   */
  private String getUsername(String id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query =
        new Query("UserInfo")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
      return null;
    }
    String username = (String) entity.getProperty("username");
    return username;
  }
}
