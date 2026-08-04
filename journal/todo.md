# todo.md — что осталось (статусы правим на месте)

Легенда: [ ] не начато · [~] в работе · [x] сделано

## Реализация блоков (по ядру из algo-prep-plan.md)
- [x] Блок 1. Массивы, два указателя (4 класса, проверено 16/16 PASS)
- [~] Блок 2. Строки + стек: ValidParentheses [x] (32/32 PASS, +виз.),
      ValidAnagram [ ], ReverseString [ ], FirstUniqueChar [ ]
- [~] Блок 3. Хеш-таблицы: TwoSum [x] (7/7 PASS, +Pattern_HashMap.md +виз.),
      ContainsDuplicate [ ], GroupAnagrams [ ], MajorityElement [ ]
- [ ] Блок 4. Скользящее окно (LongestSubstringNoRepeat, MaxAverageSubarray)
- [ ] Блок 5. Бинарный поиск (BinarySearch, SearchInsertPosition)
- [ ] Блок 6. Деревья и базовые графы (MaxDepth, SymmetricTree, BFS)

## Следующий шаг
- [~] Собрать Блок 2 «Строки + стек» — ПО ОДНОЙ ЗАДАЧЕ за подход, а не все четыре
      сразу. Для каждой: класс + docs/problems/.md + тесты в main() + компиляция
      и прогон, потом визуализация. Порядок — по договорённости с Lela.
      Следующая по блоку 2: ValidAnagram (242), опора в книге — задача 1.2.

- [~] Визуализации для остальных задач блока 1: MergeSortedArray [x],
      ValidPalindrome [ ] (шаблон и рецепт — в progress.md, запись 2026-07-06).
      Имя файла — <Класс>.html в docs/visualizations/blockNN_<тема>/
      (старое «.viz.html» из записи 2026-07-06 отменено 2026-07-10).

## Справочники по структурам данных (docs/datastructures/)
- [x] Queue — интерфейс, шесть методов, PriorityQueue, блокирующие очереди
- [x] Deque — отличия от Queue, ArrayDeque изнутри, стек вместо java.util.Stack
- [x] Stack — абстрактный тип данных, рекурсия ↔ явный стек, монотонный стек
- [ ] Решить судьбу ASCII-графики: в Deque.md схемы переведены с SVG на ASCII
      (кроме кольцевого массива). Если приживётся — перевести Queue.md и Stack.md
      и поправить соглашение про иллюстрации в CLAUDE.md.
      До этого решения НИЧЕГО из docs/datastructures/img/ не удалять, даже
      неиспользуемые файлы (deque-both-ends.svg).
- [ ] HashMap / HashSet — хеширование, коллизии, treeify, equals+hashCode
- [ ] ArrayList vs LinkedList — рост массива, когда что брать
- [ ] TreeMap / TreeSet — красно-чёрное дерево, навигационные методы
- [ ] PriorityQueue отдельным файлом (куча, siftUp/siftDown) — если понадобится глубже

## Идеи на потом (по желанию)
- [ ] Чек-лист прогресса в Excel для отметки решённых задач.
- [ ] Mock-собеседование: проговаривание решения вслух + оценка сложности.
