package block06_trees_graphs;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * LeetCode 104 — Maximum Depth of Binary Tree (Easy), вариант без рекурсии:
 * обход в глубину с явным стеком вместо стека вызовов.
 * <p>
 * В стек кладётся узел вместе с его глубиной (запись NodeAtDepth): глубина
 * известна в момент, когда узел кладут, — на единицу больше глубины
 * родителя, — и после снятия возвращаться к узлу не надо. Ответ — наибольшая
 * из снятых глубин. Время O(n), память O(h), где h — высота дерева: в стеке
 * одновременно лежит не больше h + 1 записей — по одному отложенному правому
 * потомку на каждый уровень текущего пути от корня.
 *
 * @see <a href="../../docs/problems/block06_trees_graphs/MaxDepthBinaryTree.md">MaxDepthBinaryTree.md</a>
 */
public class MaxDepthBinaryTreeIterative {

    /* Элемент стека: узел вместе с глубиной, на которой он стоит в дереве. */
    private record NodeAtDepth(TreeNode node, int depth) {
    }

    public static int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }

        Deque<NodeAtDepth> nodesToVisit = new ArrayDeque<>();
        nodesToVisit.push(new NodeAtDepth(root, 1));

        int maxDepth = 0;
        while (!nodesToVisit.isEmpty()) {
            NodeAtDepth current = nodesToVisit.pop();
            maxDepth = Math.max(maxDepth, current.depth());

            int childDepth = current.depth() + 1;
            if (current.node().right != null) {
                nodesToVisit.push(new NodeAtDepth(current.node().right, childDepth));
            }
            /* Левый кладётся последним, поэтому снимется первым: спуск идёт влево. */
            if (current.node().left != null) {
                nodesToVisit.push(new NodeAtDepth(current.node().left, childDepth));
            }
        }
        return maxDepth;
    }

    public static void main(String[] args) {
        record TestCase(TreeNode root, int expected, String name) {
        }

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
                new TestCase(TreeNode.fromLevelOrder(1, 2, 3, null, null, 4, null, 5), 4,
                        "левое поддерево — один лист, самый глубокий лист справа"),
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

        /* Предельное дерево по ограничениям задачи: 10 000 узлов в цепочку. */
        int limit = 10_000;
        check(maxDepth(TreeNode.chainOfLength(limit)) == limit,
                "цепочка из " + limit + " узлов: глубина равна длине цепочки");

        /*
         * То, чего рекурсивный вариант не выдерживает: цепочка вдвое длиннее
         * предела. С явным стеком в куче переполнения быть не должно.
         */
        int beyondLimit = 20_000;
        check(maxDepth(TreeNode.chainOfLength(beyondLimit)) == beyondLimit,
                "цепочка из " + beyondLimit + " узлов: без рекурсии стек не переполняется");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
