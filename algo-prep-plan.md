# План подготовки к алгоритмической секции

Java Senior · рынок РФ/СНГ · референс — Т-Банк (бывш. Тинькофф)
Дата составления: 27.06.2026

---

## Главная идея

Алгосекция на Java enterprise-собесах в РФ — это не спортивное программирование. Цель не «выучить алгоритмы», а натренировать распознавание паттерна: увидел условие → вспомнил приём → написал чисто и проговорил сложность. Ядро узкое и предсказуемое: массивы, строки, хеш-таблицы, деревья. У Т-Банка бывает отборочный «Контест», типичные задачи — корректность скобок, нормализация данных, обход деревьев.

Оба плана ниже построены вокруг одного и того же ядра. Разница — не в темах, а в глубине. Даже если бросишь на полпути, ты всё равно закроешь самое частое.

---

## Ядро паттернов (6 блоков, от простого к сложному)

| № | Блок | Что закрывает | Ключевой приём |
|---|------|---------------|----------------|
| 1 | Массивы, два указателя | разворот, палиндром, слияние, пары с суммой | два индекса с разных концов |
| 2 | Строки + стек | анаграммы, подсчёт символов, корректность скобок | стек для парных структур |
| 3 | Хеш-таблицы | two sum, частоты, дубликаты, первый уникальный | HashMap за один проход |
| 4 | Скользящее окно | подстрока без повторов, max-сумма окна | окно с двумя границами |
| 5 | Бинарный поиск | поиск в отсортированном, граница | деление пополам, O(log n) |
| 6 | Деревья и базовые графы | обход DFS/BFS, глубина, симметрия | рекурсия / очередь |

Блоки 1–3 закрывают большинство задач уровня скрининга. Блоки 4–6 добавляют то, что отличает Senior.

---

## План A — экспресс, 1–2 недели

Для ситуации «усидчивости может не хватить». Только ядро 1–3 глубоко + 4 и 6 по верхам. Темп: 1 час в день, ~2 задачи. Всего ~25–30 задач.

Ключевой приём против забывания при малой воле: каждый день — одна новая тема + повтор вчерашней задачи по памяти, без подсказок.

| День | Тема | Задачи (LeetCode) |
|------|------|-------------------|
| 1 | Массивы, два указателя | [Two Sum II (167)](https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/), [Valid Palindrome (125)](https://leetcode.com/problems/valid-palindrome/) |
| 2 | Массивы | [Merge Sorted Array (88)](https://leetcode.com/problems/merge-sorted-array/), [Move Zeroes (283)](https://leetcode.com/problems/move-zeroes/) |
| 3 | Строки + стек | [Valid Parentheses (20)](https://leetcode.com/problems/valid-parentheses/), [Valid Anagram (242)](https://leetcode.com/problems/valid-anagram/) |
| 4 | Строки | [Reverse String (344)](https://leetcode.com/problems/reverse-string/), [First Unique Character (387)](https://leetcode.com/problems/first-unique-character-in-a-string/) |
| 5 | Хеш-таблицы | [Two Sum (1)](https://leetcode.com/problems/two-sum/), [Contains Duplicate (217)](https://leetcode.com/problems/contains-duplicate/) |
| 6 | Хеш-таблицы | [Group Anagrams (49)](https://leetcode.com/problems/group-anagrams/), [Majority Element (169)](https://leetcode.com/problems/majority-element/) |
| 7 | Повтор без подсказок | прорешать заново 4 задачи из дней 1–6 |
| 8 | Скользящее окно | [Longest Substring Without Repeating (3)](https://leetcode.com/problems/longest-substring-without-repeating-characters/), [Max Average Subarray (643)](https://leetcode.com/problems/maximum-average-subarray-i/) |
| 9 | Бинарный поиск | [Binary Search (704)](https://leetcode.com/problems/binary-search/), [Search Insert Position (35)](https://leetcode.com/problems/search-insert-position/) |
| 10 | Деревья | [Max Depth of Binary Tree (104)](https://leetcode.com/problems/maximum-depth-of-binary-tree/), [Symmetric Tree (101)](https://leetcode.com/problems/symmetric-tree/) |
| 11–14 | Микс + mock | прорешка вперемешку, проговаривание решения вслух |

Осознанно жертвуем: DP, сложные графы, хитрые структуры. На скрининге риск низкий.

---

## План B — месяц, спокойный темп

То же ядро, но каждый блок глубже (5–7 задач), плюс DP по верхам, сортировки с пониманием устройства, графы серьёзнее. Темп: 1–1.5 часа в день, 4 дня в неделю (3 дня отдых, чтобы не выгореть).

| Неделя | Фокус | Что добавляется к плану A |
|--------|-------|---------------------------|
| 1 | Блоки 1–3 | по 5–7 задач на блок, разбор сложности вслух |
| 2 | Блоки 4–6 | окно/бинпоиск/деревья глубже, обход графа (BFS/DFS) |
| 3 | DP + сортировки + графы | [Climbing Stairs (70)](https://leetcode.com/problems/climbing-stairs/), [House Robber (198)](https://leetcode.com/problems/house-robber/), [Coin Change (322)](https://leetcode.com/problems/coin-change/), [Number of Islands (200)](https://leetcode.com/problems/number-of-islands/); устройство quicksort/mergesort |
| 4 | Только mock | имитация собеса, повтор слабых мест, оценка сложности |

Разница планов: A даёт проходной балл на скрининге и снижает шанс зависнуть на простой задаче. B даёт уверенность на полной алгосекции Senior, включая нестандартную задачу.

Рекомендация: стартуй по плану A. Если на 5–7 день войдёшь в ритм — плавно перетекай в B, ядро одинаковое.

---

## Опорные решения на Java

### Блок 1. Два указателя — Two Sum II (отсортированный массив)

```java
int[] twoSum(int[] nums, int target) {
    int l = 0, r = nums.length - 1;
    while (l < r) {
        int sum = nums[l] + nums[r];
        if (sum == target) return new int[]{l + 1, r + 1};
        if (sum < target) l++;
        else r--;
    }
    return new int[]{-1, -1};
}
```

### Блок 2. Стек — корректность скобок (любимая задача Т-Банка)

```java
boolean isValid(String s) {
    Deque<Character> stack = new ArrayDeque<>();
    Map<Character, Character> pairs = Map.of(')', '(', ']', '[', '}', '{');
    for (char c : s.toCharArray()) {
        if (pairs.containsValue(c)) {
            stack.push(c);
        } else if (pairs.containsKey(c)) {
            if (stack.isEmpty() || stack.pop() != pairs.get(c)) return false;
        }
    }
    return stack.isEmpty();
}
```

### Блок 3. Хеш-таблица — Two Sum за один проход

```java
int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> seen = new HashMap<>();
    for (int i = 0; i < nums.length; i++) {
        int need = target - nums[i];
        if (seen.containsKey(need)) return new int[]{seen.get(need), i};
        seen.put(nums[i], i);
    }
    return new int[]{-1, -1};
}
```

### Блок 4. Скользящее окно — самая длинная подстрока без повторов

```java
int lengthOfLongestSubstring(String s) {
    Map<Character, Integer> last = new HashMap<>();
    int best = 0, start = 0;
    for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        if (last.containsKey(c) && last.get(c) >= start) {
            start = last.get(c) + 1;
        }
        last.put(c, i);
        best = Math.max(best, i - start + 1);
    }
    return best;
}
```

### Блок 5. Бинарный поиск

```java
int search(int[] nums, int target) {
    int l = 0, r = nums.length - 1;
    while (l <= r) {
        int mid = l + (r - l) / 2;   // защита от переполнения
        if (nums[mid] == target) return mid;
        if (nums[mid] < target) l = mid + 1;
        else r = mid - 1;
    }
    return -1;
}
```

### Блок 6. Деревья — максимальная глубина (DFS) и обход в ширину (BFS)

```java
int maxDepth(TreeNode root) {
    if (root == null) return 0;
    return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
}

List<Integer> bfs(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    if (root == null) return result;
    Queue<TreeNode> queue = new LinkedList<>();
    queue.add(root);
    while (!queue.isEmpty()) {
        TreeNode node = queue.poll();
        result.add(node.val);
        if (node.left != null) queue.add(node.left);
        if (node.right != null) queue.add(node.right);
    }
    return result;
}
```

---

## Как готовиться, чтобы запоминалось

Проговаривай решение вслух — на Senior это оценивают отдельно от кода. Всегда называй сложность по времени и памяти (O(n), O(log n), O(n²)). Повторяй вчерашнюю задачу по памяти — повтор важнее количества новых задач. Не гонись за объёмом: 30 задач с пониманием паттерна сильнее 150 прорешанных «на автомате».

---

## Источники

- [Как проходит интервью по Java — Т-Банк](https://www.tbank.ru/career/it/interview/java/)
- [Как проходят интервью в Т-Банке — Т-Образование](https://education.tbank.ru/study/conspectus/interview/)
- [Задачи с собеседований в Т-Банк на Java — Солвит](https://solvit.space/coding?company_ids=62&lang_ids=5)
- [Собеседование в Т-банк разработчиком — ENIGMA AI](https://enigmai.ru/blog/tinkoff-interview/)
- [Вопросы и задачи на собеседовании Java — Практикум Яндекса](https://practicum.yandex.ru/blog/sobesedovanie-java-razrabotchika/)
- [Вопросы и задачи на собеседовании Java в 2025 — iFellow](https://ifellow.ru/media-center/voprosy-i-zadachi-na-sobesedovanii-java-v-2025-godu/)
