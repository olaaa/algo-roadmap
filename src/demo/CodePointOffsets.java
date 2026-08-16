package demo;

/**
 * Демонстрация {@code String.offsetByCodePoints(int index, int codePointOffset)}
 * на строке "a🐷b🐽c" — пять символов, семь char.
 * <p>
 * Метод переводит «номер символа» в «индекс char», перешагивая суррогатные пары
 * целиком. Возвращает он не сам символ, а ГРАНИЦУ перед N-м символом, поэтому
 * границ всегда на одну больше, чем символов: последняя равна length().
 * <p>
 * Это не задача LeetCode, а учебный пример к справочнику по String.
 *
 * @see <a href="../../docs/datastructures/String.md">String.md</a>
 * @see <a href="../../docs/java/README.md">docs/java/README.md</a>
 */
public class CodePointOffsets {

    private static final String SAMPLE = "a🐷b🐽c";   // a🐷b🐽c

    /*
     * Безопасно вырезает символ по его ПОРЯДКОВОМУ НОМЕРУ, а не по индексу char.
     * Оба конца диапазона получаем через offsetByCodePoints, поэтому суррогатная
     * пара никогда не разрезается пополам.
     */
    public static String characterAt(String text, int characterNumber) {
        int start = text.offsetByCodePoints(0, characterNumber);
        int end = text.offsetByCodePoints(0, characterNumber + 1);
        return text.substring(start, end);
    }

    public static void main(String[] args) {
        int characterCount = SAMPLE.codePointCount(0, SAMPLE.length());

        System.out.println("строка          : " + SAMPLE);
        System.out.println("length()        : " + SAMPLE.length() + "  (единиц char)");
        System.out.println("codePointCount(): " + characterCount + "  (символов)");

        System.out.println();
        System.out.println("offsetByCodePoints(0, N) — с какого char начинается N-й символ:");
        for (int characterNumber = 0; characterNumber < characterCount; characterNumber++) {
            int charIndex = SAMPLE.offsetByCodePoints(0, characterNumber);
            System.out.printf("  N=%d -> char-индекс %d   символ [%s]%n",
                    characterNumber, charIndex, characterAt(SAMPLE, characterNumber));
        }

        /* Правильная нарезка против наивной. */
        System.out.println();
        System.out.println("вырезаем 1-й символ (нумерация с нуля):");
        System.out.println("  через offsetByCodePoints : [" + characterAt(SAMPLE, 1) + "]");
        System.out.println("  наивно substring(1, 2)   : [" + SAMPLE.substring(1, 2)
                + "]  — половина суррогатной пары");

        /* Перебор всех символов: три способа, результат одинаковый. */
        System.out.println();
        System.out.println("перебор символов:");

        StringBuilder viaOffsets = new StringBuilder();
        for (int characterNumber = 0; characterNumber < characterCount; characterNumber++) {
            viaOffsets.append(characterAt(SAMPLE, characterNumber)).append(' ');
        }
        System.out.println("  offsetByCodePoints : " + viaOffsets);

        /*
         * Так делать в цикле не надо: каждый вызов offsetByCodePoints идёт по
         * строке от начала, поэтому проход выходит квадратичным. Ниже линейные
         * способы — они и есть рабочие.
         */
        StringBuilder viaCharCount = new StringBuilder();
        for (int charIndex = 0; charIndex < SAMPLE.length(); ) {
            int codePoint = SAMPLE.codePointAt(charIndex);
            viaCharCount.append(new String(Character.toChars(codePoint))).append(' ');
            charIndex += Character.charCount(codePoint);
        }
        System.out.println("  codePointAt + шаг  : " + viaCharCount);

        StringBuilder viaStream = new StringBuilder();
        SAMPLE.codePoints().forEach(codePoint ->
                viaStream.append(new String(Character.toChars(codePoint))).append(' '));
        System.out.println("  codePoints()       : " + viaStream);

        System.out.println();
        check(characterCount == 5, "в строке пять символов");
        check(SAMPLE.length() == 7, "и семь единиц char");
        check(characterAt(SAMPLE, 1).equals("🐷"), "1-й символ — целая свинья");
        check(characterAt(SAMPLE, 1).length() == 2, "и занимает она два char");
        check(SAMPLE.substring(1, 2).length() == 1, "а наивный substring даёт один char");
        check(Character.isSurrogate(SAMPLE.substring(1, 2).charAt(0)),
                "и этот char — суррогат, то есть половина символа");
        check(viaOffsets.toString().equals(viaCharCount.toString()),
                "все три способа перебора дают одно и то же");
        check(viaCharCount.toString().equals(viaStream.toString()),
                "codePoints() совпадает с ручным шагом");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
