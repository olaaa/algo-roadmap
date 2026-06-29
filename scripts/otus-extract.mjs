// Извлечение "Краткое содержание" по занятиям из сохранённой страницы курса OTUS.
//
// Как пользоваться:
// 1. Открой залогиненную страницу курса https://otus.ru/learning/140561/
// 2. Ctrl+S -> сохрани как "Веб-страница, только HTML" в эту папку, например otus.html
// 3. node scripts/otus-extract.mjs otus.html
//
// Зависимостей нет. Парсер — на регулярках по классам, которые отдаёт OTUS:
//   .js-lesson            — контейнер одного занятия
//   .learning-near__main  — заголовок занятия (номер + название)
//   .learning-near__header == "Краткое содержание" — нужный блок

import { readFileSync } from 'node:fs';

const file = process.argv[2];
if (!file) {
  console.error('Укажи путь к сохранённому HTML: node scripts/otus-extract.mjs otus.html');
  process.exit(1);
}
const html = readFileSync(file, 'utf8');

const strip = (s) =>
  s.replace(/<[^>]+>/g, ' ')
   .replace(/&nbsp;/g, ' ').replace(/&amp;/g, '&')
   .replace(/&lt;/g, '<').replace(/&gt;/g, '>').replace(/&quot;/g, '"')
   .replace(/\s+/g, ' ').trim();

// Разбиваем по контейнерам занятий
const lessons = html.split(/class="[^"]*js-lesson[^"]*"/).slice(1);

const out = [];
for (const chunk of lessons) {
  const titleM = chunk.match(/class="[^"]*learning-near__main[^"]*"[^>]*>([\s\S]*?)<\/a>/);
  const title = titleM ? strip(titleM[1]) : '(без названия)';

  // Находим блок, чей header == "Краткое содержание", и берём текст до следующего header
  let kr = '';
  const headerRe = /class="[^"]*learning-near__header[^"]*"[^>]*>([\s\S]*?)<\/h2>([\s\S]*?)(?=class="[^"]*learning-near__header|class="[^"]*js-lesson|$)/g;
  let m;
  while ((m = headerRe.exec(chunk))) {
    if (/Краткое содержание/.test(strip(m[1]))) { kr = strip(m[2].replace(/<h2[\s\S]*$/i, '')); break; }
  }
  out.push({ title, kratkoe: kr });
}

// Вывод
for (const l of out) {
  console.log('### ' + l.title);
  console.log(l.kratkoe || '(нет краткого содержания)');
  console.log();
}
console.log(`Всего занятий: ${out.length}`);
