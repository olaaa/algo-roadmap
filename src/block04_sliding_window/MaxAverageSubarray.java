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
        if (nums == null) {
            throw new NullPointerException("массив не должен быть null");
        }
        if (k < 1 || k > nums.length) {
            throw new IllegalArgumentException(
                    "длина окна вне диапазона 1.." + nums.length + ": " + k);
        }

        int windowSum = 0;
// первое окно
        for (int currentIndex = 0; currentIndex < k; currentIndex++) {
            windowSum += nums[currentIndex];
        }

        /*
         * Начальный максимум — сумма первого окна, а не ноль: на массиве
         * из одних отрицательных чисел ноль остался бы победителем навсегда.
         */
        int maxSum = windowSum;
        for (int currentIndex = k; currentIndex < nums.length; currentIndex++) {
            /* Вошёл nums[currentIndex], вышел nums[currentIndex - k]. */
            windowSum = windowSum + nums[currentIndex] - nums[currentIndex - k];
            maxSum = Math.max(maxSum, windowSum);
        }
        return (double) maxSum / k;
    }

    public static void main(String[] args) {
        /*
         * Ветви метода и тест, который каждую из них закрывает:
         *   1) охранная проверка на null ..................... отдельным тестом
         *   2) охранная проверка k вне диапазона ............. k = 0 и k = n + 1
         *   3) первый цикл набирает окно ..................... любой вход
         *   4) второй цикл не начался, k == n ................ [1,2,3], k = 3
         *   5) второй цикл, Math.max выбрал новое окно ....... [1,12,-5,-6,50,3]
         *   6) второй цикл, Math.max оставил прежнее ......... [5,1,1,1], k = 1
         *   7) окно из одного элемента ....................... k = 1
         *   8) все числа отрицательные ....................... [-1,-2,-3], k = 2
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

        /* Охранные проверки. */
        check(throwsIllegalArgument(new int[]{1, 2, 3}, 0), "k = 0 отвергается");
        check(throwsIllegalArgument(new int[]{1, 2, 3}, 4), "k больше длины отвергается");
        check(throwsIllegalArgument(new int[]{1, 2, 3}, -1), "отрицательное k отвергается");

        boolean nullRejected = false;
        try {
            findMaxAverage(null, 1);
        } catch (NullPointerException expected) {
            nullRejected = true;
        }
        check(nullRejected, "null отвергается");

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

    private static boolean throwsIllegalArgument(int[] nums, int windowSize) {
        try {
            findMaxAverage(nums, windowSize);
            return false;
        } catch (IllegalArgumentException expected) {
            return true;
        }
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
