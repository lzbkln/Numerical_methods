import express from "express";
import path from "path";

const app = express();
const __dirname = path.resolve();
app.use(express.static(path.join(__dirname, "public")));
app.get("/", (req, res) => {
  res.sendFile(path.join(__dirname, `/public/pages/page1.html`));
})
app.get("/groups", (req, res) => {
  res.sendFile(path.join(__dirname, `/public/pages/page2.html`));
})
app.get("/method/:group/:method", (req, res) => {
  res.sendFile(path.join(__dirname, `/public/pages/page3.html`));
})

app.listen(800, () => console.log("Listening on port 800"));

