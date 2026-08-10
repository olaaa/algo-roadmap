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
//  указывает на самое большое число в первом массиве (т.к. оно в конце)
//  потом бежим до нулевого значимого элемента
        int firstIndex = nums1Count - 1;                // последний значимый в nums1
//  самое большое число во втором массиве  (т.к. оно в конце)
        int secondIndex = nums2Count - 1;               // последний в nums2

//  заполняем с конца, так как оба массива по возрастающей, и в результирующем массиве последний
//  элемент -- самое большое число
        int writeIndex = nums1Count + nums2Count - 1;   // позиция записи (конец nums1)

// еще есть, что переносить?
//        итерация по длине nums2
        while (secondIndex >= 0) {
// Проверяем, что остались необработанные элементы в num1. Их может не остаться,
//            если они большие и ушли в конец. А в nums2 остались мелкие.
// сравниваем самые большие числа в nums1 и nums2
            if (firstIndex >= 0 && nums1[firstIndex] > nums2[secondIndex]) {
// в конец результирующего массива попадает самое большое число из nums1
                nums1[writeIndex--] = nums1[firstIndex--];
// обработали элемент из nums1, поэтому индекс firstIndex смещается влево (-1)
//  указатель в массиве num2 стоит на месте
            } else {
// в конец результирующего массива попадает самое большое число из nums2
//          ИЛИ если все значимые элементы nums1 уже обработаны
                nums1[writeIndex--] = nums2[secondIndex--];
//  обработали элемент из nums2, поэтому индекс, указывающий на его конец декрементируется
            }

// позиция записи всегда смещается вправо (-1)
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

        /*
         * Значимые элементы nums1 кончаются раньше nums2: firstIndex доходит до -1,
         * пока цикл ещё идёт, и защита firstIndex >= 0 не даёт прочитать nums1[-1].
         * Минимум всех чисел (1) лежит в nums2, поэтому первым исчерпывается nums1.
         */
        int[] d = {5, 6, 0, 0};
        merge(d, 2, new int[]{1, 2}, 2);
        check(Arrays.equals(d, new int[]{1, 2, 5, 6}), "nums1 исчерпан раньше nums2 (firstIndex доходит до -1)");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
