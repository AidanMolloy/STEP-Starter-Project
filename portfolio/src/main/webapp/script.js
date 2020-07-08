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
    getComments();
  }
}

// Fetches data and adds the result to the DOM
function getComments() {
  fetch('/data').then(response => response.json()).then((comments) => {

    // Select the table
    const table = document.getElementById("comment-table");

    // For each entry in the Comments select the id and the array
    for (let [key, value] of Object.entries(comments)) {
      // Create new row 
      let row = table.insertRow(0);

      // Create cells in the row
      let cell1 = row.insertCell(0);
      let cell2 = row.insertCell(1);
      let cell3 = row.insertCell(2);

      // Add some text to the new cells:
      cell1.innerHTML = value[0];
      cell2.innerHTML = value[1];
      cell3.innerHTML = value[2];
    }
  });
}
