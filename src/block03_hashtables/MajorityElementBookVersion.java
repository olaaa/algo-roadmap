package block03_hashtables;

import java.util.Arrays;

/**
 * Задача 17.10 «Доминирующее значение» из книги Г. Лакман Макдауэлл
 * «Карьера программиста». Условие на с. 191, решение на с. 586–589
 * (глава 17 «Сложные задачи» начинается на с. 190 и на с. 560).
 * <p>
 * Отличие от LeetCode 169: в книге НЕ гарантируется, что доминирующее значение
 * существует. Если его нет, метод обязан вернуть -1. Отсюда и вся разница
 * в устройстве решения — оно двухпроходное: первый проход выбирает кандидата,
 * второй пересчитывает его вхождения и подтверждает или отвергает.
 * <p>
 * Требования из условия книги те же: время O(N), память O(1).
 * <p>
 * Решение из LeetCode-версии — {@link MajorityElement}. Там гарантия из условия
 * позволяет обойтись одним проходом и не делать проверку вовсе.
 *
 * @see <a href="../../docs/problems/block03_hashtables/MajorityElement.md">MajorityElement.md</a>
 */
public class MajorityElementBookVersion {

    private static final int NOT_FOUND = -1;

    /*
     * Решение из книги, оптимальное. Два прохода:
     * getCandidate находит единственное значение, которое МОЖЕТ быть
     * доминирующим, validate проверяет, что оно им действительно является.
     * Второй проход обязателен именно потому, что существование не гарантировано:
     * getCandidate всегда что-то возвращает, даже когда доминирующего значения
     * нет вовсе.
     */
    public static int findMajorityElement(int[] array) {
        int candidate = getCandidate(array);
        return validate(array, candidate) ? candidate : NOT_FOUND;
    }

    /*
     * Первый проход. Логика книги описана так: берём подмассив, начинающийся
     * с текущего значения, и расширяем его, пока значение остаётся в нём
     * доминирующим. Как только счётчик обнулился — в пройденном подмассиве
     * доминирующего значения нет вовсе, и его можно целиком выбросить:
     * если доминирующее значение есть во всём массиве, оно останется
     * доминирующим и в оставшейся части.
     * Одна переменная count вместо пары countYes и countNo: своё значение
     * увеличивает её, чужое уменьшает.
     * Код воспроизведён из книги без изменений, включая однобуквенное n.
     */
    private static int getCandidate(int[] array) {
        int majority = 0;
        int count = 0;
        for (int n : array) {
            if (count == 0) {
                /* Нет доминирующего элемента. */
                majority = n;
            }
            if (n == majority) {
                count++;
            } else {
                count--;
            }
        }
        return majority;
    }

    /*
     * Второй проход: пересчитать вхождения кандидата и сравнить с половиной
     * длины. Строгое неравенство, потому что «более чем в половине элементов».
     */
    private static boolean validate(int[] array, int majority) {
        int count = 0;
        for (int n : array) {
            if (n == majority) {
                count++;
            }
        }
        return count > array.length / 2;
    }

    /*
     * Первое решение из книги, названное там медленным: для каждого элемента
     * массива проверить, доминирующий ли он. Время O(N^2), память O(1).
     * Приведено потому, что книга подаёт его как отправную точку рассуждения:
     * ограничение по времени временно снимается, чтобы найти хоть какое-то
     * решение в O(1) памяти, а потом оно оптимизируется.
     * Код воспроизведён из книги без изменений, включая однобуквенное x.
     */
    public static int findMajorityElementSlow(int[] array) {
        for (int x : array) {
            if (validate(array, x)) {
                return x;
            }
        }
        return NOT_FOUND;
    }

    public static void main(String[] args) {
        /*
         * Ветви методов и тест, который каждую из них закрывает:
         *   1) getCandidate: count == 0, смена кандидата ..... [3,1,7,...]
         *   2) getCandidate: value == majority, count++ ...... [5,5,5]
         *   3) getCandidate: value != majority, count-- ...... [1,2,1]
         *   4) validate вернул true -> возвращаем кандидата .. [1,2,1]
         *   5) validate вернул false -> возвращаем -1 ........ [1,2,3] — ФОЛБЭК
         *   6) массив из одного элемента ..................... [7]
         *   7) ровно половина, доминирующего нет ............. [1,1,2,2]
         * Обе реализации из книги прогоняются по одному набору случаев.
         */
        record TestCase(int[] input, int expected, String name) {}

        TestCase[] testCases = {
            new TestCase(new int[]{1, 2, 5, 9, 5, 9, 5, 5, 5}, 5, "пример из книги"),
            new TestCase(new int[]{3, 1, 7, 1, 1, 7, 7, 3, 7, 7, 7}, 7, "разбор из книги, семёрка"),
            new TestCase(new int[]{1}, 1, "один элемент"),
            new TestCase(new int[]{1, 2, 1}, 1, "простое большинство"),
            new TestCase(new int[]{2, 2, 1, 1, 1, 2, 2}, 2, "пример из LeetCode"),
            new TestCase(new int[]{1, 2, 3}, NOT_FOUND, "доминирующего нет — фолбэк"),
            new TestCase(new int[]{1, 1, 2, 2}, NOT_FOUND, "ровно половина — не доминирующее"),
            new TestCase(new int[]{1, 2}, NOT_FOUND, "два разных элемента"),
            new TestCase(new int[]{1, 2, 3, 4, 5}, NOT_FOUND, "все различны"),
            new TestCase(new int[]{7, 7, 7}, 7, "все одинаковые"),
        };

        /*
         * ОСТОРОЖНО С ЭТИМ МАССИВОМ. В книге он приведён дважды, и версии
         * не совпадают — это опечатка самой книги, сверено по оригиналу PDF:
         * на с. 586 в строку «3 1 7 1 3 7 3 7 1 7 7» (семёрок 5 из 11,
         * доминирующего значения нет), на с. 587 таблицей со строкой индексов
         * «3 1 7 1 1 7 7 3 7 7 7» (семёрок 6 из 11, доминирующее есть).
         * Правильная версия табличная: только на ней сходится трассировка
         * с с. 587–588 — validate(3) видит 3,1 и останавливается; validate(7)
         * видит 7,1; validate(1) видит 1,7; validate(7) видит 7,3; последний
         * validate(7) видит 7,7,7.
         * Проверяем и промежуточное состояние, и итог.
         */
        int[] bookWalkthrough = {3, 1, 7, 1, 1, 7, 7, 3, 7, 7, 7};
        check(getCandidate(bookWalkthrough) == 7, "разбор из книги: кандидатом становится 7");
        check(validate(bookWalkthrough, 7), "разбор из книги: проверка семёрку подтверждает");

        for (TestCase testCase : testCases) {
            check(findMajorityElement(testCase.input()) == testCase.expected(),
                  "книга, оптимальное: " + testCase.name() + ": "
                          + Arrays.toString(testCase.input()) + " -> "
                          + findMajorityElement(testCase.input()));
            check(findMajorityElementSlow(testCase.input()) == testCase.expected(),
                  "книга, медленное: " + testCase.name() + ": "
                          + Arrays.toString(testCase.input()) + " -> "
                          + findMajorityElementSlow(testCase.input()));
        }

        /*
         * Ключевое отличие от LeetCode-версии: там гарантия существования
         * позволяет вернуть кандидата без проверки. Здесь такой гарантии нет,
         * и на входе БЕЗ доминирующего значения одноходовое решение соврало бы.
         * Проверяем это прямо: сравниваем ответы двух классов.
         */
        int[] withoutMajority = {1, 2, 3};
        check(findMajorityElement(withoutMajority) == NOT_FOUND,
              "без доминирующего: книжная версия честно возвращает -1");
        check(MajorityElement.majorityElementByVoting(withoutMajority) != NOT_FOUND,
              "без доминирующего: версия для LeetCode возвращает мусор — ей гарантия из условия");

        /* На корректном входе обе версии обязаны совпасть. */
        int[] withMajority = {2, 2, 1, 1, 1, 2, 2};
        check(findMajorityElement(withMajority)
                      == MajorityElement.majorityElementByVoting(withMajority),
              "на корректном входе обе версии дают одинаковый ответ");

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

        /* Тот же длинный вход, но без доминирующего значения. */
        int[] longWithout = new int[length];
        for (int currentIndex = 0; currentIndex < length; currentIndex++) {
            longWithout[currentIndex] = currentIndex % 3;
        }
        check(findMajorityElement(longWithout) == NOT_FOUND,
              "длинный вход без доминирующего значения");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
