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

/**
 * Opens the selected content.
 */

function openContent(evt, page) {
  let i, content, navitem;

  content = document.getElementsByClassName("content");
  for (i = 0; i < content.length; i++) {
    content[i].style.display = "none";
  }

  navitem = document.getElementsByClassName("navitem");
  for (i = 0; i < navitem.length; i++) {
    navitem[i].className = navitem[i].className.replace(" active", "");
  }

  document.getElementById(page).style.display = "block";
  evt.currentTarget.className += " active";

  if(page == "about"){
    let headerElement = document.getElementById("headerSection");
    headerElement.classList.remove("minimise");
    headerElement.classList.add("maximise");
    let footerElement = document.getElementById("footerSection");
    footerElement.classList.add("maximiseFooter");
    footerElement.classList.remove("minimiseFooter");
  }else{
    let headerElement = document.getElementById("headerSection");
    headerElement.classList.add("minimise");
    headerElement.classList.remove("maximise");
    let footerElement = document.getElementById("footerSection");
    footerElement.classList.remove("maximiseFooter");
    footerElement.classList.add("minimiseFooter");
  }

  if (page =="comments"){
    getComments();
  }
}

function getComments() {
  fetch('/data').then(response => response.json()).then((comments) => {
    // comment is an object, not a string, so we have to
    // reference its fields to create HTML content

    const commentListElement = document.getElementById('comment-area');
    commentListElement.innerHTML = '';
    commentListElement.appendChild(
        createListElement('Author: ' + comments.author0));
    commentListElement.appendChild(
        createListElement('Comment: ' + comments.comment0));
    commentListElement.appendChild(
        createListElement(" "));
    commentListElement.appendChild(
        createListElement('Author: ' + comments.author1));
    commentListElement.appendChild(
        createListElement('Comment: ' + comments.comment1));
    commentListElement.appendChild(
        createListElement(" "));
    commentListElement.appendChild(
        createListElement('Author: ' + comments.author2));
    commentListElement.appendChild(
        createListElement('Comment: ' + comments.comment2));
  });
}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}
