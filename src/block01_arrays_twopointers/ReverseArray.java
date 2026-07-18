package block01_arrays_twopointers;

import java.util.Arrays;

/**
 * Reverse Array — базовое упражнение на паттерн «два указателя»
 * (аналог LeetCode 344 Reverse String, но для массива чисел).
 * <p>
 * Развернуть массив на месте (in-place), с памятью O(1). Указатели слева и
 * справа идут навстречу и меняют элементы местами через временную переменную,
 * пока не встретятся в середине. Время O(n), память O(1).
 * <p>
 * Полное условие, примеры и ограничения:
 * {@code docs/problems/block01_arrays_twopointers/ReverseArray.md}
 *
 * @see <a href="../../docs/problems/block01_arrays_twopointers/ReverseArray.md">ReverseArray.md</a>
 */
public class ReverseArray {

    public static void reverse(int[] nums) {
        int leftIndex = 0, rightIndex = nums.length - 1;
        while (leftIndex < rightIndex) {
            int temp = nums[leftIndex];
            nums[leftIndex] = nums[rightIndex];
            nums[rightIndex] = temp;

//  Для нечётной длины указатели сойдутся на середине
            leftIndex++;
            rightIndex--;
        }
    }

    /* Мини-тесты: запусти main() — увидишь PASS/FAIL по каждому случаю. */
    public static void main(String[] args) {
        int[] a = {1, 2, 3, 4, 5};
        reverse(a);
        check(Arrays.equals(a, new int[]{5, 4, 3, 2, 1}), "нечётная длина");

        int[] b = {1, 2, 3, 4};
        reverse(b);
        check(Arrays.equals(b, new int[]{4, 3, 2, 1}), "чётная длина");

        int[] c = {42};
        reverse(c);
        check(Arrays.equals(c, new int[]{42}), "один элемент");

        int[] d = {};
        reverse(d);
        check(Arrays.equals(d, new int[]{}), "пустой массив");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
