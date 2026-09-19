package block06_trees_graphs;

import java.util.*;
import java.util.function.BooleanSupplier;

/**
 * Заготовка для самостоятельной реализации двух преобразований: из записи
 * дерева по уровням в дерево и обратно. Тесты готовы, тела двух методов пустые.
 * <p>
 * Готовые методы лежат рядом, в {@link TreeNode#fromLevelOrder} и
 * {@link TreeNode#toLevelOrder}, разбор — по ссылке ниже. До первой попытки
 * туда лучше не заглядывать: весь нужный для решения контракт изложен
 * в javadoc обоих методов здесь.
 * <p>
 * Предшественник такого же рода — {@code SearchInsertPositionPractice}
 * из блока 5.
 *
 * @see <a href="../../docs/problems/block06_trees_graphs/TreeNotation.md">TreeNotation.md</a>
 */
public class TreeNotationPractice {

    /**
     * Собрать дерево из перечисления значений по уровням — в том формате,
     * в каком LeetCode печатает примеры: {@code [3, 9, 20, null, null, 15, 7]}.
     * <p>
     * Что означает запись. Первое значение — корень. Дальше значения раздаются
     * узлам по два: узлы получают потомков в том же порядке, в каком сами
     * появились в дереве, сначала левого потомка, потом правого. Значение
     * {@code null} означает, что потомка нет, но свою ячейку в записи он
     * занимает. Хвостовые {@code null} в записи можно опускать: {@code [1, 2]}
     * и {@code [1, 2, null]} — одно и то же дерево.
     * <p>
     * Краевые случаи. Пустая запись и запись {@code [null]} дают пустое дерево,
     * то есть {@code null}. Запись, в которой значения остались, а раздавать их
     * уже некому (например {@code [1, null, null, 5]}), неправильна: на ней
     * метод должен бросить исключение, а не молча вернуть дерево. Какое именно
     * исключение — дело реализации, тест принимает любое.
     *
     * @param values значения по уровням; {@code null} — пропущенный потомок
     * @return корень собранного дерева или {@code null} для пустого дерева
     */
    public static TreeNode fromLevelOrder(Integer... values) {
        if (values.length == 0 || values[0] == null) {
            return null;
        }
// создали узел, а потомков зададим при извлечении из очереди
        TreeNode root = new TreeNode(values[0]);
//        неограниченная
        Queue<TreeNode> nodesToVisit = new ArrayDeque<>();
// нам не нужны исключения, которые мог бы выбрасывать add, так как очередь неограниченная,
// поэтому юзаем offer
        nodesToVisit.offer(root);
        int positionInArray = 1;

        while (positionInArray < values.length) {
            //  Throws NoSuchElementException – if this queue is empty
            TreeNode parent = nodesToVisit.remove();

            if (values[positionInArray] != null) {
// создали узел, а потомков зададим потом
                TreeNode leftChild = new TreeNode(values[positionInArray]);
                parent.left = leftChild;
                nodesToVisit.offer(leftChild);
            }
            positionInArray++;

            if (positionInArray < values.length) {
                if (values[positionInArray] != null) {
                    TreeNode rightChild = new TreeNode(values[positionInArray]);
                    parent.right = rightChild;
                    nodesToVisit.offer(rightChild);
                }
                positionInArray++;
            }
        }

        return root;
    }

    /**
     * Обратное преобразование: записать дерево по уровням в том же формате.
     * <p>
     * Что должно получиться. У каждого непустого узла в запись попадают ОБА
     * потомка — либо значение, либо {@code null}; пустой узел потомков не
     * получает и в запись их не добавляет. Хвостовые {@code null}
     * отбрасываются. Пустое дерево даёт пустой список.
     * <p>
     * Главное требование: на правильной записи
     * {@code toLevelOrder(fromLevelOrder(запись))} обязан дать ту же запись.
     *
     * @param root корень дерева, допускается {@code null}
     * @return запись дерева по уровням без хвостовых {@code null}
     */
    public static List<Integer> toLevelOrder(TreeNode root) {
        if (root == null) {
            return Collections.emptyList();
        }

        List<Integer> result = new ArrayList<>();
//        ArrayDeque не позволяет хранить null
        Queue<TreeNode> nodesToVisit = new ArrayDeque<>();
        result.add(root.val);
        nodesToVisit.offer(root);

        while (!nodesToVisit.isEmpty()) {
            TreeNode parent = nodesToVisit.remove();
            TreeNode leftChild = parent.left;
            if (leftChild != null) {
                nodesToVisit.add(leftChild);
                result.add(leftChild.val);
            } else {
                result.add(null);
            }

            TreeNode rightChild = parent.right;
            if (rightChild != null) {
                result.add(rightChild.val);
                nodesToVisit.offer(rightChild);
            } else {
                result.add(null);
            }

        }

        int end = result.size() - 1;
        while (result.get(end) == null) {
            end--;
        }

        return result.subList(0, end + 1);
    }

    public static void main(String[] args) {
        /*
         * Случаи, которые закрывают тесты fromLevelOrder:
         *   1) пустая запись и запись [null] ......... пустое дерево
         *   2) одно значение ......................... корень без потомков
         *   3) левого потомка нет, правый есть ....... [1, null, 2]
         *   4) оба потомка есть ...................... [1, 2, 3]
         *   5) запись кончилась на левом потомке ..... [1, 2]
         *   6) правый потомок задан как null ......... [1, 2, null]
         *   7) пропуск внутри записи ................. [3, 9, 20, null, null, 15, 7]
         *   8) null потомков не получает ............. [1, null, 2, null, 3]
         *   9) неправильная запись ................... [1, null, null, 5]
         * Форма дерева проверяется по полям val, left и right напрямую, а не
         * через toLevelOrder: иначе ошибка, общая для обоих методов, осталась
         * бы незамеченной.
         */
        check(() -> fromLevelOrder() == null,
                "fromLevelOrder: пустая запись даёт пустое дерево");
        check(() -> fromLevelOrder((Integer) null) == null,
                "fromLevelOrder: [null] даёт пустое дерево");

        check(() -> {
            TreeNode single = fromLevelOrder(7);
            return single.val == 7 && single.left == null && single.right == null;
        }, "fromLevelOrder: [7] — один узел без потомков");

        check(() -> {
            TreeNode onlyRight = fromLevelOrder(1, null, 2);
            return onlyRight.left == null && onlyRight.right != null && onlyRight.right.val == 2;
        }, "fromLevelOrder: [1, null, 2] — левого потомка нет, правый равен 2");

        check(() -> {
            TreeNode both = fromLevelOrder(1, 2, 3);
            return both.left.val == 2 && both.right.val == 3;
        }, "fromLevelOrder: [1, 2, 3] — левый потомок 2, правый 3, порядок не перепутан");

        check(() -> {
            TreeNode endsOnLeft = fromLevelOrder(1, 2);
            return endsOnLeft.left.val == 2 && endsOnLeft.right == null;
        }, "fromLevelOrder: [1, 2] — запись кончилась на левом потомке, правого нет");

        check(() -> {
            TreeNode rightIsNull = fromLevelOrder(1, 2, null);
            return rightIsNull.left.val == 2 && rightIsNull.right == null;
        }, "fromLevelOrder: [1, 2, null] — то же дерево, что [1, 2]");

        check(() -> {
            TreeNode example = fromLevelOrder(3, 9, 20, null, null, 15, 7);
            return example.val == 3
                    && example.left.val == 9 && example.left.left == null && example.left.right == null
                    && example.right.val == 20 && example.right.left.val == 15
                    && example.right.right.val == 7;
        }, "fromLevelOrder: пример из условия — у 9 потомков нет, у 20 они 15 и 7");

        check(() -> {
            TreeNode skipInside = fromLevelOrder(1, null, 2, null, 3);
            return skipInside.left == null
                    && skipInside.right.val == 2
                    && skipInside.right.left == null
                    && skipInside.right.right.val == 3;
        }, "fromLevelOrder: [1, null, 2, null, 3] — null занимает ячейку, но потомков не получает");

        boolean brokenRecordRejected = false;
        try {
            fromLevelOrder(1, null, null, 5);
        } catch (RuntimeException expected) {
            brokenRecordRejected = true;
        }
        check(brokenRecordRejected,
                "fromLevelOrder: [1, null, null, 5] — раздавать значение некому, вылетело исключение");

        /*
         * Случаи, которые закрывают тесты toLevelOrder:
         *   1) пустое дерево ......................... пустой список
         *   2) один узел ............................. хвостовые null отброшены
         *   3) есть только левый потомок ............. правый null отброшен как хвостовой
         *   4) есть только правый потомок ............ левый null остался в записи
         *   5) оба потомка ........................... [1, 2, 3]
         *   6) цепочка из трёх узлов ................. [0, null, 1, null, 2]
         * Деревья для этих тестов собраны конструкторами TreeNode, а не
         * методом fromLevelOrder — по той же причине, что и выше.
         */
        check(() -> toLevelOrder(null).isEmpty(),
                "toLevelOrder: пустое дерево даёт пустой список");
        check(() -> toLevelOrder(new TreeNode(7)).equals(List.of(7)),
                "toLevelOrder: один узел — хвостовые null отброшены");
        check(() -> toLevelOrder(new TreeNode(1, new TreeNode(2), null)).equals(List.of(1, 2)),
                "toLevelOrder: есть только левый потомок — запись [1, 2] без хвостового null");
        check(() -> toLevelOrder(new TreeNode(1, null, new TreeNode(2))).equals(Arrays.asList(1, null, 2)),
                "toLevelOrder: есть только правый потомок — левый null остался в записи");
        check(() -> toLevelOrder(new TreeNode(1, new TreeNode(2), new TreeNode(3))).equals(List.of(1, 2, 3)),
                "toLevelOrder: оба потомка — запись [1, 2, 3]");
        check(() -> toLevelOrder(TreeNode.chainOfLength(3)).equals(Arrays.asList(0, null, 1, null, 2)),
                "toLevelOrder: цепочка из трёх узлов — [0, null, 1, null, 2]");

        /*
         * Круги: запись → дерево → запись → дерево → … Каждый круг сверяется
         * с ИСХОДНОЙ записью, а не с предыдущим кругом, поэтому уехать
         * незаметно на пятом круге не получится.
         */
        Integer[][] levelOrderRecords = {
                {3, 9, 20, null, null, 15, 7},
                {1, null, 2, null, 3},
                {1, 2, 3, 4, null, null, null, 5},
                {1, 2, null, 3, null, 4},
                {7},
                {1, 2},
                {1, null, 2},
        };
        final int cycleCount = 5;
        for (Integer[] levelOrderRecord : levelOrderRecords) {
            int brokenCycle;
            String outcome;
            try {
                brokenCycle = firstBrokenCycle(levelOrderRecord, cycleCount);
                outcome = brokenCycle == 0
                        ? " выдержала " + cycleCount + " преобразований подряд"
                        : " разошлась на круге " + brokenCycle;
            } catch (RuntimeException failure) {
                brokenCycle = -1;
                outcome = ": вылетело " + failure.getClass().getSimpleName();
            }
            check(brokenCycle == 0, "круги: запись " + Arrays.toString(levelOrderRecord) + outcome);
        }

        /* Круг с другой стороны: дерево → запись → дерево → запись. */
        check(() -> {
            TreeNode builtByHand = new TreeNode(1,
                    new TreeNode(2, new TreeNode(4), null),
                    new TreeNode(3, null, new TreeNode(5)));
            List<Integer> firstRecord = toLevelOrder(builtByHand);
            List<Integer> secondRecord = toLevelOrder(fromLevelOrder(firstRecord.toArray(new Integer[0])));
            return firstRecord.equals(Arrays.asList(1, 2, 3, 4, null, null, 5))
                    && firstRecord.equals(secondRecord);
        }, "круги: дерево из конструкторов даёт запись [1, 2, 3, 4, null, null, 5], и круг её не меняет");

        /* Тот же круг на самом глубоком дереве, какое здесь имеет смысл гонять. */
        check(() -> {
            int chainLength = 500;
            List<Integer> chainRecord = toLevelOrder(TreeNode.chainOfLength(chainLength));
            return chainRecord.size() == chainLength * 2 - 1
                    && firstBrokenCycle(chainRecord.toArray(new Integer[0]), 3) == 0;
        }, "круги: цепочка из 500 узлов даёт 999 ячеек и выдерживает 3 преобразования подряд");
    }

    /**
     * Прогоняет запись через оба преобразования несколько раз подряд —
     * запись в дерево, дерево обратно в запись, и снова — и возвращает номер
     * первого круга, на котором результат разошёлся с ИСХОДНОЙ записью.
     * Ноль означает, что все круги дали ту же запись.
     */
    private static int firstBrokenCycle(Integer[] levelOrderRecord, int cycleCount) {
        List<Integer> expectedRecord = Arrays.asList(levelOrderRecord);
        Integer[] currentRecord = levelOrderRecord;

        for (int cycleNumber = 1; cycleNumber <= cycleCount; cycleNumber++) {
            List<Integer> restoredRecord = toLevelOrder(fromLevelOrder(currentRecord));
            if (!expectedRecord.equals(restoredRecord)) {
                return cycleNumber;
            }
            currentRecord = restoredRecord.toArray(new Integer[0]);
        }
        return 0;
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }

    /**
     * Та же печать, но условие передаётся замыканием и вычисляется внутри
     * try. Пока тела методов пустые, обращение к полям недописанного дерева
     * бросает исключение, и без этой обёртки прогон обрывался бы на первом
     * же тесте вместо того, чтобы показать весь список.
     */
    private static void check(BooleanSupplier condition, String name) {
        boolean ok;
        try {
            ok = condition.getAsBoolean();
        } catch (RuntimeException failure) {
            System.out.println("FAIL — " + name + " (вылетело " + failure.getClass().getSimpleName() + ")");
            return;
        }
        check(ok, name);
    }
}
