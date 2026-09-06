package block06_trees_graphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * LeetCode 102 — Binary Tree Level Order Traversal (Medium). Обход в ширину
 * с очередью; каждый уровень собирается в отдельный список.
 * <p>
 * Границу между уровнями даёт размер очереди в начале уровня: столько узлов
 * надо снять, прежде чем в очереди останутся только их потомки.
 * Время O(n), память O(w), где w — наибольшее число узлов на одном уровне.
 *
 * @see <a href="../../docs/problems/block06_trees_graphs/LevelOrderTraversal.md">LevelOrderTraversal.md</a>
 */
public class LevelOrderTraversal {

    /* Пустое дерево — законный вход по условию, ответ на него — пустой список. */
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> levels = new ArrayList<>();
        if (root == null) {
            return levels;
        }

        Queue<TreeNode> pending = new ArrayDeque<>();
        pending.add(root);
        while (!pending.isEmpty()) {
            /*
             * В начале витка в очереди лежит ровно один уровень целиком:
             * потомки снятых узлов дописываются в хвост и в этот счёт не входят.
             */
            int nodesOnLevel = pending.size();
            List<Integer> levelValues = new ArrayList<>(nodesOnLevel);
            for (int taken = 0; taken < nodesOnLevel; taken++) {
                TreeNode node = pending.remove();
                levelValues.add(node.val);
                if (node.left != null) {
                    pending.add(node.left);
                }
                if (node.right != null) {
                    pending.add(node.right);
                }
            }
            levels.add(levelValues);
        }
        return levels;
    }

    public static void main(String[] args) {
        /*
         * Ветви метода и тесты, которые их закрывают:
         *   1) root == null, пустой список ............... пример 3
         *   2) внешний цикл: один виток .................. один узел
         *   3) внешний цикл: много витков ................ пример 1, цепочки
         *   4) left != null / == null .................... пример 1 (у 20 есть, у 9 нет)
         *   5) right != null / == null ................... то же
         *   6) уровень из узлов с РАЗНЫМИ родителями ..... [1, 2, 3, 4, null, null, 5]
         * Пункт 6 — главный: он ловит ошибку, когда уровнем считают
         * потомков одного родителя, а не всех родителей сразу.
         */
        record TestCase(TreeNode root, List<List<Integer>> expected, String name) {}

        TestCase[] testCases = {
            new TestCase(TreeNode.fromLevelOrder(3, 9, 20, null, null, 15, 7),
                         List.of(List.of(3), List.of(9, 20), List.of(15, 7)),
                         "пример 1 из условия"),
            new TestCase(TreeNode.fromLevelOrder(1), List.of(List.of(1)),
                         "пример 2: один узел"),
            new TestCase(null, List.of(), "пример 3: пустое дерево"),
            new TestCase(TreeNode.fromLevelOrder(1, 2, 3, 4, 5, 6, 7),
                         List.of(List.of(1), List.of(2, 3), List.of(4, 5, 6, 7)),
                         "полное дерево из трёх уровней"),
            new TestCase(TreeNode.fromLevelOrder(1, 2, 3, 4, null, null, 5),
                         List.of(List.of(1), List.of(2, 3), List.of(4, 5)),
                         "узлы одного уровня у разных родителей"),
            new TestCase(TreeNode.fromLevelOrder(1, null, 2, null, 3),
                         List.of(List.of(1), List.of(2), List.of(3)),
                         "цепочка вправо: по одному узлу на уровень"),
            new TestCase(TreeNode.fromLevelOrder(1, 2, null, 3),
                         List.of(List.of(1), List.of(2), List.of(3)),
                         "цепочка влево: по одному узлу на уровень"),
            new TestCase(TreeNode.fromLevelOrder(1, 2, 3, null, 4, 5),
                         List.of(List.of(1), List.of(2, 3), List.of(4, 5)),
                         "внутри уровня порядок слева направо: правый потомок 2 раньше левого потомка 3"),
            new TestCase(TreeNode.fromLevelOrder(-1000, 1000, -1000),
                         List.of(List.of(-1000), List.of(1000, -1000)),
                         "края диапазона значений"),
        };

        for (TestCase testCase : testCases) {
            List<List<Integer>> actual = levelOrder(testCase.root());
            check(actual.equals(testCase.expected()),
                  testCase.name() + " -> " + actual + " (ожидалось " + testCase.expected() + ")");
        }

        /*
         * Предел условия: 2000 узлов. Запись [0, 1, …, 1999] даёт полное дерево,
         * где на уровне k лежат значения от 2ᵏ − 1 до 2ᵏ⁺¹ − 2, то есть уровни
         * удваиваются: 1, 2, 4, …, 1024 и последний из 977 узлов.
         */
        int limit = 2000;
        Integer[] values = new Integer[limit];
        for (int index = 0; index < limit; index++) {
            values[index] = index;
        }
        List<List<Integer>> levels = levelOrder(TreeNode.fromLevelOrder(values));
        boolean sizesDouble = true;
        int expectedSize = 1;
        int total = 0;
        for (int levelIndex = 0; levelIndex < levels.size(); levelIndex++) {
            int size = levels.get(levelIndex).size();
            total += size;
            boolean last = levelIndex == levels.size() - 1;
            if (size != expectedSize && !(last && size == limit - (expectedSize - 1))) {
                sizesDouble = false;
            }
            expectedSize *= 2;
        }
        check(levels.size() == 11 && total == limit && sizesDouble,
              "полное дерево из " + limit + " узлов: 11 уровней, размеры 1, 2, 4, …, 1024, 977");
        check(levels.get(10).get(0) == 1023 && levels.get(10).get(976) == 1999,
              "последний уровень: от 1023 до 1999 слева направо");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
