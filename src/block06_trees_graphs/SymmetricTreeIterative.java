package block06_trees_graphs;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * LeetCode 101 — Symmetric Tree (Easy), follow-up из условия: то же, что
 * {@link SymmetricTree}, но без рекурсии — на явном стеке.
 * <p>
 * В стеке лежат пары узлов из противоположных половин дерева, которые ещё
 * предстоит сравнить. Снятая пара сравнивается, и, если совпала, в стек
 * кладутся две пары её потомков: внешняя и внутренняя — те же, что рекурсия
 * передавала в два вызова {@code isMirror}. Время O(n), память O(h).
 *
 * @see <a href="../../docs/problems/block06_trees_graphs/SymmetricTree.md">SymmetricTree.md</a>
 */
public class SymmetricTreeIterative {

    /* Пара узлов, которые должны оказаться зеркальными. Любой из них может быть null. */
    private record MirrorPair(TreeNode left, TreeNode right) {
    }

    /**
     * По условию в дереве от 1 до 1000 узлов, поэтому root не проверяется на null.
     * <pre>
     *                       1
     *                /             \
     *               2               2
     *            /     \         /     \
     *           3       4       4       3
     *          / \     / \     / \     / \
     *         5   6   7   8   8   7   6   5
     * </pre>
     */
    public static boolean isSymmetric(TreeNode root) {
        Deque<MirrorPair> pairsToCompare = new ArrayDeque<>();
        pairsToCompare.push(new MirrorPair(root.left, root.right));

        while (!pairsToCompare.isEmpty()) {
            MirrorPair current = pairsToCompare.pop();
            TreeNode left = current.left();
            TreeNode right = current.right();

            if (left == null && right == null) {
                continue;
            }
            if (left == null || right == null) {
                return false;
            }
            if (left.val != right.val) {
                return false;
            }

            /* Внешняя пара: левое у левого и правое у правого; внутренняя — наоборот. */
            pairsToCompare.push(new MirrorPair(left.left, right.right));
            pairsToCompare.push(new MirrorPair(left.right, right.left));
        }
        return true;
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
