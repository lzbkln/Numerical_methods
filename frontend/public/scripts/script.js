let startButton = document.getElementById("startButtonId");

startButton.addEventListener("click", function(e) {
  e.preventDefault();
  window.location.href = '/groups';
});
