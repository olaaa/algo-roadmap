package demo;

import block06_trees_graphs.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Четыре порядка обхода двоичного дерева на одном и том же дереве
 * {@code [1, 2, 3, 4, 5, 6, 7]}: три обхода в глубину (прямой, симметричный,
 * обратный) и обход в ширину. Иллюстрация к справочнику по деревьям,
 * не задача LeetCode.
 *
 * @see <a href="../../docs/datastructures/BinaryTree.md">BinaryTree.md</a>
 */
public class TreeTraversals {

    /* Прямой порядок: узел записывается при входе, до обоих поддеревьев. */
    public static void preOrder(TreeNode node, List<Integer> visited) {
        if (node == null) {
            return;
        }
        visited.add(node.val);
        preOrder(node.left, visited);
        preOrder(node.right, visited);
    }

    /* Симметричный порядок: узел записывается между левым и правым поддеревом. */
    public static void inOrder(TreeNode node, List<Integer> visited) {
        if (node == null) {
            return;
        }
        inOrder(node.left, visited);
        visited.add(node.val);
        inOrder(node.right, visited);
    }

    /* Обратный порядок: узел записывается при возврате, после обоих поддеревьев. */
    public static void postOrder(TreeNode node, List<Integer> visited) {
        if (node == null) {
            return;
        }
        postOrder(node.left, visited);
        postOrder(node.right, visited);
        visited.add(node.val);
    }

    /**
     * Обход в ширину: значения узлов уровень за уровнем, внутри уровня слева
     * направо. Для дерева {@code [1, 2, 3, 4, 5, 6, 7]} даёт
     * {@code 1, 2, 3, 4, 5, 6, 7} — тот же порядок, в каком дерево записано
     * в условии задачи.
     * <p>
     * Очередь хранит узлы, чьих потомков ещё не смотрели. Каждый шаг цикла
     * одинаков: снять узел с головы, записать его значение, положить его
     * непустых потомков в хвост. Никакого управления маршрутом в коде нет —
     * порядок по уровням даёт сама очередь: она отдаёт узлы в порядке
     * поступления, а потомки всегда поступают позже своих родителей.
     * <p>
     * Пустое дерево даёт пустой список. Память сверх входа — O(w), где w —
     * наибольшее число узлов на одном уровне: столько узлов одновременно
     * лежит в очереди.
     *
     * @param root корень дерева или {@code null} для пустого дерева
     * @return значения узлов в порядке обхода в ширину
     */
    public static List<Integer> levelOrder(TreeNode root) {
        List<Integer> visited = new ArrayList<>();
        if (root == null) {
            return visited;
        }
        Queue<TreeNode> pending = new ArrayDeque<>();
        pending.add(root);
        while (!pending.isEmpty()) {
            TreeNode node = pending.remove();
            visited.add(node.val);
            if (node.left != null) {
                pending.add(node.left);
            }
            if (node.right != null) {
                pending.add(node.right);
            }
        }
        return visited;
    }

    public static void main(String[] args) {
        TreeNode root = TreeNode.fromLevelOrder(1, 2, 3, 4, 5, 6, 7);

        List<Integer> pre = new ArrayList<>();
        preOrder(root, pre);
        List<Integer> in = new ArrayList<>();
        inOrder(root, in);
        List<Integer> post = new ArrayList<>();
        postOrder(root, post);
        List<Integer> level = levelOrder(root);

        System.out.println("pre-order   : " + pre);
        System.out.println("in-order    : " + in);
        System.out.println("post-order  : " + post);
        System.out.println("level-order : " + level);

        check(pre.equals(List.of(1, 2, 4, 5, 3, 6, 7)), "прямой порядок: 1 2 4 5 3 6 7");
        check(in.equals(List.of(4, 2, 5, 1, 6, 3, 7)), "симметричный порядок: 4 2 5 1 6 3 7");
        check(post.equals(List.of(4, 5, 2, 6, 7, 3, 1)), "обратный порядок: 4 5 2 6 7 3 1");
        check(level.equals(List.of(1, 2, 3, 4, 5, 6, 7)), "обход в ширину: 1 2 3 4 5 6 7");
        check(levelOrder(null).isEmpty(), "обход в ширину пустого дерева: пустой список");
    }

    private static void check(boolean ok, String name) {
        System.out.println((ok ? "PASS" : "FAIL") + " — " + name);
    }
}
