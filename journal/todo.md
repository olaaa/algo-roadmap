# todo.md — что осталось (статусы правим на месте)

Легенда: [ ] не начато · [~] в работе · [x] сделано

## Реализация блоков (по ядру из algo-prep-plan.md)
- [x] Блок 1. Массивы, два указателя (5 классов: TwoSumSorted, ReverseArray,
      ValidPalindrome, MergeSortedArray — 16/16 PASS; MoveZeroes — 9/9 PASS)
- [~] Блок 2. Строки + стек: ValidParentheses [x] (32/32 PASS, +виз.),
      ValidAnagram [x] (40/40 PASS, +виз.),
      ReverseString [x] (10/10 PASS, +виз.),
      FirstUniqueChar [~] код написан (22/22 PASS, +виз.), Lela разбирает
      ПОМЕТКА: «[x]» у задачи ставим, только когда Lela закончила её изучать,
      а не когда код написан и прогнан. Написанный код — это [~].
- [~] Блок 3. Хеш-таблицы: TwoSum [x] (7/7 PASS, +Pattern_HashMap.md +виз.),
      ContainsDuplicate [ ], GroupAnagrams [ ], MajorityElement [ ]
- [ ] Блок 4. Скользящее окно (LongestSubstringNoRepeat, MaxAverageSubarray)
- [ ] Блок 5. Бинарный поиск (BinarySearch, SearchInsertPosition)
- [ ] Блок 6. Деревья и базовые графы (MaxDepth, SymmetricTree, BFS).
      ВНИМАНИЕ: docs/problems/block06_trees_graphs/Pattern_GraphTraversal.md
      уже существует, но это ЧЕРНОВИК, а не готовый материал. Он писался не для
      блока 6, а как побочный результат разбора структур данных: объяснения
      DFS/BFS разрослись внутри Stack.md и Queue.md, и их вынесли в отдельный
      файл, чтобы не раздувать справочники.
      Что сделать при работе над блоком 6:
        1. переписать Pattern_GraphTraversal.md под реальные задачи блока
           (сейчас код не компилировался, API вершины выдуман, примеры на
           абстрактном графе, а по плану дерево и, возможно, сетка);
        2. добавить в него ссылки на классы блока, как сделано в
           Pattern_HashMap.md («сигнал → опорный класс»);
        3. ОСВЕЖИТЬ ССЫЛКИ на него в docs/datastructures/Stack.md и
           docs/datastructures/Queue.md — там оставлены короткие абзацы
           со ссылкой, их формулировки тоже надо будет сверить.

## Следующий шаг
- [~] Собрать Блок 2 «Строки + стек» — ПО ОДНОЙ ЗАДАЧЕ за подход, а не все четыре
      сразу. Для каждой: класс + docs/problems/.md + тесты в main() + компиляция
      и прогон, потом визуализация. Порядок — по договорённости с Lela.
      Сейчас в работе: FirstUniqueChar (387) — последняя задача блока,
      после неё блок 2 закрыт.

- [~] Визуализации для остальных задач блока 1: MergeSortedArray [x],
      ValidPalindrome [ ], MoveZeroes [x] (шаблон и рецепт — в progress.md,
      запись 2026-07-06).
      Имя файла — <Класс>.html в docs/visualizations/blockNN_<тема>/
      (старое «.viz.html» из записи 2026-07-06 отменено 2026-07-10).

## Справочники по структурам данных (docs/datastructures/)
- [x] Queue — интерфейс, шесть методов, PriorityQueue, блокирующие очереди
- [x] Deque — отличия от Queue, ArrayDeque изнутри, стек вместо java.util.Stack
- [x] Stack — абстрактный тип данных, рекурсия ↔ явный стек, монотонный стек
- [x] Судьба ASCII-графики РЕШЕНА: не распространяем. Deque.md остаётся
      единственным файлом с ASCII-схемами, Queue.md и Stack.md живут на SVG.
      Соглашение в CLAUDE.md («иллюстрации — отдельными .svg») не меняем.
      Из docs/datastructures/img/ ничего не удаляем.
- [ ] HashMap / HashSet — хеширование, коллизии, treeify, equals+hashCode
- [ ] ArrayList vs LinkedList — рост массива, когда что брать
- [ ] TreeMap / TreeSet — красно-чёрное дерево, навигационные методы
- [ ] PriorityQueue отдельным файлом (куча, siftUp/siftDown) — если понадобится глубже

## Идеи на потом (по желанию)
- [ ] Чек-лист прогресса в Excel для отметки решённых задач.
- [ ] Mock-собеседование: проговаривание решения вслух + оценка сложности.
