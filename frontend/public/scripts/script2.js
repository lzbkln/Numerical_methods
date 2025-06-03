'use strict'

document.addEventListener("DOMContentLoaded", function() {
  fetch('http://51.250.110.159:8080/numerical_methods/problems').then(response => response.json()).then(data => {
      data.forEach(problem => {
        let newDiv = document.createElement("div");
        newDiv.classList.add("group");

        let button = document.createElement("button");
        button.classList.add("group-button");
        button.innerText = problem.name;

        newDiv.append(button);

        let newDivMethods = document.createElement("div");
        newDivMethods.classList.add("methods");

        problem.methods.forEach(method => {
          let a = document.createElement("a");
          a.classList.add("method-link");
          a.href = `/method/${problem.id}/${method.id}`;
          a.innerText = method.name;
          newDivMethods.append(a);
        });

        newDiv.append(newDivMethods);

        let menu = document.getElementById("menu");
        menu.append(newDiv);
      });

      const buttons = document.querySelectorAll('.group-button');
      buttons.forEach(button => {
        button.addEventListener('click', function() {
          const methods = this.nextElementSibling;
          if (methods.style.maxHeight && methods.style.maxHeight !== '0px') {
            methods.style.maxHeight = '0px';
          } else {
            const methodCount = methods.children.length;
            const heightPerMethod = 100;
            methods.style.maxHeight = `${methodCount * heightPerMethod}px`;
          }
        });
      });
    })
    //.catch(error => console.error('Ошибка при получении данных:', error));
});