package block03_hashtables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LeetCode 49 — Group Anagrams (Medium). Паттерн «хеш-таблица», форма
 * «группировка по ключу».
 * <p>
 * Дан массив строк. Сгруппировать вместе анаграммы. Анаграммы неразличимы
 * по составу букв, поэтому нужен «отпечаток» строки — величина, одинаковая
 * у всех анаграмм и разная у остальных. Дальше задача сводится к раскладыванию
 * строк по корзинам карты «ключ -> список слов».
 * <p>
 * Две реализации отличаются только способом построить ключ:
 * {@link #groupAnagrams} сортирует буквы слова, {@link #groupAnagramsByCounts}
 * склеивает вектор частот. Первая короче, вторая быстрее на длинных строках.
 * <p>
 * n — количество строк, k — максимальная длина строки.
 * Время O(n·k·log k) и O(n·k) соответственно, память O(n·k) у обеих.
 * <p>
 * Полное условие, примеры и ограничения:
 * {@code docs/problems/block03_hashtables/GroupAnagrams.md}
 *
 * @see <a href="../../docs/problems/block03_hashtables/GroupAnagrams.md">GroupAnagrams.md</a>
 */
public class GroupAnagrams {

    private static final int LOWERCASE_LETTER_COUNT = 26;

    /*
     * Ключ — отсортированные буквы слова: у анаграмм они совпадают.
     * computeIfAbsent возвращает список по ключу, а если ключа ещё нет —
     * создаёт его функцией, кладёт в карту и возвращает. Функция вызывается
     * только при отсутствии ключа, поэтому лишних ArrayList не создаётся.
     */
    public static List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> groups = new HashMap<>();
        for (String word : strs) {
            char[] letters = word.toCharArray();
            Arrays.sort(letters);
            String key = new String(letters);
            groups.computeIfAbsent(key, absentKey -> new ArrayList<>()).add(word);
        }
        return new ArrayList<>(groups.values());
    }

    /*
     * Ключ — вектор частот, склеенный в строку. Считаем буквы в int[26]
     * и выписываем счётчики через разделитель.
     * Разделитель обязателен: без него счётчики склеиваются в неоднозначную
     * строку. Слово с одной «a» и одиннадцатью «b» дало бы 111..., и слово
     * с одиннадцатью «a» и одной «b» — тоже, то есть разные слова получили бы
     * один ключ и группы слились бы.
     */
    public static List<List<String>> groupAnagramsByCounts(String[] strs) {
        Map<String, List<String>> groups = new HashMap<>();
        for (String word : strs) {
            groups.computeIfAbsent(buildCountKey(word), absentKey -> new ArrayList<>()).add(word);
        }
        return new ArrayList<>(groups.values());
    }

    private static String buildCountKey(String word) {
        int[] letterCounts = new int[LOWERCASE_LETTER_COUNT];
        for (int currentIndex = 0; currentIndex < word.length(); currentIndex++) {
            letterCounts[word.charAt(currentIndex) - 'a']++;
        }
        StringBuilder key = new StringBuilder();
        for (int count : letterCounts) {
            key.append(count).append('#');
        }
        return key.toString();
    }

    public static void main(String[] args) {
        /*
         * Ветви методов и тест, который каждую из них закрывает:
         *   1) computeIfAbsent, ключа ещё нет — создаём список ... любой вход
         *   2) computeIfAbsent, ключ уже есть — дописываем ....... ["ab","ba"]
         *   3) цикл не начался, пустой массив .................... []
         *   4) слово нулевой длины ............................... [""]
         *   5) все слова в одной группе .......................... ["ab","ba"]
         *   6) все слова в разных группах ........................ ["a","b"]
         *   7) в buildCountKey цикл по слову не начался .......... [""]
         * Обе реализации прогоняются по одному и тому же набору случаев.
         */
        record TestCase(String[] input, String[][] expected, String name) {}

        TestCase[] testCases = {
            new TestCase(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"},
                         new String[][]{{"eat", "tea", "ate"}, {"tan", "nat"}, {"bat"}},
                         "пример из условия, три группы"),
            new TestCase(new String[]{""},
                         new String[][]{{""}},
                         "пустая строка образует свою группу"),
            new TestCase(new String[]{"a"},
                         new String[][]{{"a"}},
                         "одно слово"),
            new TestCase(new String[]{"ab", "ba"},
                         new String[][]{{"ab", "ba"}},
                         "все слова в одной группе"),
            new TestCase(new String[]{"a", "b", "c"},
                         new String[][]{{"a"}, {"b"}, {"c"}},
                         "все слова в разных группах"),
            new TestCase(new String[]{"ab", "ba", "abc"},
                         new String[][]{{"ab", "ba"}, {"abc"}},
                         "разная длина — разные группы"),
            new TestCase(new String[]{},
                         new String[][]{},
                         "пустой массив, цикл не начался"),
            new TestCase(new String[]{"aacc", "ccac"},
                         new String[][]{{"aacc"}, {"ccac"}},
                         "тот же набор букв, разные количества — разные группы"),
            new TestCase(new String[]{"", ""},
                         new String[][]{{"", ""}},
                         "две пустые строки — одна группа"),
            new TestCase(new String[]{"abc", "abc"},
                         new String[][]{{"abc", "abc"}},
                         "одинаковые слова — одна группа"),
        };

        for (TestCase testCase : testCases) {
            check(sameGrouping(groupAnagrams(testCase.input()), testCase.expected()),
                  "сортировка: " + testCase.name());
            check(sameGrouping(groupAnagramsByCounts(testCase.input()), testCase.expected()),
                  "счётчики: " + testCase.name());
        }

        /*
         * Разделитель в ключе: без него слово с одной «a» и одиннадцатью «b»
         * и слово с одиннадцатью «a» и одной «b» дали бы одинаковый ключ.
         * Проверяем, что они попали в РАЗНЫЕ группы.
         */
        String oneAelevenB = "a" + "b".repeat(11);
        String elevenAoneB = "a".repeat(11) + "b";
        check(groupAnagramsByCounts(new String[]{oneAelevenB, elevenAoneB}).size() == 2,
              "разделитель в ключе: 1×a+11×b и 11×a+1×b — разные группы");

        /* Ключи двух анаграмм обязаны совпасть, а двух не-анаграмм — различаться. */
        check(buildCountKey("eat").equals(buildCountKey("tea")),
              "buildCountKey: у анаграмм ключи совпадают");
        check(!buildCountKey("eat").equals(buildCountKey("eaat")),
              "buildCountKey: лишняя буква меняет ключ");
        check(buildCountKey("").equals(buildCountKey("")),
              "buildCountKey: пустая строка даёт ключ из одних нулей");

        /* Все исходные слова обязаны попасть в результат ровно по одному разу. */
        String[] input = {"eat", "tea", "tan", "ate", "nat", "bat"};
        int totalWords = 0;
        for (List<String> group : groupAnagrams(input)) {
            totalWords += group.size();
        }
        check(totalWords == input.length, "ни одно слово не потерялось и не задвоилось");
    }

    /*
     * Сравнение результата с ожиданием. Порядок групп и порядок слов внутри
     * группы по условию не важен, поэтому перед сравнением и то и другое
     * приводится к каноническому виду: слова внутри группы сортируются,
     * затем сортируются сами группы.
     */
    private static boolean sameGrouping(List<List<String>> actual, String[][] expected) {
        List<List<String>> normalizedActual = normalize(actual);
        List<List<String>> expectedAsList = new ArrayList<>();
        for (String[] group : expected) {
            expectedAsList.add(new ArrayList<>(Arrays.asList(group)));
        }
        return normalizedActual.equals(normalize(expectedAsList));
    }

    private static List<List<String>> normalize(List<List<String>> groups) {
        List<List<String>> copy = new ArrayList<>();
        for (List<String> group : groups) {
            List<String> sortedGroup = new ArrayList<>(group);
            sortedGroup.sort(Comparator.naturalOrder());
            copy.add(sortedGroup);
        }
        copy.sort(Comparator.comparing(group -> String.join(",", group)));
        return copy;
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
