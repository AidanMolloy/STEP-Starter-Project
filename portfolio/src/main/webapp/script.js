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
}

/**
 * Fetches a my name from the server and adds it to the DOM.
 */
function getName() {
  console.log('Fetching my name.');

  // The fetch() function returns a Promise because the request is asynchronous.
  const responsePromise = fetch('/data');

  // When the request is complete, pass the response into handleResponse().
  responsePromise.then(handleResponse);
}

/**
 * Handles response by converting it to text and passing the result to
 * addNameToDOM().
 */
function handleResponse(response) {
  console.log('Handling the response.');

  // response.text() returns a Promise, because the response is a stream of
  // content and not a simple variable.
  const textPromise = response.text();

  // When the response is converted to text, pass the result into the
  // addNameToDOM() function.
  textPromise.then(addNameToDOM);
}

/** Adds my name to the DOM. */
function addNameToDOM(name) {
  console.log('Adding name to dom: ' + name);

  const nameContainer = document.getElementById('name-container');
  nameContainer.innerText = quote;
}
