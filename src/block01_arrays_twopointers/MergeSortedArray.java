package block01_arrays_twopointers;

import java.util.Arrays;

/*
 * Задача: слить два отсортированных массива в первый (nums1).
 * В nums1 первые m элементов значимые, дальше зарезервировано место под n
 * элементов из nums2. Длина nums1 равна m + n. (LeetCode 88 — Merge Sorted Array)
 *
 * Пример:
 *   nums1 = [1, 2, 3, 0, 0, 0], m = 3
 *   nums2 = [2, 5, 6],          n = 3
 *   результат: [1, 2, 2, 3, 5, 6]
 *
 * Идея (паттерн "два указателя", но идём С КОНЦА):
 *   Если заполнять с начала — затрём ещё не использованные элементы nums1.
 *   Поэтому ставим самый большой элемент в конец и движемся к началу.
 *   i — конец значимой части nums1, j — конец nums2, k — конец всего nums1.
 *
 * Сложность: время O(m + n), память O(1).
 */
public class MergeSortedArray {

    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1;       // последний значимый в nums1
        int j = n - 1;       // последний в nums2
        int k = m + n - 1;   // последняя позиция в nums1
        while (j >= 0) {
            if (i >= 0 && nums1[i] > nums2[j]) {
                nums1[k--] = nums1[i--];
            } else {
                nums1[k--] = nums2[j--];
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
