package block01_arrays_twopointers;

import java.util.Arrays;

/**
 * LeetCode 283 — Move Zeroes (Easy). Паттерн «два указателя», однонаправленное
 * движение, форма «чтение и запись по одному массиву».
 * <p>
 * Перенести все нули в конец массива, сохранив относительный порядок остальных
 * элементов. Работаем на месте (in-place): readIndex просматривает все элементы,
 * writeIndex отмечает позицию для следующего ненулевого. Время O(n), память O(1).
 * <p>
 * Полное условие, примеры и ограничения:
 * {@code docs/problems/block01_arrays_twopointers/MoveZeroes.md}
 *
 * @see <a href="../../docs/problems/block01_arrays_twopointers/MoveZeroes.md">MoveZeroes.md</a>
 */
public class MoveZeroes {

    public static void moveZeroes(int[] nums) {
        /*
         * Позиция, куда ляжет следующий ненулевой элемент. В момент записи она
         * никогда не обгоняет readIndex, поэтому запись не может затереть ещё
         * не прочитанное значение.
         */
        int writeIndex = 0;

        /* Первый проход: сдвигаем все ненулевые элементы влево, сохраняя их порядок. */
        for (int readIndex = 0; readIndex < nums.length; readIndex++) {
            if (nums[readIndex] != 0) {
                nums[writeIndex] = nums[readIndex];
                writeIndex++;
            }
            /*
             * Ноль просто пропускаем: readIndex идёт дальше, writeIndex остаётся
             * на месте — так между указателями копится «долг» из пропущенных нулей.
             */
        }

        /*
         * Второй проход: хвост от writeIndex до конца — это ровно те позиции,
         * которые остались от пропущенных нулей. Забиваем их нулями.
         */
        while (writeIndex < nums.length) {
            nums[writeIndex] = 0;
            writeIndex++;
        }
    }

    /* Мини-тесты: запусти main() — увидишь PASS/FAIL по каждому случаю. */
    public static void main(String[] args) {
        int[] a = {0, 1, 0, 3, 12};
        moveZeroes(a);
        check(Arrays.equals(a, new int[]{1, 3, 12, 0, 0}), "обычный случай: нули вперемешку");

        /* Нулей нет: if срабатывает на каждом шаге, второй цикл не начинается. */
        int[] b = {1, 2, 3};
        moveZeroes(b);
        check(Arrays.equals(b, new int[]{1, 2, 3}), "нулей нет — массив не меняется");

        /* Все элементы нулевые: if не срабатывает ни разу, всю работу делает второй цикл. */
        int[] c = {0, 0, 0};
        moveZeroes(c);
        check(Arrays.equals(c, new int[]{0, 0, 0}), "все элементы нулевые");

        /* Пустой массив: ни один цикл не начинается. */
        int[] d = {};
        moveZeroes(d);
        check(Arrays.equals(d, new int[]{}), "пустой массив");

        int[] e = {0};
        moveZeroes(e);
        check(Arrays.equals(e, new int[]{0}), "один элемент — ноль");

        int[] f = {5};
        moveZeroes(f);
        check(Arrays.equals(f, new int[]{5}), "один элемент — не ноль");

        /* Ноль уже стоит в конце: writeIndex отстаёт только на последнем шаге. */
        int[] g = {1, 0};
        moveZeroes(g);
        check(Arrays.equals(g, new int[]{1, 0}), "ноль уже в конце");

        /* Порядок ненулевых обязан сохраниться, а не просто «все ненулевые слева». */
        int[] h = {0, 7, 0, 0, 3, 1};
        moveZeroes(h);
        check(Arrays.equals(h, new int[]{7, 3, 1, 0, 0, 0}), "относительный порядок сохранён");

        /* Отрицательные значения — обычные ненулевые, спецобработки нет. */
        int[] i = {-1, 0, -2};
        moveZeroes(i);
        check(Arrays.equals(i, new int[]{-1, -2, 0}), "отрицательные числа не считаются нулями");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
