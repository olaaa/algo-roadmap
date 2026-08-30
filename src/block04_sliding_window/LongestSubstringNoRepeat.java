package block04_sliding_window;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * LeetCode 3 — Longest Substring Without Repeating Characters (Medium).
 * Скользящее окно переменной ширины.
 * <p>
 * Найти длину самой длинной подстроки без повторяющихся символов. Правая
 * граница расширяет окно на каждом шаге, левая сжимает его, пока в окне
 * остаётся повтор. Время O(n), память O(min(длина строки, размер алфавита)).
 *
 * @see <a href="../../docs/problems/block04_sliding_window/LongestSubstringNoRepeat.md">LongestSubstringNoRepeat.md</a>
 */
public class LongestSubstringNoRepeat {

    /*
     * Инвариант: окно всегда содержит подстроку без повторов. Он
     * восстанавливается циклом while ДО того, как новый символ войдёт
     * в окно, поэтому длина считается уже на корректном окне.
     */
    public static int lengthOfLongestSubstring(String text) {
        Set<Character> windowChars = new HashSet<>();
        int longestLength = 0;
        int windowStart = 0;

        for (int windowEnd = 0; windowEnd < text.length(); windowEnd++) {
            char enteringChar = text.charAt(windowEnd);
            /* Сжимаем окно слева до тех пора, пока входящий символ в нём есть. */
            while (windowChars.contains(enteringChar)) {
                windowChars.remove(text.charAt(windowStart));
                windowStart++;
            }
            windowChars.add(enteringChar);
            /* Множество и окно совпадают по содержимому, поэтому size() — это длина окна. */
            longestLength = Math.max(longestLength, windowChars.size());
        }
        return longestLength;
    }

    /*
     * Тот же ответ без цикла сжатия: карта помнит последнюю позицию каждого
     * символа, и левая граница прыгает сразу за прошлое вхождение повтора.
     * Math.max при сдвиге windowStart обязателен — карта хранит в том числе
     * символы, уже покинувшие окно, а левая граница пятиться не имеет права.
     */
    public static int lengthOfLongestSubstringWithJump(String text) {
        Map<Character, Integer> lastIndexByChar = new HashMap<>();
        int longestLength = 0;
        int windowStart = 0;

        for (int windowEnd = 0; windowEnd < text.length(); windowEnd++) {
            char enteringChar = text.charAt(windowEnd);
// Объектный тип, а не примитивный, потому что null говорит о том, что символ еще не встречался
            Integer previousIndex = lastIndexByChar.get(enteringChar);
            if (previousIndex != null) {
                /*
                 * Инвариант: ЛЕВАЯ ГРАНИЦА ТОЛЬКО РАСТЁТ
                 * Карта помнит последнюю позицию символа в СТРОКЕ, а не в окне,
                 * поэтому прошлое вхождение может лежать левее границы окна.
                 * Случай windowStart > previousIndex + 1: вхождение вне окна.
                 * На "abba" первая 'a' выпала из окна, вытесненная повтором 'b',
                 * и к моменту последней 'a' её прошлая позиция уже за границей —
                 * такое вхождение надо проигнорировать, границу не двигать.
                 * Случай windowStart < previousIndex + 1: вхождение внутри окна,
                 * граница прыгает за него.
                 */
                windowStart = Math.max(windowStart, previousIndex + 1);
            }
            lastIndexByChar.put(enteringChar, windowEnd);
            int windowLength = windowEnd - windowStart + 1;
            longestLength = Math.max(longestLength, windowLength);
        }
        return longestLength;
    }

    public static void main(String[] args) {
        /*
         * Ветви обоих методов и тест, который каждую из них закрывает:
         *   1) цикл for не начался, пустая строка ................ ""
         *   2) while ни разу не сработал, повторов нет ........... "abcde"
         *   3) while сработал один раз ........................... "abcabcbb"
         *   4) while сработал несколько раз за итерацию .......... "abcb"
         *   5) while съел всё окно, все символы равны ............ "bbbbb"
         *   6) Math.max обновил максимум ......................... "pwwkew"
         *   7) Math.max оставил прежний максимум ................. "abcab"
         *   8) previousIndex == null, символ виден впервые ....... любой вход
         *   9) previousIndex внутри окна, граница прыгает ........ "dvdf"
         *  10) previousIndex вне окна, Math.max держит границу ... "abba"
         * Охранной проверки на null нет: ограничения задают 0 <= s.length,
         * то есть строка приходит всегда, пустая строка — законный вход.
         */
        record TestCase(String text, int expected, String name) {}

        TestCase[] testCases = {
            new TestCase("abcabcbb", 3, "пример 1 из условия: abc"),
            new TestCase("bbbbb", 1, "пример 2: все символы одинаковые"),
            new TestCase("pwwkew", 3, "пример 3: лучшее окно wke, а не подпоследовательность pwke"),
            new TestCase("", 0, "пустая строка, цикл не начался"),
            new TestCase("a", 1, "один символ"),
            new TestCase("abcde", 5, "повторов нет вовсе, окно во всю строку"),
            new TestCase("abcb", 3, "за одну итерацию левая граница сдвинулась дважды"),
            new TestCase("abcab", 3, "лучшее окно в начале, максимум больше не растёт"),
            new TestCase("abcdaefg", 7, "лучшее окно в конце"),
            new TestCase("dvdf", 3, "повтор внутри окна: левая граница прыгает за прошлое d"),
            new TestCase("abba", 2, "повтор вне окна: левая граница не пятится назад"),
            new TestCase("   ", 1, "пробелы — такие же символы"),
            new TestCase("a b!a", 4, "пробелы и знаки внутри окна"),
            new TestCase("aab", 2, "повтор в самом начале"),
            new TestCase("tmmzuxt", 5, "лучшее окно mzuxt, начинается после второго m"),
        };

        for (TestCase testCase : testCases) {
            int actualBySet = lengthOfLongestSubstring(testCase.text());
            check(actualBySet == testCase.expected(),
                  "окно с множеством — " + testCase.name()
                          + ": \"" + testCase.text() + "\" -> " + actualBySet);

            int actualByJump = lengthOfLongestSubstringWithJump(testCase.text());
            check(actualByJump == testCase.expected(),
                  "окно с прыжком — " + testCase.name()
                          + ": \"" + testCase.text() + "\" -> " + actualByJump);
        }

        /*
         * Предельный вход по ограничениям задачи: 100 000 символов из четырёх
         * повторяющихся букв. Проверяет, что левая граница движется только
         * вперёд и суммарно даёт O(n), а не квадрат.
         */
        StringBuilder longInput = new StringBuilder();
        while (longInput.length() < 100_000) {
            longInput.append("abcd");
        }
        check(lengthOfLongestSubstring(longInput.toString()) == 4,
              "предельный вход 100 000 символов: лучшее окно abcd");
        check(lengthOfLongestSubstringWithJump(longInput.toString()) == 4,
              "предельный вход, вариант с прыжком: лучшее окно abcd");

        /*
         * Строка из уникальных символов во всю длину: окно ни разу не сжимается,
         * ответ равен длине строки.
         */
        StringBuilder allDistinct = new StringBuilder();
        for (char letter = 0; letter < 512; letter++) {
            allDistinct.append(letter);
        }
        check(lengthOfLongestSubstring(allDistinct.toString()) == 512,
              "512 различных символов: окно во всю строку");
        check(lengthOfLongestSubstringWithJump(allDistinct.toString()) == 512,
              "512 различных символов, вариант с прыжком");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
