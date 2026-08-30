package block04_sliding_window;

import java.util.Arrays;

/**
 * LeetCode 643 — Maximum Average Subarray I (Easy). Скользящее окно
 * фиксированной ширины.
 * <p>
 * Найти непрерывный подмассив длины ровно k с максимальным средним. Соседние
 * окна отличаются двумя элементами, поэтому сумма не пересчитывается заново,
 * а правится на разницу. Время O(n), память O(1).
 *
 * @see <a href="../../docs/problems/block04_sliding_window/MaxAverageSubarray.md">MaxAverageSubarray.md</a>
 */
public class MaxAverageSubarray {

    /*
     * Максимум ищется по СУММЕ, а деление на k делается один раз в конце:
     * все окна одной длины, поэтому порядок по сумме совпадает с порядком
     * по среднему.
     * Приведение к double стоит ДО деления. Иначе оба операнда int, деление
     * целочисленное, и дробная часть молча теряется.
     */
    public static double findMaxAverage(int[] nums, int k) {
        int windowSum = 0;
// первое окно
        for (int windowEnd = 0; windowEnd < k; windowEnd++) {
            windowSum += nums[windowEnd];
        }
// набрано первым циклом (индексы):  [0..3]
        /*
         * Начальный максимум — сумма первого окна, а не ноль: на массиве
         * из одних отрицательных чисел ноль остался бы победителем навсегда.
         */
        int maxSum = windowSum;
        for (int windowEnd = k; windowEnd < nums.length; windowEnd++) {
            /*
             * Окно занимает позиции windowEnd - k + 1 .. windowEnd, поэтому
             * покидает его элемент ровно левее — на windowEnd - k.
             * В первой итерации по данному циклу:
             *     k = 4, windowEnd = 4, новое окно (индексы) [1..4] -> leavingIndex = 0.
             */
            int leavingIndex = windowEnd - k;
            windowSum = windowSum + nums[windowEnd] - nums[leavingIndex];
            maxSum = Math.max(maxSum, windowSum);
        }
        return (double) maxSum / k;
    }

    public static void main(String[] args) {
        /*
         * Ветви метода и тест, который каждую из них закрывает:
         *   1) первый цикл набирает окно ..................... любой вход
         *   2) второй цикл не начался, k == n ................ [1,2,3], k = 3
         *   3) второй цикл, Math.max выбрал новое окно ....... [1,12,-5,-6,50,3]
         *   4) второй цикл, Math.max оставил прежнее ......... [5,1,1,1], k = 1
         *   5) окно из одного элемента ....................... k = 1
         *   6) все числа отрицательные ....................... [-1,-2,-3], k = 2
         * Охранных проверок нет: ограничения задают 1 <= k <= n, то есть
         * ни null, ни ширина окна вне диапазона на вход не приходят.
         */
        record TestCase(int[] nums, int windowSize, double expected, String name) {}

        TestCase[] testCases = {
            new TestCase(new int[]{1, 12, -5, -6, 50, 3}, 4, 12.75, "пример из условия"),
            new TestCase(new int[]{5}, 1, 5.0, "один элемент, окно в него"),
            new TestCase(new int[]{0, 4, 0, 3, 2}, 1, 4.0, "k = 1, ищем максимум"),
            new TestCase(new int[]{-1, -2, -3}, 2, -1.5, "все числа отрицательные"),
            new TestCase(new int[]{1, 2, 3}, 3, 2.0, "окно во весь массив, второй цикл не начался"),
            new TestCase(new int[]{5, 1, 1, 1}, 1, 5.0, "лучшее окно первое, максимум не меняется"),
            new TestCase(new int[]{1, 1, 1, 5}, 1, 5.0, "лучшее окно последнее"),
            new TestCase(new int[]{0, 0, 0}, 2, 0.0, "нули"),
            new TestCase(new int[]{-10000, 10000}, 2, 0.0, "края диапазона значений"),
            new TestCase(new int[]{4, 0, 4, 0}, 2, 2.0, "все окна с одинаковой суммой"),
            new TestCase(new int[]{1, 2}, 1, 2.0, "дробного среднего нет, но деление вещественное"),
        };

        for (TestCase testCase : testCases) {
            double actual = findMaxAverage(testCase.nums(), testCase.windowSize());
            check(Math.abs(actual - testCase.expected()) < 1e-9,
                  testCase.name() + ": " + Arrays.toString(testCase.nums())
                          + ", k = " + testCase.windowSize() + " -> " + actual);
        }

        /*
         * Целочисленное деление. Сумма 42 и k = 4 дают 10.5, а не 10:
         * если бы приведение к double стояло после деления, тест бы упал.
         */
        check(findMaxAverage(new int[]{10, 11, 10, 11}, 4) == 10.5,
              "дробное среднее не потеряно при делении");

        /*
         * Длинный вход: лучшее окно в самом конце, чтобы проверить, что сдвиг
         * доезжает до края массива.
         */
        int length = 100_000;
        int[] longInput = new int[length];
        Arrays.fill(longInput, 1);
        longInput[length - 1] = 10_000;
        double expectedAverage = (1 + 10_000) / 2.0;
        check(findMaxAverage(longInput, 2) == expectedAverage,
              "длинный вход: лучшее окно на самом краю");

        /* Предельная сумма: сто тысяч раз по десять тысяч — миллиард, int выдержит. */
        int[] maxValues = new int[length];
        Arrays.fill(maxValues, 10_000);
        check(findMaxAverage(maxValues, length) == 10_000.0,
              "предельная сумма 10^9 помещается в int");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
