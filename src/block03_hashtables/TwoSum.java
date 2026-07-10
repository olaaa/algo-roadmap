package block03_hashtables;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 1 — Two Sum (Easy). Паттерн «хеш-таблица за один проход».
 * <p>
 * В НЕотсортированном массиве найти два числа с суммой target и вернуть их
 * индексы в исходном порядке (0-based). За один проход храним в HashMap
 * «число -> индекс» и для каждого элемента ищем среди уже виденных его
 * дополнение (target - текущее). Время O(n), память O(n).
 * <p>
 * Для сравнения тут же оставлен метод-baseline {@link #twoSumBruteForce}
 * — наивный перебор всех пар элементов за O(n²), с которого на собеседовании
 * стартуют, а потом ускоряют до HashMap.
 * <p>
 * Полное условие, примеры и ограничения:
 * {@code docs/problems/block03_hashtables/TwoSum.md}
 *
 * @see <a href="../../docs/problems/block03_hashtables/TwoSum.md">TwoSum.md</a>
 */
public class TwoSum {

    /*
     * Эталонное решение: один проход, HashMap «виденное число -> его индекс».
     * Кладём в map ПОСЛЕ проверки — так элемент не спарится сам с собой, а пара
     * всегда состоит из двух разных позиций.
     */
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seenValueToIndex = new HashMap<>();
        for (int currentIndex = 0; currentIndex < nums.length; currentIndex++) {
            int complement = target - nums[currentIndex];
            if (seenValueToIndex.containsKey(complement)) {
                return new int[]{seenValueToIndex.get(complement), currentIndex};
            }
            seenValueToIndex.put(nums[currentIndex], currentIndex);
        }
        return new int[]{-1, -1};   // по условию сюда не дойдём
    }

    /*
     * Решение в лоб: два вложенных цикла перебирают все пары элементов.
     * Внутренний цикл стартует с firstIndex + 1 — «треугольный» проход, чтобы
     * не брать пару элемента с самим собой и не проверять одну пару дважды.
     * Итого n(n-1)/2 проверок → O(n²) по времени, O(1) по памяти.
     */
    public static int[] twoSumBruteForce(int[] nums, int target) {
        for (int firstIndex = 0; firstIndex < nums.length; firstIndex++) {
            for (int secondIndex = firstIndex + 1; secondIndex < nums.length; secondIndex++) {
                if (nums[firstIndex] + nums[secondIndex] == target) {
                    return new int[]{firstIndex, secondIndex};
                }
            }
        }
        return new int[]{-1, -1};
    }

    public static void main(String[] args) {
        /*
         * Тестовые случаи заданы ОДИН раз (у каждого единственное решение) и
         * прогоняются по обеим реализациям — без дублирования входов и сообщений.
         * Случаи покрывают все ветви: найдено сразу / после вставок, два одинаковых
         * числа, отрицательные, и «пары нет» (возврат {-1, -1}).
         */
        record TestCase(int[] nums, int target, int[] expected, String name) {}

        TestCase[] testCases = {
            new TestCase(new int[]{2, 7, 11, 15}, 9, new int[]{0, 1}, "пара в начале массива"),
            new TestCase(new int[]{3, 2, 4}, 6, new int[]{1, 2}, "неотсортированный массив"),
            new TestCase(new int[]{3, 3}, 6, new int[]{0, 1}, "два одинаковых числа"),
            new TestCase(new int[]{-1, -2, -3, -4}, -6, new int[]{1, 3}, "отрицательные числа"),
            new TestCase(new int[]{1, 2, 3}, 100, new int[]{-1, -1}, "пары нет (возврат {-1,-1})"),
        };

        for (TestCase testCase : testCases) {
            check(Arrays.equals(twoSum(testCase.nums(), testCase.target()), testCase.expected()),
                  "HashMap: " + testCase.name());
            check(Arrays.equals(twoSumBruteForce(testCase.nums(), testCase.target()), testCase.expected()),
                  "BruteForce: " + testCase.name());
        }
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
