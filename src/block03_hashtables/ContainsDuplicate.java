package block03_hashtables;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * LeetCode 217 — Contains Duplicate (Easy). Паттерн «хеш-таблица», форма
 * «множество виденного (seen set)».
 * <p>
 * Дан массив целых чисел. Вернуть true, если хотя бы одно значение встречается
 * больше одного раза. Идём по массиву и складываем значения в HashSet; если
 * значение уже там — ответ найден, дальше идти незачем.
 * Диапазон значений ±10^9, поэтому массив-счётчик как в блоке 2 не годится:
 * под все возможные значения понадобилось бы два миллиарда ячеек.
 * Время O(n) в среднем, память O(n).
 * <p>
 * Полное условие, примеры и ограничения:
 * {@code docs/problems/block03_hashtables/ContainsDuplicate.md}
 *
 * @see <a href="../../docs/problems/block03_hashtables/ContainsDuplicate.md">ContainsDuplicate.md</a>
 */
public class ContainsDuplicate {

    /*
     * Эталонное решение: множество уже виденных значений.
     * Метод add возвращает false, если элемент в множестве уже был. Это даёт
     * проверку и вставку за одно обращение к таблице, вместо пары
     * contains + add.
     * Утверждение задачи — «существует пара одинаковых», поэтому находка
     * возвращается изнутри цикла, а false — только после цикла, когда весь
     * массив просмотрен. Ставить return false внутри цикла нельзя: одно
     * уникальное значение ничего не доказывает.
     */
    public static boolean containsDuplicate(int[] nums) {
        Set<Integer> seenValues = new HashSet<>();
        for (int currentValue : nums) {
            if (!seenValues.add(currentValue)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        /*
         * Ветви метода и тест, который каждую из них закрывает:
         *   1) add вернул true, значение новое — идём дальше .... [1,2,3,4]
         *   2) add вернул false, дубликат -> return true ........ [1,2,3,1]
         *   3) return false после цикла ........................ [1,2,3,4] — ФОЛБЭК
         *   4) один элемент, повторяться нечему ................ [7]
         *   5) дубликат в самом начале, ранний выход ........... [1,1,2,3]
         *   6) дубликат в самом конце .......................... [1,2,3,3]
         * Пустого массива среди случаев нет: ограничения задают
         * 1 <= nums.length.
         */
        record TestCase(int[] input, boolean expected, String name) {}

        TestCase[] testCases = {
            new TestCase(new int[]{1, 2, 3, 1}, true, "дубликат через весь массив"),
            new TestCase(new int[]{1, 2, 3, 4}, false, "все различны — фолбэк после цикла"),
            new TestCase(new int[]{1, 1, 1, 3, 3, 4, 3, 2, 4, 2}, true, "много дубликатов"),
            new TestCase(new int[]{1, 1, 2, 3}, true, "дубликат в начале, ранний выход"),
            new TestCase(new int[]{1, 2, 3, 3}, true, "дубликат в конце"),
            new TestCase(new int[]{7}, false, "один элемент"),
            new TestCase(new int[]{5, 5}, true, "два одинаковых элемента"),
            new TestCase(new int[]{-1, -2, -1}, true, "отрицательные числа"),
            new TestCase(new int[]{0, -0}, true, "ноль и минус ноль — одно значение"),
            new TestCase(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE}, false,
                         "края диапазона int, разные значения"),
            new TestCase(new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE}, true,
                         "края диапазона int, одинаковые значения"),
        };

        for (TestCase testCase : testCases) {
            check(containsDuplicate(testCase.input()) == testCase.expected(),
                  testCase.name() + ": " + Arrays.toString(testCase.input())
                          + " -> " + containsDuplicate(testCase.input()));
        }

        /*
         * Ранний выход: метод обязан вернуть true, не дочитав массив.
         * Проверяем на входе, где дубликат стоит вторым, а дальше идёт
         * сто тысяч различных значений.
         */
        int[] earlyExit = new int[100_000];
        earlyExit[0] = 1;
        earlyExit[1] = 1;
        for (int currentIndex = 2; currentIndex < earlyExit.length; currentIndex++) {
            earlyExit[currentIndex] = currentIndex;
        }
        check(containsDuplicate(earlyExit), "длинный вход: дубликат на второй позиции");

        /* Длинный вход без дубликатов — фолбэк на полном проходе. */
        int[] allDistinct = new int[100_000];
        for (int currentIndex = 0; currentIndex < allDistinct.length; currentIndex++) {
            allDistinct[currentIndex] = currentIndex;
        }
        check(!containsDuplicate(allDistinct), "длинный вход: все значения различны");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
