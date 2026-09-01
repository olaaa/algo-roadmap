package block05_binary_search;

import java.util.Arrays;

/**
 * LeetCode 35 — Search Insert Position (Easy). Бинарный поиск, у которого
 * ответом служит не только находка, но и место промаха.
 * <p>
 * Дан отсортированный по возрастанию массив различных чисел и target. Вернуть
 * индекс target, а если его нет — индекс, на который его следовало бы
 * вставить, сохранив порядок. Время O(log n), память O(1).
 *
 * @see <a href="../../docs/problems/block05_binary_search/SearchInsertPosition.md">SearchInsertPosition.md</a>
 */
public class SearchInsertPosition {

    /*
     * Отличие от BinarySearch ровно в одной строке: там после цикла стоит
     * return -1, здесь — return lowIndex. Левая граница к этому моменту
     * и есть искомая позиция вставки.
     */
    public static int searchInsert(int[] nums, int target) {
        int lowIndex = 0;
        int highIndex = nums.length - 1;

        while (lowIndex <= highIndex) {
            int middleIndex = (lowIndex + highIndex) / 2;
            int middleValue = nums[middleIndex];

            if (middleValue == target) {
                return middleIndex;
            }
            if (middleValue < target) {
                lowIndex = middleIndex + 1;
            } else {
                highIndex = middleIndex - 1;
            }
        }

        /*
         * Инвариант цикла: всё левее lowIndex строго меньше target,
         * всё правее highIndex строго больше. Границы разошлись, значит
         * между ними ничего не осталось, и lowIndex — первая позиция,
         * где значение уже больше target. Туда и вставлять.
         */
        return lowIndex;
    }

    public static void main(String[] args) {
        /*
         * Ветви метода и тесты, которые их закрывают:
         *   1) middleValue == target, выход из цикла ......... target есть в массиве
         *   2) middleValue < target, сдвиг левой границы ..... target правее середины
         *   3) middleValue > target, сдвиг правой границы .... target левее середины
         *   4) return lowIndex после цикла ................... target в массиве нет
         * Отдельно проверяются три места вставки: перед всеми, между соседями
         * и после всех — последнее даёт индекс, равный длине массива.
         */
        record TestCase(int[] nums, int target, int expected, String name) {}

        TestCase[] testCases = {
            new TestCase(new int[]{1, 3, 5, 6}, 5, 2, "пример 1: значение есть"),
            new TestCase(new int[]{1, 3, 5, 6}, 2, 1, "пример 2: вставка между соседями"),
            new TestCase(new int[]{1, 3, 5, 6}, 7, 4, "пример 3: больше всех, вставка в хвост"),
            new TestCase(new int[]{1, 3, 5, 6}, 0, 0, "меньше всех, вставка в начало"),
            new TestCase(new int[]{1, 3, 5, 6}, 1, 0, "первый элемент"),
            new TestCase(new int[]{1, 3, 5, 6}, 6, 3, "последний элемент"),
            new TestCase(new int[]{1, 3, 5, 6}, 4, 2, "вставка в середину"),
            new TestCase(new int[]{1}, 1, 0, "один элемент, он же ответ"),
            new TestCase(new int[]{1}, 0, 0, "один элемент, вставка перед ним"),
            new TestCase(new int[]{1}, 2, 1, "один элемент, вставка после него"),
            new TestCase(new int[]{1, 3}, 3, 1, "два элемента, правый"),
            new TestCase(new int[]{1, 3}, 2, 1, "два элемента, вставка между"),
            new TestCase(new int[]{-9999, 0, 9999}, -9999, 0, "край диапазона значений снизу"),
            new TestCase(new int[]{-9999, 0, 9999}, 9999, 2, "край диапазона значений сверху"),
            new TestCase(new int[]{-9999, 0, 9999}, -10000, 0, "меньше самого маленького"),
            new TestCase(new int[]{-9999, 0, 9999}, 10000, 3, "больше самого большого"),
        };

        for (TestCase testCase : testCases) {
            int actual = searchInsert(testCase.nums(), testCase.target());
            check(actual == testCase.expected(),
                  testCase.name() + ": " + Arrays.toString(testCase.nums())
                          + ", target = " + testCase.target()
                          + " -> " + actual + " (ожидалось " + testCase.expected() + ")");
        }

        /*
         * Сплошная проверка на массиве чётных чисел от 0 до 1998.
         * Каждое чётное обязано найтись по своему индексу, каждое нечётное —
         * дать позицию вставки сразу за предыдущим чётным.
         */
        int size = 1000;
        int[] evenNumbers = new int[size];
        for (int position = 0; position < size; position++) {
            evenNumbers[position] = position * 2;
        }
        boolean everyValueFound = true;
        boolean everyGapPlaced = true;
        for (int position = 0; position < size; position++) {
            if (searchInsert(evenNumbers, position * 2) != position) {
                everyValueFound = false;
            }
            if (searchInsert(evenNumbers, position * 2 + 1) != position + 1) {
                everyGapPlaced = false;
            }
        }
        check(everyValueFound, "все 1000 значений найдены по своим индексам");
        check(everyGapPlaced, "все 1000 промежуточных значений дали позицию вставки");

        /*
         * Сверка с независимой реализацией: у Arrays.binarySearch при неудаче
         * результат равен -(точка вставки) - 1, значит точка вставки
         * разворачивается обратно как -(результат) - 1.
         */
        boolean agreesWithLibrary = true;
        for (int target = -3; target <= 2003; target++) {
            int library = Arrays.binarySearch(evenNumbers, target);
            int expected = library >= 0 ? library : -(library) - 1;
            if (searchInsert(evenNumbers, target) != expected) {
                agreesWithLibrary = false;
            }
        }
        check(agreesWithLibrary, "ответы совпали с Arrays.binarySearch на 2007 значениях");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
