package block01_arrays_twopointers;

import java.util.Arrays;

/*
 * Задача: развернуть массив на месте (in-place).
 *
 * Что значит in-place: критерий — дополнительная память O(1). Разрешено
 * держать несколько скалярных переменных (индексы, временная переменная
 * tmp для обмена), но НЕЛЬЗЯ выделять новую структуру размером со вход
 * (второй массив или список на n элементов). Модифицируем сам входной массив.
 *
 * Пример:
 *   вход:  [1, 2, 3, 4, 5]
 *   выход: [5, 4, 3, 2, 1]
 *
 * Идея (паттерн "два указателя"):
 *   Один указатель слева (l), другой справа (r).
 *   Меняем местами nums[l] и nums[r], затем сдвигаем l вправо, r влево.
 *   Останавливаемся, когда указатели встретились.
 *
 * Сложность: время O(n), память O(1) — разворачиваем без доп. массива.
 */
public class ReverseArray {

    public static void reverse(int[] nums) {
        int l = 0, r = nums.length - 1;
        while (l < r) {
            int tmp = nums[l];
            nums[l] = nums[r];
            nums[r] = tmp;
            l++;
            r--;
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
