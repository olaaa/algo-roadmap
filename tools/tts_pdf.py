#!/usr/bin/env python3
"""Сборка PDF из markdown-файлов проекта для чтения вслух в Natural Reader.

Запускается в Linux-песочнице Клода, не на машине Lela: нужны pandoc,
weasyprint и beautifulsoup4. На Windows ничего этого нет и не требуется.

    pip install weasyprint beautifulsoup4 --break-system-packages
    python3 tools/tts_pdf.py A.md B.md -o /путь/выход.pdf

Зачем всё это. Синтез речи часть знаков молча проглатывает, а без точек
читает текст сплошняком, сливая таблицы и схемы в одно предложение. Скрипт
правит это в СОБРАННОМ PDF, не трогая исходные .md. Что именно и почему —
в CLAUDE.md, раздел «PDF для чтения вслух (Natural Reader)».

Проверено на слух в Natural Reader:
  «-» между пробелами (a - b) не произносится    -> рядом слово «минус»;
  «-» перед цифрой (-5) произносится верно       -> не трогаем;
  «−» U+2212 не произносится вообще              -> приводим к дефису;
  «→» не произносится                            -> рядом слово-подсказка,
      причём слово зависит от роли стрелки: в записи отображения
      «ключ → значение» это «соответствует», в выводе «сигнал → класс» —
      «значит». Роль определяется по обрамлению, см. arrow_word.

Подсказки и точки красятся бледно-серым: глазу почти не видно, в текстовом
слое они есть. В блоки с кодом (```java) подсказки НЕ ставятся.
"""

import argparse
import re
import subprocess
import sys
from pathlib import Path

from bs4 import BeautifulSoup, NavigableString

ENDINGS = '.!?:…»'
BLOCKS = ['h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'p', 'li', 'td', 'th', 'dt', 'dd']
NBSP = ' '
TYPOGRAPHIC_MINUS = '−'
MAPPING_ARROW_WORD = 'соответствует'
LOGIC_ARROW_WORD = 'значит'

CSS = """
@page {
  size: A4; margin: 16mm 14mm 18mm 14mm;
  @bottom-center { content: counter(page) " / " counter(pages);
    font-family:"DejaVu Sans",sans-serif; font-size:10pt; color:#8b90a0 }
}
body { font-family:"DejaVu Serif",serif; font-size:13pt; line-height:1.55; color:#1b1f2a }
h1 { font-family:"DejaVu Sans",sans-serif; font-size:23pt; margin:0 0 4px; color:#10131b;
     border-bottom:2px solid #4f8cff; padding-bottom:7px }
h2 { font-family:"DejaVu Sans",sans-serif; font-size:17pt; margin:24px 0 9px; color:#10131b;
     border-bottom:1px solid #d8dce6; padding-bottom:4px; page-break-after:avoid }
h3 { font-family:"DejaVu Sans",sans-serif; font-size:14pt; margin:18px 0 7px; color:#23324d;
     page-break-after:avoid }
h4 { font-family:"DejaVu Sans",sans-serif; font-size:12.5pt; margin:15px 0 6px; color:#23324d;
     page-break-after:avoid }
p { margin:9px 0; text-align:justify }
ul, ol { margin:9px 0; padding-left:24px }
li { margin:5px 0 }
code { font-family:"DejaVu Sans Mono",monospace; font-size:11pt; background:#eef1f6;
       padding:1px 4px; border-radius:3px; color:#1f3355 }
pre { background:#f6f8fb; border:1px solid #dde2ec; border-left:3px solid #4f8cff;
      border-radius:4px; padding:11px 14px; margin:11px 0; page-break-inside:avoid }
pre code { background:none; padding:0; font-size:10.5pt; line-height:1.45; color:#1b2430 }
table { border-collapse:collapse; width:100%; margin:12px 0; font-size:11.5pt;
        page-break-inside:avoid }
th, td { border:1px solid #ccd3e0; padding:7px 9px; text-align:left; vertical-align:top }
th { background:#eef1f6; font-family:"DejaVu Sans",sans-serif; font-weight:bold }
tr:nth-child(even) td { background:#fafbfd }
blockquote { border-left:3px solid #c9d2e2; margin:11px 0; padding:3px 0 3px 14px; color:#48505f }
strong { color:#10131b }
hr.sep { border:none; border-top:2px solid #4f8cff; margin:0; page-break-before:always }

/* Служебные вставки для синтеза речи: в тексте есть, глазу почти не видны. */
.tts { color:#cdd3dd }
"""


def md_to_html(path: Path) -> str:
    return subprocess.run(['pandoc', str(path), '-f', 'gfm', '-t', 'html5'],
                          capture_output=True, text=True, check=True).stdout


def in_code_block(node) -> bool:
    """True для содержимого блока с указанным языком: <pre><code class=...>.

    У текстового блока без языка класса нет, и он считается текстом.
    """
    holder = node.find_parent('code')
    return holder is not None and holder.get('class') is not None


def normalize_minus_in_code(soup) -> int:
    """В коде типографский минус меняем на дефис молча, без подсказки:
    иначе строка перестаёт быть валидным кодом."""
    fixed = 0
    for node in list(soup.find_all(string=True)):
        if not in_code_block(node):
            continue
        text = str(node)
        if TYPOGRAPHIC_MINUS in text:
            fixed += text.count(TYPOGRAPHIC_MINUS)
            node.replace_with(NavigableString(text.replace(TYPOGRAPHIC_MINUS, '-')))
    return fixed


def arrow_word(text: str, position: int, inside_inline_code: bool) -> str:
    """
    У стрелки две роли, и вслух они звучат по-разному.

    Отображение — запись вида «символ → последняя позиция» или `a→0`: слева
    ключ, справа значение. «Значит» тут не подходит («символ значит позиция»),
    нужно «соответствует».

    Вывод — «сигнал → класс», «участок → окно». Здесь как раз «значит».

    Отличаем по обрамлению: стрелка внутри кавычек-ёлочек или внутри
    инлайнового кода считается отображением, в остальных случаях выводом.
    """
    if inside_inline_code:
        return MAPPING_ARROW_WORD

    opening_before = text.rfind('«', 0, position)
    closing_before = text.rfind('»', 0, position)
    if opening_before == -1 or closing_before > opening_before:
        return LOGIC_ARROW_WORD

    closing_after = text.find('»', position)
    opening_after = text.find('«', position)
    if closing_after == -1:
        return LOGIC_ARROW_WORD
    if opening_after != -1 and opening_after < closing_after:
        return LOGIC_ARROW_WORD
    return MAPPING_ARROW_WORD


def voice_symbols(soup) -> tuple[int, int]:
    """Ставит слово-подсказку рядом с минусом и стрелкой. Код не трогает."""
    minuses = arrows = 0
    for node in list(soup.find_all(string=True)):
        if in_code_block(node):
            continue
        text = str(node).replace(TYPOGRAPHIC_MINUS, '-')
        if ' - ' not in text and '→' not in text:
            if text != str(node):
                node.replace_with(NavigableString(text))
            continue

        inside_inline_code = node.find_parent('code') is not None
        pieces = []
        position = 0
        for match in re.finditer(r'(?<=\s)-(?=\s)|→', text):
            start, end = match.span()
            if start > position:
                pieces.append(NavigableString(text[position:start]))
            symbol = match.group()
            if symbol == '-':
                word = 'минус'
                minuses += 1
            else:
                word = arrow_word(text, start, inside_inline_code)
                arrows += 1
            pieces.append(NavigableString(symbol + ' '))
            hint = soup.new_tag('span')
            hint['class'] = 'tts'
            hint.string = word
            pieces.append(hint)
            position = end
            # В записи вида a→0 за подсказкой сразу идёт цифра, и синтез
            # слышит «соответствует0». Отделяем пробелом.
            if position < len(text) and not text[position].isspace():
                pieces.append(NavigableString(' '))
        if position < len(text):
            pieces.append(NavigableString(text[position:]))
        node.replace_with(*pieces)
    return minuses, arrows


def glue_inline_code(soup) -> int:
    """Пробел рядом с инлайновым <code> теряется при извлечении текста, и синтез
    слышит «участкаk». Ставим неразрывный пробел; если он оказывается одиноким
    узлом между двумя тегами, теряется и он — тогда переносим внутрь
    предыдущего тега."""
    fixed = 0
    for tag in soup.find_all('code'):
        if tag.find_parent('pre'):
            continue
        before = tag.previous_sibling
        if isinstance(before, NavigableString) and str(before).endswith((' ', NBSP)):
            head = str(before)[:-1]
            host = before.previous_sibling
            if head == '' and hasattr(host, 'append'):
                before.extract()
                host.append(NavigableString(NBSP))
            else:
                before.replace_with(NavigableString(head + NBSP))
            fixed += 1
    return fixed


def stops_in_text_blocks(soup) -> int:
    """Блоки без указания языка — это текст: примеры ввода-вывода и ASCII-схемы.
    Синтез читает их сплошняком, поэтому точка идёт в конец каждой строки."""
    added = 0
    for code in soup.find_all('code'):
        pre = code.find_parent('pre')
        if pre is None or code.get('class'):
            continue
        lines = code.get_text().split('\n')
        code.clear()
        for number, line in enumerate(lines):
            code.append(NavigableString(line))
            if any(ch.isalnum() for ch in line) and line.rstrip()[-1] not in ENDINGS:
                dot = soup.new_tag('span')
                dot['class'] = 'tts'
                dot.string = '.'
                code.append(dot)
                added += 1
            if number != len(lines) - 1:
                code.append(NavigableString('\n'))
    return added


def add_stops(soup) -> int:
    """Точка в конец заголовка, пункта списка и ячейки таблицы: без неё синтез
    читает их подряд одним предложением."""
    added = 0
    for tag in soup.find_all(BLOCKS):
        if tag.find_parent(['pre', 'code']):
            continue
        if tag.name == 'li' and tag.find('p', recursive=False):
            continue
        nested = tag.find(['ul', 'ol'], recursive=False)
        text = tag.get_text()
        if nested:
            text = text[:len(text) - len(nested.get_text())]
        text = text.rstrip()
        if not text or text[-1] in ENDINGS:
            continue
        dot = soup.new_tag('span')
        dot['class'] = 'tts'
        dot.string = '.'
        nested.insert_before(dot) if nested else tag.append(dot)
        added += 1
    return added


def verify(pdf_path: Path) -> None:
    """На глаз подсказки в PDF не проверить — смотрим текстовый слой."""
    from pypdf import PdfReader

    reader = PdfReader(str(pdf_path))
    text = "\n".join(page.extract_text() for page in reader.pages)
    merges = sorted(set(re.findall(r'[а-яёА-ЯЁ][A-Za-z]{2,}|[A-Za-z]{2,}[а-яёА-ЯЁ]', text)))

    print(f'страниц           : {len(reader.pages)}')
    print(f'осталось U+2212   : {text.count(TYPOGRAPHIC_MINUS)} (должно быть 0)')
    print(f'слов «минус»      : {text.count("минус")}')
    print(f'слов «значит»     : {text.count("значит")}')
    print(f'склейки слов      : {merges[:5] if merges else "нет"}')


def build(sources: list[Path], output: Path) -> None:
    body = '\n<hr class="sep">\n'.join(md_to_html(path) for path in sources)
    # Ссылки на локальные .md в PDF никуда не ведут — оставляем текст.
    body = re.sub(r'<a href="(?!https?:)[^"]*">(.*?)</a>', r'\1', body, flags=re.S)

    soup = BeautifulSoup(body, 'html.parser')
    in_code = normalize_minus_in_code(soup)
    minuses, arrows = voice_symbols(soup)
    glued = glue_inline_code(soup)
    block_dots = stops_in_text_blocks(soup)
    dots = add_stops(soup)

    html = ('<!DOCTYPE html><html lang="ru"><head><meta charset="utf-8">'
            f'<title>{output.stem}</title></head><body>{soup}</body></html>')

    from weasyprint import CSS as WeasyCSS, HTML as WeasyHTML
    WeasyHTML(string=html).write_pdf(str(output), stylesheets=[WeasyCSS(string=CSS)])

    print(f'минусов озвучено  : {minuses}')
    print(f'стрелок озвучено  : {arrows}')
    print(f'минусов в коде    : {in_code} (приведены к дефису)')
    print(f'пробелов склеено  : {glued}')
    print(f'точек в блоках    : {block_dots}')
    print(f'точек в прозе     : {dots}')
    verify(output)


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__,
                                     formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument('sources', nargs='+', type=Path, help='исходные .md, по порядку')
    parser.add_argument('-o', '--output', type=Path, required=True, help='куда положить PDF')
    args = parser.parse_args()

    missing = [str(path) for path in args.sources if not path.is_file()]
    if missing:
        print('не найдены файлы: ' + ', '.join(missing), file=sys.stderr)
        return 1

    args.output.parent.mkdir(parents=True, exist_ok=True)
    build(args.sources, args.output)
    return 0


if __name__ == '__main__':
    raise SystemExit(main())
