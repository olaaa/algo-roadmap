package block02_strings_stack;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LeetCode 387 — First Unique Character in a String (Easy). Паттерн «счётчик
 * частот», форма «поиск по свойству частоты», два прохода.
 * <p>
 * Дана строка из строчных латинских букв. Вернуть индекс первого символа,
 * который встречается ровно один раз, или -1, если такого нет.
 * Первый проход считает частоты в int[26], второй ищет ответ — и идёт ПО СТРОКЕ,
 * а не по массиву счётчиков, иначе потеряется порядок появления символов.
 * Время O(n), память O(1).
 * <p>
 * Рядом лежит вторая реализация {@link #firstUniqCharLinkedHashMap} — обобщение
 * на произвольный алфавит, включая символы вне базовой плоскости. LinkedHashMap
 * хранит порядок вставки, поэтому ответ ищется проходом по самой карте, а ключ —
 * кодовая точка (int), потому что в Character эмодзи не помещается.
 * <p>
 * Полное условие, примеры и ограничения:
 * {@code docs/problems/block02_strings_stack/FirstUniqueChar.md}
 *
 * @see <a href="../../docs/problems/block02_strings_stack/FirstUniqueChar.md">FirstUniqueChar.md</a>
 */
public class FirstUniqueChar {

    private static final int LOWERCASE_LETTER_COUNT = 26;

    private static final int NOT_FOUND = -1;

    /*
     * Эталонное решение: массив на 26 счётчиков и два прохода.
     * Одним проходом задача не решается: уникальность символа на позиции i
     * зависит от того, что стоит правее него, а правая часть строки на этот
     * момент ещё не просмотрена.
     * Второй цикл идёт по СТРОКЕ, а не по массиву счётчиков. В массиве буквы
     * лежат в алфавитном порядке, и обход по нему вернул бы алфавитно первую
     * уникальную букву вместо первой по позиции.
     */
    public static int firstUniqChar(String s) {
        int[] letterCounts = new int[LOWERCASE_LETTER_COUNT];
        for (int currentIndex = 0; currentIndex < s.length(); currentIndex++) {
            letterCounts[s.charAt(currentIndex) - 'a']++;
        }
        for (int currentIndex = 0; currentIndex < s.length(); currentIndex++) {
            if (letterCounts[s.charAt(currentIndex) - 'a'] == 1) {
                return currentIndex;
            }
        }
        /*
         * Фолбэк: цикл прошёл всю строку и ни одного символа со счётчиком 1
         * не встретил. Утверждение задачи — «существует уникальный символ»,
         * поэтому опровержение стоит после цикла, а не внутри.
         */
        return NOT_FOUND;
    }

    /*
     * Обобщение на произвольный алфавит: LinkedHashMap вместо массива.
     * Отличие от HashMap в том, что порядок обхода совпадает с порядком
     * вставки ключей, то есть с порядком первого появления символа в строке.
     * Поэтому второй проход идёт по самой карте, а строка больше не нужна —
     * но индекс приходится хранить в значении вместе со счётчиком.
     * Ключ карты — КОДОВАЯ ТОЧКА (int), а не Character. Character вмещает
     * ровно 16 бит, потолок U+FFFF, поэтому символы вне базовой плоскости
     * (эмодзи, редкие иероглифы) в такой ключ не помещаются: они занимают
     * две кодовые единицы. Считать по Character значило бы считать половинки
     * суррогатных пар — а у 🐷 и 🐽 старшая половина общая, и они склеились бы
     * в один «символ».
     * Возвращаемый индекс — в char-единицах, как у эталона и как требует
     * LeetCode. Поэтому шаг цикла не единица, а Character.charCount.
     * Память O(k), где k — число различных символов, то есть хуже эталона.
     */
    public static int firstUniqCharLinkedHashMap(String s) {
        record CountAndFirstIndex(int count, int firstIndex) {}

        Map<Integer, CountAndFirstIndex> symbolStats = new LinkedHashMap<>();
        int currentIndex = 0;
        while (currentIndex < s.length()) {
            int codePoint = s.codePointAt(currentIndex);
            CountAndFirstIndex seen = symbolStats.get(codePoint);
            if (seen == null) {
                symbolStats.put(codePoint, new CountAndFirstIndex(1, currentIndex));
            } else {
                symbolStats.put(codePoint, new CountAndFirstIndex(seen.count() + 1, seen.firstIndex()));
            }
            currentIndex += Character.charCount(codePoint);
        }
        for (CountAndFirstIndex stats : symbolStats.values()) {
            if (stats.count() == 1) {
                return stats.firstIndex();
            }
        }
        return NOT_FOUND;
    }

    public static void main(String[] args) {
        /*
         * Ветви метода firstUniqChar и тест, который каждую из них закрывает:
         *   1) первый цикл, тело выполняется .............. любая непустая строка
         *   2) второй цикл, условие == 1 истинно .......... "leetcode" -> 0
         *   3) второй цикл, условие == 1 ложно, идём дальше "loveleetcode" -> 2
         *   4) return currentIndex, ответ НЕ в начале ...... "loveleetcode", "aadadaad" нет
         *   5) return NOT_FOUND после цикла ............... "aabb" — ФОЛБЭК
         *   6) второй цикл не начался (пустая строка) ..... "" -> -1
         *   7) ответ на последней позиции ................. "aab" -> 2
         * Обе реализации прогоняются по одному и тому же набору случаев.
         */
        record TestCase(String input, int expected, String name) {}

        TestCase[] testCases = {
            new TestCase("leetcode", 0, "ответ на нулевой позиции"),
            new TestCase("loveleetcode", 2, "первая буква повторяется, ответ дальше"),
            new TestCase("aabb", NOT_FOUND, "уникальных нет — фолбэк после цикла"),
            new TestCase("z", 0, "один символ, он же уникальный"),
            new TestCase("", NOT_FOUND, "пустая строка, цикл не начался"),
            new TestCase("aab", 2, "ответ на последней позиции"),
            new TestCase("abcabd", 2, "уникальны c и d, берём первый по позиции"),
            new TestCase("aaaa", NOT_FOUND, "все символы одинаковые"),
            new TestCase("abcd", 0, "все символы уникальны, ответ первый"),
            new TestCase("dddccbba", 7, "алфавитно последняя буква — первая уникальная"),
        };

        for (TestCase testCase : testCases) {
            check(firstUniqChar(testCase.input()) == testCase.expected(),
                  "массив: " + testCase.name() + ": \"" + testCase.input()
                          + "\" -> " + firstUniqChar(testCase.input()));
            check(firstUniqCharLinkedHashMap(testCase.input()) == testCase.expected(),
                  "LinkedHashMap: " + testCase.name() + ": \"" + testCase.input()
                          + "\" -> " + firstUniqCharLinkedHashMap(testCase.input()));
        }

        /*
         * Отдельная проверка того, что второй проход идёт по строке, а не по
         * массиву счётчиков. В строке "bbaacd" уникальны c (индекс 4) и
         * d (индекс 5). Обход по массиву счётчиков вернул бы c — и здесь это
         * совпало бы с верным ответом. А в строке "ccdaab" уникальны d (индекс 2)
         * и b (индекс 5): алфавитный обход дал бы b, то есть 5 вместо 2.
         */
        check(firstUniqChar("ccdaab") == 2,
              "обход по строке, а не по алфавиту: \"ccdaab\" -> 2, а не 5");

        /* Длинная строка: проверяем, что решение не зависит от размера входа. */
        StringBuilder longInput = new StringBuilder("x".repeat(50_000));
        longInput.append('q');
        check(firstUniqChar(longInput.toString()) == 50_000,
              "длинный вход: единственный уникальный символ в самом конце");

        /*
         * Проверка того, ради чего ключом взята кодовая точка, а не Character.
         * 🐷 (U+1F437) и 🐽 (U+1F43D) — разные символы, но старшая половина
         * суррогатной пары у них общая, U+D83D. Карта с ключом Character
         * увидела бы D83D дважды и решила бы, что символ повторяется; первым
         * «уникальным» оказалась бы младшая половина U+DC37 на индексе 1 —
         * не символ вовсе. Счёт по кодовым точкам даёт верный ответ 0.
         */
        check(firstUniqCharLinkedHashMap("🐷🐽") == 0,
              "Unicode: 🐷🐽 — общая старшая половина не склеивает символы, ответ 0");
        check(firstUniqCharLinkedHashMap("🐷a🐷b") == 2,
              "Unicode: повторяется эмодзи, первый уникальный — a на индексе 2");
        check(firstUniqCharLinkedHashMap("🐷🐽🐷") == 2,
              "Unicode: уникален 🐽, его индекс в char-единицах равен 2");
        check(firstUniqCharLinkedHashMap("🐷🐷") == NOT_FOUND,
              "Unicode: оба символа повторяются — фолбэк");
        check(firstUniqCharLinkedHashMap("ёжик") == 0,
              "Unicode: кириллица в базовой плоскости работает как обычно");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
