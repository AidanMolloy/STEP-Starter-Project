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

  // When comments page is selected load the comments
  if (page == "comments"){
    getComments(getNumResults());
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
      let dateCell = row.insertCell(2);
      let deleteCell = row.insertCell(3);
      let deleteForm = `
        <form action="/delete-data" method="POST">
          <input type="hidden" id="userId" name="userId" value="${comment.id}">
          <input type="submit" value="Delete">
        </form>
      `

      usernameCell.innerHTML = comment.username;
      commentCell.innerHTML = comment.comment;
      dateCell.innerHTML = comment.currentDate;
      deleteCell.innerHTML = deleteForm;
    })
  });
}

// Authenticate user
function authenticate() {
  userInfo = document.getElementById("userInfo");
  logInOut = document.getElementById("logInOut");
  fetch(`/auth`).then(response => response.json()).then((authenticated) => {
    // Let user leave a comment if they are logged in.
    if (authenticated.email) {
      userInfo.innerHTML = "Hello, " + authenticated.email + ", would you like to leave a comment?";
      logInOut.innerHTML = `<a href="${authenticated.logoutUrl}">Logout</a>`;
    } else {
      hideCommentForm()
      userInfo.innerHTML = "Please login to leave a comment.";
      logInOut.innerHTML = `<a href="${authenticated.loginUrl}">Login</a>`;
    }
  });
}

// By default hide comment form
function hideCommentForm() {
    commentForm = document.getElementById("commentForm");
    commentForm.style.display = "none";
}
