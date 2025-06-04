document.addEventListener('DOMContentLoaded', function () {
  let url = window.location.href.split('/');
  let methodId = url.at(-1);
  let groupId = url.at(-2);

  fetch(`http://51.250.110.159:8080/numerical_methods/methods/${methodId}`)
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
        imgElement.src = `http://51.250.110.159:8080${data.imageUrl}`;
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
  let url = window.location.href.split('/');
  let methodId = url.at(-1);

  let form = document.createElement('form');
  form.id = 'solve-form';
  form.style.display = 'flex';
  form.style.flexDirection = 'column';

  let functionLabel = document.createElement('label');
  functionLabel.innerText = 'Функция: ';
  functionLabel.style.display = 'block';

  let functionInput = document.createElement('input');
  functionInput.type = 'text';
  functionInput.name = 'userFunction';
  functionInput.required = true;
  functionInput.style.display = 'block';

  let aLabel = document.createElement('label');
  aLabel.innerText = 'Начало отрезка (a): ';
  aLabel.style.display = 'block';

  let aInput = document.createElement('input');
  aInput.type = 'number';
  aInput.name = 'a';
  aInput.step = 'any';
  aInput.required = true;
  aInput.style.display = 'block';

  let bLabel = document.createElement('label');
  bLabel.innerText = 'Конец отрезка (b): ';
  bLabel.style.display = 'block';

  let bInput = document.createElement('input');
  bInput.type = 'number';
  bInput.name = 'b';
  bInput.step = 'any';
  bInput.required = true;
  bInput.style.display = 'block';

  let epsilonLabel = document.createElement('label');
  epsilonLabel.innerText = 'Точность (ε): ';
  epsilonLabel.style.display = 'block';

  let epsilonInput = document.createElement('input');
  epsilonInput.type = 'number';
  epsilonInput.name = 'epsilon';
  epsilonInput.step = 'any';
  epsilonInput.required = true;
  epsilonInput.style.display = 'block';

  let submitButton = document.createElement('button');
  submitButton.type = 'submit';
  submitButton.innerText = 'Решить';
  submitButton.style.display = 'block';

  form.appendChild(functionLabel);
  form.appendChild(functionInput);

  form.appendChild(aLabel);
  form.appendChild(aInput);

  form.appendChild(bLabel);
  form.appendChild(bInput);

  form.appendChild(epsilonLabel);
  form.appendChild(epsilonInput);

  if (methodId !== '1') {
    let minLabel = document.createElement('label');
    minLabel.innerText = 'Оценка производной снизу (m): ';
    minLabel.style.display = 'block';

    let minInput = document.createElement('input');
    minInput.type = 'number';
    minInput.name = 'minimum';
    minInput.step = 'any';
    minInput.style.display = 'block';

    let note = document.createElement('small');
    note.innerText =
      'Если оценка неизвестна, будет использовано эвристическое условие.';
    note.style.display = 'block';
    note.style.color = 'gray';

    form.appendChild(minLabel);
    form.appendChild(minInput);
    form.appendChild(note);
  }

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
  let requestData = {};
  let mValue = formData.get('minimum');
  let m = mValue ? parseFloat(mValue) : null;
  if (methodId === '2' || methodId === '3' || methodId === '4') {
    requestData = {
      methodId: methodId,
      userFunction: formData.get('userFunction'),
      a: parseFloat(formData.get('a')),
      b: parseFloat(formData.get('b')),
      epsilon: parseFloat(formData.get('epsilon')),
      m: m,
    };
  } else {
    requestData = {
      methodId: methodId,
      userFunction: formData.get('userFunction'),
      a: parseFloat(formData.get('a')),
      b: parseFloat(formData.get('b')),
      epsilon: parseFloat(formData.get('epsilon')),
      m: null,
    };
  }

  fetch('http://51.250.110.159:8080/numerical_methods/nonlinear_equation', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(requestData),
  })
    .then(async (response) => {
      if (!response.ok) {
        const text = await response.text();
        try {
          const errorData = JSON.parse(text);
          throw new Error(errorData.message || 'Network response was not ok');
        } catch (e) {
          throw new Error(text || 'Network response was not ok');
        }
      }
      return response.json();
    })
    .then((data) => {
      displayResult(data.solutionMessage);
    })
    .catch((error) => {
      displayError(error.message);
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

function displayError(errorMessage) {
  let container = document.getElementById('result-container');
  container.innerHTML = '';

  let errorElement = document.createElement('p');
  errorElement.classList.add('error');

  errorElement.innerHTML = `<strong>Ошибка:</strong> ${errorMessage}`;

  container.appendChild(errorElement);

  container.style.display = 'block';
}
