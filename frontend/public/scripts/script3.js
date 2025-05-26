document.addEventListener('DOMContentLoaded', function () {
  let url = window.location.href.split('/');
  let methodId = url.at(-1);
  let groupId = url.at(-2);

  fetch(`http://localhost:8080/numerical_methods/methods/${methodId}`)
    .then((response) => response.json())
    .then((data) => {
      let container = document.getElementById('data-container');

      let nameElement = document.createElement('h2');
      nameElement.classList.add('method-name');
      nameElement.innerText = data.name;

      let descriptionElement = document.createElement('p');
      descriptionElement.classList.add('method-description');
      descriptionElement.innerHTML = `${data.description}`;

      container.appendChild(nameElement);
      container.appendChild(descriptionElement);

      if (data.imageUrl) {
        let imgElement = document.createElement('img');
        imgElement.src = `http://localhost:8080${data.imageUrl}`;
        imgElement.classList.add('method-image');
        imgElement.alt = 'Method Image';
        imgElement.style.maxWidth = '250px';

        container.appendChild(imgElement);
      }

      let exampleElement = document.createElement('p');
      exampleElement.classList.add('method-example');
      exampleElement.innerHTML = `${data.example}`;
      container.appendChild(exampleElement);

      MathJax.typesetPromise([exampleElement]).catch((err) => console.error('Ошибка при рендеринге формул:', err));

      if (groupId === '1') {
        let inputContainer = document.getElementById('input-container');
        createFunctionInput(inputContainer);
      }
    })
    .catch((error) => {
      console.error('Ошибка при загрузке данных:', error);
    });
});

function createFunctionInput(container) {
  let form = document.createElement('form');
  form.id = 'solve-form';

  let functionLabel = document.createElement('label');
  functionLabel.innerText = 'Функция: ';
  let functionInput = document.createElement('input');
  functionInput.type = 'text';
  functionInput.name = 'userFunction';
  functionInput.required = true;

  let aLabel = document.createElement('label');
  aLabel.innerText = 'Начало отрезка (a): ';
  let aInput = document.createElement('input');
  aInput.type = 'number';
  aInput.name = 'a';
  aInput.step = 'any';
  aInput.required = true;

  let bLabel = document.createElement('label');
  bLabel.innerText = 'Конец отрезка (b): ';
  let bInput = document.createElement('input');
  bInput.type = 'number';
  bInput.name = 'b';
  bInput.step = 'any';
  bInput.required = true;

  let epsilonLabel = document.createElement('label');
  epsilonLabel.innerText = 'Точность (ε): ';
  let epsilonInput = document.createElement('input');
  epsilonInput.type = 'number';
  epsilonInput.name = 'epsilon';
  epsilonInput.step = 'any';
  epsilonInput.required = true;

  let submitButton = document.createElement('button');
  submitButton.type = 'submit';
  submitButton.innerText = 'Решить';

  form.appendChild(functionLabel);
  form.appendChild(functionInput);
  form.appendChild(document.createElement('br'));
  form.appendChild(aLabel);
  form.appendChild(aInput);
  form.appendChild(document.createElement('br'));
  form.appendChild(bLabel);
  form.appendChild(bInput);
  form.appendChild(document.createElement('br'));
  form.appendChild(epsilonLabel);
  form.appendChild(epsilonInput);
  form.appendChild(document.createElement('br'));
  form.appendChild(submitButton);

  container.appendChild(form);

  form.addEventListener('submit', function (event) {
    event.preventDefault();
    solveNonlinearEquation();
  });
}

function solveNonlinearEquation() {
  let form = document.getElementById('solve-form');
  let formData = new FormData(form);

  let url = window.location.href.split('/');
  let methodId = url.at(-1);

  let requestData = {
    methodId: parseInt(methodId),
    userFunction: formData.get('userFunction'),
    a: parseFloat(formData.get('a')),
    b: parseFloat(formData.get('b')),
    epsilon: parseFloat(formData.get('epsilon')),
  };

  fetch('http://localhost:8080/numerical_methods/nonlinear_equation', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(requestData),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.json();
    })
    .then((data) => {
      displayResult(data.root);
    })
    .catch((error) => {
      console.error('Ошибка:', error);
    });
}

function displayResult(root) {
  let container = document.getElementById('result-container');

  container.innerHTML = '';

  let resultElement = document.createElement('p');
  resultElement.classList.add('result');
  resultElement.innerHTML = `<strong>Решение:</strong> ${root.toFixed(15)}`;

  container.appendChild(resultElement);

  container.style.display = 'block';
}
