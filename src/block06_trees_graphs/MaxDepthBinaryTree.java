package block06_trees_graphs;

/**
 * LeetCode 104 — Maximum Depth of Binary Tree (Easy). Обход в глубину
 * рекурсией.
 * <p>
 * Глубина дерева — число узлов на самом длинном пути от корня до листа.
 * Глубина узла на единицу больше, чем у более глубокого из его поддеревьев.
 * Время O(n), память O(h), где h — высота дерева.
 *
 * @see <a href="../../docs/problems/block06_trees_graphs/MaxDepthBinaryTree.md">MaxDepthBinaryTree.md</a>
 */
public class MaxDepthBinaryTree {

    /*
     * Проверка на null — не охранная, а базовый случай рекурсии: условие
     * прямо допускает пустое дерево, и ответ на нём равен нулю. Она же
     * останавливает спуск, когда у узла нет потомка.
     */
    public static int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftDepth = maxDepth(root.left);
        int rightDepth = maxDepth(root.right);
        return 1 + Math.max(leftDepth, rightDepth);
    }

    public static void main(String[] args) {
        /*
         * Ветви метода и тесты, которые их закрывают:
         *   1) root == null, возврат нуля ................. пустое дерево
         *   2) оба поддерева пусты, лист .................. дерево из одного узла
         *   3) Math.max выбрал левое поддерево ............ перекос влево
         *   4) Math.max выбрал правое поддерево ........... перекос вправо
         *   5) поддеревья равной глубины .................. полное дерево
         */
        record TestCase(TreeNode root, int expected, String name) {}

        TestCase[] testCases = {
            new TestCase(TreeNode.fromLevelOrder(3, 9, 20, null, null, 15, 7), 3,
                         "пример 1 из условия"),
            new TestCase(TreeNode.fromLevelOrder(1, null, 2), 2,
                         "пример 2: только правый потомок"),
            new TestCase(null, 0, "пустое дерево"),
            new TestCase(TreeNode.fromLevelOrder(7), 1, "дерево из одного узла"),
            new TestCase(TreeNode.fromLevelOrder(1, 2, 3, 4, 5, 6, 7), 3,
                         "полное дерево, поддеревья равной глубины"),
            new TestCase(TreeNode.fromLevelOrder(1, 2, null, 3, null, 4), 4,
                         "перекос влево, глубже левое поддерево"),
            new TestCase(TreeNode.fromLevelOrder(1, null, 2, null, 3), 3,
                         "перекос вправо, глубже правое поддерево"),
            new TestCase(TreeNode.fromLevelOrder(1, 2, 3, 4, null, null, null, 5), 4,
                         "левое поддерево глубже правого на два уровня"),
            new TestCase(TreeNode.fromLevelOrder(-100, 100, -100), 2,
                         "края диапазона значений"),
        };

        for (TestCase testCase : testCases) {
            int actual = maxDepth(testCase.root());
            check(actual == testCase.expected(),
                  testCase.name() + " -> " + actual
                          + " (ожидалось " + testCase.expected() + ")");
        }

        /*
         * Предельное дерево по ограничениям задачи: 10 000 узлов в цепочку.
         * Это худший случай для рекурсии — глубина спуска равна числу узлов.
         */
        int limit = 10_000;
        check(maxDepth(TreeNode.chainOfLength(limit)) == limit,
              "цепочка из " + limit + " узлов: глубина равна длине цепочки");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
