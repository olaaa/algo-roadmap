package block02_strings_stack;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * LeetCode 20 — Valid Parentheses (Easy). Паттерн «стек».
 * <p>
 * Дана строка из символов ()[]{}. Проверить, что каждая открывающая скобка
 * закрыта скобкой того же типа и в правильном порядке. Закрывающая скобка
 * всегда обязана закрыть последнюю открытую — это поведение стека (LIFO).
 * Время O(n), память O(n).
 * <p>
 * Рядом лежит вторая реализация {@link #isValidPushExpected} — вместо
 * открывающей скобки в стек кладётся ожидаемая закрывающая, что убирает
 * функцию сопоставления типов. Логика та же, полезна как второй вариант
 * ответа на собеседовании.
 * <p>
 * Полное условие, примеры и ограничения:
 * {@code docs/problems/block02_strings_stack/ValidParentheses.md}
 *
 * @see <a href="../../docs/problems/block02_strings_stack/ValidParentheses.md">ValidParentheses.md</a>
 */
public class ValidParentheses {

    /*
     * Эталонное решение: в стек кладём саму открывающую скобку.
     * Закрывающая скобка даёт два повода отвергнуть строку сразу — пустой стек
     * (закрывать нечего) и несовпадение типа с вершиной стека.
     * После цикла возвращаем НЕ true, а «стек пуст»: строка вида "(" не нарушает
     * ни одного правила внутри цикла, но оставляет незакрытую скобку.
     */
    public static boolean isValid(String s) {
        Deque<Character> openBrackets = new ArrayDeque<>();
        for (int currentIndex = 0; currentIndex < s.length(); currentIndex++) {
            char currentBracket = s.charAt(currentIndex);
            if (isOpening(currentBracket)) {
                openBrackets.push(currentBracket);
            } else {
   // закрывающая скобка
                if (openBrackets.isEmpty()) {
// открывающих не было -- выходим
                    return false;
                }
                char lastOpened = openBrackets.pop();
                if (!isMatchingPair(lastOpened, currentBracket)) {
                    return false;
                }
            }
        }
// работа считается сделанной, только если стек пуст, то
// есть каждая открытая скобка нашла свою пару
        return openBrackets.isEmpty();
    }

    /*
     * Альтернатива: в стек кладём ту закрывающую скобку, которую ожидаем увидеть.
     * Тогда сопоставление типов сводится к одному сравнению символов, отдельная
     * функция isMatchingPair не нужна.
     */
    public static boolean isValidPushExpected(String s) {
        Deque<Character> expectedClosing = new ArrayDeque<>();
        for (int currentIndex = 0; currentIndex < s.length(); currentIndex++) {
            char currentBracket = s.charAt(currentIndex);
            if (currentBracket == '(') {
                expectedClosing.push(')');
            } else if (currentBracket == '[') {
                expectedClosing.push(']');
            } else if (currentBracket == '{') {
                expectedClosing.push('}');
//  пришла закрывающая
            } else {
                if (expectedClosing.isEmpty() || (expectedClosing.pop() != currentBracket)) {
                    return false;
                }
            }
        }

// остались закрывающие скобки, которые ожидались, но которых не подали на вход
        return expectedClosing.isEmpty();
    }

    private static boolean isOpening(char bracket) {
        return bracket == '(' || bracket == '[' || bracket == '{';
    }

    private static boolean isMatchingPair(char openingBracket, char closingBracket) {
        return (openingBracket == '(' && closingBracket == ')')
            || (openingBracket == '[' && closingBracket == ']')
            || (openingBracket == '{' && closingBracket == '}');
    }

    public static void main(String[] args) {
        /*
         * Ветви метода isValid и тест, который каждую из них закрывает:
         *   1) ветка isOpening — push в стек ................. "()" и все вложенные случаи
         *   2) ветка else, стек пуст -> return false ......... ")" и "())"
         *   3) ветка else, тип не совпал -> return false ..... "(]" и "([)]"
         *   4) ветка else, тип совпал (проход дальше) ........ "()" и "([{}])"
         *   5) return после цикла = true (стек пуст) ......... "()", "()[]{}"
         *   6) return после цикла = false (стек НЕ пуст) ..... "(" и "([{"  — ФОЛБЭК
         * Пустой строки среди случаев нет: ограничения задают 1 <= s.length.
         * Обе реализации прогоняются по одному и тому же набору случаев.
         */
        record TestCase(String input, boolean expected, String name) {}

        TestCase[] testCases = {
            new TestCase("()", true, "простая пара"),
            new TestCase("()[]{}", true, "три пары подряд, все типы"),
            new TestCase("([{}])", true, "полная вложенность"),
            new TestCase("{[()]}", true, "вложенность в другом порядке"),
            new TestCase("(]", false, "тип не совпал"),
            new TestCase("([)]", false, "нарушен порядок закрытия"),
            new TestCase(")", false, "закрывающая при пустом стеке"),
            new TestCase("())", false, "лишняя закрывающая в конце"),
            new TestCase("(", false, "незакрытая скобка (фолбэк после цикла)"),
            new TestCase("([{", false, "три незакрытых скобки (фолбэк после цикла)"),
            new TestCase("{[}]", false, "пересечение вместо вложенности"),
        };

        for (TestCase testCase : testCases) {
            check(isValid(testCase.input()) == testCase.expected(),
                  "isValid: " + testCase.name());
            check(isValidPushExpected(testCase.input()) == testCase.expected(),
                  "PushExpected: " + testCase.name());
        }

        /*
         * Отдельно проверяем isMatchingPair: три истинные пары и промах.
         * Через основные тесты каждая пара по отдельности не различима.
         */
        check(isMatchingPair('(', ')'), "isMatchingPair: круглые");
        check(isMatchingPair('[', ']'), "isMatchingPair: квадратные");
        check(isMatchingPair('{', '}'), "isMatchingPair: фигурные");
        check(!isMatchingPair('(', '}'), "isMatchingPair: разные типы -> false");

        check(isOpening('('), "isOpening: круглая открывающая");
        check(isOpening('['), "isOpening: квадратная открывающая");
        check(isOpening('{'), "isOpening: фигурная открывающая");
        check(!isOpening(')'), "isOpening: закрывающая -> false");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
