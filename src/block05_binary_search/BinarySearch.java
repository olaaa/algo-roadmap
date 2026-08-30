package block05_binary_search;

import java.util.Arrays;

/**
 * LeetCode 704 — Binary Search (Easy). Классический бинарный поиск
 * по отсортированному массиву.
 * <p>
 * Дан массив, отсортированный по возрастанию, и число target. Вернуть индекс
 * target или -1, если такого числа нет. На каждом шаге сравнение со средним
 * элементом выбрасывает половину оставшегося участка. Время O(log n),
 * память O(1).
 *
 * @see <a href="../../docs/problems/block05_binary_search/BinarySearch.md">BinarySearch.md</a>
 */
public class BinarySearch {

    /*
     * Охранных проверок нет по условию: ограничения задают 1 <= nums.length,
     * то есть ни null, ни пустой массив на вход не приходят.
     * Отсортированность — предусловие: проверка стоила бы O(n) и съела бы
     * весь выигрыш от O(log n).
     */
    public static int search(int[] nums, int target) {
        int lowIndex = 0;
        int highIndex = nums.length - 1;

        /*
         * Границы включительные, поэтому условие нестрогое: при lowIndex ==
         * highIndex участок ещё содержит один непроверенный элемент.
         * Со строгим < этот элемент остался бы непросмотренным.
         */
        while (lowIndex <= highIndex) {
            int middleIndex = (lowIndex + highIndex) / 2;
            int middleValue = nums[middleIndex];

            if (middleValue == target) {
                return middleIndex;
            }
            if (middleValue < target) {
                /* Средний элемент мал, значит и всё левее него мало. */
                lowIndex = middleIndex + 1;
            } else {
                /* Средний элемент велик, значит и всё правее него велико. */
                highIndex = middleIndex - 1;
            }
        }

        /*
         * Границы разошлись — непросмотренных элементов не осталось.
         * Это фолбэк утверждения «существует»: находка возвращается
         * из цикла, отсутствие — только после него.
         */
        return -1;
    }

    public static void main(String[] args) {
        /*
         * Ветви метода и тесты, которые их закрывают:
         *   1) middleValue == target, выход из цикла ............ большинство тестов
         *   2) middleValue < target, сдвиг левой границы ........ target правее середины
         *   3) middleValue > target, сдвиг правой границы ....... target левее середины
         *   4) фолбэк return -1 после цикла ..................... значения нет в массиве
         */
        record TestCase(int[] nums, int target, int expected, String name) {}

        TestCase[] testCases = {
            new TestCase(new int[]{-1, 0, 3, 5, 9, 12}, 9, 4, "пример из условия, есть"),
            new TestCase(new int[]{-1, 0, 3, 5, 9, 12}, 2, -1, "пример из условия, нет"),
            new TestCase(new int[]{-1, 0, 3, 5, 9, 12}, 5, 3, "середина, найдено на первом шаге"),
            new TestCase(new int[]{-1, 0, 3, 5, 9, 12}, 12, 5, "последний элемент, только сдвиги левой границы"),
            new TestCase(new int[]{-1, 0, 3, 5, 9, 12}, -1, 0, "первый элемент, только сдвиги правой границы"),
            new TestCase(new int[]{5}, 5, 0, "один элемент, он же ответ"),
            new TestCase(new int[]{5}, 3, -1, "один элемент, не тот"),
            new TestCase(new int[]{1, 2}, 1, 0, "два элемента, левый"),
            new TestCase(new int[]{1, 2}, 2, 1, "два элемента, правый"),
            new TestCase(new int[]{1, 2}, 3, -1, "два элемента, ни один не подошёл"),
            new TestCase(new int[]{2, 4, 6, 8}, 1, -1, "меньше всех"),
            new TestCase(new int[]{2, 4, 6, 8}, 9, -1, "больше всех"),
            new TestCase(new int[]{2, 4, 6, 8}, 5, -1, "попадает между соседями"),
            new TestCase(new int[]{-9, -7, -3, -1}, -7, 1, "все числа отрицательные"),
            new TestCase(new int[]{-9999, 0, 9999}, 9999, 2, "край диапазона значений сверху"),
            new TestCase(new int[]{-9999, 0, 9999}, -9999, 0, "край диапазона значений снизу"),
        };

        for (TestCase testCase : testCases) {
            int actual = search(testCase.nums(), testCase.target());
            check(actual == testCase.expected(),
                  testCase.name() + ": " + Arrays.toString(testCase.nums())
                          + ", target = " + testCase.target()
                          + " -> " + actual + " (ожидалось " + testCase.expected() + ")");
        }

        /*
         * Сплошная проверка: массив чётных чисел от 0 до 1998. Каждое чётное
         * обязано найтись по своему индексу, каждое нечётное — не найтись
         * вовсе. Это разом закрывает и все варианты положения target внутри
         * массива, и фолбэк на всех промежутках между элементами.
         */
        int size = 1000;
        int[] evenNumbers = new int[size];
        for (int position = 0; position < size; position++) {
            evenNumbers[position] = position * 2;
        }
        boolean everyValueFound = true;
        boolean everyGapMissed = true;
        for (int position = 0; position < size; position++) {
            if (search(evenNumbers, position * 2) != position) {
                everyValueFound = false;
            }
            if (search(evenNumbers, position * 2 + 1) != -1) {
                everyGapMissed = false;
            }
        }
        check(everyValueFound, "все 1000 значений найдены по своим индексам");
        check(everyGapMissed, "все 1000 промежуточных значений дали -1");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
