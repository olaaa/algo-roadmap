package block02_strings_stack;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LeetCode 387 — First Unique Character in a String (Easy), обобщение
 * на произвольный алфавит, включая символы вне базовой плоскости.
 * <p>
 * Эталонное решение на int[26] — в {@link FirstUniqueChar}. Здесь массив
 * счётчиков заменён на LinkedHashMap: она обходится в порядке вставки ключей,
 * то есть в порядке первого появления символа, поэтому второй проход идёт
 * по самой карте и строка больше не нужна. Индекс первого появления хранится
 * в значении вместе со счётчиком.
 * <p>
 * Ключ карты — кодовая точка (int). Обход через codePointAt, шаг цикла —
 * Character.charCount. Возвращаемый индекс остаётся в char-единицах, как
 * у эталона и как требует LeetCode.
 * Устройство кодовых точек и суррогатных пар — docs/datastructures/String.md.
 * <p>
 * Время O(n), память O(k), где k — число различных символов.
 * <p>
 * Полное условие, примеры и ограничения:
 * {@code docs/problems/block02_strings_stack/FirstUniqueChar.md}
 *
 * @see <a href="../../docs/problems/block02_strings_stack/FirstUniqueChar.md">FirstUniqueChar.md</a>
 */
public class FirstUniqueCharLinkedHashMap {

    private static final int NOT_FOUND = -1;

    public static int firstUniqChar(String s) {
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
        /*
         * Повторный put по существующему ключу обновляет только значение
         * и не двигает запись в порядке обхода. Именно на этом всё держится:
         * позиция записи остаётся позицией первого появления символа.
         */
        for (CountAndFirstIndex stats : symbolStats.values()) {
            if (stats.count() == 1) {
                return stats.firstIndex();
            }
        }
        return NOT_FOUND;
    }

    public static void main(String[] args) {
        /*
         * Ветви метода и тест, который каждую из них закрывает:
         *   1) ветка seen == null, символ встретился впервые ... любая непустая строка
         *   2) ветка else, символ уже был ..................... "loveleetcode"
         *   3) цикл обхода карты, count == 1 истинно .......... "leetcode" -> 0
         *   4) цикл обхода карты, count == 1 ложно ............ "loveleetcode" -> 2
         *   5) return NOT_FOUND после цикла ................... "aabb" — ФОЛБЭК
         *   6) while не начался (пустая строка) ............... "" -> -1
         *   7) шаг charCount == 2 ............................. случаи с эмодзи
         * Латинские случаи те же, что у эталона: обе реализации обязаны
         * давать одинаковые ответы там, где применимы обе.
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
            new TestCase("ccdaab", 2, "порядок вставки, а не алфавитный"),
        };

        for (TestCase testCase : testCases) {
            check(firstUniqChar(testCase.input()) == testCase.expected(),
                  testCase.name() + ": \"" + testCase.input()
                          + "\" -> " + firstUniqChar(testCase.input()));
        }

        /*
         * Символы вне базовой плоскости: 🐷 занимает две char-единицы, поэтому
         * индексы в ответах считаются с учётом суррогатных пар.
         * Случай "🐷🐽" ключевой: у этих символов общая старшая половина пары
         * U+D83D, и счёт по кодовым точкам различает их как два разных символа.
         */
        check(firstUniqChar("🐷🐽") == 0,
              "Unicode: 🐷🐽 — два разных символа, каждый встречается один раз, ответ 0");
        check(firstUniqChar("🐷a🐷b") == 2,
              "Unicode: повторяется эмодзи, первый уникальный — a на индексе 2");
        check(firstUniqChar("🐷🐽🐷") == 2,
              "Unicode: уникален 🐽, его индекс в char-единицах равен 2");
        check(firstUniqChar("🐷🐷") == NOT_FOUND,
              "Unicode: оба символа повторяются — фолбэк");
        check(firstUniqChar("ёжик") == 0,
              "Unicode: кириллица в базовой плоскости работает как обычно");

        /* Ответы обеих реализаций обязаны совпадать на латинице. */
        for (TestCase testCase : testCases) {
            check(firstUniqChar(testCase.input())
                          == FirstUniqueChar.firstUniqChar(testCase.input()),
                  "сверка с эталоном: \"" + testCase.input() + "\"");
        }
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
