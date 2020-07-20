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

// Get query from URL to activate correct page
function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

// Displays selected content
function openContent(evt, page) {
  let i;

  // Set all the pages display to none
  Array.from(document.getElementsByClassName("content")).forEach(
    function(page) {
        page.style.display = "none";
    }
  );

  // Once a page is selected make it active and display it
  const navitem = document.getElementsByClassName("navitem");
  for (i = 0; i < navitem.length; i++) {
    navitem[i].className = navitem[i].className.replace(" active", "");
  }

  document.getElementById(page).style.display = "block";
  evt.currentTarget.className += " active";

  // Select the header and footer
  const headerElement = document.getElementById("headerSection");
  const footerElement = document.getElementById("footerSection");

  // If on the about page make the header and footer large otherwise shrink them
  if(page == "about"){
    headerElement.classList.remove("minimise");
    headerElement.classList.add("maximise");
    footerElement.classList.add("maximiseFooter");
    footerElement.classList.remove("minimiseFooter");
  }else{
    headerElement.classList.add("minimise");
    headerElement.classList.remove("maximise");
    footerElement.classList.remove("maximiseFooter");
    footerElement.classList.add("minimiseFooter");
  }
}

// Get users preference for max comments results
function getNumResults() {
  let numResults = document.getElementById("results").value;
  if (numResults == ""){
    numResults = 5;
  }

  return numResults;
}

// Fetches data and adds the result to the DOM
function getComments(numResults) {
  fetch(`/data?results=${numResults}`).then(response => response.json()).then((comments) => {
    // Select the table and empty it
    const table = document.getElementById("comment-table");
    table.innerHTML = `
          <tr>
            <th>Username</th>
            <th>Comment</th>
            <th>Image</th>
            <th>Date</th>
            <th>Delete Comment</th>
          </tr>`;

    // For every comment create a new row and fill cells with data
    // Reversed to show newest first
    comments.reverse();
    comments.forEach((comment) => {
      let row = table.insertRow(1);
      let usernameCell = row.insertCell(0);
      let commentCell = row.insertCell(1);
      let imageCell = row.insertCell(2);
      let dateCell = row.insertCell(3);
      let deleteCell = row.insertCell(4);
      let deleteForm = `
        <form action="/delete-data" method="POST">
          <input type="hidden" name="commentId" value="${comment.id}">
          <input type="submit" value="Delete">
        </form>
      `

      usernameCell.innerHTML = comment.username;
      commentCell.innerHTML = comment.comment;
      if (comment.image) {
        imageCell.innerHTML = `<img width="100px" height="100px" src="${comment.image}"/>`;
      } else {
        imageCell.innerHTML = "No image";
      }
      dateCell.innerHTML = comment.currentDate;
      deleteCell.innerHTML = deleteForm;
    })
  });
  authenticate();
}

// Authenticate user
function authenticate() {
  userInfo = document.getElementById("userInfo");
  logInOut = document.getElementById("logInOut");
  fetch(`/auth`).then(response => response.json()).then((authenticated) => {
    // User can leave a comment if they are logged in and have set a username.
    if (authenticated.loggedIn == "true") {
      if (authenticated.username) {
        userInfo.innerHTML = "Hello, " + authenticated.username + ", would you like to leave a comment?";
        logInOut.innerHTML = `<a href="${authenticated.logoutUrl}">Logout</a><br><p>Change your username <a href=\"/username\">here</a>.</p>`;
        showCommentForm(authenticated.username);
      } else {
        userInfo.innerHTML = "Please set a username to leave a comment.";
        logInOut.innerHTML = `<p>Change your username <a href=\"/username\">here</a>.</p>`;
      }
    } else {
      userInfo.innerHTML = "Please login to leave a comment.";
      logInOut.innerHTML = `<a href="${authenticated.loginUrl}">Login</a>`;
    }

  });
}

// Create comment form with action pointing to blobstore 
function showCommentForm(username) {
    let formExists = document.getElementById("comment-form");
    if (formExists) {
      formExists.parentNode.removeChild(f);
    }
    fetch('/blobstore-upload-url')
      .then((response) => {
        return response.text();
      })
      .then((imageUploadUrl) => {
        let commentForm = document.createElement("FORM");
        commentForm.id = "comment-form";
        commentForm.action = imageUploadUrl;
        commentForm.method = "POST";
        commentForm.enctype = "multipart/form-data";
        commentForm.innerHTML =  `
          <input type="hidden" id="username" name="username" value="${username}">
          <label for="comment">Comment:</label><br>
          <textarea id="comment" name="comment" placeholder="Enter your comment here..." rows="4" cols="50"></textarea><br>
          <label for="image">Image: </label>
          <input type="file" name="image">
          <br>

          <input type="submit" />
        `;
        document.getElementById("commentFormSection").appendChild(commentForm);
      });
}