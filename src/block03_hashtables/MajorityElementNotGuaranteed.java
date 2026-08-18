package block03_hashtables;

import java.util.Arrays;

import static block03_hashtables.MajorityElement.majorityElementByVoting;

/**
 * Мажоритарный элемент, когда его существование НЕ гарантировано: вернуть -1,
 * если элемента нет.
 * <p>
 * Голосование из {@link MajorityElement} возвращает значение всегда, поэтому
 * кандидата приходится проверять вторым проходом. Время O(n), память O(1).
 *
 * @see <a href="../../docs/problems/block03_hashtables/MajorityElement.md">MajorityElement.md</a>
 */
public class MajorityElementNotGuaranteed {

    private static final int NOT_FOUND = -1;

    public static int findMajorityElement(int[] nums) {
        /* Голосование берёт nums[0] без оглядки, поэтому пустой вход — сюда. */
        if (nums.length == 0) {
            return NOT_FOUND;
        }
        int candidate = majorityElementByVoting(nums);
        return isMajority(nums, candidate) ? candidate : NOT_FOUND;
    }

    /* Неравенство строгое: ровно половина большинством не считается. */
    private static boolean isMajority(int[] nums, int candidate) {
        int occurrences = 0;
        for (int value : nums) {
            if (value == candidate) {
                occurrences++;
            }
        }
        return occurrences > nums.length / 2;
    }

    public static void main(String[] args) {
        /*
         * Ветви метода и тест, который каждую из них закрывает:
         *   1) ранний выход на пустом массиве ................... []
         *   2) isMajority вернул true -> возвращаем кандидата ... [1,2,1]
         *   3) isMajority вернул false -> возвращаем -1 ......... [1,2,3] — ФОЛБЭК
         *   4) внутри isMajority: value == candidate ............ [7,7,7]
         *   5) внутри isMajority: value != candidate ............ [1,2,1]
         *   6) массив из одного элемента ........................ [7]
         *   7) ровно половина — порог не взят ................... [1,1,2,2]
         */
        record TestCase(int[] input, int expected, String name) {}

        TestCase[] testCases = {
            new TestCase(new int[]{1, 2, 5, 9, 5, 9, 5, 5, 5}, 5, "большинство вперемешку"),
            new TestCase(new int[]{3, 1, 7, 1, 1, 7, 7, 3, 7, 7, 7}, 7, "шесть семёрок из одиннадцати"),
            new TestCase(new int[]{1}, 1, "один элемент"),
            new TestCase(new int[]{1, 2, 1}, 1, "простое большинство"),
            new TestCase(new int[]{2, 2, 1, 1, 1, 2, 2}, 2, "пример из LeetCode"),
            new TestCase(new int[]{7, 7, 7}, 7, "все одинаковые"),
            new TestCase(new int[]{-5, -5, 3}, -5, "отрицательные числа"),
            new TestCase(new int[]{1, 2, 3}, NOT_FOUND, "большинства нет — фолбэк"),
            new TestCase(new int[]{1, 1, 2, 2}, NOT_FOUND, "ровно половина — не большинство"),
            new TestCase(new int[]{1, 2}, NOT_FOUND, "два разных элемента"),
            new TestCase(new int[]{1, 2, 3, 4, 5}, NOT_FOUND, "все различны"),
            new TestCase(new int[]{}, NOT_FOUND, "пустой массив"),
            new TestCase(new int[]{1, 1, 1, 2, 2, 2}, NOT_FOUND, "два значения поровну"),
        };

        for (TestCase testCase : testCases) {
            check(findMajorityElement(testCase.input()) == testCase.expected(),
                  testCase.name() + ": " + Arrays.toString(testCase.input())
                          + " -> " + findMajorityElement(testCase.input()));
        }

        /* Ради чего всё и затевалось: сравниваем поведение двух классов. */
        int[] withoutMajority = {1, 2, 3};
        check(findMajorityElement(withoutMajority) == NOT_FOUND,
              "без большинства: с проверкой честно возвращается -1");
        check(majorityElementByVoting(withoutMajority) != NOT_FOUND,
              "без большинства: одноходовое решение возвращает мусор");

        /* На корректном входе оба класса обязаны совпасть. */
        int[] withMajority = {2, 2, 1, 1, 1, 2, 2};
        check(findMajorityElement(withMajority) == majorityElementByVoting(withMajority),
              "с большинством: оба класса дают одинаковый ответ");

        /* Граница порога: на семи элементах большинство начинается с четырёх. */
        check(findMajorityElement(new int[]{1, 1, 1, 2, 3, 4, 5}) == NOT_FOUND,
              "порог: три вхождения из семи — мало");
        check(findMajorityElement(new int[]{1, 1, 1, 1, 3, 4, 5}) == 1,
              "порог: четыре вхождения из семи — достаточно");

        /* Длинный вход: перевес ровно в один элемент. */
        int length = 50_001;
        int[] longInput = new int[length];
        int majorityCount = length / 2 + 1;
        for (int currentIndex = 0; currentIndex < majorityCount; currentIndex++) {
            longInput[currentIndex] = 7;
        }
        for (int currentIndex = majorityCount; currentIndex < length; currentIndex++) {
            longInput[currentIndex] = currentIndex;
        }
        check(findMajorityElement(longInput) == 7, "длинный вход, перевес в один элемент");

        /* Тот же длинный вход, но без большинства. */
        int[] longWithout = new int[length];
        for (int currentIndex = 0; currentIndex < length; currentIndex++) {
            longWithout[currentIndex] = currentIndex % 3;
        }
        check(findMajorityElement(longWithout) == NOT_FOUND, "длинный вход без большинства");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
