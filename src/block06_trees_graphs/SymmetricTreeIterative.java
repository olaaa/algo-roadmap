package block06_trees_graphs;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * LeetCode 101 — Symmetric Tree (Easy), follow-up из условия: то же, что
 * {@link SymmetricTree}, но без рекурсии — на явной структуре данных.
 * <p>
 * Каркас: тесты те же, что у рекурсивного решения. Реализацию пишет Lela.
 *
 * @see <a href="../../docs/problems/block06_trees_graphs/SymmetricTree.md">SymmetricTree.md</a>
 */
public class SymmetricTreeIterative {

    /**
     * The number of nodes in the tree is in the range [1, 1000].
     *
     * @param root не может быть null
     *             <pre>
     *                                               1
     *                                        /             \
     *                                       2               2
     *                                    /     \         /     \
     *                                   3       4       4       3
     *                                  / \     / \     / \     / \
     *                                 5   6   7   8   8   7   6   5
     *                         </pre>
     */
    public static boolean isSymmetric(TreeNode root) {
// region Root
        Deque<TreeNode> nodesToVisit = new ArrayDeque<>();
        if (isLeaf(root)) {
            return true;
        }
        if (hasExactlyOneChild(root.left, root.right)) {
            return false;
        }

        nodesToVisit.push(root.right);
        nodesToVisit.push(root.left); // левый извлечем первым
// endregion
        while (!nodesToVisit.isEmpty()) {
            TreeNode leftSubtree = nodesToVisit.pop();
            TreeNode rightSubtree = nodesToVisit.pop();

// проверки не имеют смысла, если текущие узлы неравны
            if (leftSubtree.val == rightSubtree.val) {
// null-ы нельзя класть в стек, поэтому выполняем проверки
                if ((rightSubtree.right == null) && (leftSubtree.left == null)) {
                    continue;
                }

                if ((rightSubtree.right == null) || (leftSubtree.left == null)) {
                    return false;
                }
                // после проверок можем класть в стек оба потомка и не получать NPE
                nodesToVisit.push(rightSubtree.right);
                nodesToVisit.push(leftSubtree.left);
            } else {
                return false;
            }
        }

        return true;
    }

    private static boolean hasExactlyOneChild(TreeNode leftChild, TreeNode rightChild) {
        return (leftChild == null) || (rightChild == null);
    }

    private static boolean isLeaf(TreeNode node) {
        return node.left == null && node.right == null;
    }

    public static void main(String[] args) {
        record TestCase(TreeNode root, boolean expected, String name) {
        }

        TestCase[] testCases = {
                new TestCase(TreeNode.fromLevelOrder(1, 2, 2, 3, 4, 4, 3), true,
                        "пример 1 из условия: зеркальное дерево"),
                new TestCase(TreeNode.fromLevelOrder(1, 2, 2, null, 3, null, 3), false,
                        "пример 2 из условия: одинаковые поддеревья, но не зеркальные"),
                new TestCase(TreeNode.fromLevelOrder(1), true,
                        "один узел: оба поддерева пусты"),
                new TestCase(TreeNode.fromLevelOrder(1, 2, 2), true,
                        "корень и два равных потомка"),
                new TestCase(TreeNode.fromLevelOrder(1, 2, 3), false,
                        "значения потомков корня различны"),
                new TestCase(TreeNode.fromLevelOrder(1, 2, 2, 3, null, null, 3), true,
                        "потомки на разных сторонах, но зеркально"),
                new TestCase(TreeNode.fromLevelOrder(1, 2, 2, 3, 4, 4, 5), false,
                        "внешняя пара 3 и 5 не совпадает"),
                new TestCase(TreeNode.fromLevelOrder(1, 2, 2, 3, 4, 5, 3), false,
                        "внешняя пара совпадает, внутренняя 4 и 5 нет"),
                new TestCase(TreeNode.fromLevelOrder(1, 2, null), false,
                        "у корня только левый потомок"),
                new TestCase(TreeNode.fromLevelOrder(-100, 100, 100), true,
                        "края диапазона значений"),
        };

        for (TestCase testCase : testCases) {
            boolean actual = isSymmetric(testCase.root());
            check(actual == testCase.expected(),
                    testCase.name() + " -> " + actual
                            + " (ожидалось " + testCase.expected() + ")");
        }

        /*
         * Предельная глубина по ограничениям задачи: 1000 узлов. «Галочка» —
         * у корня два потомка, дальше левая ветвь растёт только влево, правая
         * только вправо; на 999 узлах глубина 500.
         */
        int depth = 500;
        TreeNode root = new TreeNode(0);
        root.left = new TreeNode(1);
        root.right = new TreeNode(1);
        TreeNode leftTail = root.left;
        TreeNode rightTail = root.right;
        for (int level = 2; level < depth; level++) {
            leftTail.left = new TreeNode(level);
            rightTail.right = new TreeNode(level);
            leftTail = leftTail.left;
            rightTail = rightTail.right;
        }
        check(isSymmetric(root), "галочка глубиной " + depth + " из 999 узлов: симметрична");
        rightTail.val = -1;
        check(!isSymmetric(root), "та же галочка с одним изменённым листом: не симметрична");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
