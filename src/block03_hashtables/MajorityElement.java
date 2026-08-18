package block03_hashtables;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 169 — Majority Element (Easy). Паттерн «хеш-таблица», форма
 * «счётчик частот»; follow-up закрывается голосованием Бойера—Мура.
 * <p>
 * Дан массив целых чисел длины n. Найти элемент, встречающийся СТРОГО больше
 * n/2 раз. По условию он существует и единственен.
 * <p>
 * Две реализации отвечают на разные требования условия.
 * {@link #majorityElement} — счётчик частот, приём блока: O(n) времени
 * и O(n) памяти.
 * {@link #majorityElementByVoting} — голосование Бойера—Мура, ответ
 * на follow-up «линейное время и O(1) дополнительной памяти».
 * <p>
 * Полное условие, примеры и ограничения:
 * {@code docs/problems/block03_hashtables/MajorityElement.md}
 *
 * @see <a href="../../docs/problems/block03_hashtables/MajorityElement.md">MajorityElement.md</a>
 */
public class MajorityElement {

    /*
     * Счётчик частот. merge(value, 1, Integer::sum) кладёт единицу, если ключа
     * не было, и прибавляет её к существующему значению, если был; возвращает
     * новое значение, поэтому отдельный get после вставки не нужен.
     * Порог n/2 не проверяется: по условию мажоритарный элемент существует,
     * значит самый частый и есть ответ.
     */
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

    /*
     * Голосование Бойера—Мура: две переменные, больше ничего.
     * majorityCandidate — элемент, претендующий на роль мажоритарного.
     * survivingVotes — сколько голосов за него пережило взаимные выбывания.
     * Каждый элемент, не равный кандидату, взаимно выбывает с одним голосом
     * за него. Мажоритарный элемент занимает строго больше половины массива,
     * поэтому чужих на все его голоса не хватает, и хотя бы один голос
     * выживает.
     * ВАЖНО: по ходу цикла кандидат ответом быть не обязан. Гарантия
     * относится только к состоянию после последней итерации.
     * Алгоритм и структура кода взяты из книги Макдауэлл (задача 17.10):
     * развёрнутый if/else и инициализация нулём вместо nums[0] — оттуда.
     * Имена переменных свои: в книге стоят majority, count и n. Здесь
     * majorityCandidate, потому что majority это прилагательное без
     * существительного, а переменная хранит именно кандидата; survivingVotes,
     * потому что count звучит как «просто считаем», а величина и растёт,
     * и падает; vote вместо n, потому что перебираются голоса.
     * Почему именно surviving, а не unmatched или outstanding, — разобрано
     * в .md, раздел «Почему survivingVotes, а не другое английское слово».
     * Дословная копия книжного кода — в {@link MajorityElementBookVersion}.
     */
    public static int majorityElementByVoting(int[] nums) {
        int majorityCandidate = 0;
        int survivingVotes = 0;
        for (int vote : nums) {
            if (survivingVotes == 0) {
                /* Голоса за прежнего кандидата погашены — выдвигаем нового. */
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
         *   7) голосование: vote != кандидат, голос погашен ....... [1,2,1]
         *   8) массив из одного элемента ......................... [7]
         *   9) пустой массив, цикл не начался .................... [] — отдельным тестом
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
         * Пустой массив. По ограничениям задачи длина не меньше единицы,
         * то есть такого входа не бывает, но начальное значение кандидата
         * определяет, упадёт метод или нет. Инициализация нулём (как в книге)
         * позволяет дойти до конца цикла, который просто не начнётся.
         * Инициализация вида majorityCandidate = nums[0] бросала бы здесь
         * ArrayIndexOutOfBoundsException — это и есть причина, по которой
         * книжный вариант лучше.
         */
        boolean emptyArrayThrows = false;
        try {
            majorityElementByVoting(new int[]{});
        } catch (RuntimeException failure) {
            emptyArrayThrows = true;
        }
        check(!emptyArrayThrows, "пустой массив: голосование не падает");
        check(majorityElementByVoting(new int[]{}) == 0,
              "пустой массив: возвращается начальное значение кандидата");

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
