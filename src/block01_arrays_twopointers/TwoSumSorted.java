package block01_arrays_twopointers;

import java.util.Arrays;

/*
 * Задача: в ОТСОРТИРОВАННОМ массиве найти два числа с суммой target.
 * Вернуть их индексы 1-based (нумерация с единицы: первый элемент —
 * индекс 1, а не 0), как в LeetCode 167 — Two Sum II.
 * Гарантируется ровно одно решение.
 *
 * Пример:
 *   nums = [2, 7, 11, 15], target = 9  ->  [1, 2]   (2 + 7 = 9)
 *
 * Идея (паттерн "два указателя"):
 *   Массив отсортирован, поэтому: указатель слева (l) и справа (r).
 *   sum слишком мал  -> двигаем l вправо (берём число побольше).
 *   sum слишком велик -> двигаем r влево  (берём число поменьше).
 *   Так за один проход без вложенных циклов.
 *
 * Сложность: время O(n), память O(1).
 */
public class TwoSumSorted {

    public static int[] twoSum(int[] nums, int target) {
        int l = 0, r = nums.length - 1;
        while (l < r) {
            int sum = nums[l] + nums[r];
            if (sum == target) return new int[]{l + 1, r + 1};
            if (sum < target) l++;
            else r--;
        }
        return new int[]{-1, -1};   // по условию сюда не дойдём
    }

    public static void main(String[] args) {
        check(Arrays.equals(twoSum(new int[]{2, 7, 11, 15}, 9), new int[]{1, 2}), "начало массива");
        check(Arrays.equals(twoSum(new int[]{2, 3, 4}, 6), new int[]{1, 3}), "края массива");
        check(Arrays.equals(twoSum(new int[]{-1, 0}, -1), new int[]{1, 2}), "отрицательные числа");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
