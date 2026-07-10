package block01_arrays_twopointers;

import java.util.Arrays;

/**
 * LeetCode 167 — Two Sum II (Medium). Паттерн «два указателя».
 * <p>
 * В отсортированном по неубыванию массиве найти два числа с суммой target и
 * вернуть их 1-based индексы. Массив отсортирован, поэтому идём указателями
 * навстречу: сумма мала — двигаем левый вправо, велика — правый влево.
 * Гарантируется ровно одно решение. Время O(n), память O(1).
 * <p>
 * Полное условие, примеры и ограничения:
 * {@code docs/problems/block01_arrays_twopointers/TwoSumSorted.md}
 *
 * @see <a href="../../docs/problems/block01_arrays_twopointers/TwoSumSorted.md">TwoSumSorted.md</a>
 */
public class TwoSumSorted {

    public static int[] twoSum(int[] nums, int target) {
        int leftIndex = 0, rightIndex = nums.length - 1;
        while (leftIndex < rightIndex) {
            int sum = nums[leftIndex] + nums[rightIndex];
            if (sum == target) {
                return new int[]{leftIndex + 1, rightIndex + 1};
            }
            if (sum < target) {
                leftIndex++;
            } else {
                rightIndex--;
            }
        }
        return new int[]{-1, -1};   // по условию сюда не дойдём
    }

    public static void main(String[] args) {
        check(Arrays.equals(twoSum(new int[]{2, 7, 11, 15}, 9), new int[]{1, 2}), "начало массива");
        check(Arrays.equals(twoSum(new int[]{2, 3, 4}, 6), new int[]{1, 3}), "края массива");
        check(Arrays.equals(twoSum(new int[]{-1, 0}, -1), new int[]{1, 2}), "отрицательные числа");
        check(Arrays.equals(twoSum(new int[]{1, 2, 3, 4}, 7), new int[]{3, 4}), "сумма мала → сдвигаем левый (ветка sum < target)");
        check(Arrays.equals(twoSum(new int[]{1, 2, 3}, 100), new int[]{-1, -1}), "пары нет → {-1,-1} (цикл завершается)");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
