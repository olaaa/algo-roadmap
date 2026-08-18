package block03_hashtables;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 169 — Majority Element (Easy). Найти элемент, встречающийся строго
 * больше n/2 раз; по условию он существует.
 * <p>
 * {@link #majorityElement} — счётчик частот, O(n) памяти.
 * {@link #majorityElementByVoting} — голосование Бойера—Мура, O(1) памяти,
 * ответ на follow-up.
 * <p>
 * Случай без гарантии существования — {@link MajorityElementNotGuaranteed}.
 *
 * @see <a href="../../docs/problems/block03_hashtables/MajorityElement.md">MajorityElement.md</a>
 */
public class MajorityElement {

    /* Счётчик частот. merge возвращает новое значение, поэтому get не нужен. */
    public static int majorityElement(int[] nums) {
        Map<Integer, Integer> counts = new HashMap<>();
        int bestValue = nums[0];
        for (int value : nums) {
            int countOfValue = counts.merge(value, 1, Integer::sum);
            if (countOfValue > counts.get(bestValue)) {
                bestValue = value;
            }
        }
        return bestValue;
    }

    /* Голосование Бойера—Мура. Чужой голос выбивает один голос за кандидата. */
    public static int majorityElementByVoting(int[] nums) {
        int majorityCandidate = nums[0];
        int survivingVotes = 1;
        for (int currentIndex = 1; currentIndex < nums.length; currentIndex++) {
            int vote = nums[currentIndex];
            if (survivingVotes == 0) {
                majorityCandidate = vote;
            }
            if (vote == majorityCandidate) {
                survivingVotes++;
            } else {
                survivingVotes--;
            }
        }
        return majorityCandidate;
    }

    public static void main(String[] args) {
        /*
         * Ветви методов и тест, который каждую из них закрывает:
         *   1) счётчик: новый ключ ..................... любой вход
         *   2) счётчик: ключ уже есть ................. [3,2,3]
         *   3) счётчик: условие «стал чаще» истинно ... [2,1,1] — ответ не первый
         *   4) счётчик: условие ложно, best не меняем . [1,1,2]
         *   5) голосование: survivingVotes == 0, смена кандидата . [2,1,1]
         *   6) голосование: vote == кандидат, голос добавлен ...... [1,1,1]
         *   7) голосование: vote != кандидат, голос выбит ......... [1,2,1]
         *   8) один элемент, цикл не начался ..................... [7]
         * Пустого массива в наборе нет намеренно: по ограничениям задачи
         * длина не меньше единицы. Случай без этой гарантии разбирает
         * MajorityElementNotGuaranteed.
         * Обе реализации прогоняются по одному и тому же набору случаев.
         */
        record TestCase(int[] input, int expected, String name) {}

        TestCase[] testCases = {
            new TestCase(new int[]{3, 2, 3}, 3, "пример из условия, короткий"),
            new TestCase(new int[]{2, 2, 1, 1, 1, 2, 2}, 2, "пример из условия, длинный"),
            new TestCase(new int[]{1}, 1, "один элемент"),
            new TestCase(new int[]{1, 1}, 1, "два одинаковых"),
            new TestCase(new int[]{2, 1, 1}, 1, "ответ не в начале, кандидат меняется"),
            new TestCase(new int[]{1, 1, 2}, 1, "ответ в начале, кандидат не меняется"),
            new TestCase(new int[]{1, 2, 1}, 1, "большинство через один"),
            new TestCase(new int[]{6, 5, 5}, 5, "первый элемент — чужой"),
            new TestCase(new int[]{-1, -1, 2}, -1, "отрицательные числа"),
            new TestCase(new int[]{0, 0, 1}, 0, "ноль как ответ"),
            new TestCase(new int[]{1, 2, 3, 1, 1}, 1, "большинство ровно 3 из 5"),
            new TestCase(new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE},
                         Integer.MIN_VALUE, "края диапазона int"),
        };

        for (TestCase testCase : testCases) {
            check(majorityElement(testCase.input()) == testCase.expected(),
                  "счётчик: " + testCase.name() + ": "
                          + Arrays.toString(testCase.input()) + " -> "
                          + majorityElement(testCase.input()));
            check(majorityElementByVoting(testCase.input()) == testCase.expected(),
                  "голосование: " + testCase.name() + ": "
                          + Arrays.toString(testCase.input()) + " -> "
                          + majorityElementByVoting(testCase.input()));
        }

        /*
         * Кандидат по ходу цикла ответом быть не обязан — на этом входе
         * после первого элемента кандидатом становится 5, хотя ответ 9.
         * Проверяем, что итоговый ответ всё равно верен.
         */
        check(majorityElementByVoting(new int[]{5, 9, 9, 9, 9}) == 9,
              "голосование: промежуточный кандидат не ответ");

        /*
         * Длинный вход: большинство ровно на один экземпляр превышает
         * половину, то есть худший случай для голосования.
         */
        int length = 50_001;
        int[] longInput = new int[length];
        int majorityCount = length / 2 + 1;
        for (int currentIndex = 0; currentIndex < majorityCount; currentIndex++) {
            longInput[currentIndex] = 7;
        }
        for (int currentIndex = majorityCount; currentIndex < length; currentIndex++) {
            longInput[currentIndex] = currentIndex;
        }
        check(majorityElement(longInput) == 7, "счётчик: длинный вход, перевес в один элемент");
        check(majorityElementByVoting(longInput) == 7, "голосование: длинный вход, перевес в один элемент");

        /* Тот же вход, но большинство раскидано по концам массива. */
        int[] scattered = new int[length];
        Arrays.fill(scattered, 7);
        for (int currentIndex = 1; currentIndex < length; currentIndex += 2) {
            scattered[currentIndex] = currentIndex;
        }
        check(majorityElementByVoting(scattered) == 7, "голосование: большинство вперемешку");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
