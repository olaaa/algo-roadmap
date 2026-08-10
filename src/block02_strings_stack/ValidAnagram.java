package block02_strings_stack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 242 — Valid Anagram (Easy). Паттерн «счётчик частот».
 * <p>
 * Даны две строки. Проверить, что вторая составлена из тех же символов в том же
 * количестве. Порядок не важен, важны только частоты, поэтому сравниваем не
 * строки, а наборы счётчиков. Время O(n), память O(1) — счётчиков всегда 26.
 * <p>
 * Рядом лежат два альтернативных решения: {@link #isAnagramBySorting} —
 * короткий вариант через сортировку за O(n log n), с которого на собеседовании
 * стартуют, и {@link #isAnagramUnicode} — ответ на follow-up «а если не только
 * латиница», где счёт идёт по кодовым точкам, а не по char.
 * <p>
 * Полное условие, примеры и ограничения:
 * {@code docs/problems/block02_strings_stack/ValidAnagram.md}
 *
 * @see <a href="../../docs/problems/block02_strings_stack/ValidAnagram.md">ValidAnagram.md</a>
 */
public class ValidAnagram {

    /* Строчных латинских букв ровно столько, отсюда размер массива счётчиков. */
    private static final int LOWERCASE_LETTER_COUNT = 26;

    /*
     * Эталонное решение: один массив счётчиков на обе строки.
     * Буква из s увеличивает счётчик, буква из t уменьшает его. Если строки
     * анаграммы, каждое увеличение компенсируется уменьшением и массив
     * обнуляется. Ненулевая ячейка означает перекос: положительная — буква
     * лишняя в s, отрицательная — лишняя в t.
     * Единый проход по обеим строкам возможен только потому, что длины уже
     * проверены и совпадают.
     */
    public static boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        int[] letterCounts = new int[LOWERCASE_LETTER_COUNT];
        for (int currentIndex = 0; currentIndex < s.length(); currentIndex++) {
            letterCounts[s.charAt(currentIndex) - 'a']++;
            letterCounts[t.charAt(currentIndex) - 'a']--;
        }
        for (int count : letterCounts) {
            if (count != 0) {
                return false;
            }
        }
        return true;
    }

    /*
     * Альтернатива для собеседования: отсортировать обе строки и сравнить.
     * Короче и не зависит от алфавита, но O(n log n) по времени и O(n) по памяти
     * из-за копий массивов.
     */
    public static boolean isAnagramBySorting(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        char[] sortedFirst = s.toCharArray();
        char[] sortedSecond = t.toCharArray();
        Arrays.sort(sortedFirst);
        Arrays.sort(sortedSecond);
        return Arrays.equals(sortedFirst, sortedSecond);
    }

    /*
     * Ответ на follow-up «а если строки не только из латиницы».
     * Считаем по КОДОВЫМ ТОЧКАМ, а не по char: символ вне базовой плоскости
     * (эмодзи, редкие иероглифы) занимает два char — суррогатную пару, и посчёт
     * по char развалил бы его на две половинки.
     * Проверка длин здесь не нужна: разное количество символов само даст
     * ненулевой счётчик.
     */
    public static boolean isAnagramUnicode(String s, String t) {
        Map<Integer, Integer> codePointCounts = new HashMap<>();
        s.codePoints().forEach(codePoint -> codePointCounts.merge(codePoint, 1, Integer::sum));
        t.codePoints().forEach(codePoint -> codePointCounts.merge(codePoint, -1, Integer::sum));
        for (int count : codePointCounts.values()) {
            if (count != 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        /*
         * Ветви метода isAnagram и тест, который каждую из них закрывает:
         *   1) длины не совпали -> return false ............. "ab" / "abc"
         *   2) тело цикла подсчёта (проход состоялся) ....... любой случай равной длины
         *   3) цикл подсчёта не начался (пустые строки) ..... "" / ""
         *   4) второй цикл: count != 0 -> return false ...... "rat" / "car", "aacc" / "ccac"
         *   5) второй цикл прошёл, всё ноль -> return true ... "anagram" / "nagaram"
         * Отдельно проверяются перекосы в обе стороны: лишняя буква в s даёт
         * положительный счётчик, лишняя в t — отрицательный.
         */
        record TestCase(String first, String second, boolean expected, String name) {}

        TestCase[] testCases = {
            new TestCase("anagram", "nagaram", true, "классическая анаграмма"),
            new TestCase("ab", "ba", true, "перестановка двух букв"),
            new TestCase("a", "a", true, "один символ, совпадает"),
            new TestCase("listen", "silent", true, "анаграмма из разных слов"),
            new TestCase("abc", "abc", true, "строка сама себе анаграмма"),
            new TestCase("", "", true, "две пустые строки (цикл не начался)"),
            new TestCase("rat", "car", false, "та же длина, другой набор букв"),
            new TestCase("aacc", "ccac", false, "набор тот же, количества разные"),
            new TestCase("a", "b", false, "один символ, не совпадает"),
            new TestCase("ab", "abc", false, "разная длина, вторая длиннее"),
            new TestCase("abc", "ab", false, "разная длина, первая длиннее"),
            new TestCase("aab", "abb", false, "перекос в обе стороны сразу"),
        };

        for (TestCase testCase : testCases) {
            check(isAnagram(testCase.first(), testCase.second()) == testCase.expected(),
                  "isAnagram: " + testCase.name());
            check(isAnagramBySorting(testCase.first(), testCase.second()) == testCase.expected(),
                  "BySorting: " + testCase.name());
            check(isAnagramUnicode(testCase.first(), testCase.second()) == testCase.expected(),
                  "Unicode: " + testCase.name());
        }

        /*
         * Отдельные случаи для варианта с кодовыми точками: символы вне базовой
         * плоскости занимают по два char, поэтому подсчёт по char их бы разрушил.
         * Основные реализации на такой вход не рассчитаны и здесь не проверяются.
         */
        check(isAnagramUnicode("🙂🙃", "🙃🙂"),
              "Unicode: перестановка двух эмодзи -> true");
        check(!isAnagramUnicode("🙂🙂", "🙂🙃"),
              "Unicode: разные эмодзи -> false");

        /*
         * Свинья U+1F437 и пятачок U+1F43D — соседи по таблице, поэтому у их
         * суррогатных пар СОВПАДАЕТ старшая половина U+D83D, различается только
         * младшая (U+DC37 против U+DC3D). Наглядный случай, почему считать надо
         * по кодовым точкам: посимвольный подсчёт видел бы здесь одинаковые
         * половинки и одну различающуюся, то есть работал бы не с теми единицами.
         */
        check(isAnagramUnicode("🐷🐽", "🐽🐷"),
              "Unicode: свинья и пятачок переставлены -> true");
        check(!isAnagramUnicode("🐷🐷", "🐷🐽"),
              "Unicode: две свиньи против свиньи с пятачком -> false");
        check(!isAnagramUnicode("🐷", "🐽"),
              "Unicode: свинья не анаграмма пятачка");
        check("🐷".length() == 2 && "🐷".codePointCount(0, 2) == 1,
              "Unicode: у свиньи length() = 2, а кодовая точка одна");

        check(isAnagramUnicode("кот", "ток"), "Unicode: кириллица, анаграмма");
        check(!isAnagramUnicode("кот", "кит"), "Unicode: кириллица, не анаграмма");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
