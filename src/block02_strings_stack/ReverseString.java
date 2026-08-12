package block02_strings_stack;

import java.util.Arrays;

/**
 * LeetCode 344 — Reverse String (Easy). Паттерн «два указателя», встречное
 * движение.
 * <p>
 * Дан массив символов. Развернуть его НА МЕСТЕ, с O(1) дополнительной памяти.
 * Два указателя идут с концов навстречу, на каждом шаге меняя символы местами.
 * Итераций ровно n/2 — каждый обмен ставит на места сразу два символа.
 * Время O(n), память O(1).
 * <p>
 * На вход подаётся char[], а не String, потому что String неизменяем и
 * развернуть его «на месте» невозможно в принципе.
 * <p>
 * Полное условие, примеры и ограничения:
 * {@code docs/problems/block02_strings_stack/ReverseString.md}
 *
 * @see <a href="../../docs/problems/block02_strings_stack/ReverseString.md">ReverseString.md</a>
 */
public class ReverseString {

    /*
     * Условие цикла строгое: leftIndex < rightIndex.
     * На нечётной длине указатели сходятся в одной точке, и при leftIndex ==
     * rightIndex обмен означал бы «поменять центральный символ сам с собой» —
     * три операции впустую. На чётной длине указатели не совпадают никогда:
     * они перескакивают друг через друга сразу после последнего обмена.
     */
    public static void reverseString(char[] s) {
        int leftIndex = 0;
        int rightIndex = s.length - 1;
        while (leftIndex < rightIndex) {
            /*
             * Временная переменная обязательна: без неё первое присваивание
             * затрёт значение, которое ещё понадобится второму.
             */
            char temp = s[leftIndex];
            s[leftIndex] = s[rightIndex];
            s[rightIndex] = temp;
            leftIndex++;
            rightIndex--;
        }
    }

    public static void main(String[] args) {
        /*
         * Ветви метода и случай, который каждую из них закрывает:
         *   1) цикл не начался, длина 0 ................. пустой массив
         *   2) цикл не начался, длина 1 ................. один символ
         *   3) ровно одна итерация ...................... два символа
         *   4) чётная длина, указатели перескакивают .... "Hannah", "abcd"
         *   5) нечётная длина, центр не трогаем ......... "hello", "abc"
         * Отдельно проверяется, что палиндром после разворота не меняется —
         * это ловит ошибку, при которой обмен вообще не выполняется.
         */
        record TestCase(char[] input, char[] expected, String name) {}

        TestCase[] testCases = {
            new TestCase("hello".toCharArray(), "olleh".toCharArray(), "нечётная длина"),
            new TestCase("Hannah".toCharArray(), "hannaH".toCharArray(), "чётная длина"),
            new TestCase("abc".toCharArray(), "cba".toCharArray(), "три символа, центр на месте"),
            new TestCase("abcd".toCharArray(), "dcba".toCharArray(), "четыре символа"),
            new TestCase("ab".toCharArray(), "ba".toCharArray(), "ровно одна итерация"),
            new TestCase("a".toCharArray(), "a".toCharArray(), "один символ, цикл не начался"),
            new TestCase(new char[]{}, new char[]{}, "пустой массив, цикл не начался"),
            new TestCase("aba".toCharArray(), "aba".toCharArray(), "палиндром не меняется"),
            new TestCase("  ".toCharArray(), "  ".toCharArray(), "пробелы — обычные символы"),
        };

        for (TestCase testCase : testCases) {
            char[] actual = testCase.input().clone();
            reverseString(actual);
            check(Arrays.equals(actual, testCase.expected()),
                  testCase.name() + ": " + new String(testCase.input())
                          + " -> " + new String(actual));
        }

        /* Двойной разворот обязан вернуть исходный массив. */
        char[] twice = "algorithm".toCharArray();
        reverseString(twice);
        reverseString(twice);
        check(Arrays.equals(twice, "algorithm".toCharArray()),
              "двойной разворот возвращает исходное");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
