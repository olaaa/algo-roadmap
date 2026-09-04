package block06_trees_graphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Узел двоичного дерева — общий для всех задач блока 6.
 * <p>
 * Имена полей {@code val}, {@code left}, {@code right} взяты из условия
 * LeetCode: там этот класс дан заранее, и на собеседовании его тоже приносят
 * готовым. Поэтому правило проекта о раскрывающих именах здесь не действует —
 * менять чужой контракт нельзя.
 * <p>
 * Статические методы ниже — вспомогательные для тестов, в решениях задач
 * не используются. Запись дерева по уровням разобрана в
 * {@code docs/problems/block06_trees_graphs/TreeNotation.md}.
 */
public class TreeNode {

    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(int val) {
        this.val = val;
    }

    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    /**
     * Собирает дерево из перечисления значений по уровням — в том же формате,
     * в каком LeetCode печатает примеры: {@code [3, 9, 20, null, null, 15, 7]}.
     * Пропущенный потомок задаётся значением {@code null}, хвостовые
     * {@code null} можно опускать.
     * <p>
     * Запись, в которой значения остались, а раздавать их уже некому
     * (например {@code [1, null, null, 5]}), считается ошибкой:
     * {@code parents.remove()} бросит {@link NoSuchElementException}.
     */
    public static TreeNode fromLevelOrder(Integer... values) {
        if (values.length == 0 || values[0] == null) {
            return null;
        }

        TreeNode root = new TreeNode(values[0]);
        Queue<TreeNode> parents = new ArrayDeque<>();
        parents.add(root);

        int position = 1;
        while (position < values.length) {
            TreeNode parent = parents.remove();

            /*
             * Ячейка под левого потомка есть всегда — это гарантирует условие
             * цикла. Само значение может оказаться null, тогда потомка нет.
             */
            Integer leftValue = values[position];
            position++;
            if (leftValue != null) {
                parent.left = new TreeNode(leftValue);
                parents.add(parent.left);
            }

            /* А ячейки под правого может не быть вовсе: массив мог кончиться на левом. */
            if (position < values.length) {
                Integer rightValue = values[position];
                position++;
                if (rightValue != null) {
                    parent.right = new TreeNode(rightValue);
                    parents.add(parent.right);
                }
            }
        }
        return root;
    }

    /**
     * Обратная операция к {@link #fromLevelOrder}: записывает дерево
     * по уровням в формате LeetCode, хвостовые {@code null} отбрасывает.
     * Пустое дерево даёт пустой список.
     */
    public static List<Integer> toLevelOrder(TreeNode root) {
        List<Integer> values = new ArrayList<>();
        if (root == null) {
            return values;
        }

        values.add(root.val);
        Queue<TreeNode> parents = new ArrayDeque<>();
        parents.add(root);

        /* У каждого непустого узла записываются оба потомка — значение или null. */
        while (!parents.isEmpty()) {
            TreeNode parent = parents.remove();
            for (TreeNode child : new TreeNode[]{parent.left, parent.right}) {
                if (child == null) {
                    values.add(null);
                } else {
                    values.add(child.val);
                    parents.add(child);
                }
            }
        }

        int lastValueIndex = values.size() - 1;
        while (values.get(lastValueIndex) == null) {
            lastValueIndex--;
        }
        return values.subList(0, lastValueIndex + 1);
    }

    /**
     * Строит вырожденное дерево-цепочку заданной длины: у каждого узла есть
     * только правый потомок, значения идут от нуля по порядку. Нужно тестам,
     * чтобы проверить поведение на самом глубоком дереве, какое допускают
     * ограничения задачи.
     */
    public static TreeNode chainOfLength(int length) {
        if (length <= 0) {
            return null;
        }
        TreeNode root = new TreeNode(0);
        TreeNode tail = root;
        for (int nodeValue = 1; nodeValue < length; nodeValue++) {
            tail.right = new TreeNode(nodeValue);
            tail = tail.right;
        }
        return root;
    }

    public static void main(String[] args) {
        /*
         * Ветви fromLevelOrder и тесты, которые их закрывают:
         *   1) values пуст ................................ пустой вызов
         *   2) values[0] == null .......................... запись [null]
         *   3) цикл не начался ............................ один узел
         *   4) leftValue == null / != null ................ [1, null, 2] и [1, 2, 3]
         *   5) ячейки под правого нет ..................... [1, 2]
         *   6) rightValue == null / != null ............... [1, 2, null] и [1, 2, 3]
         *   7) значения остались, очередь пуста ........... [1, null, null, 5]
         * Форма дерева проверяется по полям напрямую, а не через toLevelOrder:
         * иначе ошибка, общая для обоих методов, осталась бы незамеченной.
         */
        check(fromLevelOrder() == null, "fromLevelOrder: пустой вызов даёт пустое дерево");
        check(fromLevelOrder((Integer) null) == null, "fromLevelOrder: [null] даёт пустое дерево");

        TreeNode single = fromLevelOrder(7);
        check(single.val == 7 && single.left == null && single.right == null,
              "fromLevelOrder: [7] — один узел без потомков");

        TreeNode onlyRight = fromLevelOrder(1, null, 2);
        check(onlyRight.left == null && onlyRight.right != null && onlyRight.right.val == 2,
              "fromLevelOrder: [1, null, 2] — левого нет, правый равен 2");

        TreeNode both = fromLevelOrder(1, 2, 3);
        check(both.left.val == 2 && both.right.val == 3,
              "fromLevelOrder: [1, 2, 3] — левый 2, правый 3, порядок не перепутан");

        TreeNode endsOnLeft = fromLevelOrder(1, 2);
        check(endsOnLeft.left.val == 2 && endsOnLeft.right == null,
              "fromLevelOrder: [1, 2] — массив кончился на левом, правого нет");

        TreeNode rightIsNull = fromLevelOrder(1, 2, null);
        check(rightIsNull.left.val == 2 && rightIsNull.right == null,
              "fromLevelOrder: [1, 2, null] — то же дерево, что [1, 2]");

        TreeNode example = fromLevelOrder(3, 9, 20, null, null, 15, 7);
        check(example.left.val == 9 && example.left.left == null && example.left.right == null
                      && example.right.val == 20 && example.right.left.val == 15
                      && example.right.right.val == 7,
              "fromLevelOrder: пример из условия — у 9 потомков нет, у 20 они 15 и 7");

        TreeNode skipInside = fromLevelOrder(1, null, 2, null, 3);
        check(skipInside.left == null && skipInside.right.left == null
                      && skipInside.right.right.val == 3,
              "fromLevelOrder: [1, null, 2, null, 3] — null занимает одну ячейку, потомков не получает");

        boolean thrown = false;
        try {
            fromLevelOrder(1, null, null, 5);
        } catch (NoSuchElementException e) {
            thrown = true;
        }
        check(thrown, "fromLevelOrder: [1, null, null, 5] — раздавать некому, исключение");

        /*
         * Ветви toLevelOrder:
         *   1) root == null ............................... пустое дерево
         *   2) child == null / != null .................... дерево с пропуском
         *   3) отбрасывание хвостовых null ................ один узел, лист
         */
        check(toLevelOrder(null).isEmpty(), "toLevelOrder: пустое дерево даёт пустой список");
        check(toLevelOrder(new TreeNode(7)).equals(List.of(7)),
              "toLevelOrder: один узел — хвостовые null отброшены");
        TreeNode builtByHand = new TreeNode(1, null, new TreeNode(2, null, new TreeNode(3)));
        check(toLevelOrder(builtByHand).equals(Arrays.asList(1, null, 2, null, 3)),
              "toLevelOrder: дерево, собранное конструкторами, записано с пропусками");

        /* Круговая проверка: запись → дерево → запись даёт исходную запись. */
        Integer[][] records = {
            {3, 9, 20, null, null, 15, 7},
            {1, null, 2, null, 3},
            {1, 2, 3, 4, null, null, null, 5},
            {1, 2, null, 3, null, 4},
        };
        for (Integer[] record : records) {
            List<Integer> restored = toLevelOrder(fromLevelOrder(record));
            check(restored.equals(Arrays.asList(record)),
                  "круг: " + Arrays.toString(record) + " -> " + restored);
        }

        /*
         * Ветви chainOfLength:
         *   1) length <= 0 ................................ ноль и отрицательная длина
         *   2) цикл не начался ............................ длина 1
         *   3) цикл работал ............................... длина 3
         */
        check(chainOfLength(0) == null && chainOfLength(-1) == null,
              "chainOfLength: длина 0 и -1 дают пустое дерево");
        TreeNode chainOfOne = chainOfLength(1);
        check(chainOfOne.val == 0 && chainOfOne.right == null,
              "chainOfLength: длина 1 — корень со значением 0 без потомков");
        check(toLevelOrder(chainOfLength(3)).equals(Arrays.asList(0, null, 1, null, 2)),
              "chainOfLength: длина 3 — [0, null, 1, null, 2]");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
