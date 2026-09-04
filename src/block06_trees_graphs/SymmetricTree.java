package block06_trees_graphs;

/**
 * LeetCode 101 — Symmetric Tree (Easy). Обход в глубину рекурсией, но сразу
 * по двум узлам: левое поддерево корня сравнивается с правым в зеркальном
 * порядке.
 * <p>
 * Дерево симметрично, если левое и правое поддеревья корня — зеркальные
 * отражения друг друга. Два дерева зеркальны, когда их корни равны, левое
 * поддерево первого зеркально правому поддереву второго и наоборот.
 * Время O(n), память O(h), где h — высота дерева.
 *
 * @see <a href="../../docs/problems/block06_trees_graphs/SymmetricTree.md">SymmetricTree.md</a>
 */
public class SymmetricTree {

    /* По условию в дереве хотя бы один узел, поэтому root не проверяется на null. */
    public static boolean isSymmetric(TreeNode root) {
        return isMirror(root.left, root.right);
    }

    /*
     * Два поддерева зеркальны, если оба пусты, либо оба непусты, корни равны
     * и внешняя пара (левое у левого, правое у правого) зеркальна вместе
     * с внутренней (правое у левого, левое у правого).
     */
    private static boolean isMirror(TreeNode leftSubtree, TreeNode rightSubtree) {
        if (leftSubtree == null && rightSubtree == null) {
            return true;
        }
        if (leftSubtree == null || rightSubtree == null) {
            return false;
        }
        return leftSubtree.val == rightSubtree.val
                && isMirror(leftSubtree.left, rightSubtree.right)
                && isMirror(leftSubtree.right, rightSubtree.left);
    }

    public static void main(String[] args) {
        /*
         * Ветви isMirror и тесты, которые их закрывают:
         *   1) оба поддерева пусты, true ............... один узел; и дно любого спуска
         *   2) пусто только одно, false ................ пример 2: форма не совпадает
         *   3) значения корней различны, false ......... [1, 2, 3]
         *   4) внешняя пара не зеркальна, false ........ [1, 2, 2, 3, 4, 4, 5]
         *   5) внутренняя пара не зеркальна, false ..... [1, 2, 2, 3, 4, 5, 3]
         *   6) всё совпало, true ....................... пример 1
         * У isSymmetric ветвь одна — вызов isMirror для потомков корня.
         */
        record TestCase(TreeNode root, boolean expected, String name) {}

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
         * Предельная глубина по ограничениям задачи: 1000 узлов. Самое глубокое
         * симметричное дерево — «галочка»: у корня два потомка, дальше левая
         * ветвь растёт только влево, правая только вправо. На 999 узлах
         * глубина 500, рекурсия isMirror спускается на 500 уровней.
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
