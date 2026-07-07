package block01_arrays_twopointers;

import java.util.Arrays;

/**
 * LeetCode 88 — Merge Sorted Array (Easy). Паттерн «два указателя» (с конца).
 * <p>
 * Слить два отсортированных массива в первый (nums1) так, чтобы результат
 * остался отсортированным по неубыванию. Слияние на месте (in-place): заполняем
 * nums1 с конца, чтобы не затереть ещё не использованные значимые элементы.
 * Время O(nums1Count + nums2Count), память O(1).
 * <p>
 * Полное условие, примеры и ограничения:
 * {@code docs/problems/block01_arrays_twopointers/MergeSortedArray.md}
 *
 * @see <a href="../../docs/problems/block01_arrays_twopointers/MergeSortedArray.md">MergeSortedArray.md</a>
 */
public class MergeSortedArray {

    public static void merge(int[] nums1, int nums1Count, int[] nums2, int nums2Count) {
        int firstIndex = nums1Count - 1;                // последний значимый в nums1
        int secondIndex = nums2Count - 1;               // последний в nums2
        int writeIndex = nums1Count + nums2Count - 1;   // позиция записи (конец nums1)
        while (secondIndex >= 0) {
            if (firstIndex >= 0 && nums1[firstIndex] > nums2[secondIndex]) {
                nums1[writeIndex--] = nums1[firstIndex--];
            } else {
                nums1[writeIndex--] = nums2[secondIndex--];
            }
        }
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 3, 0, 0, 0};
        merge(a, 3, new int[]{2, 5, 6}, 3);
        check(Arrays.equals(a, new int[]{1, 2, 2, 3, 5, 6}), "обычный случай");

        int[] b = {0};
        merge(b, 0, new int[]{1}, 1);
        check(Arrays.equals(b, new int[]{1}), "nums1 пустой");

        int[] c = {1};
        merge(c, 1, new int[]{}, 0);
        check(Arrays.equals(c, new int[]{1}), "nums2 пустой");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
