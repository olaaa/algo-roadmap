package demo;

/**
 * Демонстрация к вопросу «почему компаратор нельзя писать вычитанием».
 * <p>
 * Приём {@code return first - second} читается как «отрицательное, если первый
 * меньше». Он верен, пока разность влезает в int. Как только она достигает
 * 2^31, происходит переполнение, и знак становится ПРОТИВОПОЛОЖНЫМ правильному —
 * молча, без исключения.
 * <p>
 * Переполнение возможно только при РАЗНЫХ знаках операндов. Если оба значения
 * неотрицательные или оба неположительные, разность гарантированно влезает.
 * <p>
 * Это не задача LeetCode, а иллюстрация к справочникам {@code docs/java/Int.md}
 * и {@code docs/java/ComparableAndComparator.md}.
 * Вопрос возник из метода normalize в {@code block03_hashtables.GroupAnagrams}.
 *
 * @see <a href="../../docs/java/Int.md">Int.md</a>
 * @see <a href="../../docs/java/ComparableAndComparator.md">ComparableAndComparator.md</a>
 */
public class IntSubtractionOverflow {

    /* Наивный компаратор: разность как результат сравнения. */
    private static int compareBySubtraction(int first, int second) {
        return first - second;
    }

    /* Правильный компаратор: сравнение без арифметики. */
    private static int compareCorrectly(int first, int second) {
        return Integer.compare(first, second);
    }

    /*
     * Печатает строку таблицы и возвращает true, если наивный способ дал
     * ВЕРНЫЙ знак. Сравниваются именно знаки: величина по контракту
     * компаратора значения не имеет.
     */
    private static boolean showRow(int first, int second) {
        int subtraction = compareBySubtraction(first, second);
        int correct = compareCorrectly(first, second);
        boolean signIsRight = Integer.signum(subtraction) == correct;
        System.out.printf("%12d - %12d = %12d   знак %2d, верный %2d   %s%n",
                first, second, subtraction, Integer.signum(subtraction), correct,
                signIsRight ? "совпало" : "ЗНАК НЕВЕРЕН");
        return signIsRight;
    }

    public static void main(String[] args) {
        System.out.println("int вмещает от " + Integer.MIN_VALUE + " до " + Integer.MAX_VALUE);
        System.out.println("переполнение начинается с разности 2^31 = " + (1L << 31));

        /*
         * Блок 1: знаки разные — истинная разность больше 2^31 и не влезает,
         * поэтому знак переворачивается. Каждая строка обязана оказаться
         * НЕВЕРНОЙ, это и проверяется.
         */
        System.out.println();
        System.out.println("--- знаки разные: наивный способ врёт ---");
        check(!showRow(2_000_000_000, -2_000_000_000), "два миллиарда против минус двух");
        check(!showRow(Integer.MAX_VALUE, -1), "максимум int против минус единицы");
        check(!showRow(-2_000_000_000, 2_000_000_000), "то же самое в обратном порядке");
        check(!showRow(1, -Integer.MAX_VALUE), "единица «меньше» минус двух миллиардов");

        /*
         * Блок 2: оба неотрицательные либо оба неположительные — разность
         * гарантированно влезает в int, знак верен даже на краях диапазона.
         */
        System.out.println();
        System.out.println("--- одинаковые знаки: наивный способ работает ---");
        check(showRow(Integer.MAX_VALUE, 0), "максимум int против нуля");
        check(showRow(0, Integer.MAX_VALUE), "ноль против максимума int");
        check(showRow(30, 25), "обычные мелкие значения, как возраст");
        check(showRow(-1, Integer.MIN_VALUE), "минус единица против минимума int");
        check(showRow(Integer.MIN_VALUE, -1), "минимум int против минус единицы");

        /*
         * hashCode — практический случай, где значения бывают большими
         * и отрицательными одновременно, поэтому сравнение вычитанием
         * там особенно опасно.
         */
        System.out.println();
        System.out.println("--- почему hashCode опасен ---");
        int bigPositiveHash = Integer.MAX_VALUE;
        int bigNegativeHash = Integer.MIN_VALUE + 1;
        System.out.println("хеши бывают по обе стороны нуля, например "
                + bigPositiveHash + " и " + bigNegativeHash);
        check(!showRow(bigPositiveHash, bigNegativeHash), "разность двух хешей переполняется");

        /* Правильный способ обязан быть верен на всех тех же парах. */
        System.out.println();
        check(compareCorrectly(2_000_000_000, -2_000_000_000) > 0,
              "Integer.compare: два миллиарда больше минус двух");
        check(compareCorrectly(1, -Integer.MAX_VALUE) > 0,
              "Integer.compare: единица больше минус двух миллиардов");
        check(compareCorrectly(Integer.MIN_VALUE, Integer.MAX_VALUE) < 0,
              "Integer.compare: минимум меньше максимума");
        check(compareCorrectly(42, 42) == 0,
              "Integer.compare: равные значения дают ноль");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
