document.addEventListener('DOMContentLoaded', function () {
  let url = window.location.href.split('/');
  let methodId = url.at(-1);
  let groupId = url.at(-2);

  fetch(`http://51.250.110.159/numerical_methods/methods/${methodId}`)
    .then((response) => response.json())
    .then((data) => {
      let nameElement = document.querySelector('.method-name');
      let descriptionElement = document.querySelector('.method-description');
      let exampleElement = document.querySelector('.method-example');
      let imgElement = document.querySelector('.method-image');

      nameElement.innerText = data.name;
      descriptionElement.innerHTML = data.description;
      exampleElement.innerHTML = data.example;

      MathJax.typeset([exampleElement]);

      if (data.imageUrl) {
        imgElement.src = `http://51.250.110.159${data.imageUrl}`;
        imgElement.alt = 'Method Image';
        imgElement.style.display = 'block';
      }

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
    methodId: methodId,
    userFunction: formData.get('userFunction'),
    a: parseFloat(formData.get('a')),
    b: parseFloat(formData.get('b')),
    epsilon: parseFloat(formData.get('epsilon')),
  };

  fetch('http://51.250.110.159/numerical_methods/nonlinear_equation', {
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
      displayResult(data.solutionMessage);
    })
    .catch((error) => {
      console.error('Ошибка:', error);
    });
}

function displayResult(text) {
  let container = document.getElementById('result-container');

  container.innerHTML = '';

  let resultElement = document.createElement('p');
  resultElement.classList.add('result');

  let formattedText = text.replace(/\n/g, '<br>');

  resultElement.innerHTML = `<strong>Решение:</strong> ${formattedText}`;

  MathJax.typeset([resultElement]);

  container.appendChild(resultElement);

  container.style.display = 'block';
}
