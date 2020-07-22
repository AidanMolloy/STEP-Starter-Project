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

package com.google.sps;

import com.google.sps.servlets.ViewCommentServlet;
import java.io.IOException;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import javax.servlet.http.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.Date;
import java.io.*;
import java.text.SimpleDateFormat;
import com.google.gson.Gson;
import static org.mockito.Mockito.*;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import static org.junit.Assert.assertTrue;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;


@RunWith(JUnit4.class)
public final class ViewCommentServletTest extends ViewCommentServlet{
  private final LocalServiceTestHelper helper = 
    new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    
  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }


  /**
   * Tests the doPost function. Provides data from form.
   * Compares expected username and comment in commentEntity.
   */

  /* TODO: Generate blobstore url and send test to that url
  @Test
  public void doPostTest() throws IOException{
    HttpServletRequest request = mock(HttpServletRequest.class);       
    HttpServletResponse response = mock(HttpServletResponse.class);
    DataServlet servlet = new DataServlet();

    // Requested paramater results
    when(request.getParameter("username")).thenReturn("test@user.com");
    when(request.getParameter("comment")).thenReturn("Test Comment");

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);

    servlet.doPost(request, response);
    String result = stringWriter.toString();
    Assert.assertTrue(result.contains("\"username\":\"test@user.com\""));
    Assert.assertTrue(result.contains("\"comment\":\"Test Comment\""));

  } */

  /**
   * Tests the doGet function. Creates datastore with comment.
   * Compares datastore response to expected output.
   */

  @Test
  public void doGetTest() throws IOException{
    HttpServletRequest request = mock(HttpServletRequest.class);       
    HttpServletResponse response = mock(HttpServletResponse.class);
    ViewCommentServlet servlet = new ViewCommentServlet();
    Date currentDate = new Date();

    // Requested paramater results
    when(request.getParameter("results")).thenReturn("1");

    // Create datastore
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    // Create a new commentEntity for testing
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("username", "test@user.com");
    commentEntity.setProperty("comment", "Test Comment");
    commentEntity.setProperty("date", currentDate);
    commentEntity.setProperty("id", commentEntity.getKey().getId());

    datastore.put(commentEntity);

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);

    servlet.doGet(request, response);
    String result = stringWriter.toString();
    System.out.println(result);
    Assert.assertTrue(result.contains("\"username\":\"test@user.com\""));
    Assert.assertTrue(result.contains("\"comment\":\"Test Comment\""));
  }

}