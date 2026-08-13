package block02_strings_stack;

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
 * Обобщение на произвольный алфавит вынесено в отдельный класс
 * {@link FirstUniqueCharLinkedHashMap}.
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

    public static void main(String[] args) {
        /*
         * Ветви метода firstUniqChar и тест, который каждую из них закрывает:
         *   1) первый цикл, тело выполняется .............. любая непустая строка
         *   2) второй цикл, условие == 1 истинно .......... "leetcode" -> 0
         *   3) второй цикл, условие == 1 ложно, идём дальше "loveleetcode" -> 2
         *   4) return currentIndex, ответ НЕ в начале ...... "loveleetcode" -> 2
         *   5) return NOT_FOUND после цикла ............... "aabb" — ФОЛБЭК
         *   6) второй цикл не начался (пустая строка) ..... "" -> -1
         *   7) ответ на последней позиции ................. "aab" -> 2
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
                  testCase.name() + ": \"" + testCase.input()
                          + "\" -> " + firstUniqChar(testCase.input()));
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
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
