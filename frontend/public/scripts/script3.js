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

      MathJax.typeset([descriptionElement]);
      MathJax.typeset([exampleElement]);

      if (data.imageUrl !== null) {
        imgElement.src = `http://51.250.110.159:8080${data.imageUrl}`;
        imgElement.alt = 'Method Image';
        imgElement.style.display = 'block';
      } else {
        imgElement.remove();
      }

      let inputContainer = document.getElementById('input-container');
      if (groupId === '1') {
        createRulesSection(inputContainer);
        createFunctionInput(inputContainer);
      }

      if (groupId === '2' && methodId !== '5' && methodId !== '8') {
        createInterpolationInput(inputContainer);
      }

      if (groupId === '2' && methodId === '8') {
        createHermiteInput(inputContainer);
      }

      if (groupId === '3' && methodId === '9') {
        createGaussianInput(inputContainer);
      }
      if (methodId === '10') {
        createTridiagonalForm(inputContainer);
      }
    })
    .catch((error) => {
      console.error('Ошибка при загрузке данных:', error);
    });
});

function createTridiagonalForm(container) {
  container.innerHTML = '';

  let form = document.createElement('form');
  form.id = 'tridiagonal-form';
  form.style.display = 'flex';
  form.style.flexDirection = 'column';
  form.style.maxWidth = '600px';
  form.style.margin = '0 auto';
  form.style.gap = '1rem';

  let header = document.createElement('h3');
  header.innerText = 'Решить систему линейных уравнений методом трехдиагональной прогонки';
  header.style.fontWeight = '600';
  header.style.fontSize = '20px';
  form.appendChild(header);

  let sizeContainer = document.createElement('div');
  sizeContainer.style.display = 'flex';
  sizeContainer.style.alignItems = 'center';
  sizeContainer.style.gap = '10px';
  sizeContainer.style.marginBottom = '1rem';

  let sizeLabel = document.createElement('label');
  sizeLabel.innerText = 'Количество неизвестных величин в системе:';
  sizeLabel.style.marginRight = '10px';

  let sizeInput = document.createElement('input');
  sizeInput.type = 'number';
  sizeInput.id = 'size';
  sizeInput.name = 'size';
  sizeInput.min = '2';
  sizeInput.max = '6';
  sizeInput.value = '3';
  sizeInput.style.width = '50px';
  sizeInput.style.padding = '0.5rem';
  sizeInput.style.borderRadius = '6px';
  sizeInput.style.border = '1px solid #d1d5db';

  let changeSizeButton = document.createElement('button');
  changeSizeButton.type = 'button';
  changeSizeButton.innerText = 'Изменить размерность';
  changeSizeButton.style.padding = '0.5rem 1rem';
  changeSizeButton.style.fontSize = '16px';
  changeSizeButton.style.borderRadius = '6px';
  changeSizeButton.style.border = 'none';
  changeSizeButton.style.backgroundColor = '#b095b5';
  changeSizeButton.style.color = '#fff';
  changeSizeButton.style.cursor = 'pointer';
  changeSizeButton.style.transition = 'background-color 0.3s ease';

  changeSizeButton.onmouseenter = () => {
    changeSizeButton.style.backgroundColor = '#74597e';
  };

  changeSizeButton.onmouseleave = () => {
    changeSizeButton.style.backgroundColor = '#b095b5';
  };

  changeSizeButton.onclick = (e) => {
    e.preventDefault();
    createTridiagonalEquationInputs(form, parseInt(sizeInput.value));
  };

  sizeContainer.appendChild(sizeLabel);
  sizeContainer.appendChild(sizeInput);
  sizeContainer.appendChild(changeSizeButton);
  form.appendChild(sizeContainer);

  createTridiagonalEquationInputs(form, parseInt(sizeInput.value));

  let submitButton = document.createElement('button');
  submitButton.type = 'submit';
  submitButton.innerText = 'Решить систему уравнений';
  submitButton.style.padding = '0.75rem 1.5rem';
  submitButton.style.fontSize = '18px';
  submitButton.style.borderRadius = '6px';
  submitButton.style.border = 'none';
  submitButton.style.backgroundColor = '#b095b5';
  submitButton.style.color = '#fff';
  submitButton.style.cursor = 'pointer';
  submitButton.style.marginTop = '1rem';
  submitButton.style.transition = 'background-color 0.3s ease';

  submitButton.onmouseenter = () => {
    submitButton.style.backgroundColor = '#74597e';
  };

  submitButton.onmouseleave = () => {
    submitButton.style.backgroundColor = '#b095b5';
  };

  form.appendChild(submitButton);

  form.addEventListener('submit', function (event) {
    event.preventDefault();
    solveTridiagonalSystem();
  });

  container.appendChild(form);
}

function createTridiagonalEquationInputs(form, size) {
  let existingTable = form.querySelector('.equation-input');
  if (existingTable) {
    form.removeChild(existingTable);
  }

  let table = document.createElement('table');
  table.style.width = '100%';
  table.style.borderCollapse = 'collapse';
  table.style.marginBottom = '1rem';
  table.className = 'equation-input';

  let headerRow = document.createElement('tr');
  let headers = [''];
  for (let i = 0; i < size; i++) {
    headers.push(`x${i + 1}`);
  }
  headers.push('b');

  headers.forEach((header) => {
    let th = document.createElement('th');
    th.innerText = header;
    th.style.border = '1px solid #d1d5db';
    th.style.padding = '0.5rem';
    th.style.textAlign = 'center';
    headerRow.appendChild(th);
  });
  table.appendChild(headerRow);

  for (let i = 0; i < size; i++) {
    let row = document.createElement('tr');

    let rowHeader = document.createElement('td');
    rowHeader.innerText = `Уравнение ${i + 1}`;
    rowHeader.style.border = '1px solid #d1d5db';
    rowHeader.style.padding = '0.5rem';
    rowHeader.style.textAlign = 'center';
    row.appendChild(rowHeader);

    for (let j = 0; j < size + 1; j++) {
      let cell = document.createElement('td');
      cell.style.border = '1px solid #d1d5db';
      cell.style.padding = '0.5rem';

      let input = document.createElement('input');
      input.type = 'number';
      input.name = `eq_${i}_${j}`;
      input.style.width = '100%';
      input.style.padding = '0.5rem';
      input.style.borderRadius = '6px';
      input.style.border = '1px solid #d1d5db';
      input.style.textAlign = 'center';

      if (Math.abs(i - j) > 1 && j < size) {
        input.value = '0';
        input.readOnly = true;
        input.style.backgroundColor = '#f3f4f6';
      }

      cell.appendChild(input);
      row.appendChild(cell);
    }
    table.appendChild(row);
  }

  let submitButton = form.querySelector('button[type="submit"]');
  form.insertBefore(table, submitButton);
}

function solveTridiagonalSystem() {
  let form = document.getElementById('tridiagonal-form');
  let formData = new FormData(form);

  let size = parseInt(formData.get('size'));
  let A = [[]];
  let b = [];

  for (let i = 0; i < size; i++) {
    let row = [];
    for (let j = 0; j < size; j++) {
      let value = formData.get(`eq_${i}_${j}`);
      row.push(parseFloat(value) || 0);
    }
    A[i] = row;
    let bValue = formData.get(`eq_${i}_${size}`);
    b.push(parseFloat(bValue) || 0);
  }

  let requestData = {
    n: size,
    A: A,
    b: b,
    tolerance: 1e-6,
    maxIterations: 1000,
  };

  fetch('http://51.250.110.159:8080/numerical_methods/tridiagonal_sweep', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(requestData),
  })
    .then(async (response) => {
      if (!response.ok) {
        const text = await response.text();
        try {
          const errorData = JSON.parse(text);
          throw new Error(errorData.message || 'Network response was not ok');
        } catch {
          throw new Error(text || 'Network response was not ok');
        }
      }
      return response.json();
    })
    .then((data) => {
      displayInterpolationResult(data);
    })
    .catch((error) => {
      displayError(error.message);
    });
}

function createGaussianInput(container) {
  container.innerHTML = '';

  let form = document.createElement('form');
  form.id = 'gaussian-form';
  form.style.display = 'flex';
  form.style.flexDirection = 'column';
  form.style.maxWidth = '600px';
  form.style.margin = '0 auto';
  form.style.gap = '1rem';

  // Заголовок
  let header = document.createElement('h3');
  header.innerText = 'Решить систему линейных уравнений методом Гаусса';
  header.style.fontWeight = '600';
  header.style.fontSize = '20px';
  form.appendChild(header);

  // Контейнер для выбора размерности системы
  let sizeContainer = document.createElement('div');
  sizeContainer.style.display = 'flex';
  sizeContainer.style.alignItems = 'center';
  sizeContainer.style.gap = '10px';
  sizeContainer.style.marginBottom = '1rem';

  let sizeLabel = document.createElement('label');
  sizeLabel.innerText = 'Количество неизвестных величин в системе:';
  sizeLabel.style.marginRight = '10px';

  let sizeInput = document.createElement('input');
  sizeInput.type = 'number';
  sizeInput.id = 'size';
  sizeInput.name = 'size';
  sizeInput.min = '2';
  sizeInput.max = '6';
  sizeInput.value = '3';
  sizeInput.style.width = '50px';
  sizeInput.style.padding = '0.5rem';
  sizeInput.style.borderRadius = '6px';
  sizeInput.style.border = '1px solid #d1d5db';

  let changeSizeButton = document.createElement('button');
  changeSizeButton.type = 'button';
  changeSizeButton.innerText = 'Изменить размерность';
  changeSizeButton.style.padding = '0.5rem 1rem';
  changeSizeButton.style.fontSize = '16px';
  changeSizeButton.style.borderRadius = '6px';
  changeSizeButton.style.border = 'none';
  changeSizeButton.style.backgroundColor = '#b095b5';
  changeSizeButton.style.color = '#fff';
  changeSizeButton.style.cursor = 'pointer';
  changeSizeButton.style.transition = 'background-color 0.3s ease';

  changeSizeButton.onmouseenter = () => {
    changeSizeButton.style.backgroundColor = '#74597e';
  };

  changeSizeButton.onmouseleave = () => {
    changeSizeButton.style.backgroundColor = '#b095b5';
  };

  changeSizeButton.onclick = (e) => {
    e.preventDefault();
    createEquationInputs(form, parseInt(sizeInput.value));
  };

  sizeContainer.appendChild(sizeLabel);
  sizeContainer.appendChild(sizeInput);
  sizeContainer.appendChild(changeSizeButton);
  form.appendChild(sizeContainer);

  // Создание полей ввода для системы уравнений
  createEquationInputs(form, parseInt(sizeInput.value));

  // Кнопка для отправки формы
  let submitButton = document.createElement('button');
  submitButton.type = 'submit';
  submitButton.innerText = 'Решить систему уравнений';
  submitButton.style.padding = '0.75rem 1.5rem';
  submitButton.style.fontSize = '18px';
  submitButton.style.borderRadius = '6px';
  submitButton.style.border = 'none';
  submitButton.style.backgroundColor = '#b095b5';
  submitButton.style.color = '#fff';
  submitButton.style.cursor = 'pointer';
  submitButton.style.marginTop = '1rem';
  submitButton.style.transition = 'background-color 0.3s ease';

  submitButton.onmouseenter = () => {
    submitButton.style.backgroundColor = '#74597e';
  };

  submitButton.onmouseleave = () => {
    submitButton.style.backgroundColor = '#b095b5';
  };

  form.appendChild(submitButton);

  form.addEventListener('submit', function (event) {
    event.preventDefault();
    solveGaussianSystem();
  });

  container.appendChild(form);
}

function createEquationInputs(form, size) {
  // Удаляем предыдущие поля ввода, если они существуют
  let existingTable = form.querySelector('.equation-input');
  if (existingTable) {
    form.removeChild(existingTable);
  }

  // Создаем таблицу для ввода уравнений
  let table = document.createElement('table');
  table.style.width = '100%';
  table.style.borderCollapse = 'collapse';
  table.style.marginBottom = '1rem';
  table.className = 'equation-input';

  // Заголовок таблицы
  let headerRow = document.createElement('tr');
  let headers = [''];
  for (let i = 0; i < size; i++) {
    headers.push(`x${i + 1}`);
  }
  headers.push('b');

  headers.forEach((header) => {
    let th = document.createElement('th');
    th.innerText = header;
    th.style.border = '1px solid #d1d5db';
    th.style.padding = '0.5rem';
    th.style.textAlign = 'center';
    headerRow.appendChild(th);
  });
  table.appendChild(headerRow);

  // Создаем строки для ввода уравнений
  for (let i = 0; i < size; i++) {
    let row = document.createElement('tr');

    // Создаем ячейку для номера строки
    let rowHeader = document.createElement('td');
    rowHeader.innerText = `Уравнение ${i + 1}`;
    rowHeader.style.border = '1px solid #d1d5db';
    rowHeader.style.padding = '0.5rem';
    rowHeader.style.textAlign = 'center';
    row.appendChild(rowHeader);

    // Создаем ячейки для коэффициентов и значения b
    for (let j = 0; j < size + 1; j++) {
      let cell = document.createElement('td');
      cell.style.border = '1px solid #d1d5db';
      cell.style.padding = '0.5rem';

      let input = document.createElement('input');
      input.type = 'number';
      input.name = `eq_${i}_${j}`;
      input.style.width = '100%';
      input.style.padding = '0.5rem';
      input.style.borderRadius = '6px';
      input.style.border = '1px solid #d1d5db';
      input.style.textAlign = 'center';

      cell.appendChild(input);
      row.appendChild(cell);
    }
    table.appendChild(row);
  }

  // Вставляем таблицу перед кнопкой отправки формы
  let submitButton = form.querySelector('button[type="submit"]');
  form.insertBefore(table, submitButton);
}

function solveGaussianSystem() {
  let form = document.getElementById('gaussian-form');
  let formData = new FormData(form);

  let size = parseInt(formData.get('size'));
  let A = [[]];
  let b = [];

  for (let i = 0; i < size; i++) {
    let row = [];
    for (let j = 0; j < size; j++) {
      let value = formData.get(`eq_${i}_${j}`);
      row.push(parseFloat(value) || 0);
    }
    A[i] = row;
    let bValue = formData.get(`eq_${i}_${size}`);
    b.push(parseFloat(bValue) || 0);
  }

  let requestData = {
    n: size,
    A: A,
    b: b,
    tolerance: 1e-6,
    maxIterations: 1000,
  };

  fetch('http://51.250.110.159:8080/numerical_methods/gauss', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(requestData),
  })
    .then(async (response) => {
      if (!response.ok) {
        const text = await response.text();
        try {
          const errorData = JSON.parse(text);
          throw new Error(errorData.message || 'Network response was not ok');
        } catch {
          throw new Error(text || 'Network response was not ok');
        }
      }
      return response.json();
    })
    .then((data) => {
      displayResultSystems(data.solutionMessage);
    })
    .catch((error) => {
      displayError(error.message);
    });
}

function displayResultSystems(data) {
  let container = document.getElementById('result-container');
  container.innerHTML = '';

  let resultElement = document.createElement('p');
  resultElement.classList.add('result');
  let formattedText = data.replace(/\n/g, '<br>');

  resultElement.innerHTML = `<strong>Решение:</strong> ${formattedText}`;

  MathJax.typeset([resultElement]);

  container.appendChild(resultElement);

  container.style.display = 'block';
}

function createRulesSection(container) {
  let rulesSection = document.createElement('details');
  let summary = document.createElement('summary');
  summary.innerText = 'Правила ввода функций';
  rulesSection.appendChild(summary);

  let rulesContent = document.createElement('div');
  rulesContent.className = 'content';
  rulesContent.innerHTML = `
                <div class="section">
                    <h3>1. Операции и их приоритет</h3>
                    <table>
                        <tr>
                            <th>Операция</th>
                            <th>Как писать</th>
                            <th>Примеры</th>
                        </tr>
                        <tr>
                            <td>Сложение</td>
                            <td>+</td>
                            <td>x + 2</td>
                        </tr>
                        <tr>
                            <td>Вычитание</td>
                            <td>-</td>
                            <td>x - 5</td>
                        </tr>
                        <tr>
                            <td>Умножение</td>
                            <td>* (всегда указывать!)</td>
                            <td>2 * x, x * (x + 1)</td>
                        </tr>
                        <tr>
                            <td>Деление</td>
                            <td>/</td>
                            <td>(x^2 + 1) / (x - 2)</td>
                        </tr>
                        <tr>
                            <td>Степень</td>
                            <td>^</td>
                            <td>x^2, x^(1/2)</td>
                        </tr>
                    </table>
                </div>
                <div class="section">
                    <h3>2. Функции</h3>
                    <table>
                        <tr>
                            <th>Название</th>
                            <th>Как писать</th>
                            <th>Пример</th>
                        </tr>
                        <tr>
                            <td>Квадратный корень</td>
                            <td>sqrt(x)</td>
                            <td>sqrt(x + 1)</td>
                        </tr>
                        <tr>
                            <td>Натуральный логарифм</td>
                            <td>log(x) или ln(x)</td>
                            <td>log(x), ln(x)</td>
                        </tr>
                        <tr>
                            <td>Десятичный логарифм</td>
                            <td>log10(x)</td>
                            <td>log10(x)</td>
                        </tr>
                        <tr>
                            <td>Экспонента</td>
                            <td>exp(x)</td>
                            <td>exp(x) = e^x</td>
                        </tr>
                        <tr>
                            <td>Синус / косинус / тангенс</td>
                            <td>sin(x), cos(x), tan(x)</td>
                            <td>sin(x) + cos(x)</td>
                        </tr>
                        <tr>
                            <td>Абсолютное значение</td>
                            <td>abs(x)</td>
                            <td>abs(x - 3)</td>
                        </tr>
                    </table>
                </div>
                <div class="section">
                    <h3>3. Типичные ошибки, которых избегаем</h3>
                    <table>
                        <tr>
                            <th>Неправильно</th>
                            <th>Почему?</th>
                            <th>Правильно</th>
                        </tr>
                        <tr>
                            <td>2x</td>
                            <td>Неявное умножение — не распарсится</td>
                            <td>2 * x</td>
                        </tr>
                        <tr>
                            <td>x(x+1)</td>
                            <td>То же самое</td>
                            <td>x * (x + 1)</td>
                        </tr>
                        <tr>
                            <td>x^1/2</td>
                            <td>Это x^1 делить на 2</td>
                            <td>x^(1/2)</td>
                        </tr>
                        <tr>
                            <td>-x^2</td>
                            <td>Это -(x^2), а не (-x)^2</td>
                            <td>(-x)^2, если это имелось</td>
                        </tr>
                        <tr>
                            <td>sin x</td>
                            <td>Аргумент всегда в скобках</td>
                            <td>sin(x)</td>
                        </tr>
                        <tr>
                            <td>sqrt x+1</td>
                            <td>Нужно скобки</td>
                            <td>sqrt(x + 1)</td>
                        </tr>
                    </table>
                </div>
            `;

  rulesSection.appendChild(rulesContent);
  container.appendChild(rulesSection);

  const details = container.querySelector('details');
  const content = details.querySelector('.content');

  details.addEventListener('toggle', function () {
    if (details.open) {
      content.style.maxHeight = content.scrollHeight + 'px';
    } else {
      content.style.maxHeight = '0';
    }
  });
}

function createHermiteInput(container) {
  let form = document.createElement('form');
  form.id = 'hermite-form';
  form.style.display = 'flex';
  form.style.flexDirection = 'column';
  form.style.maxWidth = '600px';
  form.style.margin = '0 auto';
  form.style.gap = '1rem';

  // Заголовок таблицы
  let tableHeader = document.createElement('h3');
  tableHeader.innerText = 'Введите данные для построения полинома';
  tableHeader.style.fontWeight = '600';
  tableHeader.style.fontSize = '20px';
  container.appendChild(tableHeader);

  // Создаем таблицу
  let table = document.createElement('table');
  table.style.width = '100%';
  table.style.borderCollapse = 'collapse';
  table.style.marginBottom = '1rem';

  // Создаем заголовок таблицы
  let headerRow = document.createElement('tr');
  let headers = ['Узел x', 'Кратность', 'f(x)', 'Производные'];
  headers.forEach((header) => {
    let th = document.createElement('th');
    th.innerText = header;
    th.style.border = '1px solid #d1d5db';
    th.style.padding = '0.5rem';
    th.style.textAlign = 'left';
    headerRow.appendChild(th);
  });
  table.appendChild(headerRow);

  let nodeCount = 2;
  for (let i = 0; i < nodeCount; i++) {
    addNodeRow(table, i);
  }

  // Кнопка для добавления нового узла
  let addNodeButton = document.createElement('button');
  addNodeButton.type = 'button';
  addNodeButton.innerText = 'Добавить узел';
  addNodeButton.style.padding = '0.5rem';
  addNodeButton.style.marginBottom = '1.2rem';
  addNodeButton.style.fontSize = '16px';
  addNodeButton.style.borderRadius = '6px';
  addNodeButton.style.border = '1px solid #d1d5db';
  addNodeButton.style.cursor = 'pointer';
  addNodeButton.onclick = () => {
    addNodeRow(table, nodeCount++);
  };
  container.appendChild(addNodeButton);

  // Добавляем таблицу в форму
  form.appendChild(table);
  container.appendChild(form);

  // Кнопка для отправки формы
  let submitButton = document.createElement('button');
  submitButton.type = 'submit';
  submitButton.innerText = 'Построить полином Эрмита';
  submitButton.style.padding = '0.75rem';
  submitButton.style.fontSize = '18px';
  submitButton.style.borderRadius = '6px';
  submitButton.style.border = 'none';
  submitButton.style.backgroundColor = '#b095b5';
  submitButton.style.color = '#fff';
  submitButton.style.cursor = 'pointer';
  submitButton.style.marginTop = '1rem';
  submitButton.style.transition = 'background-color 0.3s ease';
  submitButton.onmouseenter = () => {
    submitButton.style.backgroundColor = '#74597e';
  };
  submitButton.onmouseleave = () => {
    submitButton.style.backgroundColor = '#7f6a87';
  };

  form.appendChild(submitButton);

  form.addEventListener('submit', function (event) {
    event.preventDefault();
    solveHermiteInterpolation();
  });

  function addNodeRow(table, index) {
    let row = document.createElement('tr');

    // Узел x
    let xInput = document.createElement('input');
    xInput.type = 'text';
    xInput.name = `xValues[${index}]`;
    xInput.placeholder = 'Например: 0';
    xInput.required = true;
    xInput.style.width = '100%';
    xInput.style.padding = '0.5rem';
    xInput.style.borderRadius = '6px';
    xInput.style.border = '1px solid #d1d5db';

    let xCell = document.createElement('td');
    xCell.style.border = '1px solid #d1d5db';
    xCell.appendChild(xInput);
    row.appendChild(xCell);

    // Кратность
    let multiplicityInput = document.createElement('input');
    multiplicityInput.type = 'number';
    multiplicityInput.name = `multiplicities[${index}]`;
    multiplicityInput.placeholder = 'Например: 2';
    multiplicityInput.required = true;
    multiplicityInput.style.width = '100%';
    multiplicityInput.style.padding = '0.5rem';
    multiplicityInput.style.borderRadius = '6px';
    multiplicityInput.style.border = '1px solid #d1d5db';

    let multiplicityCell = document.createElement('td');
    multiplicityCell.style.border = '1px solid #d1d5db';
    multiplicityCell.appendChild(multiplicityInput);
    row.appendChild(multiplicityCell);

    // Значение f(x)
    let fxInput = document.createElement('input');
    fxInput.type = 'text';
    fxInput.name = `fxValues[${index}]`;
    fxInput.placeholder = 'Например: 1';
    fxInput.required = true;
    fxInput.style.width = '100%';
    fxInput.style.padding = '0.5rem';
    fxInput.style.borderRadius = '6px';
    fxInput.style.border = '1px solid #d1d5db';

    let fxCell = document.createElement('td');
    fxCell.style.border = '1px solid #d1d5db';
    fxCell.appendChild(fxInput);
    row.appendChild(fxCell);

    // Производные
    let derivativesInput = document.createElement('input');
    derivativesInput.type = 'text';
    derivativesInput.name = `derivatives[${index}]`;
    derivativesInput.placeholder = 'Например: 3;5';
    derivativesInput.style.width = '100%';
    derivativesInput.style.padding = '0.5rem';
    derivativesInput.style.borderRadius = '6px';
    derivativesInput.style.border = '1px solid #d1d5db';

    let derivativesCell = document.createElement('td');
    derivativesCell.style.border = '1px solid #d1d5db';
    derivativesCell.appendChild(derivativesInput);
    row.appendChild(derivativesCell);

    table.appendChild(row);
  }
}

function solveHermiteInterpolation() {
  let form = document.getElementById('hermite-form');
  let formData = new FormData(form);

  try {
    let xValues = [];
    let multiplicities = [];
    let fxValues = [];
    let derivativesList = [];

    for (let [key, value] of formData.entries()) {
      console.log(key + ' ' + value);
      if (key.startsWith('x')) {
        xValues.push(parseFloat(value));
      } else if (key.startsWith('multiplicities')) {
        multiplicities.push(parseInt(value));
      } else if (key.startsWith('fx')) {
        fxValues.push(parseFloat(value));
      } else if (key.startsWith('derivatives')) {
        let derivativeArray = value ? value.split(';').map(Number) : [];
        derivativesList.push(derivativeArray);
      }
    }

    let requestData = {
      nodes: xValues,
      multiplicities: multiplicities,
      functionValues: fxValues,
      derivatives: derivativesList,
    };

    fetch('http://51.250.110.159:8080/numerical_methods/hermit_interpolation', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(requestData),
    })
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          try {
            const errorData = JSON.parse(text);
            throw new Error(errorData.message || 'Network response was not ok');
          } catch {
            throw new Error(text || 'Network response was not ok');
          }
        }
        return response.json();
      })
      .then((data) => {
        displayInterpolationResult(data);
      })
      .catch((error) => {
        displayError(error.message);
      });
  } catch (e) {
    displayError(e.message);
  }
}

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
      'Если оценка неизвестна, оставьте поле незаполненным.\nБудет использовано эвристическое условие.';
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

  requestData = {
    methodId: methodId,
    userFunction: formData.get('userFunction'),
    a: parseFloat(formData.get('a')),
    b: parseFloat(formData.get('b')),
    epsilon: parseFloat(formData.get('epsilon')),
    m: m,
  };

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

function createInterpolationInput(container) {
  let form = document.createElement('form');
  form.id = 'interpolation-form';
  form.style.display = 'flex';
  form.style.flexDirection = 'column';

  let xLabel = document.createElement('label');
  xLabel.innerText = 'Значения x (через запятую): ';
  xLabel.style.display = 'block';

  let xInput = document.createElement('input');
  xInput.type = 'text';
  xInput.name = 'xValues';
  xInput.required = true;
  xInput.style.display = 'block';

  let fxLabel = document.createElement('label');
  fxLabel.innerText = 'Значения f(x) (через запятую): ';
  fxLabel.style.display = 'block';

  let fxInput = document.createElement('input');
  fxInput.type = 'text';
  fxInput.name = 'fxValues';
  fxInput.required = true;
  fxInput.style.display = 'block';

  let interpLabel = document.createElement('label');
  interpLabel.innerText = 'Точки интерполяции (через запятую): ';
  interpLabel.style.display = 'block';

  let interpInput = document.createElement('input');
  interpInput.type = 'text';
  interpInput.name = 'interpolationPoints';
  interpInput.required = true;
  interpInput.style.display = 'block';

  let submitButton = document.createElement('button');
  submitButton.type = 'submit';
  submitButton.innerText = 'Построить полином';
  submitButton.style.display = 'block';

  form.appendChild(xLabel);
  form.appendChild(xInput);
  form.appendChild(fxLabel);
  form.appendChild(fxInput);
  form.appendChild(interpLabel);
  form.appendChild(interpInput);
  form.appendChild(submitButton);

  container.appendChild(form);

  form.addEventListener('submit', function (event) {
    event.preventDefault();
    solveInterpolation();
  });
}

function solveInterpolation() {
  let url = window.location.href.split('/');
  let methodId = url.at(-1);

  let form = document.getElementById('interpolation-form');
  let formData = new FormData(form);

  let xValues = formData.get('xValues').split(',').map(Number);
  let fxValues = formData.get('fxValues').split(',').map(Number);
  let interpolationPoints = formData
    .get('interpolationPoints')
    .split(',')
    .map(Number);

  let requestData = {
    methodId: methodId,
    xValues: xValues,
    fxValues: fxValues,
    interpolationPoints: interpolationPoints,
  };

  fetch('http://51.250.110.159:8080/numerical_methods/interpolation', {
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
      displayInterpolationResult(data);
    })
    .catch((error) => {
      displayError(error.message);
    });
}

function displayInterpolationResult(data) {
  let container = document.getElementById('result-container');
  container.innerHTML = '';
  let resultElement = document.createElement('div');
  resultElement.classList.add('result');

  let formattedText = data.solutionMessage.replace(/\n/g, '<br>');
  resultElement.innerHTML = `<strong>Результаты интерполяции:</strong><br>${formattedText}`;

  MathJax.typeset([resultElement]);
  container.appendChild(resultElement);
  container.style.display = 'block';
}
