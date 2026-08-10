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
 * Для сравнения тут же оставлен метод-baseline {@link #moveZeroesSwapping}
 * — наивный обмен с ближайшим ненулевым справа за O(n²), с которого на
 * собеседовании стартуют, а потом ускоряют до двух указателей.
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

        System.out.println("После того, как ненулевые сдвинули влево: " + Arrays.toString(nums));
        /*
         * Второй проход: хвост от writeIndex до конца — это ровно те позиции,
         * которые остались от пропущенных нулей. Забиваем их нулями.
         * writeIndex инкрементируется в конце цикла, поэтому здесь не надо.
         */
        while (writeIndex < nums.length) {
            nums[writeIndex] = 0;
            writeIndex++;
        }

    }

    /*
     * Решение в лоб, с которого стартуют на собеседовании: встретили ноль —
     * ищем справа ближайший ненулевой элемент и меняем их местами.
     * Порядок ненулевых при этом не ломается именно потому, что берётся
     * БЛИЖАЙШИЙ справа: между собой ненулевые никогда не переставляются,
     * меняется только их расстояние до начала массива.
     * break обязателен, иначе теряются данные: внутренний цикл пойдёт дальше и
     * при КАЖДОМ следующем ненулевом снова запишет его в nums[currentIndex],
     * затирая то, что положили туда до этого. Уцелеет только последний
     * найденный элемент, остальные пропадут вместе со своими старыми позициями,
     * которые уже обнулены. Для [0, 1, 0, 3, 12] без break получается
     * [12, 0, 0, 0, 0] вместо [1, 3, 12, 0, 0].
     * Внешний цикл делает n шагов, внутренний до n — O(n²) времени при O(1)
     * памяти. Вся неэффективность здесь в том, что внутренний поиск каждый раз
     * начинается заново; если позволить ему продолжаться с прошлого места,
     * получится ровно решение двумя указателями за O(n).
     */
    public static void moveZeroesSwapping(int[] nums) {
        for (int currentIndex = 0; currentIndex < nums.length; currentIndex++) {
            if (nums[currentIndex] != 0) {
                continue;
            }
            // текущий элемент ноль
            for (int searchIndex = currentIndex + 1; searchIndex < nums.length; searchIndex++) {
// если следующий элемент ненулевой, то меняем их местами. Обмен происходит только один раз.
                if (nums[searchIndex] != 0) {
                    nums[currentIndex] = nums[searchIndex];
//  0 у нас константа, поэтому не обязательно вводить промежуточную переменную
                    nums[searchIndex] = 0;
// без break будет затираться nums[currentIndex] последующими значениями
                    break;
                }
// иначе: бежим до следующего ненулевого
            }
        }
    }

    /* Мини-тесты: запусти main() — увидишь PASS/FAIL по каждому случаю. */
    public static void main(String[] args) {
        /*
         * Ветви эталонного moveZeroes и случай, который каждую из них закрывает:
         *   1) if сработал (элемент ненулевой) ............... "нулей нет"
         *   2) if не сработал (элемент нулевой) .............. "все элементы нулевые"
         *   3) обе ветки вперемешку .......................... "нули вперемешку"
         *   4) первый цикл не начался ........................ "пустой массив"
         *   5) второй цикл не начался (нулей не было) ........ "нулей нет"
         *   6) второй цикл делает всю работу ................. "все элементы нулевые"
         *   7) writeIndex отстал только на последнем шаге .... "ноль уже в конце"
         * Дополнительно проверяется то, что легко сломать: относительный порядок
         * ненулевых обязан сохраниться, а отрицательные числа нулями не считаются.
         *
         * Обе реализации прогоняются по одному и тому же набору. Массив каждый
         * раз копируется, потому что обе меняют его на месте.
         */
        record TestCase(int[] input, int[] expected, String name) {}

        TestCase[] testCases = {
            new TestCase(new int[]{0, 1, 0, 3, 12}, new int[]{1, 3, 12, 0, 0}, "обычный случай: нули вперемешку"),
            new TestCase(new int[]{1, 2, 3}, new int[]{1, 2, 3}, "нулей нет — массив не меняется"),
            new TestCase(new int[]{0, 0, 0}, new int[]{0, 0, 0}, "все элементы нулевые"),
            new TestCase(new int[]{}, new int[]{}, "пустой массив"),
            new TestCase(new int[]{0}, new int[]{0}, "один элемент — ноль"),
            new TestCase(new int[]{5}, new int[]{5}, "один элемент — не ноль"),
            new TestCase(new int[]{1, 0}, new int[]{1, 0}, "ноль уже в конце"),
            new TestCase(new int[]{0, 7, 0, 0, 3, 1}, new int[]{7, 3, 1, 0, 0, 0}, "относительный порядок сохранён"),
            new TestCase(new int[]{-1, 0, -2}, new int[]{-1, -2, 0}, "отрицательные числа не считаются нулями"),
            new TestCase(new int[]{0, 0, 1}, new int[]{1, 0, 0}, "нули подряд в начале"),
        };

        for (TestCase testCase : testCases) {
            int[] swapped = testCase.input().clone();
            moveZeroesSwapping(swapped);
            check(Arrays.equals(swapped, testCase.expected()), "Swapping: " + testCase.name());

            int[] twoPointers = testCase.input().clone();
            moveZeroes(twoPointers);
            check(Arrays.equals(twoPointers, testCase.expected()), "TwoPointers: " + testCase.name());
        }
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
